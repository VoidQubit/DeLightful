package io.github.voidqubit;

import io.github.voidqubit.particles.FallingLeavesParticle;
import io.github.voidqubit.particles.FireflyParticle;
import io.github.voidqubit.particles.RippleParticle;
import io.github.voidqubit.particles.SandstormParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class DeLightfulClient implements ClientModInitializer {

    // I added 4 particles. Took me 3 months. Help me. I haven't slept in the last 6 days. I am hallucinating fireflies now.

    private static KeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(DeLightfulParticles.FIREFLY, FireflyParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(DeLightfulParticles.SANDSTORM, SandstormParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(DeLightfulParticles.RIPPLE, RippleParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(DeLightfulParticles.FALLING_LEAVES, FallingLeavesParticle.Factory::new);

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.delightful.toggle_flashlight", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "key.categories.misc"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                if (client == null || client.player == null) return;
                DeLightful.setFlashlightEnabled(!DeLightful.isFlashlightEnabled());
                client.player.playSound(DeLightfulSounds.FLASHLIGHT_TOGGLE, 1.0f, DeLightful.isFlashlightEnabled() ? 1.0f : 1.2f);
            }
        });
    }
}
