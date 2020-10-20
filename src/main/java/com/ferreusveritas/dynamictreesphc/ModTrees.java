package com.ferreusveritas.dynamictreesphc;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictreesphc.blocks.BlockPamFruit;
import com.ferreusveritas.dynamictreesphc.blocks.BlockPamFruitPalm;
import com.ferreusveritas.dynamictreesphc.trees.*;
import com.ferreusveritas.dynamictreesphc.worldgen.BiomeDataBasePopulator;
import com.pam.harvestcraft.HarvestCraft;
import com.pam.harvestcraft.blocks.FruitRegistry;
import com.pam.harvestcraft.blocks.growables.BlockPamSapling.SaplingType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;
import java.util.Map.Entry;

@Mod.EventBusSubscriber(modid = ModConstants.MODID)
public class ModTrees {
	
	protected interface ISpeciesCreator {
		Species createSpecies(ResourceLocation name, TreeFamily treeFamily, ILeavesProperties leavesProperties, String fruitName, SaplingType saplingType);
	}
	
	public static boolean fruitTreeGen;
	
	public static ArrayList<TreeFamily> phcTrees = new ArrayList<>();
	public static Map<String, Species> phcFruitSpecies = new HashMap<>();

	public static TreeFamily palmTrees, dragonfruitTree, cinnamonTree, mapleTree, paperBarkTree;
	
	public static void init() {
		fruitTreeGen = HarvestCraft.fruitTreeConfigManager.enableFruitTreeGeneration;
	}
	
	public static void preInit() {
		cinnamonTree = new TreeCinnamon();
		mapleTree = new TreeMaple();
		paperBarkTree = new TreePaperBark();
		palmTrees = new TreePalm();
		dragonfruitTree = new TreeDragonfruit();

		//Register all of the trees
		Collections.addAll(phcTrees, cinnamonTree, mapleTree, paperBarkTree, palmTrees, dragonfruitTree);

		//Register non-fruit species
		cinnamonTree.registerSpecies(Species.REGISTRY);
		mapleTree.registerSpecies(Species.REGISTRY);
		paperBarkTree.registerSpecies(Species.REGISTRY);

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
			} else {
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
			if (fruitName.equals(FruitRegistry.DRAGONFRUIT)){
				phcFruitSpecies.put(fruitName, dragonfruitTree.getCommonSpecies());
				dragonfruitTree.registerSpecies(Species.REGISTRY);
			} else {
				TreeFamily family = (ModConstants.PALMS.contains(fruitName)) ? palmTrees : familyMap.get(saplingType);
				ResourceLocation resLoc = new ResourceLocation(ModConstants.MODID, fruitName);
				ILeavesProperties leavesProperties = (ModConstants.PALMS.contains(fruitName)) ? ModBlocks.palmLeavesProperties.get(fruitName) : family.getCommonSpecies().getLeavesProperties();
				Species species = creator.createSpecies(resLoc, family, leavesProperties, fruitName, saplingType);
				if (family.getCommonSpecies() == Species.NULLSPECIES) family.setCommonSpecies(species);
				phcFruitSpecies.put(fruitName, species);
				Species.REGISTRY.register(species);
			}
		}

		//Create Passionfruit seperately
		//This isnt a passionfruit tree but a normal oak with passionfruit vines around it
		Species passionfruit = new SpeciesPassionfruit(familyMap.get(SaplingType.WARM));
		Species.REGISTRY.register(passionfruit);

		//Create fruit blocks
		for(Entry<String, Species> entry : phcFruitSpecies.entrySet()) {
			if (!ModConstants.NOFRUIT.contains(entry.getKey())){
				BlockFruit fruit;
				if (ModConstants.SIDEDFRUIT.contains(entry.getKey())){
					fruit = new BlockPamFruitPalm(new ResourceLocation(ModConstants.MODID, entry.getKey()));
				} else {
					fruit = new BlockPamFruit(new ResourceLocation(ModConstants.MODID, entry.getKey()));
				}
				ModBlocks.fruits.put(entry.getKey(), fruit);
				if (ModConstants.FRUITDROPSSEED.contains(entry.getKey())){
					fruit.setDroppedItem(entry.getValue().getSeedStack(1));
				} else {
					fruit.setDroppedItem(new ItemStack(FruitRegistry.getFood(entry.getKey())));
				}
				Species species = entry.getValue();
				if (species instanceof SpeciesFruit){
					((SpeciesFruit)species).setFruitBlock(fruit);
				}
			}
		};

		for(Entry<String, Species> entry : phcFruitSpecies.entrySet()) {
			TreeRegistry.registerSaplingReplacer(FruitRegistry.getSapling(entry.getKey()).getDefaultState(), entry.getValue());
		}

		//we add passionfruit to the fruit species list to avoid adding fruit blocks and sapling replacers.
		phcFruitSpecies.put(FruitRegistry.PASSIONFRUIT, passionfruit);
	}
	
	private static void alterCreatorMap(Map<String, ISpeciesCreator> creatorMap) {

		//Dynamic Trees already has an apple tree
		creatorMap.remove(FruitRegistry.APPLE);
		//Passion fruit is not a tree, its a vine
		creatorMap.remove(FruitRegistry.PASSIONFRUIT);

		creatorMap.put(FruitRegistry.PEPPERCORN, (name, treeFamily, leavesProperties, fruitName, saplingType) -> new SpeciesFruit(name, treeFamily, leavesProperties, fruitName, saplingType) {
			@Override protected void fruitTreeDefaults(String name) { setBasicGrowingParameters(0.2f, 6.0f, 1, 3, 1.0f, 3); }
		});
		creatorMap.put(FruitRegistry.GOOSEBERRY, (name, treeFamily, leavesProperties, fruitName, saplingType) -> new SpeciesFruit(name, treeFamily, leavesProperties, fruitName, saplingType) {
			@Override protected void fruitTreeDefaults(String name) { setBasicGrowingParameters(0.2f, 6.0f, 1, 3, 1.0f, 3); }
		});
		creatorMap.put(FruitRegistry.BANANA, (name, treeFamily, leavesProperties, fruitName, saplingType) -> new SpeciesPalm(name, treeFamily, leavesProperties, fruitName, saplingType) {
			@Override protected void fruitTreeDefaults(String name) { setBasicGrowingParameters(0.8f, 5.0f, 1, 4, 0.6f, 5); }
		});
		creatorMap.put(FruitRegistry.DATE, (name, treeFamily, leavesProperties, fruitName, saplingType) -> new SpeciesPalm(name, treeFamily, leavesProperties, fruitName, saplingType) {
			@Override protected void fruitTreeDefaults(String name) { setBasicGrowingParameters(1f, 12.0f, 1, 4, 0.3f, 7); }
		});

		//Temperate nut trees are typically hardwoods that grow slowly and usually very large.
		creatorMap.put(FruitRegistry.WALNUT, (name, treeFamily, leavesProperties, fruitName, saplingType) -> new SpeciesFruit(name, treeFamily, leavesProperties, fruitName, saplingType) {
			@Override protected void fruitTreeDefaults(String name) { setBasicGrowingParameters(0.4f, 12.0f, 1, 4, 0.7f, 8); }
			@Override public boolean isThick() { return true; }
			@Override public int maxBranchRadius() { return 24; }
		});
		creatorMap.put(FruitRegistry.CHESTNUT, (name, treeFamily, leavesProperties, fruitName, saplingType) -> new SpeciesFruit(name, treeFamily, leavesProperties, fruitName, saplingType) {
			@Override protected void fruitTreeDefaults(String name) { setBasicGrowingParameters(0.45f, 11.0f, 1, 4, 0.6f, 8); }
			@Override public boolean isThick() { return true; }
			@Override public int maxBranchRadius() { return 24; }
		});
		creatorMap.put(FruitRegistry.PECAN, (name, treeFamily, leavesProperties, fruitName, saplingType) -> new SpeciesFruit(name, treeFamily, leavesProperties, fruitName, saplingType) {
			@Override protected void fruitTreeDefaults(String name) { setBasicGrowingParameters(0.45f, 11.0f, 1, 4, 0.6f, 8); }
			@Override public boolean isThick() { return true; }
			@Override public int maxBranchRadius() { return 24; }
		});

	}
	
	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		if(fruitTreeGen) {
			event.register(new BiomeDataBasePopulator());
		}
	}
	
}
