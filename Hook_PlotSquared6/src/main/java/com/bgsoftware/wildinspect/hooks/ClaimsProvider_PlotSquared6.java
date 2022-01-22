package com.bgsoftware.wildinspect.hooks;

import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.plot.Plot;
import org.bukkit.entity.Player;

import java.util.Arrays;

public final class ClaimsProvider_PlotSquared6 implements ClaimsProvider {

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.PLOT_SQUARED;
    }

    @Override
    public boolean hasRole(Player player, org.bukkit.Location bukkitLocation, String... role) {
        Location location = BukkitUtil.adaptComplete(bukkitLocation);
        Plot plot = location.getOwnedPlot();
        assert plot != null; // Already checked in hasRegionAccess
        return Arrays.asList(role).contains(plot.isOwner(player.getUniqueId()) ? "OWNER" : "TRUSTED");
    }

    @Override
    public boolean hasRegionAccess(Player player, org.bukkit.Location bukkitLocation) {
        Location location = BukkitUtil.adaptComplete(bukkitLocation);
        Plot plot = location.getOwnedPlot();
        return plot != null && (plot.isAdded(player.getUniqueId()) || plot.isOwner(player.getUniqueId()));
    }

}
