package com.ferreusveritas.dynamictreesphc;

import com.ferreusveritas.dynamictreesphc.items.ItemDynamicSeedMaple;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModEntities {

    public static void registerEntities(IForgeRegistry<EntityEntry> registry) {
        int id = 0;
        EntityRegistry.registerModEntity(new ResourceLocation(ModConstants.MODID, "mapleseed"), ItemDynamicSeedMaple.EntityItemMapleSeed.class, "maple_seed", id++, ModConstants.MODID, 32, 1, true);
      //  EntityRegistry.registerModEntity(new ResourceLocation(ModConstants.MODID, "falling_fruit"), EntityFallingFruit.class, "falling_fruit", id++, ModConstants.MODID, 512, Integer.MAX_VALUE, true);
    }

}
