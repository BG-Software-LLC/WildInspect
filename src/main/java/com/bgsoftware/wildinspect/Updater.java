package com.bgsoftware.wildinspect;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

@SuppressWarnings({"WeakerAccess", "unchecked"})
public final class Updater {

    private static final WildInspectPlugin plugin = WildInspectPlugin.getPlugin();
    private static String latestVersion, versionDescription;

    static{
        setLatestVersion();
    }

    public static boolean isOutdated(){
        return !plugin.getDescription().getVersion().startsWith(latestVersion);
    }

    public static String getLatestVersion(){
        return latestVersion;
    }

    public static String getVersionDescription(){
        return versionDescription;
    }

    private static void setLatestVersion(){
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://bg-software.com/versions.json").openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
            connection.setDoInput(true);

            try(InputStream reader = connection.getInputStream()){
                Class jsonObjectClass, gsonClass;

                try{
                    jsonObjectClass = Class.forName("net.minecraft.util.com.google.gson.JsonObject");
                    gsonClass = Class.forName("net.minecraft.util.com.google.gson.Gson");
                }catch(ClassNotFoundException ex){
                    jsonObjectClass = Class.forName("com.google.gson.JsonObject");
                    gsonClass = Class.forName("com.google.gson.Gson");
                }

                Object jsonObject = gsonClass.getMethod("fromJson", Reader.class, Class.class)
                        .invoke(gsonClass.newInstance(), new InputStreamReader(reader), jsonObjectClass);

                Object jsonElement = jsonObjectClass.getMethod("get", String.class).invoke(jsonObject, "wildinspect");
                Object plugin = jsonElement.getClass().getMethod("getAsJsonObject").invoke(jsonElement);

                Object versionElement = plugin.getClass().getMethod("get", String.class).invoke(plugin, "version");
                Object descriptionElement = plugin.getClass().getMethod("get", String.class).invoke(plugin, "description");

                latestVersion = (String) versionElement.getClass().getMethod("getAsString").invoke(versionElement);
                versionDescription = (String) descriptionElement.getClass().getMethod("getAsString").invoke(descriptionElement);
            }
        } catch(Exception ex){
            //Something went wrong...
            latestVersion = plugin.getDescription().getVersion();
        }
    }

}
