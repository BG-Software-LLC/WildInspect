package com.bgsoftware.wildinspect.config;

public final class ConfigComments {

    @Comment("###################################################")
    @Comment("##                                               ##")
    @Comment("##           WildInspect Configuration           ##")
    @Comment("##              Developed by Ome_R               ##")
    @Comment("##                                               ##")
    @Comment("###################################################")
    public static String HEADER = "";

    @Comment("")
    @Comment("What commands should be used as inspect toggle command?")
    public static String COMMANDS = "commands";

    @Comment("")
    @Comment("A list of roles that can use the command.")
    @Comment("FactionsUUID: RECRUIT, NORMAL, MODERATOR, ADMIN")
    @Comment("MassiveFactions: RECRUIT, MEMBER, OFFICER, LEADER")
    @Comment("Towny: configured.")
    public static String REQUIRED_ROLES = "required-roles";

}
