package com.bgsoftware.wildinspect.hooks;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPermission;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class ClaimsProvider_SuperiorSkyblock implements ClaimsProvider{

    @Override
    public boolean hasRole(Player player, String role) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        Island island = SuperiorSkyblockAPI.getIslandAt(location);
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        return island != null && island.hasPermission(superiorPlayer, IslandPermission.BUILD);
    }
}
