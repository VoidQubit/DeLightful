package io.github.voidqubit.mixin;

import io.github.voidqubit.DeLightfulParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
public class FallingLeavesTickInject {
    @Inject(at = @At("HEAD"), method = "randomDisplayTick")
    private void init(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci) {

        int spawnChance = world.isThundering() ? 1 : world.isRaining() ? 20 : 30;

        if (!world.getBlockState(pos.down()).isAir()) return;
        if (random.nextInt(spawnChance) != 0) return;

        if (world.getBlockState(pos).getBlock() == Blocks.CHERRY_LEAVES) return;

        ParticleUtil.spawnParticle(world, pos, random, DeLightfulParticles.FALLING_LEAVES);

    }
}