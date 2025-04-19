package com.bgsoftware.wildinspect.hooks;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import pl.minecodes.plots.api.plot.PlotApi;
import pl.minecodes.plots.api.plot.PlotServiceApi;

import java.util.Collection;

public final class ClaimsProvider_minePlots implements ClaimsProvider {

    private final PlotServiceApi plotService;

    public ClaimsProvider_minePlots() {
        RegisteredServiceProvider<PlotServiceApi> plotServiceProvider =
                Bukkit.getServicesManager().getRegistration(PlotServiceApi.class);
        if (plotServiceProvider == null)
            throw new RuntimeException("Cannot find PlotServiceApi service provider");

        this.plotService = plotServiceProvider.getProvider();
        if (this.plotService == null)
            throw new RuntimeException("Cannot find PlotServiceApi service");

        WildInspectPlugin.log(" - Using minePlots as ClaimsProvider.");
    }

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.MINE_PLOTS;
    }

    @Override
    public boolean hasRole(Player pl, Location location, Collection<String> roles) {
        PlotApi plot = this.plotService.getPlot(location);
        if (plot == null)
            return false;

        for (String role : roles) {
            if (role.equals("member"))
                return plot.getMember(pl.getUniqueId()) != null;
            else if (role.equals("owner"))
                return plot.getOwner().equals(pl.getUniqueId());
        }

        return false;
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        if (player.hasPermission("plots.bypass"))
            return true;

        PlotApi plot = this.plotService.getPlot(location);
        return plot != null && plot.hasAccess(player);
    }
}
