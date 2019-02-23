package com.bgsoftware.wildinspect.coreprotect;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface CoreProtectHook {

    void performLookup(LookupType type, Player pl, Block bl, int page);



}
