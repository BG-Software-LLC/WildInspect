package com.bgsoftware.wildinspect.coreprotect;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.sql.Statement;

public interface CoreProtectHook {

    String[] performInteractLookup(Statement statement, Player player, Block block, int page);

    String[] performBlockLookup(Statement statement, Player player, Block block, int page);

    String[] performChestLookup(Statement statement, Player player, Block block, int page);

}
