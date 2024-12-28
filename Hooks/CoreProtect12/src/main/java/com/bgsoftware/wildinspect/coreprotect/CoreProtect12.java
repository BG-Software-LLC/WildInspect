package com.bgsoftware.wildinspect.coreprotect;

import com.bgsoftware.wildinspect.coreprotect.lookup.DataResultLine;
import com.bgsoftware.wildinspect.coreprotect.lookup.FooterResultLine;
import com.bgsoftware.wildinspect.coreprotect.lookup.HeaderResultLine;
import com.bgsoftware.wildinspect.coreprotect.lookup.LookupResultLine;
import com.bgsoftware.wildinspect.coreprotect.lookup.NoDataResultLine;
import net.coreprotect.database.Database;
import net.coreprotect.database.Lookup;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoreProtect12 implements CoreProtectProvider {

    private static final Pattern NO_DATA_PATTERN = Pattern.compile(
            ChatColor.WHITE + "No (.*?) found for (.*?)\\.");
    private static final Pattern HEADER_PATTERN = Pattern.compile(
            ChatColor.GRAY + "\\(x(.*?)/y(.*?)/z(.*?)\\)");
    private static final Pattern DATA_PATTERN = Pattern.compile(
            ChatColor.GRAY + "(.*)" +
                    ChatColor.WHITE + "- " +
                    ChatColor.DARK_AQUA + "(.*?) " +
                    ChatColor.WHITE + "(.*?) " +
                    ChatColor.DARK_AQUA + "(.*?)" +
                    ChatColor.WHITE + "\\.");
    private static final Pattern FOOTER_PATTERN = Pattern.compile(
            "Page (.*?)/(.*?)");

    @Nullable
    @Override
    public Connection getConnection() {
        return Database.getConnection(true);
    }

    @Override
    public List<LookupResultLine> performInteractLookup(Statement statement, Player player, Block block, int page) {
        String results = Lookup.interaction_lookup(statement, block, player.getName(), 0, page, 7);
        return parseResults(results);
    }

    @Override
    public List<LookupResultLine> performBlockLookup(Statement statement, Player player, BlockState blockState, int page) {
        String results = Lookup.block_lookup(statement, blockState.getBlock(), player.getName(), 0, page, 7);
        return parseResults(results);
    }

    @Override
    public List<LookupResultLine> performChestLookup(Statement statement, Player player, Block block, int page) {
        String results = Lookup.chest_transactions(statement, block.getLocation(), player.getName(), page, 7);
        return parseResults(results);
    }

    private static List<LookupResultLine> parseResults(String results) {
        List<LookupResultLine> parsedResults = new LinkedList<>();

        long now = System.currentTimeMillis();

        Matcher matcher;
        for (String line : results.split("\n")) {
            if ((matcher = NO_DATA_PATTERN.matcher(line)).find()) {
                NoDataResultLine.InteractionType interactionType = NoDataResultLine.InteractionType
                        .of(matcher.group(1).replace(" ", "_"));
                parsedResults.add(new NoDataResultLine(interactionType, matcher.group(2)));
            } else if ((matcher = HEADER_PATTERN.matcher(line)).find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                int z = Integer.parseInt(matcher.group(3));
                parsedResults.add(new HeaderResultLine(x, y, z));
            } else if ((matcher = DATA_PATTERN.matcher(line)).find()) {
                try {
                    long hoursAgoInMillis = TimeUnit.HOURS.toMillis(
                            (long) Double.parseDouble(matcher.group(1).split("/")[0]));
                    parsedResults.add(new DataResultLine(new Date(now - hoursAgoInMillis), matcher.group(1),
                            matcher.group(2), matcher.group(3), matcher.group(4)));
                } catch (NumberFormatException error) {
                    throw new RuntimeException(error);
                }
            } else if ((matcher = FOOTER_PATTERN.matcher(line)).find()) {
                int page = Integer.parseInt(matcher.group(1));
                parsedResults.add(new FooterResultLine(page));
            }
        }

        return parsedResults.isEmpty() ? Collections.emptyList() : parsedResults;
    }

}
