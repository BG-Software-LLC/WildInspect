package com.bgsoftware.wildinspect.hooks;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.WorldCoord;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;

public final class ClaimsProvider_Towny implements ClaimsProvider {

    public ClaimsProvider_Towny() {
        WildInspectPlugin.log(" - Using Towny as ClaimsProvider.");
    }

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.TOWNY;
    }

    @Override
    public boolean hasRole(Player player, Location location, String... roles) {
        try {
            Resident resident = TownyAPI.getInstance().getResident(player.getUniqueId());
            return Arrays.stream(roles).anyMatch(resident::hasTownRank) || (Arrays.asList(roles).contains("MAYOR") && resident.isMayor());
        } catch (Exception ignored) {
        }

        return false;
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        try {
            TownBlock block = WorldCoord.parseWorldCoord(location).getTownBlock();
            Resident resident = TownyAPI.getInstance().getResident(player.getUniqueId());

            return resident.hasTown() && resident.getTown().hasTownBlock(block);
        } catch (Exception ignored) {
        }
        return false;
    }

}
