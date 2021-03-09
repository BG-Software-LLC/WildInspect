package com.bgsoftware.wildinspect.hooks;

import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.plot.Plot;
import org.bukkit.entity.Player;

public final class ClaimsProvider_PlotSquared implements ClaimsProvider {

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.PLOTSQUARED;
    }

    @Override
    public boolean hasRole(Player player, org.bukkit.Location location, String... role) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player player, org.bukkit.Location location) {
        Location loc = new Location(location.getWorld().getName(),location.getBlockX(),location.getBlockY(),location.getBlockZ());
        Plot plot = PlotSquared.get().getPlotAreaManager().getPlotArea(loc).getOwnedPlot(loc);

        if (plot == null) return false;
        return (plot.isAdded(player.getUniqueId()) || plot.isOwner(player.getUniqueId()));
    }
}