package com.bgsoftware.wildinspect.hooks;

import net.prosavage.core.FPlayer;
import net.prosavage.core.Faction;
import net.prosavage.manager.GridManager;
import net.prosavage.manager.PlayerManager;
import net.prosavage.persist.data.FactionsKt;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.Arrays;

public final class ClaimsProvider_FactionsX implements ClaimsProvider {

    @Override
    public boolean hasRole(Player player, Location location, String... roles){
        FPlayer fPlayer = PlayerManager.INSTANCE.getFPlayer(player);
        return Arrays.asList(roles).contains(fPlayer.getRole().getRoleTag().toUpperCase());
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location){
        FPlayer fPlayer = PlayerManager.INSTANCE.getFPlayer(player);
        Faction faction = GridManager.INSTANCE.getFactionAt(FactionsKt.getFLocation(location));
        return fPlayer.getInBypass() || (fPlayer.hasFaction() && fPlayer.getFaction().equals(faction));
    }

}
