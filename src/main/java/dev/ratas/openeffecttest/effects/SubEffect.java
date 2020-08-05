package dev.ratas.openeffecttest.effects;

import org.bukkit.Location;

public interface SubEffect {

    public void play(Location loc, Runnable whenDone);

}