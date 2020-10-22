package com.ferreusveritas.dynamictreesphc;


import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModConfigs {
	
	public static File configDirectory;
	
	public static float fruitTreeOccurance;
	
	public static void preInit(FMLPreInitializationEvent event) {
		
		configDirectory = event.getModConfigurationDirectory();
		
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		//World
		fruitTreeOccurance = config.getFloat("fruitTreeOccurance", "world", 0.02f, 0.0f, 1.0f, "The chance of a spawned tree being a fruit tree from this mod");
		
		config.save();
	}
}
