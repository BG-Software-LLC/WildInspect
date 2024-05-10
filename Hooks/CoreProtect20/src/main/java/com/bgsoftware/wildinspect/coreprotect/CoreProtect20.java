package com.bgsoftware.wildinspect.coreprotect;

import com.bgsoftware.wildinspect.coreprotect.lookup.DataResultLine;
import com.bgsoftware.wildinspect.coreprotect.lookup.FooterResultLine;
import com.bgsoftware.wildinspect.coreprotect.lookup.HeaderResultLine;
import com.bgsoftware.wildinspect.coreprotect.lookup.LookupResultLine;
import com.bgsoftware.wildinspect.coreprotect.lookup.NoDataResultLine;
import net.coreprotect.database.Database;
import net.coreprotect.database.lookup.BlockLookup;
import net.coreprotect.database.lookup.ChestTransactionLookup;
import net.coreprotect.database.lookup.InteractionLookup;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoreProtect20 implements CoreProtectProvider {

    private static final String COREPROTECT_COLOR = ChatColor.translateAlternateColorCodes('&', "&x&3&1&b&0&e&8");

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

    private static final Pattern NO_DATA_PATTERN = Pattern.compile(
            "No (.*?) found (for|at) (.*?)\\.");
    private static final Pattern HEADER_PATTERN = Pattern.compile(
            ChatColor.GRAY + "\\(x(.*?)/y(.*?)/z(.*?)\\)");
    private static final Pattern DATA_PATTERN = Pattern.compile(
            "<COMPONENT>POPUP\\|" +
                    ChatColor.GRAY + "(.*?)\\|" +
                    ChatColor.GRAY + "(.*?)</COMPONENT> " +
                    ChatColor.WHITE + "- " +
                    COREPROTECT_COLOR + "(.*?)" +
                    ChatColor.WHITE + " (.*?) " +
                    COREPROTECT_COLOR + "(.*?)" +
                    ChatColor.WHITE + "\\.");
    private static final Pattern FOOTER_PATTERN = Pattern.compile(
            "Page (.*?)/(.*?)");

    public CoreProtect20() {
        NoDataResultLine.InteractionType.BLOCK_DATA.addAlias("data");
        NoDataResultLine.InteractionType.CONTAINER_TRANSACTIONS.addAlias("transactions");
        NoDataResultLine.InteractionType.PLAYER_INTERACTIONS.addAlias("interactions");
    }

    @Nullable
    @Override
    public Connection getConnection() {
        return Database.getConnection(true);
    }

    @Override
    public List<LookupResultLine> performInteractLookup(Statement statement, Player player, Block block, int page) {
        String results = InteractionLookup.performLookup(null, statement, block, player, 0, page, 7);
        return parseResults(results);
    }

    @Override
    public List<LookupResultLine> performBlockLookup(Statement statement, Player player, BlockState blockState, int page) {
        String results = BlockLookup.performLookup(null, statement, blockState, player, 0, page, 7);
        return parseResults(results);
    }

    @Override
    public List<LookupResultLine> performChestLookup(Statement statement, Player player, Block block, int page) {
        String results = ChestTransactionLookup.performLookup(null, statement, block.getLocation(), player, page, 7, false);
        return parseResults(results);
    }

    private static List<LookupResultLine> parseResults(String results) {
        List<LookupResultLine> parsedResults = new LinkedList<>();

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
                    parsedResults.add(new DataResultLine(DATE_FORMAT.parse(matcher.group(1)), matcher.group(2),
                            matcher.group(3), matcher.group(4), matcher.group(5)));
                } catch (ParseException error) {
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
