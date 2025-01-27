package dev.ratas.openeffecttest.effects;

import org.bukkit.Particle;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import dev.ratas.openeffecttest.effects.stage.SimpleEffectStage;
import dev.ratas.openeffecttest.effects.stage.EffectStage;
import dev.ratas.openeffecttest.effects.sub.FallingHeadEffect;
import dev.ratas.openeffecttest.effects.sub.ScytheEffect;
import dev.ratas.openeffecttest.effects.sub.SpiralEffect;
import dev.ratas.openeffecttest.effects.sub.SpiralEffect.Axis;

public class ScytheExampleEffect extends AbstractEffect {
    private static final double ROTATE = 2 * Math.PI / 20; // in radians

    public ScytheExampleEffect(JavaPlugin plugin) {
        super(plugin);

        double yOffset = addStageOne();
        addFallingBlockStage(yOffset, 95L);
        addCircles(95L + 20L);
    }

    private double addStageOne() {
        Vector vector = new Vector(1, 0, 1);
        double yOffset = 0.0D;
        double yStep = 0.015;
        double step = 0.015;
        long delay = 0;
        EffectStage stage = new SimpleEffectStage(plugin, delay);
        for (int nr = 0; nr < 94; nr++) { // 2^0.5 = 1.41/0.015 = 94.28
            vector = vector.clone();
            double len = vector.length();
            double newLen = len - step;
            vector = vector.normalize().multiply(newLen);
            yOffset += yStep;
            addStagePointedTowards(stage, vector, ++delay, yOffset);
            vector = vector.rotateAroundY(ROTATE);
        }
        addStage(stage);
        return yOffset;
    }

    private void addStagePointedTowards(EffectStage stage, Vector vector, long delay, double yOffset) {
        int particlesInHandler = 20;
        double headLength = 0.6D;
        int particlesInHead = 10;
        stage.addEffect(new ScytheEffect(plugin, particlesInHandler, headLength, particlesInHead, vector.clone(), delay, yOffset));
    }

    private void addFallingBlockStage(double yOffset, long delay) {
        EffectStage stage = new SimpleEffectStage(plugin, delay);
        double fallper = 0.15D;
        float rotate = 15F; // in degrees
        double fallDistance = yOffset;
        yOffset -= 1.75D;
        stage.addEffect(new FallingHeadEffect(plugin, rotate, fallper, fallDistance, yOffset, 0L));
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

    private void addCircles(long delay) {
        EffectStage stage1 = new SimpleEffectStage(plugin, delay);
        stage1.addEffect(new SpiralEffect(plugin, PARTICLE, RADIUS, SLOPE, PER_CIRCLE, MAX_HEIGHT, WAIT_TIME,
                PARTICLES_AT_ONCE, Axis.X, AXIS_OFFSET, 0L));
        stage1.addEffect(new SpiralEffect(plugin, PARTICLE, RADIUS, SLOPE, PER_CIRCLE, MAX_HEIGHT, WAIT_TIME,
                PARTICLES_AT_ONCE, Axis.Y, AXIS_OFFSET, 0L));
        stage1.addEffect(new SpiralEffect(plugin, PARTICLE, RADIUS, SLOPE, PER_CIRCLE, MAX_HEIGHT, WAIT_TIME,
                PARTICLES_AT_ONCE, Axis.Z, AXIS_OFFSET, 0L));
        stage1.addEffect(new SpiralEffect(plugin, PARTICLE, RADIUS, -SLOPE, PER_CIRCLE, MAX_HEIGHT, WAIT_TIME,
                PARTICLES_AT_ONCE, Axis.X, -AXIS_OFFSET, 0L));
        stage1.addEffect(new SpiralEffect(plugin, PARTICLE, RADIUS, -SLOPE, PER_CIRCLE, MAX_HEIGHT, WAIT_TIME,
                PARTICLES_AT_ONCE, Axis.Z, -AXIS_OFFSET, 0L));
        addStage(stage1);
        EffectStage stage2 = new SimpleEffectStage(plugin, delay + 20L);
        stage2.addEffect(new SpiralEffect(plugin, PARTICLE_2, RADIUS * 2, SLOPE * 2, PER_CIRCLE, MAX_HEIGHT, WAIT_TIME,
                PARTICLES_AT_ONCE, Axis.Y, AXIS_OFFSET, 0L));
        addStage(stage2);
    }

}