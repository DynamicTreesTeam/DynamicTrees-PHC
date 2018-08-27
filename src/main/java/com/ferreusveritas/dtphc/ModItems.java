package com.ferreusveritas.dtphc;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {
	
	public static void registerItems(IForgeRegistry<Item> registry) {
		//Generate seed items
		ModTrees.phcSpecies.values().forEach(s -> registry.register(s.getSeed()));
	}
	
}
