package com.ferreusveritas.dynamictreesphc;

import com.ferreusveritas.dynamictreesphc.items.ItemDynamicSeedPassionfruit;
import com.ferreusveritas.dynamictreesphc.trees.TreePaperBark;
import com.pam.harvestcraft.blocks.FruitRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Objects;

public class ModItems {

	public static Item passionfruitVine;
	public static ItemDynamicSeedPassionfruit passionfruitSeed;

	public static void preInit() {
		passionfruitVine = new ItemBlock(ModBlocks.passionfruitVine0).setRegistryName(Objects.requireNonNull(ModBlocks.passionfruitVine0.getRegistryName()));
		passionfruitSeed = new ItemDynamicSeedPassionfruit(new ResourceLocation(ModConstants.MODID, "passionfruitseed"));
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
		registry.registerAll(passionfruitSeed, passionfruitVine);

		ArrayList<Item> treeItems = new ArrayList<>();
		ModTrees.phcTrees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[0]));
	}
	
}
