package com.bgsoftware.wildinspect.hooks;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import me.angeschossen.lands.api.enums.LandsAction;
import me.angeschossen.lands.api.landsaddons.LandsAddon;
import me.angeschossen.lands.api.objects.LandChunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class ClaimsProvider_Lands implements ClaimsProvider {

    private LandsAddon landsAddon;

    public ClaimsProvider_Lands(){
        landsAddon = new LandsAddon(WildInspectPlugin.getPlugin(), false);
    }

    @Override
    public boolean hasRole(Player player, Location location, String... role) {
        return true;
    }

    @Override
    public boolean hasRegionAccess(Player player, Location location) {
        LandChunk landChunk = landsAddon.getLandChunkHard(location.getWorld().getName(), location.getChunk().getX(), location.getChunk().getZ());
        return landChunk == null || landChunk.canAction(player.getUniqueId().toString(), LandsAction.BLOCK_PLACE);
    }
}
