package io.github.voidqubit.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import org.joml.Quaternionf;

import java.util.Random;

public class RippleParticle extends SpriteBillboardParticle {

    public SpriteProvider spriteProvider;

    protected RippleParticle(ClientWorld clientWorld, double x, double y, double z, SpriteProvider spriteProvider) {
        super(clientWorld, x, y, z);

        this.spriteProvider = spriteProvider;

        this.setSpriteForAge(this.spriteProvider);

        int scaleAgeModifier = 1 + new Random().nextInt(10);
        this.scale *= 3.5f + random.nextFloat() / 10f * scaleAgeModifier;
        this.maxAge = 10 + new Random().nextInt(scaleAgeModifier);

        this.angle = new Random().nextFloat() * 360.0f;
        this.prevAngle = this.angle;

        this.gravityStrength = 100.0f;

        this.alpha = 0.6f;

        float brightness = 0.8f;

        BlockPos pos = new BlockPos((int) this.x, (int) this.y - 1, (int) this.z);

        int color = this.world.getColor(pos, BiomeColors.WATER_COLOR);

        float red = ((color >> 16) & 0xFF) / 255.0f;
        float green = ((color >> 8) & 0xFF) / 255.0f;
        float blue = (color & 0xFF) / 255.0f;

        // Apply brightness multiplier (clamp to 1.0f max to avoid overflow)
        red = Math.min(red * brightness, 1.0f);
        green = Math.min(green * brightness, 1.0f);
        blue = Math.min(blue * brightness, 1.0f);

        // Set the adjusted color
        this.setColor(red, green, blue);

    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Quaternionf quaternionf = new Quaternionf();

        quaternionf.rotateX((float) Math.toRadians(-90f));
        quaternionf.rotateZ((float) Math.toRadians(this.angle));

        this.method_60373(vertexConsumer, camera, quaternionf, tickDelta);
    }


    @Override
    public void tick() {

        if (this.age++ >= this.maxAge) {
            this.markDead();
        }
        int fadeStart = this.maxAge - 10; // start fading 10 ticks before death

        if (this.age >= fadeStart) {
            // Map age from [fadeStart, maxAge] to alpha [1.0 -> 0.0]
            this.alpha = (this.maxAge - this.age) / 10.0f; // alpha goes from 1 to 0
        } else {
            this.alpha = 0.6f;
        }


        this.setSpriteForAge(this.spriteProvider);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new RippleParticle(world, x, y, z, this.spriteProvider);
        }
    }
}
