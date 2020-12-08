package com.bgsoftware.wildinspect.hooks;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.perms.Role;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.Arrays;

public final class ClaimsProvider_FactionsUUID implements ClaimsProvider {

    private static Method getRoleMethod;

    static {
        try{
            getRoleMethod = FPlayer.class.getMethod("getRole");
        }catch(Exception ignored){}
    }

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.FACTIONSUUID;
    }

    @Override
    public boolean hasRole(Player player, Location location, String... roles){
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        try {
            Role role = (Role) getRoleMethod.invoke(fPlayer);
            return Arrays.asList(roles).contains(role.name());
        }catch(Throwable ex){
            return Arrays.asList(roles).contains(fPlayer.getRole().name());
        }
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location){
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        return fPlayer.isAdminBypassing() ||
                (fPlayer.hasFaction() && fPlayer.getFaction().equals(Board.getInstance().getFactionAt(new FLocation(location))));
    }

}
