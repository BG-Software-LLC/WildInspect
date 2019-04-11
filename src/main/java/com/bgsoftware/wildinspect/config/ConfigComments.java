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

    @Comment("")
    @Comment("History limits of the inspect mode.")
    @Comment("If you want to disable the feature, set the limit to -1.")
    public static String HISTORY_LIMIT = "history-limit";

    @Comment("Date limit. Limit the maximum date that can be viewed using the inspect mode.")
    public static String HISTORY_LIMIT_DATE = "history-limit.date";

    @Comment("Page limit. Limit the maximum amount of pages that can be viewed.")
    public static String HISTORY_LIMIT_PAGE = "history-limit.page";

}
