package com.ferreusveritas.dynamictreesphc.proxy;

import com.ferreusveritas.dynamictrees.api.WorldGenRegistry;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModItems;
import com.ferreusveritas.dynamictreesphc.ModTrees;
import com.ferreusveritas.dynamictreesphc.worldgen.BiomeDataBasePopulator;
import com.pam.harvestcraft.HarvestCraft;
import com.pam.harvestcraft.blocks.growables.BlockPamFruit;

public class CommonProxy {
	
	public void preInit() {
		ModBlocks.preInit();
		ModItems.preInit();
		ModTrees.preInit();
	}
	
	public void init() {
		if(HarvestCraft.fruitTreeConfigManager.enableFruitTreeGeneration) {
			WorldGenRegistry.registerBiomeDataBasePopulator(new BiomeDataBasePopulator());
		}
		preparePHC();
	}
	
	public void preparePHC() {
		//Force disable harvestcraft tree worldgen
		HarvestCraft.fruitTreeConfigManager.enableFruitTreeGeneration = false;

		//Change fruit block behavior to remove fruit completely on harvest
		BlockPamFruit.fruitRemoval = true;
	}
	
}
