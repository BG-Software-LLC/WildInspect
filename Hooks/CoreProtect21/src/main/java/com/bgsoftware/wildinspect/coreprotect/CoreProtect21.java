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
import net.coreprotect.language.Phrase;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoreProtect21 implements CoreProtectProvider {

    private static final String COREPROTECT_COLOR = ChatColor.translateAlternateColorCodes('&', "&x&3&1&b&0&e&8");

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

    private static final Pattern SELECTOR_PATTERN = Pattern.compile("\\{(.*\\|.*)}");

    private final Pattern NO_DATA_PATTERN = Pattern.compile(
            Phrase.build(Phrase.NO_DATA, "(.*?)"));
    private final Pattern NO_DATA_LOCATION_PATTERN = Pattern.compile(
            SELECTOR_PATTERN.matcher(Phrase.NO_DATA_LOCATION.getTranslatedPhrase()).replaceAll("(.*?)"));
    private final Pattern HEADER_PATTERN = Pattern.compile(
            ChatColor.GRAY + "\\(x(.*?)/y(.*?)/z(.*?)\\)");
    private final Pattern DATA_PATTERN = Pattern.compile(
            "<COMPONENT>POPUP\\|" +
                    ChatColor.GRAY + "(.*?)\\|" +
                    ChatColor.GRAY + "(.*?)</COMPONENT> " +
                    "(" + ChatColor.RED + "-|" + ChatColor.GREEN + "\\+) " +
                    COREPROTECT_COLOR + "(.*?)" +
                    ChatColor.WHITE + " (.*?) " +
                    COREPROTECT_COLOR + "(.*?)" +
                    ChatColor.WHITE);
    private final Pattern FOOTER_PATTERN =
            Pattern.compile(Phrase.build(Phrase.LOOKUP_PAGE, ChatColor.WHITE + "(.*?)/(.*?)"));

    private final Map<String, NoDataResultLine.InteractionType> interactionTypeMap = new HashMap<>();

    public CoreProtect21() {
        loadInteractionAliases();
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

    private List<LookupResultLine> parseResults(String results) {
        List<LookupResultLine> parsedResults = new LinkedList<>();

        Matcher matcher;
        for (String line : results.split("\n")) {
            if (NO_DATA_PATTERN.matcher(line).find()) {
                parsedResults.add(new NoDataResultLine(NoDataResultLine.InteractionType.BLOCK_DATA));
            } else if ((matcher = NO_DATA_LOCATION_PATTERN.matcher(line)).find()) {
                NoDataResultLine.InteractionType interactionType = this.interactionTypeMap.get(matcher.group(1));
                if (interactionType == null)
                    throw new IllegalArgumentException("Cannot find interaction type " + matcher.group(1));
                parsedResults.add(new NoDataResultLine(interactionType));
            } else if ((matcher = HEADER_PATTERN.matcher(line)).find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                int z = Integer.parseInt(matcher.group(3));
                parsedResults.add(new HeaderResultLine(x, y, z));
            } else if ((matcher = DATA_PATTERN.matcher(line)).find()) {
                try {
                    parsedResults.add(new DataResultLine(DATE_FORMAT.parse(matcher.group(1)), matcher.group(2),
                            matcher.group(4), matcher.group(5), matcher.group(6)));
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

    private void loadInteractionAliases() {
        Matcher matcher = SELECTOR_PATTERN.matcher(Phrase.NO_DATA_LOCATION.getTranslatedPhrase());

        if (matcher.find()) {
            String[] types = matcher.group(1).split("\\|");
            if (types.length >= 1) {
                this.interactionTypeMap.put(types[0], NoDataResultLine.InteractionType.BLOCK_DATA);
                if (types.length >= 2) {
                    this.interactionTypeMap.put(types[1], NoDataResultLine.InteractionType.CONTAINER_TRANSACTIONS);
                }
                if (types.length >= 3) {
                    this.interactionTypeMap.put(types[2], NoDataResultLine.InteractionType.PLAYER_INTERACTIONS);
                }
            }
        }

    }

}
