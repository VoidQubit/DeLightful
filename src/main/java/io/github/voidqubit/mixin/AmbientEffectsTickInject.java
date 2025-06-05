package io.github.voidqubit.mixin;

import io.github.voidqubit.utils.DeLightfulUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class AmbientEffectsTickInject {
    @Shadow
    @Final
    private Random random;

    @Inject(at = @At("HEAD"), method = "loadWorld")
    private void init(CallbackInfo info) {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            DeLightfulUtils.spawnEffects(client, random);
        });
    }

}