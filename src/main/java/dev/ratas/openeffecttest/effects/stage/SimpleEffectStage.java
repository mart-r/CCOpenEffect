package dev.ratas.openeffecttest.effects.stage;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import dev.ratas.openeffecttest.effects.sub.SubEffect;

public class SimpleEffectStage implements EffectStage {
    protected final JavaPlugin plugin;
    private final List<SubEffect> subEffects = new ArrayList<>();
    private final long delay;

    public SimpleEffectStage(JavaPlugin plugin, long delay) {
        this.plugin = plugin;
        this.delay = delay;
    }

    @Override
    public void addEffect(SubEffect effect) {
        if (effect == null) {
            throw new IllegalArgumentException("Sub effect cannot be null");
        }
        subEffects.add(effect);
    }

    @Override
    public void startStage(Location center) {
        for (SubEffect sub : subEffects) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                sub.play(center.clone());
            }, sub.getDelay());
        }
    }

    @Override
    public long getStageDelay() {
        return delay;
    }

    @Override
    public boolean isEmpty() {
        return subEffects.isEmpty();
    }

}