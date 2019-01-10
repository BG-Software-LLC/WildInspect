package xyz.wildseries.wildinspect.command;

import xyz.wildseries.wildinspect.Locale;
import xyz.wildseries.wildinspect.WildInspectPlugin;
import xyz.wildseries.wildinspect.coreprotect.LookupType;
import xyz.wildseries.wildinspect.utils.InspectPlayers;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Set;

@SuppressWarnings("unused")
public final class InspectCommand implements Listener {

    private WildInspectPlugin plugin;

    public InspectCommand(WildInspectPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent e){
        Set<String> cmdLabels = plugin.getSettings().commands;
        String cmdLabel = "";
        for(String label : cmdLabels) {
            if (e.getMessage().equalsIgnoreCase("/" + label) || e.getMessage().startsWith("/" + label + " ")){
                cmdLabel = label;
                break;
            }
        }

        if(cmdLabel.equals(""))
            return;

        e.setCancelled(true);

        String label = e.getMessage().split(" ")[0];
        String[] args = e.getMessage().replace(label + " ", "").split(" ");
        Player pl = e.getPlayer();

        if(!plugin.getHooksHandler().hasRole(pl, plugin.getSettings().requiredRole)){
            Locale.REQUIRED_ROLE.send(pl, plugin.getSettings().requiredRole.toLowerCase());
            return;
        }

        if(args.length > 2){
            Locale.COMMAND_USAGE.send(pl, cmdLabel, "[page]");
            return;
        }

        if(args.length == 2){
            if(!InspectPlayers.isInspectEnabled(pl) || !InspectPlayers.hasBlock(pl)){
                Locale.NO_BLOCK_SELECTED.send(pl, label + " " + args[0]);
                return;
            }

            int page;

            try{
                page = Integer.valueOf(args[1]);
            } catch(IllegalArgumentException ex){
                Locale.SPECIFY_PAGE.send(pl);
                return;
            }

            if(page < 1){
                Locale.SPECIFY_PAGE.send(pl);
                return;
            }

            Block bl = InspectPlayers.getBlock(pl);

            if(!InspectPlayers.hasClickMode(pl) || InspectPlayers.getClickMode(pl) == Action.LEFT_CLICK_BLOCK)
                plugin.getCoreProtect().performLookup(LookupType.BLOCK_LOOKUP, pl, bl, page);
            else plugin.getCoreProtect().performLookup(LookupType.INTERACTION_LOOKUP, pl, bl, page);

            return;
        }

        if(InspectPlayers.isInspectEnabled(pl)){
            InspectPlayers.disableInspectMode(pl);
            Locale.INSPECTOR_OFF.send(pl);
        }

        else{
            InspectPlayers.enableInspectMode(pl);
            Locale.INSPECTOR_ON.send(pl);
        }


    }

}
