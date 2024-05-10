package com.bgsoftware.wildinspect.coreprotect;

import com.bgsoftware.wildinspect.coreprotect.lookup.LookupResultLine;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public interface CoreProtectProvider {

    @Nullable
    Connection getConnection();

    List<LookupResultLine> performInteractLookup(Statement statement, Player player, Block block, int page);

    List<LookupResultLine> performBlockLookup(Statement statement, Player player, BlockState blockState, int page);

    List<LookupResultLine> performChestLookup(Statement statement, Player player, Block block, int page);

}
