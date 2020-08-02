package dev.ratas.openeffecttest.effects;

import org.bukkit.Location;

public interface SubEffect extends Effect {

    public default void play(Location loc, Runnable whenDone) {
        whenDone(whenDone);
        play(loc);
    }

    public void whenDone(Runnable runnable);

}