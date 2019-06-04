package com.bgsoftware.wildinspect.utils;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class InspectPlayers {

    private static final WildInspectPlugin plugin = WildInspectPlugin.getPlugin();

    private static final Map<UUID, Block> inspectModePlayers = new HashMap<>();
    private static final Map<UUID, Action> inspectModeClickMode = new HashMap<>();
    private static final Map<UUID, Long> cooldownPlayers = new HashMap<>();

    public static boolean isInspectEnabled(OfflinePlayer pl){
        return inspectModePlayers.containsKey(pl.getUniqueId());
    }

    public static void enableInspectMode(OfflinePlayer pl){
        if(!inspectModePlayers.containsKey(pl.getUniqueId()))
            inspectModePlayers.put(pl.getUniqueId(), null);
    }

    public static void setBlock(OfflinePlayer pl, Block b){
        if(inspectModePlayers.containsKey(pl.getUniqueId())) {
            inspectModePlayers.remove(pl.getUniqueId());
            inspectModePlayers.put(pl.getUniqueId(), b);
        }
    }

    public static Block getBlock(OfflinePlayer pl){
        if(inspectModePlayers.containsKey(pl.getUniqueId()))
            return inspectModePlayers.get(pl.getUniqueId());
        throw new NullPointerException("Null block for " + pl.getName());
    }

    public static boolean hasBlock(OfflinePlayer pl){
        return getBlock(pl) != null;
    }

    public static Action getClickMode(OfflinePlayer pl){
        if(inspectModeClickMode.containsKey(pl.getUniqueId()))
            return inspectModeClickMode.get(pl.getUniqueId());
        return null;
    }

    public static void setClickMode(OfflinePlayer pl, Action action) {
        inspectModeClickMode.remove(pl.getUniqueId());
        inspectModeClickMode.put(pl.getUniqueId(), action);
    }

    public static boolean hasClickMode(OfflinePlayer pl){
        return getClickMode(pl) != null;
    }

    public static void disableInspectMode(OfflinePlayer pl){
        inspectModePlayers.remove(pl.getUniqueId());
        inspectModeClickMode.remove(pl.getUniqueId());
    }

    public static boolean isCooldown(OfflinePlayer pl){
        if(System.currentTimeMillis() > cooldownPlayers.getOrDefault(pl.getUniqueId(), System.currentTimeMillis()))
            cooldownPlayers.remove(pl.getUniqueId());
        return cooldownPlayers.containsKey(pl.getUniqueId());
    }

    public static long getTimeLeft(OfflinePlayer pl){
        return isCooldown(pl) ? cooldownPlayers.get(pl.getUniqueId()) - System.currentTimeMillis() : 0;
    }

    public static void setCooldown(OfflinePlayer pl){
        cooldownPlayers.put(pl.getUniqueId(), System.currentTimeMillis() + plugin.getSettings().cooldown);
    }

}
