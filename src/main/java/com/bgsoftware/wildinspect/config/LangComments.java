package com.bgsoftware.wildinspect.config;

@SuppressWarnings("unused")
public final class LangComments {

    @Comment("###################################################")
    @Comment("##                                               ##")
    @Comment("##             WildInspect Messages              ##")
    @Comment("##              Developed by Ome_R               ##")
    @Comment("##                                               ##")
    @Comment("###################################################")
    public static String HEADER = "";

    @Comment("")
    @Comment("Called when fails to follow a command's format usage.")
    public static String COMMAND_USAGE = "COMMAND_USAGE";

    @Comment("")
    @Comment("Called when trying to insect a block in cooldown time.")
    public static String COOLDOWN = "COOLDOWN";

    @Comment("")
    @Comment("Called when toggling on/off the inspector mode.")
    public static String INSPECTOR_ON = "INSPECTOR_ON";

    @Comment("")
    @Comment("Called when inspecting data on a block.")
    public static String INSPECT_DATA_HEADER = "INSPECT_DATA_HEADER";

    @Comment("")
    @Comment("Called when using the inspect command and lacking the permission.")
    public static String NO_PERMISSION = "NO_PERMISSION";

    @Comment("")
    @Comment("Called when trying to inspect a block not inside a claim.")
    public static String NOT_INSIDE_CLAIM = "NOT_INSIDE_CLAIM";

    @Comment("")
    @Comment("Called when couldn't find block data.")
    public static String LIMIT_REACH = "LIMIT_REACH";

    @Comment("")
    @Comment("Called when player tried to inspect block's page without selecting one.")
    public static String NO_BLOCK_SELECTED = "NO_BLOCK_SELECTED";

    @Comment("")
    @Comment("Called when a player successfully reloaded all configuration files.")
    public static String RELOAD_SUCCESS = "RELOAD_SUCCESS";

    @Comment("")
    @Comment("Called when a player doesn't have the required role.")
    public static String REQUIRED_ROLE = "REQUIRED_ROLE";

    @Comment("")
    @Comment("Called when a player doesn't specify a page.")
    public static String SPECIFY_PAGE = "SPECIFY_PAGE";

}
