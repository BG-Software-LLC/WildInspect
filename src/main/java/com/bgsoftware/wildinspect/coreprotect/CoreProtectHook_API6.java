package com.bgsoftware.wildinspect.coreprotect;

import net.coreprotect.database.Lookup;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.sql.Statement;

@SuppressWarnings({"SameParameterValue", "JavaReflectionMemberAccess"})
public final class CoreProtectHook_API6 implements CoreProtectHook {

    @Override
    public String[] performInteractLookup(Statement statement, Player player, Block block, BlockState blockState, int page){
        try{
            return ((String) Lookup.class.getMethod("interaction_lookup", Statement.class, Block.class, String.class, int.class, int.class, int.class)
                    .invoke(null, statement, block, player.getName(), 0, page, 7)).split("\n");
        }catch(Throwable ex){
            ex.printStackTrace();
            return new String[0];
        }
    }

    @Override
    public String[] performBlockLookup(Statement statement, Player player, Block block, BlockState blockState, int page){
        try{
            return ((String) Lookup.class.getMethod("block_lookup", Statement.class, BlockState.class, String.class, int.class, int.class, int.class)
                    .invoke(null, statement, blockState, player.getName(), 0, page, 7)).split("\n");
        }catch(Exception ex){
            ex.printStackTrace();
            return new String[0];
        }
    }

    @Override
    public String[] performChestLookup(Statement statement, Player player, Block block, BlockState blockState, int page){
        try{
            return ((String) Lookup.class.getMethod("chest_transactions", Statement.class, Location.class, String.class, int.class, int.class, boolean.class)
                    .invoke(null, statement, block.getLocation(), player.getName(), page, 7, false)).split("\n");
        }catch(Exception ex){
            ex.printStackTrace();
            return new String[0];
        }
    }

}
