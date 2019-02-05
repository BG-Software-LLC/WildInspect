package xyz.wildseries.wildinspect.hooks;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class PluginHook_MassiveFactions implements PluginHook {

    public boolean hasRole(Player pl, String role){
        return MPlayer.get(pl).getRole().isAtLeast(Rel.valueOf(role));
    }

    public boolean hasRegionAccess(Player pl, Location loc) {
        MPlayer mPlayer = MPlayer.get(pl);
        boolean overriding = false;
        try {
            overriding = mPlayer.isOverriding();
        } catch (Throwable ex) {
            try {
                overriding = (boolean) mPlayer.getClass().getMethod("isUsingAdminMode").invoke(mPlayer);
            } catch (Exception ignored) { }
        }

        return overriding || (mPlayer.hasFaction() && mPlayer.getFaction().equals(BoardColl.get().getFactionAt(PS.valueOf(loc))));
    }
}
