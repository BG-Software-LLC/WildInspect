package com.bgsoftware.wildinspect.scheduler;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class BukkitSchedulerImplementation implements ISchedulerImplementation {

    public static final BukkitSchedulerImplementation INSTANCE = new BukkitSchedulerImplementation();

    private static final WildInspectPlugin plugin = WildInspectPlugin.getPlugin();

    private BukkitSchedulerImplementation() {

    }

    @Override
    public void scheduleTask(Location unused, Runnable task, long delay) {
        scheduleTask(task, delay);
    }

    @Override
    public void scheduleTask(Entity unused, Runnable task, long delay) {
        scheduleTask(task, delay);
    }

    @Override
    public void scheduleTask(Runnable task, long delay) {
        if (delay <= 0) {
            Bukkit.getScheduler().runTask(plugin, task);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, delay);
        }
    }

    @Override
    public void scheduleAsyncTask(Runnable task, long delay) {
        if (delay <= 0) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        } else {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delay);
        }
    }

}
