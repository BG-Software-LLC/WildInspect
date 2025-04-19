package com.bgsoftware.wildinspect.command;

import com.bgsoftware.wildinspect.Locale;
import com.bgsoftware.wildinspect.WildInspectPlugin;
import com.bgsoftware.wildinspect.coreprotect.LookupType;
import com.bgsoftware.wildinspect.hooks.ClaimsProvider;
import com.bgsoftware.wildinspect.utils.InspectPlayers;
import com.bgsoftware.wildinspect.utils.ItemUtils;
import com.bgsoftware.wildinspect.utils.StringUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

@SuppressWarnings("unused")
public final class InspectCommand implements Listener {

    private final WildInspectPlugin plugin;

    public InspectCommand(WildInspectPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        List<String> cmdLabels = plugin.getSettings().commands;
        String cmdLabel = null;
        for (String label : cmdLabels) {
            if (e.getMessage().equalsIgnoreCase("/" + label) || e.getMessage().startsWith("/" + label + " ")) {
                cmdLabel = label;
                break;
            }
        }

        if (cmdLabel == null)
            return;

        e.setCancelled(true);

        String label = e.getMessage().split(" ")[0];
        String[] args = e.getMessage().replace(label + " ", "").split(" ");
        Player pl = e.getPlayer();

        if (!plugin.getSettings().inspectPermission.isEmpty() && !
                e.getPlayer().hasPermission(plugin.getSettings().inspectPermission)) {
            Locale.NO_PERMISSION.send(e.getPlayer());
            return;
        }

        if (args.length > 2) {
            Locale.COMMAND_USAGE.send(pl, cmdLabel, "[page]");
            return;
        }

        if (args.length == 2) {
            if (!InspectPlayers.isInspectEnabled(pl) || !InspectPlayers.hasBlock(pl)) {
                Locale.NO_BLOCK_SELECTED.send(pl, label + " " + args[0]);
                return;
            }

            int page;

            try {
                page = Integer.parseInt(args[1]);
            } catch (IllegalArgumentException ex) {
                Locale.SPECIFY_PAGE.send(pl);
                return;
            }

            if (page < 1) {
                Locale.SPECIFY_PAGE.send(pl);
                return;
            }

            Block bl = InspectPlayers.getBlock(pl);

            ClaimsProvider.ClaimPlugin claimPlugin = plugin.getHooksHandler().getRegionAt(pl, bl.getLocation());
            if (!plugin.getHooksHandler().hasRole(claimPlugin, pl, bl.getLocation(), plugin.getSettings().requiredRoles)) {
                Locale.REQUIRED_ROLE.send(pl, StringUtils.format(plugin.getSettings().requiredRoles));
                return;
            }

            Action clickMode = Action.LEFT_CLICK_BLOCK;
            if (InspectPlayers.hasClickMode(pl))
                clickMode = InspectPlayers.getClickMode(pl);

            if (clickMode == Action.LEFT_CLICK_BLOCK) {
                plugin.getCoreProtect().performLookup(LookupType.BLOCK_LOOKUP, pl, bl, page);
            } else if (clickMode == Action.RIGHT_CLICK_BLOCK) {
                if (ItemUtils.isContainer(bl.getType())) {
                    plugin.getCoreProtect().performLookup(LookupType.CHEST_TRANSACTIONS, pl, bl, page);
                    InspectPlayers.setClickMode(e.getPlayer(), Action.RIGHT_CLICK_BLOCK);
                } else {
                    plugin.getCoreProtect().performLookup(LookupType.INTERACTION_LOOKUP, pl, bl, page);
                    InspectPlayers.setClickMode(e.getPlayer(), Action.RIGHT_CLICK_BLOCK);
                }
            }

            return;
        }

        if (InspectPlayers.isInspectEnabled(pl)) {
            InspectPlayers.disableInspectMode(pl);
            Locale.INSPECTOR_OFF.send(pl);
        } else {
            InspectPlayers.enableInspectMode(pl);
            Locale.INSPECTOR_ON.send(pl);
        }


    }

}
