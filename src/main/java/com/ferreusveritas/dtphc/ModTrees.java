package com.ferreusveritas.dtphc;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.pam.harvestcraft.blocks.FruitRegistry;
import com.pam.harvestcraft.blocks.growables.BlockPamSapling.SaplingType;

import net.minecraft.util.ResourceLocation;

public class ModTrees {

	public static Map<String, Species> phcSpecies = new HashMap<>();
	
	public static void init() {
		processPHCFruitTrees(FruitRegistry.registeringFruits, false);
		//processPHCFruitTrees(FruitRegistry.registeringLogFruits, true);
	}
	
	private static void processPHCFruitTrees(Map<String, SaplingType> fruits, boolean isLogFruit) {

		for(Entry<String, SaplingType> fruitTreeEntry : fruits.entrySet()) {
			final String fruitName = fruitTreeEntry.getKey();
			final SaplingType saplingType = fruitTreeEntry.getValue();
			final String familyName;
			
			switch(saplingType) {
				default:
				case TEMPERATE:	familyName = "oak"; break;
				case COLD: 		familyName = "spruce"; break;
				case WARM: 		familyName = "jungle"; break;
			}
			
			TreeFamily treeFamily = TreeRegistry.findSpeciesSloppy(familyName).getFamily();
			
			if(treeFamily != TreeFamily.NULLFAMILY) {
				ResourceLocation resLoc = new ResourceLocation(ModConstants.MODID, fruitName);
				ILeavesProperties leavesProperties = treeFamily.getCommonSpecies().getLeavesProperties();
				Species species = isLogFruit ?
						makeLogFruitTreeSpecies(resLoc, treeFamily, leavesProperties, fruitName, saplingType) :
						makeFruitTreeSpecies(resLoc, treeFamily, leavesProperties, fruitName, saplingType);
				
				phcSpecies.put(fruitName, species);
				Species.REGISTRY.register(species);
			}
		}
		
	}
	
	private static Species makeFruitTreeSpecies(ResourceLocation name, TreeFamily treeFamily, ILeavesProperties leavesProperties, String fruitName, SaplingType saplingType) {
		return new SpeciesFruit(name, treeFamily, leavesProperties, fruitName, saplingType);
	}
	
	private static Species makeLogFruitTreeSpecies(ResourceLocation name, TreeFamily treeFamily, ILeavesProperties leavesProperties, String fruitName, SaplingType saplingType) {
		//TODO: Pam's Log Fruit Tree Specific Code Goes Here
		return new Species(name, treeFamily, leavesProperties);
	}
	
}
