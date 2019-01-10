package xyz.wildseries.wildinspect.hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface PluginHook {

    boolean hasRole(Player pl, String role);

    boolean hasRegionAccess(Player pl, Location loc);

}
