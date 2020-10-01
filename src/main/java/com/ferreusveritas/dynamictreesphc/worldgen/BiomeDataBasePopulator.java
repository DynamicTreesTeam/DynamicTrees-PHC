package com.ferreusveritas.dynamictreesphc.worldgen;

import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.RandomSpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.SpeciesSelection;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase.Operation;
import com.ferreusveritas.dynamictreesphc.ModTrees;
import com.pam.harvestcraft.HarvestCraft;
import com.pam.harvestcraft.config.TreeGenerationConfiguration;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

	Map<Biome, RandomSpeciesSelector> biomeMap = new HashMap<>();
	
	@Override
	public void populate(BiomeDataBase dbase) {
		
		for(TreeGenerationConfiguration treeConfig : HarvestCraft.fruitTreeConfigManager.treeConfigurations) {
			if(treeConfig.getEnableGeneration()) {
				String fruitName = treeConfig.getFruit();
				Species species = ModTrees.phcFruitSpecies.get(fruitName);
				if(species != null) {
					for(Biome biome : treeConfig.getBiomesAllowed()) {
						if(dbase.shouldCancelVanillaTreeGen(biome)) {//Only populate biomes with Dynamic Trees that have been cancelled
							RandomSpeciesSelector selector = biomeMap.computeIfAbsent(biome, b -> new RandomSpeciesSelector());
							selector.add(species, 100 - treeConfig.getRarity());
						}
					}
				}
			}
		}
		
		float harvestCraftOccurance = 0.02f;
		
		for(Entry<Biome, RandomSpeciesSelector> entry : biomeMap.entrySet()) {
			if(!BiomeDictionary.hasType(entry.getKey() , Type.SPOOKY)) { //Little fruit trees mess up the roof of the roofed forest
				dbase.setSpeciesSelector(entry.getKey(), (pos, dirt, random) -> random.nextFloat() < harvestCraftOccurance ? entry.getValue().getSpecies(pos, dirt, random): new SpeciesSelection(), Operation.SPLICE_BEFORE);
			}
		}

	}
	
}
