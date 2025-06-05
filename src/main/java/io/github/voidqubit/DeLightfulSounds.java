package io.github.voidqubit;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class DeLightfulSounds {

    // This whole class is here to register 1 sound. That's the end of my sentence.

    public static final SoundEvent FLASHLIGHT_TOGGLE = registerSound("flashlight_toggle");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Identifier.of(DeLightful.MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static void init() {
    }
}
