package com.bgsoftware.wildinspect.hooks;

import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.palmergames.bukkit.towny.object.WorldCoord;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;

public final class ClaimsProvider_Towny implements ClaimsProvider {

    @Override
    public boolean hasRole(Player player, String... roles) {
        try{
            Resident resident = TownyUniverse.getDataSource().getResident(player.getName());
            return Arrays.stream(roles).anyMatch(resident::hasTownRank);
        }catch(Exception ignored){}

        return false;
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        try {
            TownBlock block = WorldCoord.parseWorldCoord(location).getTownBlock();
            Resident resident = TownyUniverse.getDataSource().getResident(player.getName());

            return resident.hasTown() && resident.getTown().hasTownBlock(block);
        } catch (Exception ignored) {}
        return false;
    }
}
