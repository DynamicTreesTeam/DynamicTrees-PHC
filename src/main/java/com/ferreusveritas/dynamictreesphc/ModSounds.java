package com.ferreusveritas.dynamictreesphc;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModSounds {

    public static SoundEvent BLOCK_FRUIT_BONK;

    public static void registerSounds(){
        BLOCK_FRUIT_BONK = registerSound("blocks.fruit.bonk");
    }

    private static SoundEvent registerSound(String name){
        ResourceLocation location = new ResourceLocation(ModConstants.MODID, name);
        SoundEvent soundEvent = new SoundEvent(location);
        soundEvent.setRegistryName(location);
        ForgeRegistries.SOUND_EVENTS.register(soundEvent);
        return soundEvent;
    }

}
