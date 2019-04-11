package com.bgsoftware.wildinspect.coreprotect;

import com.bgsoftware.wildinspect.Locale;
import com.bgsoftware.wildinspect.WildInspectPlugin;
import com.bgsoftware.wildinspect.utils.InspectPlayers;
import net.coreprotect.database.Database;
import net.coreprotect.database.Lookup;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"SameParameterValue", "JavaReflectionMemberAccess"})
public final class CoreProtectHook_API6 implements CoreProtectHook {

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
                        resultLines = interaction_lookup(statement, bl, pl.getName(), 0, page, 7).split("\n");
                        break;
                    case BLOCK_LOOKUP:
                        resultLines = block_lookup(statement, bl.getState(), pl.getName(), 0, page, 7).split("\n");
                        break;
                    case CHEST_TRANSACTIONS:
                        resultLines = chest_transactions(statement, bl.getLocation(), pl.getName(), page, 7, false).split("\n");
                        break;
                    default:
                        return;
                }

                Matcher matcher;

                for(String line : resultLines){
                    if((matcher = Pattern.compile("§3CoreProtect §f- §fNo (.*) found (.*).").matcher(line)).matches()){
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
                        Locale.INSPECT_DATA_FOOTER.send(pl, matcher.group(1), matcher.group(2));
                    }
                }
                statement.close();
                connection.close();
            } catch(SQLException ex){
                ex.printStackTrace();
            }
        }).start();
    }

    private String interaction_lookup(Statement statement, Block block, String playerName, int offset, int page, int limit){
        try{
            return (String) Lookup.class.getMethod("interaction_lookup", Statement.class, Block.class, String.class, int.class, int.class, int.class)
                    .invoke(null, statement, block, playerName, offset, page, limit);
        }catch(Exception ex){
            ex.printStackTrace();
            return "";
        }
    }

    private String block_lookup(Statement statement, BlockState block, String playerName, int offset, int page, int limit){
        try{
            return (String) Lookup.class.getMethod("block_lookup", Statement.class, BlockState.class, String.class, int.class, int.class, int.class)
                    .invoke(null, statement, block, playerName, offset, page, limit);
        }catch(Exception ex){
            ex.printStackTrace();
            return "";
        }
    }

    private String chest_transactions(Statement statement, Location location, String playerName, int page, int limit, boolean exact){
        try{
            return (String) Lookup.class.getMethod("chest_transactions", Statement.class, Location.class, String.class, int.class, int.class, boolean.class)
                    .invoke(null, statement, location, playerName, page, limit, exact);
        }catch(Exception ex){
            ex.printStackTrace();
            return "";
        }
    }
}
