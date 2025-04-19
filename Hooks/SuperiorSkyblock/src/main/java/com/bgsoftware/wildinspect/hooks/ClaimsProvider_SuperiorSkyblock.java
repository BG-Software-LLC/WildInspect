package com.bgsoftware.wildinspect.hooks;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.handlers.RolesManager;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import com.bgsoftware.superiorskyblock.api.island.PlayerRole;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.wildinspect.WildInspectPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Locale;

public final class ClaimsProvider_SuperiorSkyblock implements ClaimsProvider {

    private static final IslandPrivilege BUILD_PRIVILEGE = IslandPrivilege.getByName("BUILD");

    private static final RolesManager ROLES_MANAGER = SuperiorSkyblockAPI.getSuperiorSkyblock().getRoles();

    public ClaimsProvider_SuperiorSkyblock() {
        WildInspectPlugin.log(" - Using SuperiorSkyblock as ClaimsProvider.");
    }

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.SUPERIOR_SKYBLOCK;
    }

    @Override
    public boolean hasRole(Player player, Location location, Collection<String> roles) {
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        Island island = SuperiorSkyblockAPI.getIslandAt(location);
        PlayerRole playerRole = island == null || island.isMember(superiorPlayer) ? superiorPlayer.getPlayerRole() :
                island.isCoop(superiorPlayer) ? ROLES_MANAGER.getCoopRole() : ROLES_MANAGER.getGuestRole();
        return roles.contains(playerRole.getName().toLowerCase(Locale.ENGLISH));
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        Island island = SuperiorSkyblockAPI.getIslandAt(location);
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        return island != null && island.hasPermission(superiorPlayer, BUILD_PRIVILEGE);
    }
}
