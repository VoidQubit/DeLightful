package io.github.voidqubit.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.MapColor;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;

public class FallingLeavesParticle extends CherryLeavesParticle {
    protected FallingLeavesParticle(ClientWorld clientWorld, double x, double y, double z, SpriteProvider spriteProvider) {
        super(clientWorld, x, y, z, spriteProvider);


        /*
        * While this does not get the color of the leaves it spawned from, it works.
        * All I care about is; As long as it works, I don't care.
        */
        float brightness = 0.9f;

        BlockPos pos = new BlockPos((int) this.x, (int) this.y, (int) this.z);

        int color = this.world.getColor(pos, BiomeColors.FOLIAGE_COLOR);

        float red = ((color >> 16) & 0xFF) / 255.0f;
        float green = ((color >> 8) & 0xFF) / 255.0f;
        float blue = (color & 0xFF) / 255.0f;

        // Apply brightness multiplier (clamp to 1.0f max to avoid overflow)
        red = Math.min(red * brightness, 1.0f);
        green = Math.min(green * brightness, 1.0f);
        blue = Math.min(blue * brightness, 1.0f);

        // Set the adjusted color
        this.setColor(red, green, blue);

        this.scale = 0.05f + random.nextFloat() * 0.03f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new FallingLeavesParticle(world, x, y, z, this.spriteProvider);
        }
    }
}
