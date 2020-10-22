package com.ferreusveritas.dynamictreesphc;


import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModConfigs {
	
	public static File configDirectory;
	
	public static float fruitTreeOccurance;
	public static float fallingFruitFallChance;
	public static int fallingFruitDistanceFromPlayer;
	public static int mapleSyrupMaxSpiles;
	public static float treeMapleYieldPerLog;
	public static float treePaperbarkYieldPerLog;
	public static float treeCinnamonYieldPerLog;
	
	public static void preInit(FMLPreInitializationEvent event) {
		
		configDirectory = event.getModConfigurationDirectory();
		
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		//World
		fruitTreeOccurance = config.getFloat("fruitTreeOccurance", "world", 0.02f, 0.0f, 1.0f, "The chance of a spawned tree being a fruit tree from this mod.");

		//Fruit
		fallingFruitFallChance = config.getFloat("fallingFruitFallChance", "fruit", 0.005f, 0.0f, 1.0f, "The chance of a falling fruit (Coconut, Durian, Jackfruit, Breadfruit) to fall on each random tick");
		fallingFruitDistanceFromPlayer = config.getInt("fallingFruitFallChance", "fruit", 10, -1, 256, "The distance a player has to be from a falling fruit for it to fall. Set to -1 to allow fruit to fall at any distance. Set to 0 to disable falling fruits entirely.");

		//Trees
		mapleSyrupMaxSpiles = config.getInt("mapleSyrupMaxSpiles", "trees", 4, 1, 64, "The maximum amount of maple spiles that can be put in a maple tree.");
		treeMapleYieldPerLog = config.getFloat("treeMapleYieldPerLog", "trees", 0.2f, 0.0f, 128.0f, "The multiplier for Maple Syrup dropped when chopping a Maple tree down.");
		treePaperbarkYieldPerLog = config.getFloat("treePaperbarkYieldPerLog", "trees", 2.0f, 0.0f, 128.0f, "The multiplier for Paper dropped when chopping a Paperbark tree down.");
		treeCinnamonYieldPerLog = config.getFloat("treeCinnamonYieldPerLog", "trees", 3.5f, 0.0f, 128.0f, "The multiplier for Cinnamon dropped when chopping a Cinnamon tree down.");

		config.save();
	}
}
