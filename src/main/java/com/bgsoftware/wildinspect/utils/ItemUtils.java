package com.bgsoftware.wildinspect.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public final class ItemUtils {

    private static List<String> containers = Arrays.asList("DISPENSER", "CHEST", "FURNACE", "BURNING_FURNACE", "TRAPPED_CHEST", "HOPPER", "DROPPER");

    public static boolean isContainer(Material type){
        return containers.contains(type.name());
    }

}
