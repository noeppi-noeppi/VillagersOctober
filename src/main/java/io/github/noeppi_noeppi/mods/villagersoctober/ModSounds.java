package io.github.noeppi_noeppi.mods.villagersoctober;

import net.minecraft.sounds.SoundEvent;
import org.moddingx.libx.annotation.registration.RegisterClass;

@RegisterClass(registry = "SOUND_EVENT_REGISTRY")
public class ModSounds {
    
    public static final SoundEvent doorbell = new SoundEvent(VillagersOctober.getInstance().resource("doorbell"));
}
