package com.bgsoftware.wildinspect.hooks;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.PlayerData;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class ClaimsProvider_GriefPrevention implements ClaimsProvider {

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.GRIEF_PREVENTION;
    }

    @Override
    public boolean hasRole(Player player, Location location, String... role) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        DataStore dataStore = me.ryanhamshire.GriefPrevention.GriefPrevention.instance.dataStore;
        PlayerData playerData = dataStore.getPlayerData(player.getUniqueId());
        Claim claim = dataStore.getClaimAt(location, false, playerData.lastClaim);
        return claim != null && (playerData.ignoreClaims || playerData.getClaims().contains(claim));
    }
}
