package xyz.wildseries.wildinspect.hooks;

import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.palmergames.bukkit.towny.object.WorldCoord;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class PluginHook_Towny implements PluginHook{

    @Override
    public boolean hasRole(Player pl, String role) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player pl, Location loc) {
        try {
            TownBlock block = WorldCoord.parseWorldCoord(loc).getTownBlock();
            Resident resident = TownyUniverse.getDataSource().getResident(pl.getName());

            return resident.hasTown() && resident.getTown().hasTownBlock(block);
        } catch (Exception ignored) {}
        return false;
    }
}
