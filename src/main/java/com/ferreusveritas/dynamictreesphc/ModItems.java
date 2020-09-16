package com.ferreusveritas.dynamictreesphc;

import com.ferreusveritas.dynamictreesphc.compat.PamTrees;
import com.ferreusveritas.dynamictreesphc.trees.TreePaperBark;
import com.pam.harvestcraft.blocks.FruitRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Objects;

public class ModItems {

	public static void preInit() {
	}
	
	public static void register(IForgeRegistry<Item> registry) {
		//Register all of the seed items except dragonfruit and palm since they are common species of a family
		ModTrees.phcFruitSpecies.values().forEach(s -> {
			String name = s.getRegistryName().getResourcePath();
			if (!name.equals(FruitRegistry.DRAGONFRUIT) && !name.equals(FruitRegistry.DATE)){
				registry.register(s.getSeed());
			}
		});

		registry.register(new ItemBlock(TreePaperBark.paperbarkCutBranch).setRegistryName(Objects.requireNonNull(TreePaperBark.paperbarkCutBranch.getRegistryName())));

		ArrayList<Item> treeItems = new ArrayList<>();
		ModTrees.phcTrees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[0]));

		PamTrees.itemRegister(registry);
	}
	
}
