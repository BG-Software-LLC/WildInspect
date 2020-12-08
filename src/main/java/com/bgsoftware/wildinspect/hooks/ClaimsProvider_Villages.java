package com.bgsoftware.wildinspect.hooks;

import com.stefthedev.villages.Villages;
import com.stefthedev.villages.villages.Village;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class ClaimsProvider_Villages implements ClaimsProvider {

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.VILLAGES;
    }

    @Override
    public boolean hasRole(Player player, Location location, String... role) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        Village playerVillage = Villages.getInstance().getVillageManager().village(player);
        Village locationVillage = Villages.getInstance().getVillageManager().village(location.getChunk());
        return playerVillage != null && playerVillage == locationVillage;
    }
}
