package com.bgsoftware.wildinspect.coreprotect;

import net.coreprotect.database.Lookup;

import net.coreprotect.database.lookup.BlockLookup;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("JavaReflectionMemberAccess")
public final class CoreProtectHook {

    private static final Pattern COMPONENT_PATTERN = Pattern.compile("<COMPONENT>(.*)\\|(.*)\\|(.*)</COMPONENT>");

    private static Method interactionLookupMethod = null, blockLookupMethod = null, chestTransactionsLookup = null;

    static {
        try {
            interactionLookupMethod = Lookup.class.getMethod("interaction_lookup", Statement.class, Block.class, String.class, int.class, int.class, int.class);
            blockLookupMethod = Lookup.class.getMethod("block_lookup", Statement.class, BlockState.class, String.class, int.class, int.class, int.class);
            chestTransactionsLookup = Lookup.class.getMethod("chest_transactions", Statement.class, Location.class, String.class, int.class, int.class, boolean.class);
        }catch(Exception ignored){
            try{
                interactionLookupMethod = Lookup.class.getMethod("interactionLookup", String.class, Statement.class, Block.class, CommandSender.class, int.class, int.class, int.class);
                blockLookupMethod = BlockLookup.class.getMethod("results", String.class, Statement.class, BlockState.class, CommandSender.class, int.class, int.class, int.class);
                chestTransactionsLookup = Lookup.class.getMethod("chestTransactions", String.class, Statement.class, Location.class, CommandSender.class, int.class, int.class, boolean.class);
            }catch (Exception ignored2){}
        }
    }

    public static String[] performInteractLookup(Statement statement, Player player, Block block, int page) {
        try {
            return parseResult((String) interactionLookupMethod.invoke(null, statement, block, player.getName(), 0, page, 7));
        }catch(Throwable ex){
            try{
                return parseResult((String) interactionLookupMethod.invoke(null, null, statement, block, player, 0, page, 7));
            }catch (Exception ex2){
                return parseResult(Lookup.interaction_lookup(statement, block, player.getName(), 0, page, 7));
            }
        }
    }

    public static String[] performBlockLookup(Statement statement, Player player, Block block, BlockState blockState, int page) {
        try{
            return parseResult((String) blockLookupMethod.invoke(null, statement, blockState, player.getName(), 0, page, 7));
        }catch(Throwable ex) {
            try{
                return parseResult((String) blockLookupMethod.invoke(null, null, statement, blockState, player, 0, page, 7));
            }catch (Exception ex2){
                return parseResult(Lookup.block_lookup(statement, block, player.getName(), 0, page, 7));
            }
        }
    }

    public static String[] performChestLookup(Statement statement, Player player, Block block, int page) {
        try{
            return parseResult((String) chestTransactionsLookup.invoke(null, statement, block.getLocation(), player.getName(), page, 7, false));
        }catch(Throwable ex) {
            try{
                return parseResult((String) chestTransactionsLookup.invoke(null, null, statement, block.getLocation(), player, page, 7, false));
            }catch (Exception ex2){
                return parseResult(Lookup.chest_transactions(statement, block.getLocation(), player.getName(), page, 7));
            }
        }
    }

    private static String[] parseResult(String result){
        Matcher matcher = COMPONENT_PATTERN.matcher(result);

        if(matcher.find())
            result = matcher.replaceAll("$3");

        return result.split("\n");
    }

}
