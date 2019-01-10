package xyz.wildseries.wildinspect.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import xyz.wildseries.wildinspect.WildInspectPlugin;
import xyz.wildseries.wildinspect.hooks.PluginHook;
import xyz.wildseries.wildinspect.hooks.PluginHook_ASkyBlock;
import xyz.wildseries.wildinspect.hooks.PluginHook_AcidIsland;
import xyz.wildseries.wildinspect.hooks.PluginHook_FactionsUUID;
import xyz.wildseries.wildinspect.hooks.PluginHook_GriefPrevention;
import xyz.wildseries.wildinspect.hooks.PluginHook_MassiveFactions;
import xyz.wildseries.wildinspect.hooks.PluginHook_Towny;

public final class HooksHandler implements PluginHook {

    public HooksHandler(){
        loadHookups();
    }

    @Override
    public boolean hasRole(Player pl, String role) {
        for(Hookup hookup : Hookup.values())
            if(hookup.isEnabled())
                if(!hookup.hook.hasRole(pl, role))
                    return false;
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player pl, Location loc) {
        for(Hookup hookup : Hookup.values())
            if(hookup.isEnabled())
                if(!hookup.hook.hasRegionAccess(pl, loc))
                    return false;
        return true;
    }

    private void loadHookups(){
        WildInspectPlugin.log("Loading providers started...");
        long startTime = System.currentTimeMillis();
        //Checks if Factions is installed
        if(Bukkit.getPluginManager().isPluginEnabled("Factions")){
            if(Bukkit.getPluginManager().isPluginEnabled("MassiveCore")){
                Hookup.MassiveFactions.setEnabled(true);
                WildInspectPlugin.log(" - Using MassiveFactions as ClaimsProvider.");
            }else {
                Hookup.FactionsUUID.setEnabled(true);
                WildInspectPlugin.log(" - Using FactionsUUID as ClaimsProvider.");
            }
        }
        //Checks if ASkyBlock is installed
        if(Bukkit.getPluginManager().isPluginEnabled("ASkyBlock")){
            Hookup.ASkyBlock.setEnabled(true);
            WildInspectPlugin.log(" - Using ASkyBlock as ClaimsProvider.");
        }
        //Checks if AcidIsland is installed
        if(Bukkit.getPluginManager().isPluginEnabled("AcidIsland")){
            Hookup.AcidIsland.setEnabled(true);
            WildInspectPlugin.log(" - Using AcidIsland as ClaimsProvider.");
        }
        //Checks if GriefPrevention is installed
        if(Bukkit.getPluginManager().isPluginEnabled("GriefPrevention")){
            Hookup.GriefPrevention.setEnabled(true);
            WildInspectPlugin.log(" - Using GriefPrevention as ClaimsProvider.");
        }
        //Checks if Towny is installed
        if(Bukkit.getPluginManager().isPluginEnabled("Towny")){
            Hookup.Towny.setEnabled(true);
            WildInspectPlugin.log(" - Using Towny as ClaimsProvider.");
        }
        WildInspectPlugin.log("Loading providers done (Took " + (System.currentTimeMillis() - startTime) + "ms)");
    }

    private enum Hookup{
        FactionsUUID(new PluginHook_FactionsUUID()),
        MassiveFactions(new PluginHook_MassiveFactions()),
        ASkyBlock(new PluginHook_ASkyBlock()),
        GriefPrevention(new PluginHook_GriefPrevention()),
        Towny(new PluginHook_Towny()),
        AcidIsland(new PluginHook_AcidIsland());

        public PluginHook hook;
        private boolean enabled;

        Hookup(PluginHook hook){
            this.hook = hook;
            this.enabled = false;
        }

        public boolean isEnabled(){
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

}
