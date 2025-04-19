package com.bgsoftware.wildinspect.hooks;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import com.wasteofplastic.acidisland.ASkyBlockAPI;
import com.wasteofplastic.acidisland.Island;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public final class ClaimsProvider_AcidIsland implements ClaimsProvider {

    public ClaimsProvider_AcidIsland() {
        WildInspectPlugin.log(" - Using AcidIsland as ClaimsProvider.");
    }

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.ACID_ISLAND;
    }

    @Override
    public boolean hasRole(Player player, Location location, Collection<String> roles) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        try {
            Island is = ASkyBlockAPI.getInstance().getIslandAt(location);
            return player.hasPermission("acidisland.mod.bypassprotect") ||
                    is.getOwner().equals(player.getUniqueId()) || is.getMembers().contains(player.getUniqueId());
        } catch (NullPointerException e) {
            return false;
        }
    }

}
