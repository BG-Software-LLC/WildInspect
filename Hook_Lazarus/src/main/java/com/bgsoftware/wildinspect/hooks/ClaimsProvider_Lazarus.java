package com.bgsoftware.wildinspect.hooks;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;

public final class ClaimsProvider_Lazarus implements ClaimsProvider {

    public ClaimsProvider_Lazarus() {
        WildInspectPlugin.log(" - Using Lazarus as ClaimsProvider.");
    }

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.LAZARUS;
    }

    @Override
    public boolean hasRole(Player pl, Location location, String... roles) {
        PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(pl);
        String roleName = playerFaction == null ? "" : playerFaction.getMember(pl).getRole().getName().toUpperCase();
        return Arrays.asList(roles).contains(roleName);
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        Faction faction = ClaimManager.getInstance().getFactionAt(location);
        PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(player);
        return (player.getGameMode() == GameMode.CREATIVE && player.hasPermission("lazarus.factions.bypass")) || faction == playerFaction;
    }

}
