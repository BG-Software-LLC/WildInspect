package com.bgsoftware.wildinspect.handlers;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider;
import com.bgsoftware.wildinspect.scheduler.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public final class HooksHandler {

    private final EnumMap<ClaimsProvider.ClaimPlugin, ClaimsProvider> claimsProviders = new EnumMap<>(ClaimsProvider.ClaimPlugin.class);

    private final WildInspectPlugin plugin;

    public HooksHandler(WildInspectPlugin plugin) {
        this.plugin = plugin;
        Scheduler.runTask(this::loadHookups);
    }

    public boolean hasRole(ClaimsProvider.ClaimPlugin claimPlugin, Player player, Location location, String... roles) {
        ClaimsProvider claimsProvider = claimsProviders.get(claimPlugin);
        return claimsProvider == null || claimsProvider.hasRole(player, location, roles);
    }

    public ClaimsProvider.ClaimPlugin getRegionAt(Player player, Location location) {
        for (Map.Entry<ClaimsProvider.ClaimPlugin, ClaimsProvider> entry : claimsProviders.entrySet()) {
            if (entry.getValue().hasRegionAccess(player, location))
                return entry.getKey();
        }

        return claimsProviders.isEmpty() ? ClaimsProvider.ClaimPlugin.DEFAULT : ClaimsProvider.ClaimPlugin.NONE;
    }

    private void registerClaimsProvider(ClaimsProvider claimsProvider) {
        this.claimsProviders.put(claimsProvider.getClaimPlugin(), claimsProvider);
    }

    private void loadHookups() {
        WildInspectPlugin.log("Loading providers started...");
        long startTime = System.currentTimeMillis();

        if (Bukkit.getPluginManager().isPluginEnabled("AcidIsland")) {
            Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_AcidIsland");
            claimsProvider.ifPresent(this::registerClaimsProvider);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("ASkyBlock")) {
            Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_ASkyBlock");
            claimsProvider.ifPresent(this::registerClaimsProvider);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("BentoBox")) {
            Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_BentoBox");
            claimsProvider.ifPresent(this::registerClaimsProvider);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Factions")) {
            Plugin factions = Bukkit.getPluginManager().getPlugin("Factions");
            if (factions.getDescription().getAuthors().contains("drtshock")) {
                if (factions.getDescription().getVersion().startsWith("1.6.9.5-U0.5")) {
                    Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_FactionsUUID05");
                    claimsProvider.ifPresent(this::registerClaimsProvider);
                } else {
                    Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_FactionsUUID02");
                    claimsProvider.ifPresent(this::registerClaimsProvider);
                }
            } else {
                Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_MassiveFactions");
                claimsProvider.ifPresent(this::registerClaimsProvider);
            }
        }
        if (Bukkit.getPluginManager().isPluginEnabled("FactionsX")) {
            Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_FactionsX");
            claimsProvider.ifPresent(this::registerClaimsProvider);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("GriefDefender")) {
            Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_GriefDefender");
            claimsProvider.ifPresent(this::registerClaimsProvider);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("GriefPrevention")) {
            Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_GriefPrevention");
            claimsProvider.ifPresent(this::registerClaimsProvider);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Lands")) {
            Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_Lands4");
            if (!claimsProvider.isPresent())
                claimsProvider = createInstance("ClaimsProvider_Lands7");
            claimsProvider.ifPresent(this::registerClaimsProvider);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Lazarus")) {
            Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_Lazarus");
            claimsProvider.ifPresent(this::registerClaimsProvider);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlotSquared")) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("PlotSquared");
            if (plugin.getDescription().getVersion().startsWith("6.")) {
                try {
                    Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_PlotSquared6");
                    claimsProvider.ifPresent(this::registerClaimsProvider);
                } catch (Exception ex) {
                    WildInspectPlugin.log("&cYour version of PlotSquared is not supported. Please contact Ome_R for support.");
                }
            } else if (plugin.getDescription().getVersion().startsWith("5.")) {
                Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_PlotSquared5");
                claimsProvider.ifPresent(this::registerClaimsProvider);
            } else if (plugin.getDescription().getMain().contains("com.github")) {
                Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_PlotSquared4");
                claimsProvider.ifPresent(this::registerClaimsProvider);
            } else {
                Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_PlotSquaredLegacy");
                claimsProvider.ifPresent(this::registerClaimsProvider);
            }
        }
        if (Bukkit.getPluginManager().isPluginEnabled("SuperiorSkyblock2")) {
            Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_SuperiorSkyblock");
            claimsProvider.ifPresent(this::registerClaimsProvider);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Towny")) {
            Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_Towny");
            claimsProvider.ifPresent(this::registerClaimsProvider);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Villages")) {
            Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_Villages");
            claimsProvider.ifPresent(this::registerClaimsProvider);
        }

        WildInspectPlugin.log("Loading providers done (Took " + (System.currentTimeMillis() - startTime) + "ms)");
    }

    private <T> Optional<T> createInstance(String className) {
        try {
            Class<?> clazz = Class.forName("com.bgsoftware.wildinspect.hooks." + className);
            try {
                Method compatibleMethod = clazz.getDeclaredMethod("isCompatible");
                if (!(boolean) compatibleMethod.invoke(null))
                    return Optional.empty();
            } catch (Exception ignored) {
            }

            try {
                Constructor<?> constructor = clazz.getConstructor(WildInspectPlugin.class);
                // noinspection unchecked
                return Optional.of((T) constructor.newInstance(plugin));
            } catch (Exception error) {
                // noinspection unchecked
                return Optional.of((T) clazz.newInstance());
            }
        } catch (ClassNotFoundException ignored) {
            return Optional.empty();
        } catch (Exception error) {
            error.printStackTrace();
            return Optional.empty();
        }
    }

}
