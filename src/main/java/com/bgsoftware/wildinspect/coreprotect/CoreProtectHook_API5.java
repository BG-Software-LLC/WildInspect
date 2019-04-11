package com.bgsoftware.wildinspect.coreprotect;

import com.bgsoftware.wildinspect.Locale;
import com.bgsoftware.wildinspect.WildInspectPlugin;
import com.bgsoftware.wildinspect.utils.InspectPlayers;
import net.coreprotect.database.Database;
import net.coreprotect.database.Lookup;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CoreProtectHook_API5 implements CoreProtectHook {

    private WildInspectPlugin plugin = WildInspectPlugin.getPlugin();

    @Override
    public void performLookup(LookupType type, Player pl, Block bl, int page) {
        if(!plugin.getHooksHandler().hasRegionAccess(pl, bl.getLocation())){
            Locale.NOT_INSIDE_CLAIM.send(pl);
            return;
        }

        InspectPlayers.setBlock(pl, bl);

        if(plugin.getSettings().historyLimitPage < page){
            Locale.PAGE_LIMIT_REACH.send(pl);
            return;
        }

        new Thread(() -> {
            try{
                Connection connection = Database.getConnection(false);
                if(connection == null){
                    Bukkit.getScheduler().runTaskLater(plugin, () -> performLookup(type, pl, bl, page), 20L);
                    return;
                }

                Statement statement = connection.createStatement();
                String[] resultLines;

                switch(type){
                    case INTERACTION_LOOKUP:
                        resultLines = Lookup.interaction_lookup(statement, bl, pl.getName(), 0, page, 7).split("\n");
                        break;
                    case BLOCK_LOOKUP:
                        resultLines = Lookup.block_lookup(statement, bl, pl.getName(), 0, page, 7).split("\n");
                        break;
                    case CHEST_TRANSACTIONS:
                        resultLines = Lookup.chest_transactions(statement, bl.getLocation(), pl.getName(), page, 7).split("\n");
                        break;
                    default:
                        return;
                }

                Matcher matcher;

                for(String line : resultLines){
                    if((matcher = Pattern.compile("§3CoreProtect §f- §fNo (.*) found for (.*).").matcher(line)).matches()){
                        switch(matcher.group(1)){
                            case "player interactions":
                                Locale.NO_BLOCK_INTERACTIONS.send(pl, matcher.group(2));
                                break;
                            case "block data":
                                Locale.NO_BLOCK_DATA.send(pl, matcher.group(2));
                                break;
                            case "container transactions":
                                Locale.NO_CONTAINER_TRANSACTIONS.send(pl, matcher.group(2));
                                break;
                        }
                    }
                    else if((matcher = Pattern.compile("§f----- §3(.*) §f----- §7\\(x(.*)/y(.*)/z(.*)\\)").matcher(line)).matches()){
                        Locale.INSPECT_DATA_HEADER.send(pl, matcher.group(2), matcher.group(3), matcher.group(4));
                    }
                    else if((matcher = Pattern.compile("§7(.*) §f- §3(.*) §f(.*) §3(.*)§f.").matcher(line)).matches()){
                        Locale.INSPECT_DATA_ROW.send(pl, matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4));
                    }
                    else if((matcher = Pattern.compile("§fPage (.*)/(.*). View older data by typing \"§3/co l <page>§f\".").matcher(line)).matches()){
                        Locale.INSPECT_DATA_FOOTER.send(pl, matcher.group(1), Math.min(Integer.valueOf(matcher.group(2)), plugin.getSettings().historyLimitPage));
                    }
                }
                statement.close();
                connection.close();
            } catch(SQLException ex){
                ex.printStackTrace();
            }
        }).start();
    }

}
