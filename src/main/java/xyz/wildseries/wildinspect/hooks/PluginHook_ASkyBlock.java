package xyz.wildseries.wildinspect.hooks;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class PluginHook_ASkyBlock implements PluginHook {

    @Override
    public boolean hasRole(Player pl, String role) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player pl, Location loc) {
        try {
            Island is = ASkyBlockAPI.getInstance().getIslandAt(loc);
            return pl.hasPermission("askyblock.mod.bypassprotect") ||
                    is.getOwner().equals(pl.getUniqueId()) || is.getMembers().contains(pl.getUniqueId());
        } catch(NullPointerException e){
            return false;
        }
    }
}
