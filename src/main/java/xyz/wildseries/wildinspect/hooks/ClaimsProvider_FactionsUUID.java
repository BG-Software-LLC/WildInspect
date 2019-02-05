package xyz.wildseries.wildinspect.hooks;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Role;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class ClaimsProvider_FactionsUUID implements ClaimsProvider {

    @Override
    public boolean hasRole(Player player, String role){
        return FPlayers.getInstance().getByPlayer(player).getRole().isAtLeast(Role.valueOf(role));
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location){
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        return fPlayer.isAdminBypassing() ||
                (fPlayer.hasFaction() && fPlayer.getFaction().equals(Board.getInstance().getFactionAt(new FLocation(location))));
    }

}
