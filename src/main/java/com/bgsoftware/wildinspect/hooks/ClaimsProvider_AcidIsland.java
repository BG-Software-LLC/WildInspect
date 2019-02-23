package com.bgsoftware.wildinspect.hooks;

import com.wasteofplastic.acidisland.ASkyBlockAPI;
import com.wasteofplastic.acidisland.Island;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class ClaimsProvider_AcidIsland implements ClaimsProvider {

    @Override
    public boolean hasRole(Player player, String role) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        try {
            Island is = ASkyBlockAPI.getInstance().getIslandAt(location);
            return player.hasPermission("acidisland.mod.bypassprotect") ||
                    is.getOwner().equals(player.getUniqueId()) || is.getMembers().contains(player.getUniqueId());
        } catch(NullPointerException e){
            return false;
        }
    }

}
