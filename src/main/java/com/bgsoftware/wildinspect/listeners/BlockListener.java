package com.bgsoftware.wildinspect.listeners;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import com.bgsoftware.wildinspect.utils.InspectPlayers;
import com.bgsoftware.wildinspect.coreprotect.LookupType;

import com.bgsoftware.wildinspect.utils.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@SuppressWarnings("unused")
public final class BlockListener implements Listener {

    private WildInspectPlugin plugin;

    public BlockListener(WildInspectPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent e){
        if(!InspectPlayers.isInspectEnabled(e.getPlayer()))
            return;

        if(isOffhand(e))
            return;

        e.setCancelled(true);

        if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
            plugin.getCoreProtect().performLookup(LookupType.BLOCK_LOOKUP, e.getPlayer(), e.getClickedBlock(), 1);
            InspectPlayers.setClickMode(e.getPlayer(), Action.LEFT_CLICK_BLOCK);
        }

        else if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(ItemUtils.isContainer(e.getClickedBlock().getType())){
                plugin.getCoreProtect().performLookup(LookupType.CHEST_TRANSACTIONS, e.getPlayer(), e.getClickedBlock(), 1);
                InspectPlayers.setClickMode(e.getPlayer(), Action.RIGHT_CLICK_BLOCK);
            }
            else if (e.getItem() != null && e.getItem().getType().isBlock()) {
                plugin.getCoreProtect().performLookup(LookupType.BLOCK_LOOKUP, e.getPlayer(), e.getClickedBlock().getRelative(e.getBlockFace()), 1);
                InspectPlayers.setClickMode(e.getPlayer(), Action.LEFT_CLICK_BLOCK);
            }else{
                plugin.getCoreProtect().performLookup(LookupType.INTERACTION_LOOKUP, e.getPlayer(), e.getClickedBlock(), 1);
                InspectPlayers.setClickMode(e.getPlayer(), Action.RIGHT_CLICK_BLOCK);
            }
        }
    }

    private boolean isOffhand(PlayerInteractEvent event){
        try{
            return event.getClass().getMethod("getHand").invoke(event).toString().equals("OFF_HAND");
        }catch(Throwable ignored){}
        return false;
    }

}
