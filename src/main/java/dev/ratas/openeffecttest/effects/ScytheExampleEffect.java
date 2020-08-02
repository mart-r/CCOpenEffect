package dev.ratas.openeffecttest.effects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class ScytheExampleEffect extends AbstractEffect {
    private static final double ROTATE = 2 * Math.PI / 50;
    private final JavaPlugin plugin;

    public ScytheExampleEffect(JavaPlugin plugin) {
        this.plugin = plugin;

        Vector vector = new Vector(1, 0, 1);
        for (int nr = 0; nr < 100; nr++) {
            addStagePointedTowards(vector);
            vector = vector.rotateAroundY(ROTATE);
        }
    }

    private void addStagePointedTowards(Vector vector) {
        List<SubEffect> stage = new ArrayList<>();
        stage.add(new ScytheEffect(plugin, 20, 0.6, 10, vector.clone()));
        addStage(stage);
    }

}