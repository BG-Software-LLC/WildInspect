package com.bgsoftware.wildinspect.coreprotect;

import net.coreprotect.database.Lookup;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.sql.Statement;

@SuppressWarnings("JavaReflectionMemberAccess")
public final class CoreProtectHook {

    private static Method interactionLookupMethod = null, blockLookupMethod = null, chestTransactionsLookup = null;

    static {
        try {
            interactionLookupMethod = Lookup.class.getMethod("interaction_lookup", Statement.class, Block.class, String.class, int.class, int.class, int.class);
            blockLookupMethod = Lookup.class.getMethod("block_lookup", Statement.class, BlockState.class, String.class, int.class, int.class, int.class);
            chestTransactionsLookup = Lookup.class.getMethod("chest_transactions", Statement.class, Location.class, String.class, int.class, int.class, boolean.class);
        }catch(Exception ignored){}
    }

    public static String[] performInteractLookup(Statement statement, Player player, Block block, BlockState blockState, int page) {
        try {
            return ((String) interactionLookupMethod.invoke(null, statement, block, player.getName(), 0, page, 7)).split("\n");
        }catch(Throwable ex){
            return Lookup.interaction_lookup(statement, block, player.getName(), 0, page, 7).split("\n");
        }
    }

    public static String[] performBlockLookup(Statement statement, Player player, Block block, BlockState blockState, int page) {
        try{
            return ((String) blockLookupMethod.invoke(null, statement, blockState, player.getName(), 0, page, 7)).split("\n");
        }catch(Throwable ex) {
            return Lookup.block_lookup(statement, block, player.getName(), 0, page, 7).split("\n");
        }
    }

    public static String[] performChestLookup(Statement statement, Player player, Block block, BlockState blockState, int page) {
        try{
            return ((String) chestTransactionsLookup.invoke(null, statement, block.getLocation(), player.getName(), page, 7, false)).split("\n");
        }catch(Throwable ex) {
            return Lookup.chest_transactions(statement, block.getLocation(), player.getName(), page, 7).split("\n");
        }
    }
}
