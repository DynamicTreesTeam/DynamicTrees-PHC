package com.ferreusveritas.dtphc;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.ISpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.RandomSpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.SpeciesSelection;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.TreeGenerator;
import com.pam.harvestcraft.HarvestCraft;
import com.pam.harvestcraft.config.TreeGenerationConfiguration;

import net.minecraft.world.biome.Biome;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {
	
	Map<Biome, RandomSpeciesSelector> biomeMap = new HashMap<>();
	
	@Override
	public void populate() {
		
		BiomeDataBase dbase = TreeGenerator.getTreeGenerator().biomeDataBase;
		
		for(TreeGenerationConfiguration treeConfig : HarvestCraft.fruitTreeConfigManager.treeConfigurations) {
			if(treeConfig.getEnableGeneration()) {
				String fruitName = treeConfig.getFruit();
				Species species = ModTrees.phcSpecies.get(fruitName);
				if(species != null) {
					for(Biome biome : treeConfig.getBiomesAllowed()) {
						if(dbase.shouldCancelVanillaTreeGen(biome)) {//Only populate biomes with Dynamic Trees that have been cancelled
							RandomSpeciesSelector selector = biomeMap.computeIfAbsent(biome, b -> new RandomSpeciesSelector());
							selector.add(species, 100 - treeConfig.getRarity());
							//dbase.setSpeciesSelector(biome, computeSpecies(biome), Operation.SPLICE_BEFORE);
						}
					}
				}
			}
		}

		for(Entry<Biome, RandomSpeciesSelector> e : biomeMap.entrySet()) {
			
		}
		
	}

	public ISpeciesSelector computeSpecies(Biome biome) {
		//TODO: Set up species for PHC trees
		return (pos, dirt, random) -> {	return new SpeciesSelection(); };
	}
	
}
