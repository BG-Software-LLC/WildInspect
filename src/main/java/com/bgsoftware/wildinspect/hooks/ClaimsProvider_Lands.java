package com.bgsoftware.wildinspect.hooks;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.LandArea;
import me.angeschossen.lands.api.role.enums.RoleSetting;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class ClaimsProvider_Lands implements ClaimsProvider {

    private final LandsIntegration landsIntegration;

    public ClaimsProvider_Lands(){
        landsIntegration = new LandsIntegration(WildInspectPlugin.getPlugin(), false);
        landsIntegration.initialize();
    }

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.LANDS;
    }

    @Override
    public boolean hasRole(Player player, Location location, String... role) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        LandArea landArea = landsIntegration.getArea(location);
        return landArea == null || landArea.canSetting(player.getUniqueId(), RoleSetting.BLOCK_PLACE);
    }

}
