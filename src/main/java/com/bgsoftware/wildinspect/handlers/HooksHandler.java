package com.bgsoftware.wildinspect.handlers;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_ASkyBlock;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_AcidIsland;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_BentoBox;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_FactionsUUID;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_FactionsX;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_GriefPrevention;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_Lands;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_Lazarus;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_MassiveFactions;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_PlotSquared4;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_PlotSquared5;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_PlotSquaredLegacy;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_SuperiorSkyblock;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_Towny;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_Villages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.EnumMap;
import java.util.Map;

public final class HooksHandler {

    private final EnumMap<ClaimsProvider.ClaimPlugin, ClaimsProvider> claimsProviders = new EnumMap<>(ClaimsProvider.ClaimPlugin.class);

    public HooksHandler(WildInspectPlugin plugin){
        Bukkit.getScheduler().runTask(plugin, this::loadHookups);
    }

    public boolean hasRole(ClaimsProvider.ClaimPlugin claimPlugin, Player player, Location location, String... roles) {
        ClaimsProvider claimsProvider = claimsProviders.get(claimPlugin);
        return claimsProvider == null || claimsProvider.hasRole(player, location, roles);
    }

    public ClaimsProvider.ClaimPlugin getRegionAt(Player player, Location location) {
        for(Map.Entry<ClaimsProvider.ClaimPlugin, ClaimsProvider> entry : claimsProviders.entrySet()) {
            if(entry.getValue().hasRegionAccess(player, location))
                return entry.getKey();
        }

        return claimsProviders.isEmpty() ? ClaimsProvider.ClaimPlugin.DEFAULT : ClaimsProvider.ClaimPlugin.NONE;
    }

    private void loadHookups(){
        WildInspectPlugin.log("Loading providers started...");
        long startTime = System.currentTimeMillis();
        //Checks if AcidIsland is installed
        if(Bukkit.getPluginManager().isPluginEnabled("AcidIsland")){
            claimsProviders.put(ClaimsProvider.ClaimPlugin.ACID_ISLAND, new ClaimsProvider_AcidIsland());
            WildInspectPlugin.log(" - Using AcidIsland as ClaimsProvider.");
        }
        //Checks if ASkyBlock is installed
        if(Bukkit.getPluginManager().isPluginEnabled("ASkyBlock")){
            claimsProviders.put(ClaimsProvider.ClaimPlugin.ASKYBLOCK, new ClaimsProvider_ASkyBlock());
            WildInspectPlugin.log(" - Using ASkyBlock as ClaimsProvider.");
        }
        //Checks if BentoBox is installed
        if(Bukkit.getPluginManager().isPluginEnabled("BentoBox")){
            claimsProviders.put(ClaimsProvider.ClaimPlugin.BENTOBOX, new ClaimsProvider_BentoBox());
            WildInspectPlugin.log(" - Using BentoBox as ClaimsProvider.");
        }
        //Checks if Factions is installed
        if(Bukkit.getPluginManager().isPluginEnabled("Factions")){
            if(!Bukkit.getPluginManager().getPlugin("Factions").getDescription().getAuthors().contains("drtshock")){
                claimsProviders.put(ClaimsProvider.ClaimPlugin.MASSIVE_FACTIONS, new ClaimsProvider_MassiveFactions());
                WildInspectPlugin.log(" - Using MassiveFactions as ClaimsProvider.");
            }else {
                claimsProviders.put(ClaimsProvider.ClaimPlugin.FACTIONSUUID, new ClaimsProvider_FactionsUUID());
                WildInspectPlugin.log(" - Using FactionsUUID as ClaimsProvider.");
            }
        }
        //Checks if FactionsX is installed
        if(Bukkit.getPluginManager().isPluginEnabled("FactionsX")){
            claimsProviders.put(ClaimsProvider.ClaimPlugin.FACTIONSX, new ClaimsProvider_FactionsX());
            WildInspectPlugin.log(" - Using FactionsX as ClaimsProvider.");
        }
        //Checks if GriefPrevention is installed
        if(Bukkit.getPluginManager().isPluginEnabled("GriefPrevention")){
            claimsProviders.put(ClaimsProvider.ClaimPlugin.GRIEF_PREVENTION, new ClaimsProvider_GriefPrevention());
            WildInspectPlugin.log(" - Using GriefPrevention as ClaimsProvider.");
        }
        //Checks if SuperiorSkyblock is installed
        if(Bukkit.getPluginManager().isPluginEnabled("SuperiorSkyblock2")){
            claimsProviders.put(ClaimsProvider.ClaimPlugin.SUPERIOR_SKYBLOCK, new ClaimsProvider_SuperiorSkyblock());
            WildInspectPlugin.log(" - Using SuperiorSkyblock as ClaimsProvider.");
        }
        //Checks if Towny is installed
        if(Bukkit.getPluginManager().isPluginEnabled("Towny")){
            claimsProviders.put(ClaimsProvider.ClaimPlugin.TOWNY, new ClaimsProvider_Towny());
            WildInspectPlugin.log(" - Using Towny as ClaimsProvider.");
        }
        //Checks if Villages is installed
        if(Bukkit.getPluginManager().isPluginEnabled("Villages")){
            claimsProviders.put(ClaimsProvider.ClaimPlugin.VILLAGES, new ClaimsProvider_Villages());
            WildInspectPlugin.log(" - Using Villages as ClaimsProvider.");
        }
        //Checks if Lands is installed
        if(Bukkit.getPluginManager().isPluginEnabled("Lands")){
            claimsProviders.put(ClaimsProvider.ClaimPlugin.LANDS, new ClaimsProvider_Lands());
            WildInspectPlugin.log(" - Using Lands as ClaimsProvider.");
        }
        //Checks if Lazarus is installed
        if(Bukkit.getPluginManager().isPluginEnabled("Lazarus")){
            claimsProviders.put(ClaimsProvider.ClaimPlugin.LAZARUS, new ClaimsProvider_Lazarus());
            WildInspectPlugin.log(" - Using Lazarus as ClaimsProvider.");
        }
        //Checks if PlotSquared is installed
        if(Bukkit.getPluginManager().isPluginEnabled("PlotSquared")){
            Plugin plugin = Bukkit.getPluginManager().getPlugin("PlotSquared");
            if(plugin.getDescription().getVersion().startsWith("4")){
                claimsProviders.put(ClaimsProvider.ClaimPlugin.PLOT_SQUARED, new ClaimsProvider_PlotSquared4());
            }
            else if(plugin.getDescription().getVersion().startsWith("5")){
                claimsProviders.put(ClaimsProvider.ClaimPlugin.PLOT_SQUARED, new ClaimsProvider_PlotSquared5());
            }
            else{
                claimsProviders.put(ClaimsProvider.ClaimPlugin.PLOT_SQUARED, new ClaimsProvider_PlotSquaredLegacy());
            }
            WildInspectPlugin.log(" - Using PlotSquared as ClaimsProvider.");
        }
        WildInspectPlugin.log("Loading providers done (Took " + (System.currentTimeMillis() - startTime) + "ms)");
    }

}
