package com.bgsoftware.wildinspect.hooks;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import net.prosavage.factionsx.core.FPlayer;
import net.prosavage.factionsx.core.Faction;
import net.prosavage.factionsx.manager.GridManager;
import net.prosavage.factionsx.manager.PlayerManager;
import net.prosavage.factionsx.persist.data.FactionsKt;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;

public final class ClaimsProvider_FactionsX implements ClaimsProvider {

    public ClaimsProvider_FactionsX() {
        WildInspectPlugin.log(" - Using FactionsX as ClaimsProvider.");
    }

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.FACTIONSX;
    }

    @Override
    public boolean hasRole(Player player, Location location, String... roles) {
        FPlayer fPlayer = PlayerManager.INSTANCE.getFPlayer(player);
        return Arrays.asList(roles).contains(fPlayer.getRole().getRoleTag().toUpperCase());
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        FPlayer fPlayer = PlayerManager.INSTANCE.getFPlayer(player);
        Faction faction = GridManager.INSTANCE.getFactionAt(FactionsKt.getFLocation(location));
        return fPlayer.getInBypass() || (fPlayer.hasFaction() && fPlayer.getFaction().equals(faction));
    }

}
