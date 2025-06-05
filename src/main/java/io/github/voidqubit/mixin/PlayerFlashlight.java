package io.github.voidqubit.mixin;

import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.AreaLight;
import io.github.voidqubit.DeLightful;
import io.github.voidqubit.utils.DeLightfulUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerFlashlight {

    @Unique
    private final AreaLight light = new AreaLight();

    @Unique
    private boolean lightInitialized = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        /*
        * This is going to return if the code runs on the server.
        * We do this since all rendering code has to run on the client.
        */
        if (!player.getWorld().isClient) return;

        // This ensures that the following code runs on the client render thread.
        MinecraftClient.getInstance().execute(() -> {
            if (!lightInitialized) {
                VeilRenderSystem.renderer().getLightRenderer().addLight(light);
                lightInitialized = true;
            }

            float tickDelta = MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true);
            Vec3d renderPosition = player.getLerpedPos(tickDelta);
            Vec3d eyePos = player.getEyePos();
            Vec3d lookVec = player.getRotationVec(1.0f);

            eyePos = eyePos.add(lookVec.multiply(0.1));


            if (!DeLightful.isFlashlightEnabled()) {
                light.setBrightness(0.0f);
            } else {
                light.setBrightness(1.5f);
            }

            light.setSize(.001, .001);
            light.setDistance(50.0f);
            light.setAngle(0.7f);
            light.setPosition(renderPosition.x, eyePos.y, renderPosition.z);
            light.setOrientation(DeLightfulUtils.getOrientation(player.getRotationVector(), player));
        });
    }
}
