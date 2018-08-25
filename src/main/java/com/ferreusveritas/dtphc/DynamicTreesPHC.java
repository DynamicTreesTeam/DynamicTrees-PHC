package com.ferreusveritas.dtphc;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModConstants.MODID, name=ModConstants.NAME, version=ModConstants.VERSION, dependencies=ModConstants.DEPENDENCIES)
public class DynamicTreesPHC {
	
	@Mod.Instance(ModConstants.MODID)
	public static DynamicTreesPHC instance;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		preparePHC();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		ModTrees.init();
		new BiomeDataBasePopulator().populate();
	}
	
	public void preparePHC() {
		//TODO: Force disable harvestcraft tree worldgen
		//TODO: Change fruit block behavior to remove fruit completely on harvest
	}
	
}
