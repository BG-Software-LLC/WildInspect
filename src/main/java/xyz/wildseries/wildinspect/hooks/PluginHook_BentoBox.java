package xyz.wildseries.wildinspect.hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;

import java.util.Optional;

public final class PluginHook_BentoBox implements PluginHook {

    private BentoBox plugin = BentoBox.getInstance();

    @Override
    public boolean hasRole(Player pl, String role) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player pl, Location loc) {
        Optional<Island> island = plugin.getIslands().getIslandAt(loc);
        return island.isPresent() && island.get().getMembers().containsKey(pl.getUniqueId());
    }
}
