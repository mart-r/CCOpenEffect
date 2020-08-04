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
        double yStep = 0.015;
        double step = 0.015;
        for (int nr = 0; nr < 94; nr++) { // 2^0.5 = 1.41/0.015 = 94.28
            vector = vector.clone();//.multiply(0.99);
            double len = vector.length();
            double newLen = len - step;
            vector = vector.normalize().multiply(newLen);
            yOffset += yStep;
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
        double fallper = 0.15D;
        float rotate = (float) ROTATE * 30F;
        double fallDistance = yOffset;
        stage.add(new FallingHeadEffect(plugin, rotate, fallper, fallDistance, yOffset - 1.75D));
        addStage(stage);
    }

}