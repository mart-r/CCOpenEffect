package dev.ratas.openeffecttest.effects.stage;

import org.bukkit.Location;

import dev.ratas.openeffecttest.effects.sub.SubEffect;

public interface EffectStage {

    public void addEffect(SubEffect effect);

    public void startStage(Location location);

    public long getStageDelay();

    public boolean isEmpty();
    
}