package com.bgsoftware.wildinspect.hooks;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.handlers.PlayersManager;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPermission;
import com.bgsoftware.superiorskyblock.api.island.PlayerRole;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;

public final class ClaimsProvider_SuperiorSkyblock implements ClaimsProvider{

    @Override
    public boolean hasRole(Player player, Location location, String... role) {
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        Island island = SuperiorSkyblockAPI.getIslandAt(location);
        PlayersManager playersManager = SuperiorSkyblockAPI.getSuperiorSkyblock().getPlayers();
        PlayerRole playerRole = island == null || island.isMember(superiorPlayer) ? superiorPlayer.getPlayerRole() :
                island.isCoop(superiorPlayer) ? playersManager.getCoopRole() : playersManager.getGuestRole();
        return Arrays.asList(role).contains(playerRole.toString());
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        Island island = SuperiorSkyblockAPI.getIslandAt(location);
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        return island != null && island.hasPermission(superiorPlayer, IslandPermission.BUILD);
    }
}
