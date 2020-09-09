package com.ferreusveritas.dynamictreesphc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictreesphc.trees.*;
import com.ferreusveritas.dynamictreesphc.worldgen.BiomeDataBasePopulator;
import com.pam.harvestcraft.HarvestCraft;
import com.pam.harvestcraft.blocks.FruitRegistry;
import com.pam.harvestcraft.blocks.growables.BlockPamSapling.SaplingType;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ModConstants.MODID)
public class ModTrees {
	
	protected interface ISpeciesCreator {
		Species createSpecies(ResourceLocation name, TreeFamily treeFamily, ILeavesProperties leavesProperties, String fruitName, SaplingType saplingType);
	}
	
	public static boolean fruitTreeGen;
	
	public static ArrayList<TreeFamily> phcTrees = new ArrayList<>();
	public static Map<String, Species> phcFruitSpecies = new HashMap<>();
	
	public static void init() {
		fruitTreeGen = HarvestCraft.fruitTreeConfigManager.enableFruitTreeGeneration;
	}
	
	public static void preInit() {

		System.out.println(phcTrees.size());

		//Register all of the trees
		phcTrees.forEach(tree -> phcFruitSpecies.put(tree.getName().getResourcePath(), tree.getCommonSpecies()));
		//we add the special trees later so they dont get added into phcFruitSpecies
		Collections.addAll(phcTrees, new TreeCinnamon(), new TreeMaple(), new TreePaperBark());
		phcTrees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		
		//Basic creators
		ISpeciesCreator fruitTreeCreator = SpeciesFruit::new;
		ISpeciesCreator palmTreeCreator = SpeciesPalm::new;
		
		//Set up a map of species and their sapling types
		Map<String, SaplingType> saplingMap = new HashMap<>();
		saplingMap.putAll(FruitRegistry.registeringFruits);

		//We create the fruit blocks

		//Set up a map of sapling types to tree family common species
		Map<SaplingType, TreeFamily> familyMap = new EnumMap<>(SaplingType.class);
		familyMap.put(SaplingType.TEMPERATE, TreeRegistry.findSpeciesSloppy("oak").getFamily());
		familyMap.put(SaplingType.WARM, TreeRegistry.findSpeciesSloppy("jungle").getFamily());
		familyMap.put(SaplingType.COLD, TreeRegistry.findSpeciesSloppy("spruce").getFamily());
		
		//Set up a map of species names and their creator lambdas
		Map<String, ISpeciesCreator> creatorMap = new HashMap<>();
		FruitRegistry.registeringFruits.forEach((species, sapling) -> {
			if (ModConstants.PALMS.contains(species)){
				creatorMap.put(species, palmTreeCreator);
			} else{
				creatorMap.put(species, fruitTreeCreator);
			}
		});
		
		//Tailor creators to fit Dynamic Trees
		alterCreatorMap(creatorMap);
		
		//Create all of the species
		for(Entry<String, ISpeciesCreator> creatorEntry : creatorMap.entrySet()) {
			String fruitName = creatorEntry.getKey();
			ISpeciesCreator creator = creatorEntry.getValue();
			SaplingType saplingType = saplingMap.get(fruitName);
			TreeFamily family = familyMap.get(saplingType);
			ResourceLocation resLoc = new ResourceLocation(ModConstants.MODID, fruitName);
			ILeavesProperties leavesProperties = family.getCommonSpecies().getLeavesProperties();
			Species species = creator.createSpecies(resLoc, family, leavesProperties, fruitName, saplingType);
			phcFruitSpecies.put(fruitName, species);
			Species.REGISTRY.register(species);
		}

		//Create fruit blocks
		for(Entry<String, Species> entry : phcFruitSpecies.entrySet()) {
			if (!ModConstants.NOFRUIT.contains(entry.getKey())){
				BlockFruit fruit = new BlockFruit(new ResourceLocation(ModConstants.MODID, entry.getKey()).toString());
				ModBlocks.fruits.put(entry.getKey(), fruit);
			}
		};

		for(Entry<String, Species> entry : phcFruitSpecies.entrySet()) {
			TreeRegistry.registerSaplingReplacer(FruitRegistry.getSapling(entry.getKey()).getDefaultState(), entry.getValue());
		}
		
	}
	
	private static void alterCreatorMap(Map<String, ISpeciesCreator> creatorMap) {

		//Dynamic Trees already has an apple tree
		creatorMap.remove(FruitRegistry.APPLE);
		
		//Temperate nut trees are typically hardwoods that grow slowly and usually very large.
		creatorMap.put(FruitRegistry.WALNUT, (name, treeFamily, leavesProperties, fruitName, saplingType) -> new SpeciesFruit(name, treeFamily, leavesProperties, fruitName, saplingType) {
				@Override protected void fruitTreeDefaults() { setBasicGrowingParameters(0.4f, 12.0f, 1, 4, 0.7f, 8); }
		});
		creatorMap.put(FruitRegistry.CHESTNUT, (name, treeFamily, leavesProperties, fruitName, saplingType) -> new SpeciesFruit(name, treeFamily, leavesProperties, fruitName, saplingType) {
			@Override protected void fruitTreeDefaults() { setBasicGrowingParameters(0.45f, 11.0f, 1, 4, 0.6f, 8); }
		});
		creatorMap.put(FruitRegistry.PECAN, (name, treeFamily, leavesProperties, fruitName, saplingType) -> new SpeciesFruit(name, treeFamily, leavesProperties, fruitName, saplingType) {
			@Override protected void fruitTreeDefaults() { setBasicGrowingParameters(0.45f, 11.0f, 1, 4, 0.6f, 8); }
		});
		
	}
	
	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		if(fruitTreeGen) {
			event.register(new BiomeDataBasePopulator());
		}
	}
	
}
