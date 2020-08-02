package dev.ratas.openeffecttest.effects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public abstract class AbstractEffect implements Effect {
    private final List<List<SubEffect>> effectStages = new ArrayList<>();

    protected void addStage(List<SubEffect> stage) {
        if (stage == null) {
            throw new IllegalArgumentException("Stage cannot be null");
        }
        if (stage.isEmpty()) {
            throw new IllegalArgumentException("Stage needs effects");
        }
        if (stage.contains(null)) {
            throw new IllegalArgumentException("Sub effect cannot be null");
        }
        effectStages.add(stage);
    }

    @Override
    public void play(Location location) {
        if (effectStages.isEmpty()) {
            throw new IllegalStateException("No subeffects defined");
        }
        playSubIfPresent(0, location);
    }

    protected void playSubIfPresent(int index, Location location) {
        if (index >= effectStages.size()) {
            return;
        }
        List<SubEffect> subs = effectStages.get(index);
        for (SubEffect sub : subs) {
            sub.play(location, () -> {
                subs.remove(sub);
                if (subs.isEmpty()) {
                    playSubIfPresent(index + 1, location);
                }
            });
        }
    }

}