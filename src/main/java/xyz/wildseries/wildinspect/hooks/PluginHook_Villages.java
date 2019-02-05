package xyz.wildseries.wildinspect.hooks;

import com.stefthedev.villages.Villages;
import com.stefthedev.villages.villages.Village;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class PluginHook_Villages implements PluginHook {

    @Override
    public boolean hasRole(Player pl, String role) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player pl, Location loc) {
        Village playerVillage = Villages.getInstance().getVillageManager().village(pl);
        Village locationVillage = Villages.getInstance().getVillageManager().village(loc.getChunk());
        return playerVillage != null && playerVillage == locationVillage;
    }
}
