package dev.ratas.openeffecttest.effects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Particle;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import dev.ratas.openeffecttest.effects.SpiralEffect.Axis;

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
        addCircles();
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
    private static final Particle PARTICLE = Particle.FLAME;
    private static final Particle PARTICLE_2 = Particle.CRIT_MAGIC;
    private static final double RADIUS = 0.7D;
    private static final double SLOPE = 0.05D;
    private static final int PER_CIRCLE = 30;
    private static final double MAX_HEIGHT = 1.2D;
    private static final int WAIT_TIME = 80;
    private static final int PARTICLES_AT_ONCE = 5;
    private static final double AXIS_OFFSET = 0.5D;

    private void addCircles() {
        List<SubEffect> stage1 = new ArrayList<>();
        stage1.add(new SpiralEffect(plugin, PARTICLE, RADIUS, SLOPE, PER_CIRCLE, MAX_HEIGHT, WAIT_TIME,
                PARTICLES_AT_ONCE, Axis.X, AXIS_OFFSET));
        stage1.add(new SpiralEffect(plugin, PARTICLE, RADIUS, SLOPE, PER_CIRCLE, MAX_HEIGHT, WAIT_TIME,
                PARTICLES_AT_ONCE, Axis.Y, AXIS_OFFSET));
        stage1.add(new SpiralEffect(plugin, PARTICLE, RADIUS, SLOPE, PER_CIRCLE, MAX_HEIGHT, WAIT_TIME,
                PARTICLES_AT_ONCE, Axis.Z, AXIS_OFFSET));
        stage1.add(new SpiralEffect(plugin, PARTICLE, RADIUS, -SLOPE, PER_CIRCLE, MAX_HEIGHT, WAIT_TIME,
                PARTICLES_AT_ONCE, Axis.X, -AXIS_OFFSET));
        stage1.add(new SpiralEffect(plugin, PARTICLE, RADIUS, -SLOPE, PER_CIRCLE, MAX_HEIGHT, WAIT_TIME,
                PARTICLES_AT_ONCE, Axis.Z, -AXIS_OFFSET));
        addStage(stage1);
        List<SubEffect> stage2 = new ArrayList<>();
        stage2.add(new SpiralEffect(plugin, PARTICLE_2, RADIUS * 2, SLOPE * 2, PER_CIRCLE, MAX_HEIGHT, WAIT_TIME,
                PARTICLES_AT_ONCE, Axis.Y, AXIS_OFFSET));
        addStage(stage2);
    }

}