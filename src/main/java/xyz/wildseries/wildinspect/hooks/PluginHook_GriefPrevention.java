package xyz.wildseries.wildinspect.hooks;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.PlayerData;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class PluginHook_GriefPrevention implements PluginHook {

    @Override
    public boolean hasRole(Player pl, String role) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player pl, Location loc) {
        DataStore dataStore = me.ryanhamshire.GriefPrevention.GriefPrevention.instance.dataStore;
        PlayerData playerData = dataStore.getPlayerData(pl.getUniqueId());
        Claim claim = dataStore.getClaimAt(loc, false, playerData.lastClaim);
        return claim != null && (playerData.ignoreClaims || playerData.getClaims().contains(claim));
    }
}
