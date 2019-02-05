package xyz.wildseries.wildinspect.hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ClaimsProvider {

    boolean hasRole(Player player, String role);

    boolean hasRegionAccess(Player player, Location location);

}
