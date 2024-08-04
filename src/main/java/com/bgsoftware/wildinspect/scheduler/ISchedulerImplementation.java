package com.bgsoftware.wildinspect.scheduler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface ISchedulerImplementation {

    void scheduleTask(Location location, Runnable task, long delay);

    void scheduleTask(Entity entity, Runnable task, long delay);

    void scheduleTask(Runnable task, long delay);

    void scheduleAsyncTask(Runnable task, long delay);

}
