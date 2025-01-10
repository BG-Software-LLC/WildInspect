package com.bgsoftware.wildinspect.hooks;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Locale;

public final class ClaimsProvider_MassiveFactions implements ClaimsProvider {

    public ClaimsProvider_MassiveFactions() {
        WildInspectPlugin.log(" - Using MassiveFactions as ClaimsProvider.");
    }

    @Override
    public ClaimPlugin getClaimPlugin() {
        return ClaimPlugin.MASSIVE_FACTIONS;
    }

    @Override
    public boolean hasRole(Player pl, Location location, Collection<String> roles) {
        MPlayer mPlayer = MPlayer.get(pl);
        return roles.contains(mPlayer.getRole().name().toLowerCase(Locale.ENGLISH));
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        MPlayer mPlayer = MPlayer.get(player);
        boolean overriding = false;

        try {
            overriding = mPlayer.isOverriding();
        } catch (Throwable ex) {
            try {
                overriding = (boolean) mPlayer.getClass().getMethod("isUsingAdminMode").invoke(mPlayer);
            } catch (Exception ignored) {
            }
        }

        return overriding || (mPlayer.hasFaction() && mPlayer.getFaction().equals(BoardColl.get().getFactionAt(PS.valueOf(location))));
    }
}
