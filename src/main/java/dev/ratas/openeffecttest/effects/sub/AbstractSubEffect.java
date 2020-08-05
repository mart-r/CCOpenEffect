package dev.ratas.openeffecttest.effects.sub;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractSubEffect implements SubEffect {
    protected final JavaPlugin plugin;
    private final long delay;

    public AbstractSubEffect(JavaPlugin plugin, long delay) {
        this.plugin = plugin;
        this.delay = delay;
    }

    @Override
    public long getDelay() {
        return delay;
    }

}