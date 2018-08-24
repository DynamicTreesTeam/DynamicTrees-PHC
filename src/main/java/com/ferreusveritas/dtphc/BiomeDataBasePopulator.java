package com.ferreusveritas.dtphc;

import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.EnumChance;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.IChanceSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.ISpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.SpeciesSelection;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase.Operation;
import com.ferreusveritas.dynamictrees.worldgen.TreeGenerator;
import com.pam.harvestcraft.HarvestCraft;
import com.pam.harvestcraft.config.TreeGenerationConfiguration;

import net.minecraft.world.biome.Biome;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

	@Override
	public void populate() {
		
		BiomeDataBase dbase = TreeGenerator.getTreeGenerator().biomeDataBase;
		
		for(TreeGenerationConfiguration treeConfig : HarvestCraft.fruitTreeConfigManager.treeConfigurations) {
			String fruitName = treeConfig.getFruit();
			Species species = ModTrees.phcSpecies.get(fruitName);
			if(species != null) {
				for(Biome biome : treeConfig.getBiomesAllowed()) {
					if(dbase.shouldCancelVanillaTreeGen(biome)) {//Only populate biomes with Dynamic Trees that have been cancelled
						dbase.setChanceSelector(biome, computeChance(biome), Operation.SPLICE_BEFORE);
						dbase.setSpeciesSelector(biome, computeSpecies(biome), Operation.SPLICE_BEFORE);
					}
				}
			}
		}

	}

	public IChanceSelector computeChance(Biome biome) {
		//TODO: Set up chances for PHC trees
		return (rnd, spc, rad) -> { return EnumChance.UNHANDLED; };
	}

	public ISpeciesSelector computeSpecies(Biome biome) {
		//TODO: Set up species for PHC trees
		return (pos, dirt, random) -> {	return new SpeciesSelection(); };
	}
	
}
