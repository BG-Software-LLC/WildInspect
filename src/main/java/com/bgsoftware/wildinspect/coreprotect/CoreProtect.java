package com.bgsoftware.wildinspect.coreprotect;

import com.bgsoftware.wildinspect.Locale;
import com.bgsoftware.wildinspect.WildInspectPlugin;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider;
import com.bgsoftware.wildinspect.utils.InspectPlayers;
import com.bgsoftware.wildinspect.utils.StringUtils;
import net.coreprotect.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CoreProtect {

    private static final String COREPROTECT_COLOR = is116OrAbove() ? "§x§3§1§b§0§e§8" : "§3";

    private static final Pattern NO_DATA_PATTERN = Pattern.compile("%sCoreProtect §f- §fNo (.*) found for (.*)\\.".replace("%s", COREPROTECT_COLOR));
    private static final Pattern DATA_HEADER_PATTERN = Pattern.compile("§f----- %s(.*) §f----- §7\\(x(.*)/y(.*)/z(.*)\\)".replace("%s", COREPROTECT_COLOR));
    private static final Pattern DATA_LINE_PATTERN = Pattern.compile("§7(.*) §f- %s(.*) §f(.*) %s(.*)§f\\.".replace("%s", COREPROTECT_COLOR));
    private static final Pattern DATA_FOOTER_PATTERN = Pattern.compile("§fPage (.*)/(.*)\\. View older data by typing \"%s/co l <page>§f\"\\.".replace("%s", COREPROTECT_COLOR));

    private final WildInspectPlugin plugin;

    public CoreProtect(WildInspectPlugin plugin){
        this.plugin = plugin;
    }

    public void performLookup(LookupType type, Player pl, Block bl, int page) {
        ClaimsProvider.ClaimPlugin claimPlugin = plugin.getHooksHandler().getRegionAt(pl, bl.getLocation());

        if(claimPlugin == ClaimsProvider.ClaimPlugin.NONE){
            Locale.NOT_INSIDE_CLAIM.send(pl);
            return;
        }

        if(!plugin.getHooksHandler().hasRole(claimPlugin, pl, bl.getLocation(), plugin.getSettings().requiredRoles)){
            Locale.REQUIRED_ROLE.send(pl, StringUtils.format(plugin.getSettings().requiredRoles));
            return;
        }

        if(InspectPlayers.isCooldown(pl)){
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            Locale.COOLDOWN.send(pl, df.format(InspectPlayers.getTimeLeft(pl) / 1000));
            return;
        }

        if(plugin.getSettings().cooldown != -1)
            InspectPlayers.setCooldown(pl);

        InspectPlayers.setBlock(pl, bl);

        if(plugin.getSettings().historyLimitPage < page){
            Locale.LIMIT_REACH.send(pl);
            return;
        }

        BlockState blockState = bl.getState();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try(Connection connection = Database.getConnection(false)){
                if(connection == null){
                    Bukkit.getScheduler().runTaskLater(plugin, () -> performLookup(type, pl, bl, page), 20L);
                    return;
                }

                try(Statement statement = connection.createStatement()){
                    int maxPage = getMaxPage(statement, type, pl, bl, blockState);

                    if(maxPage <= page){
                        Locale.LIMIT_REACH.send(pl);
                        return;
                    }

                    String[] resultLines;

                    switch(type){
                        case INTERACTION_LOOKUP:
                            resultLines = CoreProtectHook.performInteractLookup(statement, pl, bl, page);
                            break;
                        case BLOCK_LOOKUP:
                            resultLines = CoreProtectHook.performBlockLookup(statement, pl, bl, blockState, page);
                            break;
                        case CHEST_TRANSACTIONS:
                            resultLines = CoreProtectHook.performChestLookup(statement, pl, bl, page);
                            break;
                        default:
                            return;
                    }

                    Matcher matcher;

                    StringBuilder message = new StringBuilder();
                    boolean empty = true;

                    for(String line : resultLines){
                        if((matcher = NO_DATA_PATTERN.matcher(line)).matches()){
                            switch(matcher.group(1)){
                                case "player interactions":
                                    message.append("\n").append(Locale.NO_BLOCK_INTERACTIONS.getMessage(matcher.group(2)));
                                    break;
                                case "block data":
                                    message.append("\n").append(Locale.NO_BLOCK_DATA.getMessage(matcher.group(2)));
                                    break;
                                case "container transactions":
                                    message.append("\n").append(Locale.NO_CONTAINER_TRANSACTIONS.getMessage(matcher.group(2)));
                                    break;
                            }
                        }
                        else if((matcher = DATA_HEADER_PATTERN.matcher(line)).matches()){
                            message.append("\n").append(Locale.INSPECT_DATA_HEADER.getMessage(matcher.group(2), matcher.group(3), matcher.group(4)));
                        }
                        else if((matcher = DATA_LINE_PATTERN.matcher(line)).matches()){
                            if(plugin.getSettings().hideOps) {
                                //noinspection deprecation
                                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(matcher.group(2));
                                if (offlinePlayer != null && offlinePlayer.isOp())
                                    continue;
                            }
                            double days = Double.parseDouble(matcher.group(1).split("/")[0].replace(",", ".")) / 24;
                            if(plugin.getSettings().historyLimitDate >= days) {
                                empty = false;
                                message.append("\n").append(Locale.INSPECT_DATA_ROW.getMessage(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4)));
                            }
                        }
                        else if((matcher = DATA_FOOTER_PATTERN.matcher(line)).matches()){
                            int linePage = Integer.parseInt(matcher.group(1));
                            message.append("\n").append(Locale.INSPECT_DATA_FOOTER.getMessage(Math.max(linePage, 1),
                                    Math.min(maxPage - 1, plugin.getSettings().historyLimitPage)));
                        }
                    }

                    pl.sendMessage(empty ? Locale.NO_BLOCK_DATA.getMessage("that page") : message.substring(1));
                }
            } catch(SQLException ex){
                ex.printStackTrace();
            }
        });
    }

    private int getMaxPage(Statement statement, LookupType type, Player pl, Block bl, BlockState blockState){
        String[] resultLines;

        int maxPage = 0;

        while(true) {
            switch(type){
                case INTERACTION_LOOKUP:
                    resultLines = CoreProtectHook.performInteractLookup(statement, pl, bl, maxPage);
                    break;
                case BLOCK_LOOKUP:
                    resultLines = CoreProtectHook.performBlockLookup(statement, pl, bl, blockState, maxPage);
                    break;
                case CHEST_TRANSACTIONS:
                    resultLines = CoreProtectHook.performChestLookup(statement, pl, bl, maxPage);
                    break;
                default:
                    return 0;
            }

            int amountOfRows = 0;
            Matcher matcher;

            for (String line : resultLines) {
                if ((matcher = DATA_LINE_PATTERN.matcher(line)).matches()) {
                    double days = Double.parseDouble(matcher.group(1).split("/")[0].replace(",", ".")) / 24;
                    if(plugin.getSettings().historyLimitDate >= days) {
                        amountOfRows++;
                    }
                }
            }

            if (amountOfRows == 0) {
                return maxPage;
            }

            maxPage++;
        }
    }

    private static boolean is116OrAbove(){
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        version = version.substring(1).replace("_", "").replace("R", "");
        return Integer.parseInt(version) >= 1160;
    }

}
