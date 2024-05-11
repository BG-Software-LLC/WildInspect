package com.bgsoftware.wildinspect.coreprotect;

import com.bgsoftware.wildinspect.Locale;
import com.bgsoftware.wildinspect.WildInspectPlugin;
import com.bgsoftware.wildinspect.coreprotect.lookup.DataResultLine;
import com.bgsoftware.wildinspect.coreprotect.lookup.FooterResultLine;
import com.bgsoftware.wildinspect.coreprotect.lookup.HeaderResultLine;
import com.bgsoftware.wildinspect.coreprotect.lookup.LookupResultLine;
import com.bgsoftware.wildinspect.coreprotect.lookup.NoDataResultLine;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider;
import com.bgsoftware.wildinspect.utils.InspectPlayers;
import com.bgsoftware.wildinspect.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class CoreProtect {

    private final WildInspectPlugin plugin;
    private final CoreProtectProvider coreProtectProvider;

    public CoreProtect(WildInspectPlugin plugin) {
        this.plugin = plugin;
        this.coreProtectProvider = loadCoreProtectProvider();
    }

    public void performLookup(LookupType type, Player player, Block block, int page) {
        ClaimsProvider.ClaimPlugin claimPlugin = plugin.getHooksHandler().getRegionAt(player, block.getLocation());

        if (claimPlugin == ClaimsProvider.ClaimPlugin.NONE) {
            Locale.NOT_INSIDE_CLAIM.send(player);
            return;
        }

        if (!plugin.getHooksHandler().hasRole(claimPlugin, player, block.getLocation(), plugin.getSettings().requiredRoles)) {
            Locale.REQUIRED_ROLE.send(player, StringUtils.format(plugin.getSettings().requiredRoles));
            return;
        }

        if (InspectPlayers.isCooldown(player)) {
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            Locale.COOLDOWN.send(player, df.format(InspectPlayers.getTimeLeft(player) / 1000));
            return;
        }

        if (plugin.getSettings().cooldown != -1)
            InspectPlayers.setCooldown(player);

        InspectPlayers.setBlock(player, block);

        if (plugin.getSettings().historyLimitPage < page) {
            Locale.LIMIT_REACH.send(player);
            return;
        }

        BlockState blockState = block.getState();

        List<String> operators = new ArrayList<>();
        Bukkit.getServer().getOperators().forEach(operator -> operators.add(operator.getName()));

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                performDatabaseLookup(type, player, block, blockState, page, operators));
    }

    private void performDatabaseLookup(LookupType type, Player player, Block block, BlockState blockState, int page,
                                       List<String> ignoredPlayers) {
        try (Connection connection = this.coreProtectProvider.getConnection()) {
            if (connection == null) {
                Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () ->
                        performDatabaseLookup(type, player, block, blockState, page, ignoredPlayers), 5L);
                return;
            }

            List<LookupResultLine> resultLines;
            int maxPage;

            try (Statement statement = connection.createStatement()) {
                maxPage = getMaxPage(statement, type, player, block, blockState);

                if (maxPage < page) {
                    Locale.LIMIT_REACH.send(player);
                    return;
                }

                switch (type) {
                    case INTERACTION_LOOKUP:
                        resultLines = this.coreProtectProvider.performInteractLookup(statement, player, block, page);
                        break;
                    case BLOCK_LOOKUP:
                        resultLines = this.coreProtectProvider.performBlockLookup(statement, player, blockState, page);
                        break;
                    case CHEST_TRANSACTIONS:
                        resultLines = this.coreProtectProvider.performChestLookup(statement, player, block, page);
                        break;
                    default:
                        return;
                }
            }

            StringBuilder message = new StringBuilder();
            boolean hasAnyData = false;

            for (LookupResultLine line : resultLines) {
                switch (line.getType()) {
                    case NO_DATA: {
                        NoDataResultLine noDataResultLine = (NoDataResultLine) line;
                        NoDataResultLine.InteractionType interactionType = noDataResultLine.getInteractionType();
                        switch (interactionType) {
                            case PLAYER_INTERACTIONS:
                                message.append("\n").append(Locale.NO_BLOCK_INTERACTIONS.getMessage(noDataResultLine.getPage()));
                                break;
                            case BLOCK_DATA:
                                message.append("\n").append(Locale.NO_BLOCK_DATA.getMessage(noDataResultLine.getPage()));
                                break;
                            case CONTAINER_TRANSACTIONS:
                                message.append("\n").append(Locale.NO_CONTAINER_TRANSACTIONS.getMessage(noDataResultLine.getPage()));
                                break;
                        }
                        break;
                    }
                    case HEADER: {
                        HeaderResultLine headerResultLine = (HeaderResultLine) line;
                        message.append("\n").append(Locale.INSPECT_DATA_HEADER.getMessage(
                                headerResultLine.getX(), headerResultLine.getY(), headerResultLine.getZ()));
                        break;
                    }
                    case DATA: {
                        DataResultLine dataResultLine = (DataResultLine) line;
                        if (plugin.getSettings().hideOps && ignoredPlayers.contains(dataResultLine.getPlayerName()))
                            continue;

                        long days = TimeUnit.MILLISECONDS.toDays(
                                System.currentTimeMillis() - dataResultLine.getDate().getTime());
                        if (plugin.getSettings().historyLimitDate < days)
                            continue;

                        hasAnyData = true;
                        message.append("\n").append(Locale.INSPECT_DATA_ROW.getMessage(
                                dataResultLine.getTimeSinceAction(), dataResultLine.getPlayerName(),
                                dataResultLine.getAction(), dataResultLine.getBlockAction()));

                        break;
                    }
                    case FOOTER: {
                        FooterResultLine footerResultLine = (FooterResultLine) line;
                        message.append("\n").append(Locale.INSPECT_DATA_FOOTER.getMessage(
                                Math.max(footerResultLine.getPage(), 1),
                                Math.min(maxPage - 1, plugin.getSettings().historyLimitPage)));
                        break;
                    }
                }
            }

            player.sendMessage(hasAnyData ? message.substring(1) : Locale.NO_BLOCK_DATA.getMessage("that page"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private int getMaxPage(Statement statement, LookupType type, Player player, Block block, BlockState blockState) {
        List<LookupResultLine> resultLines;

        int maxPage = 1;

        while (true) {
            switch (type) {
                case INTERACTION_LOOKUP:
                    resultLines = this.coreProtectProvider.performInteractLookup(statement, player, block, maxPage);
                    break;
                case BLOCK_LOOKUP:
                    resultLines = this.coreProtectProvider.performBlockLookup(statement, player, blockState, maxPage);
                    break;
                case CHEST_TRANSACTIONS:
                    resultLines = this.coreProtectProvider.performChestLookup(statement, player, block, maxPage);
                    break;
                default:
                    return 0;
            }

            boolean hasAnyData = false;

            for (LookupResultLine line : resultLines) {
                if (line.getType() == LookupResultLine.Type.DATA) {
                    DataResultLine dataResultLine = (DataResultLine) line;
                    long days = TimeUnit.MILLISECONDS.toDays(
                            System.currentTimeMillis() - dataResultLine.getDate().getTime());
                    if (plugin.getSettings().historyLimitDate >= days) {
                        hasAnyData = true;
                        break;
                    }
                }
            }

            if (!hasAnyData) {
                return maxPage;
            }

            maxPage++;
        }
    }

    private static CoreProtectProvider loadCoreProtectProvider() {
        Plugin coreProtectPlugin = Bukkit.getPluginManager().getPlugin("CoreProtect");
        String version = coreProtectPlugin.getDescription().getVersion().split("\\.")[0];

        Class<?> coreProtectProviderClass;

        try {
            coreProtectProviderClass = Class.forName("com.bgsoftware.wildinspect.coreprotect.CoreProtect" + version);
        } catch (ClassNotFoundException error) {
            WildInspectPlugin.log("CoreProtect version is not supported: " + version);
            throw new RuntimeException(error);
        }

        try {
            return (CoreProtectProvider) coreProtectProviderClass.newInstance();
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }

}
