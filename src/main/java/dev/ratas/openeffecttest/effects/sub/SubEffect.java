package dev.ratas.openeffecttest.effects.sub;

import org.bukkit.Location;

public interface SubEffect {

    public void play(Location loc);

    public long getDelay();

}