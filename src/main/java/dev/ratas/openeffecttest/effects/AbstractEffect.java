package dev.ratas.openeffecttest.effects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import dev.ratas.openeffecttest.effects.stage.EffectStage;

public abstract class AbstractEffect implements Effect {
    protected final JavaPlugin plugin;
    private final List<EffectStage> effectStages = new ArrayList<>();

    public AbstractEffect(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    protected void addStage(EffectStage stage) {
        if (stage == null) {
            throw new IllegalArgumentException("Stage cannot be null");
        }
        if (stage.isEmpty()) {
            throw new IllegalArgumentException("Stage needs effects");
        }
        effectStages.add(stage);
    }

    @Override
    public void play(Location location) {
        if (effectStages.isEmpty()) {
            throw new IllegalStateException("No subeffects defined");
        }
        for (EffectStage stage : effectStages) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> stage.startStage(location),
                    stage.getStageDelay());
        }
    }

}