package com.bgsoftware.wildinspect.coreprotect;

import net.coreprotect.database.Lookup;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.sql.Statement;

public final class CoreProtectHook_API5 implements CoreProtectHook {

    @Override
    public String[] performInteractLookup(Statement statement, Player player, Block block, int page) {
        return Lookup.interaction_lookup(statement, block, player.getName(), 0, page, 7).split("\n");
    }

    @Override
    public String[] performBlockLookup(Statement statement, Player player, Block block, int page) {
        return Lookup.block_lookup(statement, block, player.getName(), 0, page, 7).split("\n");
    }

    @Override
    public String[] performChestLookup(Statement statement, Player player, Block block, int page) {
        return Lookup.chest_transactions(statement, block.getLocation(), player.getName(), page, 7).split("\n");
    }
}
