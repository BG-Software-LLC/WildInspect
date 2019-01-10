package xyz.wildseries.wildinspect.hooks;

import com.wasteofplastic.acidisland.ASkyBlockAPI;
import com.wasteofplastic.acidisland.Island;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class PluginHook_AcidIsland implements PluginHook {

    @Override
    public boolean hasRole(Player pl, String role) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player pl, Location loc) {
        try {
            Island is = ASkyBlockAPI.getInstance().getIslandAt(loc);
            return pl.hasPermission("acidisland.mod.bypassprotect") ||
                    is.getOwner().equals(pl.getUniqueId()) || is.getMembers().contains(pl.getUniqueId());
        } catch(NullPointerException e){
            return false;
        }
    }

}
