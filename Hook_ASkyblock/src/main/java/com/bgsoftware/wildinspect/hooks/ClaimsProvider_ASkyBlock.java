package com.bgsoftware.wildinspect.hooks;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class ClaimsProvider_ASkyBlock implements ClaimsProvider {

    public ClaimsProvider_ASkyBlock() {
        WildInspectPlugin.log(" - Using ASkyBlock as ClaimsProvider.");
    }

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.ASKYBLOCK;
    }

    @Override
    public boolean hasRole(Player player, Location location, String... role) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        try {
            Island is = ASkyBlockAPI.getInstance().getIslandAt(location);
            return player.hasPermission("askyblock.mod.bypassprotect") ||
                    is.getOwner().equals(player.getUniqueId()) || is.getMembers().contains(player.getUniqueId());
        } catch (NullPointerException e) {
            return false;
        }
    }
}
