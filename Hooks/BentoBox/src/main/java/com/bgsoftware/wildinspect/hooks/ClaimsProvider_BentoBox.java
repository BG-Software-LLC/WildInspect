package com.bgsoftware.wildinspect.hooks;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;

import java.util.Optional;

public final class ClaimsProvider_BentoBox implements ClaimsProvider {

    public ClaimsProvider_BentoBox() {
        WildInspectPlugin.log(" - Using BentoBox as ClaimsProvider.");
    }

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.BENTOBOX;
    }

    @Override
    public boolean hasRole(Player player, Location location, String... role) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        Optional<Island> island = BentoBox.getInstance().getIslands().getIslandAt(location);
        return island.isPresent() && island.get().getMembers().containsKey(player.getUniqueId());
    }
}
