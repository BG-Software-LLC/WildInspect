package com.bgsoftware.wildinspect.hooks;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import com.stefthedev.villages.Villages;
import com.stefthedev.villages.villages.Village;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public final class ClaimsProvider_Villages implements ClaimsProvider {

    public ClaimsProvider_Villages() {
        WildInspectPlugin.log(" - Using Villages as ClaimsProvider.");
    }

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.VILLAGES;
    }

    @Override
    public boolean hasRole(Player player, Location location, Collection<String> roles) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        Village playerVillage = Villages.getInstance().getVillageManager().village(player);
        Village locationVillage = Villages.getInstance().getVillageManager().village(location.getChunk());
        return playerVillage != null && playerVillage == locationVillage;
    }
}
