package com.ferreusveritas.dynamictreesphc;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {
	
	public static void preInit() { }
	
	public static void registerItems(IForgeRegistry<Item> registry) {
		//Register all of the seed items
		ModTrees.phcSpecies.values().forEach(s -> registry.register(s.getSeed()));
	}
	
}
