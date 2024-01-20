package com.bgsoftware.wildinspect.hooks;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.claim.TrustTypes;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class ClaimsProvider_GriefDefender implements ClaimsProvider {

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.GRIEF_DEFENDER;
    }

    @Override
    public boolean hasRole(Player player, Location location, String... role) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        Claim claim = GriefDefender.getCore().getClaimAt(location);
        return claim != null && claim.isUserTrusted(player.getUniqueId(), TrustTypes.BUILDER);
    }

}
