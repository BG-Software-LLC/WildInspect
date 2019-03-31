package com.bgsoftware.wildinspect.hooks;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;

public final class ClaimsProvider_MassiveFactions implements ClaimsProvider {

    @Override
    public boolean hasRole(Player pl, String... roles){
        MPlayer mPlayer = MPlayer.get(pl);
        return Arrays.asList(roles).contains(mPlayer.getRole().name());
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
            } catch (Exception ignored) { }
        }

        return overriding || (mPlayer.hasFaction() && mPlayer.getFaction().equals(BoardColl.get().getFactionAt(PS.valueOf(location))));
    }
}
