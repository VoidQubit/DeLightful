package io.github.voidqubit.particles;

import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.PointLight;
import io.github.voidqubit.utils.DeLightfulUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

public class FireflyParticle extends SpriteBillboardParticle {
    /*
    * Fun fact: Adding fireflies took me 4 weeks. Don't ask why. WHY VEIL WHY
    */
    protected static final float BLINK_STEP = 0.05f;
    protected float nextAlphaGoal;

    PointLight light;

    protected FireflyParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, g, h, i);

        this.setSpriteForAge(spriteProvider);
        // Live between 10 seconds-1 minute. 20 ticks = 1 second.
        this.maxAge = 20 * random.nextBetween(10, 60);
        // This line does nothing. And I don't want to fix it.
        this.collidesWithWorld = true;
        this.scale = 0.02f + random.nextFloat() * 0.02f;
        this.alpha = 1.0f;
        this.nextAlphaGoal = 0;

        this.light = new PointLight();
        this.light.setColor(255, 252, 72);
        this.light.setRadius(25f * this.scale);

        VeilRenderSystem.renderer().getLightRenderer().addLight(this.light);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void markDead() {
        super.markDead();
        VeilRenderSystem.renderer().getLightRenderer().removeLight(this.light);
    }

    @Override
    public void tick() {
        super.tick();
        if (random.nextInt(20) == 0) {
            double maxSpeed = .5;
            double targetVelocityX = (random.nextDouble() - 0.5) * maxSpeed;
            double targetVelocityY = (random.nextDouble() - 0.5) * maxSpeed;
            double targetVelocityZ = (random.nextDouble() - 0.5) * maxSpeed;

            double lerpAmount = 0.1;
            this.velocityX += (targetVelocityX - this.velocityX) * lerpAmount;
            this.velocityY += (targetVelocityY - this.velocityY) * lerpAmount;
            this.velocityZ += (targetVelocityZ - this.velocityZ) * lerpAmount;
        }
        this.x += this.velocityX;
        this.y += this.velocityY;
        this.z += this.velocityZ;

        // Fade out when it's daytime or the fly is about to hit the maxAge
        if (!DeLightfulUtils.isNightTime(this.world) || this.age++ >= this.maxAge) {
            nextAlphaGoal = 0;
            if (this.alpha == 0f) {
                this.markDead();
            }
        }

        this.light.setPosition(this.x, this.y, this.z);

        // Is this straight from the Effective GitHub? Ye-No (RAT, please don't hurt me.)
        if (this.alpha > nextAlphaGoal - BLINK_STEP && this.alpha < nextAlphaGoal + BLINK_STEP) {
            nextAlphaGoal = random.nextFloat();
        } else {
            if (nextAlphaGoal > this.alpha) {
                this.alpha = Math.min(this.alpha + BLINK_STEP, 1f);
            } else if (nextAlphaGoal < this.alpha) {
                this.alpha = Math.max(this.alpha - BLINK_STEP, 0f);
            }
        }
        this.light.setBrightness(this.alpha * .02f);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new FireflyParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
        }
    }
}
