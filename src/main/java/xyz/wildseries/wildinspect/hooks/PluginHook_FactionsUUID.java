package xyz.wildseries.wildinspect.hooks;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Role;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class PluginHook_FactionsUUID implements PluginHook {

    @Override
    public boolean hasRole(Player pl, String role){
        return FPlayers.getInstance().getByPlayer(pl).getRole().isAtLeast(Role.valueOf(role));
    }

    @Override
    public boolean hasRegionAccess(Player pl, Location loc){
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(pl);
        return fPlayer.isAdminBypassing() ||
                (fPlayer.hasFaction() && fPlayer.getFaction().equals(Board.getInstance().getFactionAt(new FLocation(loc))));
    }

}
