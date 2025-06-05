package io.github.voidqubit;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static io.github.voidqubit.DeLightful.MOD_ID;

public class DeLightfulParticles {

    public static final SimpleParticleType FIREFLY = FabricParticleTypes.simple();

    public static final SimpleParticleType SANDSTORM = FabricParticleTypes.simple();

    public static final SimpleParticleType RIPPLE = FabricParticleTypes.simple();

    public static final SimpleParticleType FALLING_LEAVES = FabricParticleTypes.simple();

    public static void init() {
        register("firefly", FIREFLY);
        register("sandstorm", SANDSTORM);
        register("ripple", RIPPLE);
        register("falling_leaves", FALLING_LEAVES);
    }

    private static void register(String id, SimpleParticleType particle) {
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MOD_ID, id), particle);
    }
}
