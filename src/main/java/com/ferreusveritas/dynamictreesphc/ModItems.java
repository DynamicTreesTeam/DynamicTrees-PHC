package com.ferreusveritas.dynamictreesphc;

import com.ferreusveritas.dynamictreesphc.trees.TreePaperBark;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Objects;

public class ModItems {

	public static void preInit() {
	}
	
	public static void register(IForgeRegistry<Item> registry) {
		//Register all of the seed items
		ModTrees.phcFruitSpecies.values().forEach(s -> registry.register(s.getSeed()));

		registry.register(new ItemBlock(TreePaperBark.paperbarkCutBranch).setRegistryName(Objects.requireNonNull(TreePaperBark.paperbarkCutBranch.getRegistryName())));

		ArrayList<Item> treeItems = new ArrayList<>();
		ModTrees.phcTrees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[0]));
	}
	
}
