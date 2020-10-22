package com.ferreusveritas.dynamictreesphc.proxy;

import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModItems;
import com.ferreusveritas.dynamictreesphc.ModSounds;
import com.ferreusveritas.dynamictreesphc.ModTrees;
import com.ferreusveritas.dynamictreesphc.event.TooltipEventHandler;
import com.pam.harvestcraft.HarvestCraft;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

	public void preInit() {
		MinecraftForge.EVENT_BUS.register(new TooltipEventHandler());
		ModBlocks.preInit();
		ModItems.preInit();
		ModTrees.preInit();
	}
	
	public void init() {
		ModTrees.init();
		preparePHC();
		ModSounds.registerSounds();
	}
	
	public void preparePHC() {
		//Force disable harvestcraft tree worldgen
		HarvestCraft.fruitTreeConfigManager.enableFruitTreeGeneration = false;

		//Change fruit block behavior to remove fruit completely on harvest
		//BlockPamFruit.fruitRemoval = true;
	}
	
}
