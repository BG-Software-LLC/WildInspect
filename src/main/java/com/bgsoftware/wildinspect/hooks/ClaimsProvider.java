package com.bgsoftware.wildinspect.hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface ClaimsProvider {

    ClaimPlugin getClaimPlugin();

    boolean hasRole(Player player, Location location, Collection<String> roles);

    boolean hasRegionAccess(Player player, Location location);

    enum ClaimPlugin {

        ACID_ISLAND,
        ASKYBLOCK,
        BENTOBOX,
        FACTIONSUUID,
        FACTIONSX,
        GRIEF_DEFENDER,
        GRIEF_PREVENTION,
        LANDS,
        LAZARUS,
        MASSIVE_FACTIONS,
        MINE_PLOTS,
        PLOT_SQUARED,
        SUPERIOR_SKYBLOCK,
        TOWNY,
        VILLAGES,

        NONE,
        DEFAULT

    }

}
