package com.bgsoftware.wildinspect.handlers;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_ASkyBlock;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_AcidIsland;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_BentoBox;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_FactionsUUID;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_GriefPrevention;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_MassiveFactions;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_SuperiorSkyblock;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_Towny;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider_Villages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class HooksHandler {

    private List<ClaimsProvider> claimsProviders = new ArrayList<>();

    public HooksHandler(WildInspectPlugin plugin){
        Bukkit.getScheduler().runTask(plugin, this::loadHookups);
    }

    public boolean hasRole(Player player, String role) {
        for(ClaimsProvider provider : claimsProviders){
            if(!provider.hasRole(player, role))
                return false;
        }
        return true;
    }

    public boolean hasRegionAccess(Player player, Location location) {
        for(ClaimsProvider provider : claimsProviders){
            if(!provider.hasRegionAccess(player, location))
                return false;
        }
        return true;
    }

    private void loadHookups(){
        WildInspectPlugin.log("Loading providers started...");
        long startTime = System.currentTimeMillis();
        //Checks if AcidIsland is installed
        if(Bukkit.getPluginManager().isPluginEnabled("AcidIsland")){
            claimsProviders.add(new ClaimsProvider_AcidIsland());
            WildInspectPlugin.log(" - Using AcidIsland as ClaimsProvider.");
        }
        //Checks if ASkyBlock is installed
        if(Bukkit.getPluginManager().isPluginEnabled("ASkyBlock")){
            claimsProviders.add(new ClaimsProvider_ASkyBlock());
            WildInspectPlugin.log(" - Using ASkyBlock as ClaimsProvider.");
        }
        //Checks if BentoBox is installed
        if(Bukkit.getPluginManager().isPluginEnabled("BentoBox")){
            claimsProviders.add(new ClaimsProvider_BentoBox());
            WildInspectPlugin.log(" - Using BentoBox as ClaimsProvider.");
        }
        //Checks if Factions is installed
        if(Bukkit.getPluginManager().isPluginEnabled("Factions")){
            if(Bukkit.getPluginManager().isPluginEnabled("MassiveCore")){
                claimsProviders.add(new ClaimsProvider_MassiveFactions());
                WildInspectPlugin.log(" - Using MassiveFactions as ClaimsProvider.");
            }else {
                claimsProviders.add(new ClaimsProvider_FactionsUUID());
                WildInspectPlugin.log(" - Using FactionsUUID as ClaimsProvider.");
            }
        }
        //Checks if GriefPrevention is installed
        if(Bukkit.getPluginManager().isPluginEnabled("GriefPrevention")){
            claimsProviders.add(new ClaimsProvider_GriefPrevention());
            WildInspectPlugin.log(" - Using GriefPrevention as ClaimsProvider.");
        }
        //Checks if SuperiorSkyblock is installed
        if(Bukkit.getPluginManager().isPluginEnabled("SuperiorSkyblock2")){
            claimsProviders.add(new ClaimsProvider_SuperiorSkyblock());
            WildInspectPlugin.log(" - Using SuperiorSkyblock as ClaimsProvider.");
        }
        //Checks if Towny is installed
        if(Bukkit.getPluginManager().isPluginEnabled("Towny")){
            claimsProviders.add(new ClaimsProvider_Towny());
            WildInspectPlugin.log(" - Using Towny as ClaimsProvider.");
        }
        //Checks if Villages is installed
        if(Bukkit.getPluginManager().isPluginEnabled("Villages")){
            claimsProviders.add(new ClaimsProvider_Villages());
            WildInspectPlugin.log(" - Using Villages as ClaimsProvider.");
        }
        WildInspectPlugin.log("Loading providers done (Took " + (System.currentTimeMillis() - startTime) + "ms)");
    }

}
