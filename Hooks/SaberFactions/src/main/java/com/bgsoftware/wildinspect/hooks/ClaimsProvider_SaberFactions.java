package com.bgsoftware.wildinspect.hooks;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Locale;

public final class ClaimsProvider_SaberFactions implements ClaimsProvider {

    public ClaimsProvider_SaberFactions() {
        WildInspectPlugin.log(" - Using SaberFactions as ClaimsProvider.");
    }

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.FACTIONSUUID;
    }

    @Override
    public boolean hasRole(Player player, Location location, Collection<String> roles) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        return roles.contains(fPlayer.getRole().name().toLowerCase(Locale.ENGLISH));
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        return fPlayer.isAdminBypassing() || (fPlayer.hasFaction() &&
                fPlayer.getFaction().equals(Board.getInstance().getFactionAt(FLocation.wrap(location))));
    }

}
