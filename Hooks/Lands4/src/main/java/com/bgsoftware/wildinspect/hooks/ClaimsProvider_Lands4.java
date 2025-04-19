package com.bgsoftware.wildinspect.hooks;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.LandArea;
import me.angeschossen.lands.api.role.enums.RoleSetting;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public final class ClaimsProvider_Lands4 implements ClaimsProvider {

    private final LandsIntegration landsIntegration;

    public ClaimsProvider_Lands4() {
        landsIntegration = new LandsIntegration(WildInspectPlugin.getPlugin(), false);
        landsIntegration.initialize();

        WildInspectPlugin.log(" - Using Lands as ClaimsProvider.");
    }

    public static boolean isCompatible() {
        try {
            Class.forName("me.angeschossen.lands.api.role.enums.RoleSetting");
            return true;
        } catch (ClassNotFoundException error) {
            return false;
        }
    }

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.LANDS;
    }

    @Override
    public boolean hasRole(Player player, Location location, Collection<String> roles) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        LandArea landArea = landsIntegration.getArea(location);
        return landArea == null || landArea.canSetting(player.getUniqueId(), RoleSetting.BLOCK_PLACE);
    }

}
