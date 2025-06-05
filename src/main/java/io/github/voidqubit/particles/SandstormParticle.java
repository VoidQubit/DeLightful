package io.github.voidqubit.particles;

import io.github.voidqubit.DeLightful;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;

public class SandstormParticle extends SpriteBillboardParticle {

    protected SandstormParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, g, h, i);

        this.setSpriteForAge(spriteProvider);
        this.maxAge = 10;
        this.collidesWithWorld = true;
        this.scale = 0.02f + random.nextFloat() * 0.02f;

        // Get the MapColor from the sand block's default state
        MapColor mapColor = Blocks.SAND.getDefaultState().getMapColor(clientWorld, BlockPos.ofFloored(d, e, f));

        int color = mapColor.color; // Packed 0xRRGGBB

        // Convert to float RGB (0.0f to 1.0f)
        float red   = ((color >> 16) & 0xFF) / 255.0f;
        float green = ((color >> 8) & 0xFF) / 255.0f;
        float blue  = (color & 0xFF) / 255.0f;

        this.setColor(red, green, blue);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        double maxSpeed = 2.0;

        // Random velocity components centered around 0
        double targetVelocityX = (random.nextDouble() - 0.5) * maxSpeed;
        double targetVelocityY = (random.nextDouble() - 0.5) * maxSpeed;
        double targetVelocityZ = (random.nextDouble() - 0.5) * maxSpeed;

        if (random.nextInt(5000000) == 0) {
            DeLightful.sandstormDirectionX = random.nextFloat() * 2.0f - 1.0f;
            DeLightful.sandstormDirectionY = random.nextFloat() * 2.0f - 1.0f;
            DeLightful.sandstormDirectionZ = random.nextFloat() * 2.0f - 1.0f;
        }

        float biasX = DeLightful.sandstormDirectionX * 2.0f;
        float biasY = DeLightful.sandstormDirectionY * 0.1f;
        float biasZ = DeLightful.sandstormDirectionZ * 2.0f;

        double biasStrength = 0.6;
        targetVelocityX += biasStrength * biasX;
        targetVelocityY += biasStrength * biasY;
        targetVelocityZ += biasStrength * biasZ;

        double lerpAmount = 0.1;
        this.velocityX += (targetVelocityX - this.velocityX) * lerpAmount;
        this.velocityY += (targetVelocityY - this.velocityY) * lerpAmount;
        this.velocityZ += (targetVelocityZ - this.velocityZ) * lerpAmount;

        this.x += this.velocityX;
        this.y += this.velocityY;
        this.z += this.velocityZ;

        this.angle = this.random.nextFloat() * 360.0f;
        this.prevAngle = this.angle;
    }


    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new SandstormParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
        }
    }
}
