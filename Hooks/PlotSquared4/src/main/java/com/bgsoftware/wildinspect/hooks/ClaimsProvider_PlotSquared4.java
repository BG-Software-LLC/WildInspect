package com.bgsoftware.wildinspect.hooks;

import com.github.intellectualsites.plotsquared.plot.object.Location;
import com.github.intellectualsites.plotsquared.plot.object.Plot;
import org.bukkit.entity.Player;

import java.util.Collection;

public final class ClaimsProvider_PlotSquared4 implements ClaimsProvider {

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.PLOT_SQUARED;
    }

    @Override
    public boolean hasRole(Player player, org.bukkit.Location bukkitLocation, Collection<String> roles) {
        Location location = new Location(bukkitLocation.getWorld().getName(), bukkitLocation.getBlockX(),
                bukkitLocation.getBlockY(), bukkitLocation.getBlockZ());
        Plot plot = location.getOwnedPlot();
        assert plot != null; // Already checked in hasRegionAccess
        return roles.contains(plot.isOwner(player.getUniqueId()) ? "owner" : "trusted");
    }

    @Override
    public boolean hasRegionAccess(Player player, org.bukkit.Location bukkitLocation) {
        Location location = new Location(bukkitLocation.getWorld().getName(), bukkitLocation.getBlockX(),
                bukkitLocation.getBlockY(), bukkitLocation.getBlockZ());
        Plot plot = location.getOwnedPlot();
        return plot != null && (plot.isAdded(player.getUniqueId()) || plot.isOwner(player.getUniqueId()));
    }

}
