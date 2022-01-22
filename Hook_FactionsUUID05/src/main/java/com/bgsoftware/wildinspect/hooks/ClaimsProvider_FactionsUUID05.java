package com.bgsoftware.wildinspect.hooks;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;

public final class ClaimsProvider_FactionsUUID05 implements ClaimsProvider {

    public ClaimsProvider_FactionsUUID05() {
        WildInspectPlugin.log(" - Using FactionsUUID as ClaimsProvider.");
    }

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.FACTIONSUUID;
    }

    @Override
    public boolean hasRole(Player player, Location location, String... roles) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        return Arrays.asList(roles).contains(fPlayer.getRole().name());
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        return fPlayer.isAdminBypassing() || (fPlayer.hasFaction() &&
                fPlayer.getFaction().equals(Board.getInstance().getFactionAt(new FLocation(location))));
    }

}
