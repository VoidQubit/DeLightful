package io.github.voidqubit.utils;

import io.github.voidqubit.DeLightfulParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.joml.Quaternionf;

public class DeLightfulUtils {

    // "Utils". "Util". "Uti". "Ut". "U". " ".

    private static final Quaternionf lastOrientation = new Quaternionf();
    private static final Quaternionf tempOrientation = new Quaternionf();
    private static final float SMOOTHING = 0.35f;

    public static boolean isNightTime(ClientWorld world) {
        return world.getSkyAngle(world.getTimeOfDay()) >= 0.25965086 && world.getSkyAngle(world.getTimeOfDay()) <= 0.7403491;
    }


    public static Quaternionf getOrientation(Vec3d lookDir, PlayerEntity player) {

        // I have no idea of what I am doing.

        // Compute angles from the look direction
        float angle = (float) Math.atan2(lookDir.getX(), lookDir.getZ());
        float angle2 = (float) Math.asin(lookDir.getY());

        // First rotation around Y axis (horizontal)
        float x = 0f;
        float y = (float) -Math.toRadians(1 * Math.sin(angle / 2));
        float z = 0f;
        float w = (float) Math.toRadians(Math.cos(angle / 2));

        // Second rotation around X axis (vertical)
        float x2 = (float) Math.toRadians(1 * Math.sin(angle2 / 2));
        float y2 = 0f;
        float z2 = 0f;
        float w2 = (float) Math.toRadians(Math.cos(angle2 / 2));

        // Combine rotations
        Quaternionf target = new Quaternionf(x2, y2, z2, w2).mul(new Quaternionf(x, y, z, w)).normalize();

        // Smooth interpolation from last orientation to target
        lastOrientation.slerp(target, SMOOTHING, tempOrientation);

        // Update last orientation for next tick
        lastOrientation.set(tempOrientation);

        return new Quaternionf(tempOrientation);
    }

    // These are pretty self-explanatory.

    private static int getFireflySpawnChance(float temperature, float humidity, World world) {
        int spawnChance;

        if (temperature > 0.8f && humidity > 0.8f) {
            spawnChance = world.isThundering() ? 400 : world.isRaining() ? 450 : 500; // Hot and humid biomes
        } else if (temperature > 0.5f && humidity > 0.5f) {
            spawnChance = world.isThundering() ? 500 : world.isRaining() ? 575 : 650; // Moderately warm and humid
        } else if (temperature > 0.5f || humidity > 0.5f) {
            spawnChance = world.isThundering() ? 650 : world.isRaining() ? 750 : 850; // Either warm or humid
        } else {
            spawnChance = world.isThundering() ? 850 : world.isRaining() ? 950 : 1100; // Cold and dry biomes
        }

        return spawnChance;
    }

    public static void spawnFireflies(MinecraftClient client, Random random) {
        if (client.player != null && client.world != null) {
            RegistryEntry<Biome> worldBiome = client.world.getBiome(client.player.getBlockPos());
            float temperature = worldBiome.value().getTemperature();
            if (!DeLightfulUtils.isNightTime(client.world)) return;

            if (client.world.getTickManager().isFrozen()) return;

            if (client.isPaused()) return;

            if (worldBiome.matchesKey(BiomeKeys.DESERT)) return;

            float humidity = worldBiome.value().getPrecipitation(client.player.getBlockPos()).ordinal();

            int spawnChance = getFireflySpawnChance(temperature, humidity, client.world);

            if (random.nextInt(spawnChance) == 0) return;

            int randomX = random.nextInt(41) - 20;
            int randomY = random.nextInt(41) - 20;
            int randomZ = random.nextInt(41) - 20;

            BlockPos spawnPos = client.player.getBlockPos().add(new Vec3i(randomX, randomY, randomZ));
            if (spawnPos.getY() >= 62 && client.world.isSkyVisible(spawnPos))
                ParticleUtil.spawnParticle(client.world, spawnPos, random, DeLightfulParticles.FIREFLY);
        }
    }

    public static void spawnRipples(MinecraftClient client, Random random) {
        if (client.player == null || client.world == null) return;
        if (client.world.getTickManager().isFrozen() || client.isPaused()) return;

        for (int i = 0; i < 10000; i++) {
            int randomX = random.nextInt(81) - 40;
            int randomY = random.nextInt(81) - 40;
            int randomZ = random.nextInt(81) - 40;

            BlockPos spawnPos = client.player.getBlockPos().add(randomX, randomY, randomZ);
            BlockPos below = spawnPos.down();
            BlockState belowState = client.world.getBlockState(below);

            boolean isWater = belowState.getBlock() == Blocks.WATER;
            boolean airAbove = client.world.isAir(spawnPos.up());
            boolean skyVisible = client.world.isSkyVisible(spawnPos);
            boolean raining = client.world.getBiome(spawnPos).value().getPrecipitation(spawnPos).equals(Biome.Precipitation.RAIN) && (client.world.isRaining() || client.world.isThundering());

            boolean canSpawn = raining && airAbove && skyVisible && isWater;

            if (canSpawn) {
                ParticleUtil.spawnParticle(client.world, spawnPos, random, DeLightfulParticles.RIPPLE);
            }
        }
    }

    public static void spawnSandstorms(MinecraftClient client, Random random) {
        if (client.player == null || client.world == null) return;

        if (client.world.getTickManager().isFrozen() || client.isPaused()) return;

        RegistryEntry<Biome> biome = client.player.clientWorld.getBiome(client.player.getBlockPos());

        int radius = 10;

        int density = radius * 100;

        for (int i = 0; i < density; i++) {
            /*
             * Chooses a random blockPos in a ${radius} block radius around the player.
             * This is done to not add particles everywhere; since that will lag the game...but it looks bad :(
             */
            int randomY = random.nextInt(radius * 2 + 1) - radius;
            int randomZ = random.nextInt(radius * 2 + 1) - radius;
            int randomX = random.nextInt(radius * 2 + 1) - radius;

            BlockPos spawnPos = client.player.getBlockPos().add(randomX, randomY, randomZ);

            boolean skyVisible = client.world.isSkyVisible(spawnPos);

            boolean aboveSeaLevel = spawnPos.getY() >= 63;

            boolean spawnPosIsAir = client.world.getBlockState(spawnPos).isAir();

            boolean canSpawn = spawnPosIsAir && aboveSeaLevel && skyVisible;

            // This ensures that the particles only spawn above Y63 (sea level) and don't spawn INSIDE blocks.
            if (canSpawn) {
                if (biome.matchesKey(BiomeKeys.DESERT) && client.world.isRaining())
                    ParticleUtil.spawnParticle(client.world, spawnPos, random, DeLightfulParticles.SANDSTORM);
            }
        }
    }

    public static void spawnCaveDust(MinecraftClient client, Random random) {
        if (client.player == null || client.world == null) return;

        if (client.world.getTickManager().isFrozen() || client.isPaused()) return;

        int radius = 40;

        int density = radius * 10;

        for (int i = 0; i < density; i++) {
            /*
             * Chooses a random blockPos in a ${radius} block radius around the player.
             * This is done to not add particles everywhere; since that will lag the game...but it looks bad :(
             */
            int randomX = random.nextInt(radius + 1) - radius / 2;
            int randomY = random.nextInt(radius + 1) - radius / 2;
            int randomZ = random.nextInt(radius + 1) - radius / 2;

            BlockPos spawnPos = client.player.getBlockPos().add(randomX, randomY, randomZ);

            // This ensures that the particles only spawn under Y42 and don't spawn INSIDE blocks.
            if (spawnPos.getY() <= 42 && client.world.getBlockState(spawnPos).isAir()) {
                /*
                 * We get the light level so that in lit up areas, which are mostly lit up by players, if the light level is
                 * greater than 7, there is no cave dust.
                 * This ensures that cave dust will not spawn around player-made structures, but can still wander in to them.
                 * (You can tell I did this so that I don't have to create a "Check if not inside a house" algorithm.)
                 */
                int lightLevel = client.world.getLightLevel(spawnPos);
                if (lightLevel <= 7) {
                    ParticleUtil.spawnParticle(client.world, spawnPos, random, ParticleTypes.WHITE_ASH);
                }
            }
        }
    }

    public static void spawnEffects(MinecraftClient client, Random random) {
        spawnFireflies(client, random);
        spawnRipples(client, random);
        spawnSandstorms(client, random);
        spawnCaveDust(client, random);
    }
}
