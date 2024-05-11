package com.bgsoftware.wildinspect.coreprotect.lookup;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NoDataResultLine implements LookupResultLine {

    private final InteractionType interactionType;
    private final String page;

    public NoDataResultLine(InteractionType interactionType, String page) {
        this.interactionType = interactionType;
        this.page = page;
    }

    @Override
    public Type getType() {
        return Type.NO_DATA;
    }

    public InteractionType getInteractionType() {
        return this.interactionType;
    }

    public String getPage() {
        return this.page;
    }

    public enum InteractionType {

        PLAYER_INTERACTIONS,
        BLOCK_DATA,
        CONTAINER_TRANSACTIONS;

        private static final Map<String, InteractionType> aliases = new HashMap<>();

        InteractionType() {

        }

        public void addAlias(String alias) {
            aliases.put(alias.toUpperCase(Locale.ENGLISH), this);
        }

        public static InteractionType of(String value) {
            String name = value.toUpperCase(Locale.ENGLISH);
            try {
                return InteractionType.valueOf(name);
            } catch (IllegalArgumentException ignored) {
            }

            InteractionType interactionType = aliases.get(name);
            if (interactionType != null)
                return interactionType;

            throw new IllegalArgumentException("No enum constant " + InteractionType.class.getCanonicalName() + "." + value);
        }

    }

}
