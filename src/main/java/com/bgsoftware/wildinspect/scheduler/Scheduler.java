package com.bgsoftware.wildinspect.scheduler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class Scheduler {

    private static final ISchedulerImplementation IMP = initializeSchedulerImplementation();

    private static ISchedulerImplementation initializeSchedulerImplementation() {
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.RegionScheduler");
        } catch (ClassNotFoundException error) {
            return BukkitSchedulerImplementation.INSTANCE;
        }

        // Detected Folia, create its scheduler
        try {
            Class<?> foliaSchedulerClass = Class.forName("com.bgsoftware.wildinspect.scheduler.FoliaSchedulerImplementation");
            return (ISchedulerImplementation) foliaSchedulerClass.getField("INSTANCE").get(null);
        } catch (Throwable error) {
            throw new RuntimeException(error);
        }
    }

    private Scheduler() {

    }

    public static void initialize() {
        // Do nothing, load static initializer
    }

    public static void runTask(Location location, Runnable task, long delay) {
        IMP.scheduleTask(location, task, delay);
    }

    public static void runTask(Entity entity, Runnable task, long delay) {
        IMP.scheduleTask(entity, task, delay);
    }

    public static void runTask(Runnable task, long delay) {
        IMP.scheduleTask(task, delay);
    }

    public static void runTaskAsync(Runnable task, long delay) {
        IMP.scheduleAsyncTask(task, delay);
    }

    public static void runTask(Runnable task) {
        runTask(task, 0L);
    }

    public static void runTaskAsync(Runnable task) {
        runTaskAsync(task, 0L);
    }

}
