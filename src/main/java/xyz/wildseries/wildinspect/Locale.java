package xyz.wildseries.wildinspect;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public final class Locale {

    private static Map<String, Locale> localeMap = new HashMap<>();

    public static Locale COMMAND_USAGE = new Locale("COMMAND_USAGE");
    public static Locale INSPECTOR_ON = new Locale("INSPECTOR_ON");
    public static Locale INSPECTOR_OFF = new Locale("INSPECTOR_OFF");
    public static Locale INSPECT_DATA_HEADER = new Locale("INSPECT_DATA_HEADER");
    public static Locale INSPECT_DATA_ROW = new Locale("INSPECT_DATA_ROW");
    public static Locale INSPECT_DATA_FOOTER = new Locale("INSPECT_DATA_FOOTER");
    public static Locale NOT_INSIDE_CLAIM = new Locale("NOT_INSIDE_CLAIM");
    public static Locale NO_BLOCK_DATA = new Locale("NO_BLOCK_DATA");
    public static Locale NO_BLOCK_INTERACTIONS = new Locale("NO_BLOCK_INTERACTIONS");
    public static Locale NO_CONTAINER_TRANSACTIONS = new Locale("NO_CONTAINER_TRANSACTIONS");
    public static Locale NO_BLOCK_SELECTED = new Locale("NO_BLOCK_SELECTED");
    public static Locale RELOAD_SUCCESS = new Locale("RELOAD_SUCCESS");
    public static Locale REQUIRED_ROLE = new Locale("REQUIRED_ROLE");
    public static Locale SPECIFY_PAGE = new Locale("SPECIFY_PAGE");


    private Locale(String identifier){
        localeMap.put(identifier, this);
    }

    private String message;

    public String getMessage(Object... objects){
        if(message != null && !message.isEmpty()) {
            String msg = message;

            for (int i = 0; i < objects.length; i++)
                msg = msg.replace("{" + i + "}", objects[i].toString());

            return msg;
        }

        return null;
    }

    public void send(CommandSender sender, Object... objects){
        String message = getMessage(objects);
        if(message != null && sender != null)
            sender.sendMessage(message);
    }

    private void setMessage(String message){
        this.message = message;
    }

    public static void reload(){
        WildInspectPlugin.log("Loading messages started...");
        long startTime = System.currentTimeMillis();
        int messagesAmount = 0;
        File file = new File(WildInspectPlugin.getPlugin().getDataFolder(), "lang.yml");

        if(!file.exists())
            WildInspectPlugin.getPlugin().saveResource("lang.yml", false);

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        for(String identifier : localeMap.keySet()){
            localeMap.get(identifier).setMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString(identifier, "")));
            messagesAmount++;
        }

        WildInspectPlugin.log(" - Found " + messagesAmount + " messages in lang.yml.");
        WildInspectPlugin.log("Loading messages done (Took " + (System.currentTimeMillis() - startTime) + "ms)");
    }

}
