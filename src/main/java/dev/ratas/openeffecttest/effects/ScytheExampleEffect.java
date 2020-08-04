package dev.ratas.openeffecttest.effects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class ScytheExampleEffect extends AbstractEffect {
    private static final double ROTATE = 2 * Math.PI / 20;
    private final JavaPlugin plugin;

    public ScytheExampleEffect(JavaPlugin plugin) {
        this.plugin = plugin;

        Vector vector = new Vector(1, 0, 1);
        double yOffset = 0.0D;
        double step = 0.015;
        for (int nr = 0; nr < 94; nr++) { // 2^0.5 = 1.41/0.015 = 94.28
            vector = vector.clone();//.multiply(0.99);
            double len = vector.length();
            double newLen = len - step;
            vector = vector.normalize().multiply(newLen);
            yOffset += 0.01;
            addStagePointedTowards(vector, yOffset);
            vector = vector.rotateAroundY(ROTATE);
        }
        addFallingBlockStage(yOffset);
    }

    private void addStagePointedTowards(Vector vector, double yOffset) {
        List<SubEffect> stage = new ArrayList<>();
        stage.add(new ScytheEffect(plugin, 20, 0.6, 10, vector.clone(), yOffset));
        addStage(stage);
    }

    private void addFallingBlockStage(double yOffset) {
        List<SubEffect> stage = new ArrayList<>();
                                     // JavaPlugin plugin, float anglePer, double fallPer, double fallingDistance, double yOffset) {
        double fallper = 0.15D;
        float rotate = (float) ROTATE * 30F;
        double fallDistance = yOffset + 1.75D;
        stage.add(new FallingHeadEffect(plugin, rotate, fallper, fallDistance, yOffset));
        addStage(stage);
    }

}