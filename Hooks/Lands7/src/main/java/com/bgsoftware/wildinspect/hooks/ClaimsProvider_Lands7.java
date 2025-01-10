package com.bgsoftware.wildinspect.hooks;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.flags.type.Flags;
import me.angeschossen.lands.api.land.Area;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public final class ClaimsProvider_Lands7 implements ClaimsProvider {

    private final LandsIntegration landsIntegration;

    public ClaimsProvider_Lands7(WildInspectPlugin plugin) {
        landsIntegration = LandsIntegration.of(plugin);
        WildInspectPlugin.log(" - Using Lands as ClaimsProvider.");
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
        Area area = landsIntegration.getArea(location);
        return area == null || area.hasRoleFlag(player.getUniqueId(), Flags.BLOCK_PLACE);
    }

}
