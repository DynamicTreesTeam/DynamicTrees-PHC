package com.ferreusveritas.dynamictreesphc;

import java.util.ArrayList;

import com.ferreusveritas.dynamictreesphc.blocks.BlockMapleSpile;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

	public static Item mapleSpile;

	public static void preInit() {
		mapleSpile = new ItemBlock(ModBlocks.mapleSpile).setRegistryName(ModBlocks.mapleSpile.getRegistryName());
	}
	
	public static void register(IForgeRegistry<Item> registry) {
		//Register all of the seed items
		ModTrees.phcFruitSpecies.values().forEach(s -> registry.register(s.getSeed()));

		registry.register(mapleSpile);

		ArrayList<Item> treeItems = new ArrayList<>();
		ModTrees.phcTrees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[0]));
	}
	
}
