package com.bgsoftware.wildinspect.coreprotect;

import com.bgsoftware.common.reflection.ClassInfo;
import com.bgsoftware.common.reflection.ReflectMethod;
import net.coreprotect.database.Lookup;
import net.coreprotect.database.lookup.BlockLookup;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CoreProtectHook {

    private static final Pattern COMPONENT_PATTERN = Pattern.compile("<COMPONENT>(.*)\\|(.*)\\|(.*)</COMPONENT>");

    private static final ReflectMethod<String> INTERACTION_LOOKUP_METHOD = new ReflectMethod<>(Lookup.class,
            "interaction_lookup", Statement.class, Block.class, String.class, int.class, int.class, int.class);
    private static final ReflectMethod<String> BLOCK_LOOKUP_METHOD = new ReflectMethod<>(Lookup.class,
            "block_lookup", Statement.class, BlockState.class, String.class, int.class, int.class, int.class);
    private static final ReflectMethod<String> CHEST_TRANSACTION_LOOKUP_METHOD = new ReflectMethod<>(Lookup.class,
            "chest_transactions", Statement.class, Location.class, String.class, int.class, int.class, boolean.class);

    private static final ReflectMethod<String> INTERACTION_LOOKUP_V20_METHOD = new ReflectMethod<>(
            new ClassInfo("net.coreprotect.database.lookup.InteractionLookup", ClassInfo.PackageType.UNKNOWN),
            "performLookup", String.class, Statement.class, Block.class, CommandSender.class, int.class, int.class, int.class);
    private static final ReflectMethod<String> BLOCK_LOOKUP_V20_METHOD = new ReflectMethod<>(
            new ClassInfo("net.coreprotect.database.lookup.BlockLookup", ClassInfo.PackageType.UNKNOWN),
            "performLookup", String.class, Statement.class, BlockState.class, CommandSender.class, int.class, int.class, int.class);
    private static final ReflectMethod<String> CHEST_TRANSACTION_LOOKUP_V20_METHOD = new ReflectMethod<>(
            new ClassInfo("net.coreprotect.database.lookup.ChestTransactionLookup", ClassInfo.PackageType.UNKNOWN),
            "performLookup", String.class, Statement.class, Location.class, CommandSender.class, int.class, int.class, boolean.class);

    public static String[] performInteractLookup(Statement statement, Player player, Block block, int page) {
        if (INTERACTION_LOOKUP_METHOD.isValid()) {
            return parseResult(INTERACTION_LOOKUP_METHOD.invoke(null, statement, block, player.getName(), 0, page, 7));
        } else if (INTERACTION_LOOKUP_V20_METHOD.isValid()) {
            return parseResult(INTERACTION_LOOKUP_V20_METHOD.invoke(null, null, statement, block, player, 0, page, 7));
        } else {
            return parseResult(Lookup.interactionLookup(null, statement, block, player, 0, page, 7));
        }
    }

    public static String[] performBlockLookup(Statement statement, Player player, BlockState blockState, int page) {
        if (BLOCK_LOOKUP_METHOD.isValid()) {
            return parseResult(BLOCK_LOOKUP_METHOD.invoke(null, statement, blockState, player.getName(), 0, page, 7));
        } else if (BLOCK_LOOKUP_V20_METHOD.isValid()) {
            return parseResult(BLOCK_LOOKUP_V20_METHOD.invoke(null, null, statement, blockState, player, 0, page, 7));
        } else {
            return parseResult(BlockLookup.results(null, statement, blockState, player, 0, page, 7));
        }
    }

    public static String[] performChestLookup(Statement statement, Player player, Block block, int page) {
        if (CHEST_TRANSACTION_LOOKUP_METHOD.isValid()) {
            return parseResult(CHEST_TRANSACTION_LOOKUP_METHOD.invoke(null, statement, block.getLocation(), player.getName(), page, 7, false));
        } else if (CHEST_TRANSACTION_LOOKUP_V20_METHOD.isValid()) {
            return parseResult(CHEST_TRANSACTION_LOOKUP_V20_METHOD.invoke(null, null, statement, block.getLocation(), player, page, 7, false));
        } else {
            return parseResult(Lookup.chestTransactions(null, statement, block.getLocation(), player, page, 7, false));
        }
    }

    private static String[] parseResult(String result) {
        Matcher matcher = COMPONENT_PATTERN.matcher(result);

        if (matcher.find())
            result = matcher.replaceAll("$3");

        return result.split("\n");
    }

}
