package com.bgsoftware.wildinspect.scheduler;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.concurrent.TimeUnit;

public class FoliaSchedulerImplementation implements ISchedulerImplementation {

    public static final FoliaSchedulerImplementation INSTANCE = new FoliaSchedulerImplementation();

    private static final WildInspectPlugin plugin = WildInspectPlugin.getPlugin();

    private FoliaSchedulerImplementation() {
    }

    @Override
    public void scheduleTask(Location location, Runnable task, long delay) {
        if (delay <= 0) {
            Bukkit.getServer().getRegionScheduler().run(plugin, location, v -> task.run());
        } else {
            Bukkit.getServer().getRegionScheduler().runDelayed(plugin, location, v -> task.run(), delay);
        }
    }

    @Override
    public void scheduleTask(Entity entity, Runnable task, long delay) {
        if (delay <= 0) {
            entity.getScheduler().run(plugin, v -> task.run(), task);
        } else {
            entity.getScheduler().runDelayed(plugin, v -> task.run(), task, delay);
        }
    }

    @Override
    public void scheduleTask(Runnable task, long delay) {
        if (delay <= 0) {
            Bukkit.getServer().getGlobalRegionScheduler().run(plugin, v -> task.run());
        } else {
            Bukkit.getServer().getGlobalRegionScheduler().runDelayed(plugin, v -> task.run(), delay);
        }
    }

    @Override
    public void scheduleAsyncTask(Runnable task, long delay) {
        if (delay <= 0) {
            Bukkit.getServer().getAsyncScheduler().runNow(plugin, v -> task.run());
        } else {
            Bukkit.getServer().getAsyncScheduler().runDelayed(plugin, v -> task.run(), delay * 50L, TimeUnit.MILLISECONDS);
        }
    }

}
