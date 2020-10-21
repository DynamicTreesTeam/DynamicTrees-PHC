package com.ferreusveritas.dynamictreesphc;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictreesphc.trees.SpeciesFruit;
import com.pam.harvestcraft.blocks.FruitRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

public class ModRecipes {
	
	public static void register(IForgeRegistry<IRecipe> registry) {
		//Create dirt bucket exchange recipes
		ModTrees.phcFruitSpecies.values().forEach(ModRecipes::speciesRecipes);

		//We add apple sapling recipe separately as the PHC apple tree didnt get a dynamic version
		Species appleSpecies = TreeRegistry.findSpecies(new ResourceLocation(com.ferreusveritas.dynamictrees.ModConstants.MODID, "apple"));
		ItemStack appleSapling = new ItemStack(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("harvestcraft", "apple_sapling"))));
		createDirtBucketExchangeRecipes(appleSapling, appleSpecies.getSeedStack(1), false, "seedfromsapling");

		String[] specialSpecies = new String[]{FruitRegistry.CINNAMON, FruitRegistry.MAPLE, FruitRegistry.PAPERBARK};
		for (String name : specialSpecies){
			Species species = TreeRegistry.findSpecies(new ResourceLocation(com.ferreusveritas.dynamictreesphc.ModConstants.MODID, name));
			ItemStack sapling = new ItemStack(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("harvestcraft", name + "_sapling"))));
			createDirtBucketExchangeRecipes(sapling, species.getSeedStack(1), true, "seedfromsapling");
		}

		Species peppercornSpecies = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, FruitRegistry.PEPPERCORN));
		ItemStack peppercornSapling = new ItemStack(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("harvestcraft", "peppercorn_sapling"))));
		createDirtBucketExchangeRecipes(peppercornSpecies.getSeedStack(1), peppercornSapling, new ItemStack(ModItems.ripePeppercorn), new ResourceLocation(ModConstants.MODID, FruitRegistry.PEPPERCORN+"ripe"), false);

		//We add passionfruit recipes separately since the whole thing is handled in a different way
		ItemStack passionfruitSeed = new ItemStack(ModItems.passionfruitSeed);
		ItemStack passionfruit = new ItemStack(FruitRegistry.getFood(FruitRegistry.PASSIONFRUIT));
		ItemStack passionfruitSapling = new ItemStack(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("harvestcraft", "passionfruit_sapling"))));
		ItemStack passionfruitVine = new ItemStack(ModItems.passionfruitVine);
		createTripleComboExchangeRecipes(passionfruitSeed, passionfruitSapling, passionfruit, new ResourceLocation(ModConstants.MODID, FruitRegistry.PASSIONFRUIT), false, false);
		GameRegistry.addShapelessRecipe(new ResourceLocation(ModConstants.MODID, "passionfruitsaplingfromvines"), null, passionfruitSapling, Ingredient.fromStacks(passionfruitVine), Ingredient.fromItem(com.ferreusveritas.dynamictrees.ModItems.dirtBucket));
		OreDictionary.registerOre("treeSapling", passionfruitSeed);
	}
	
	private static void speciesRecipes(Species species) {
		addSeedExchange(species);
		//addTransformationPotion(species); //Transformation potions only work at the TreeFamily level for now
	}
	
	private static void addSeedExchange(Species species) {
		
		if(species instanceof SpeciesFruit) {			
			SpeciesFruit speciesFruit = (SpeciesFruit)species;
			BlockFruit blockFruit = speciesFruit.getFruitBlock();
			Item fruitItem = blockFruit.getFruitDrop().getItem();
			Block saplingBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(fruitItem.getRegistryName().getResourceDomain(), species.getRegistryName().getResourcePath()+"_sapling"));
			if (saplingBlock == null) return;
			ItemStack saplingStack = new ItemStack(saplingBlock);
			ItemStack fruitStack = new ItemStack(fruitItem);
			ItemStack seedStack = species.getSeedStack(1);
			String fruit = species.getRegistryName().getResourcePath();

			createTripleComboExchangeRecipes(seedStack, saplingStack, fruitStack, species.getRegistryName(), ModConstants.SEEDISFRUIT.contains(fruit), ModConstants.NUTS.contains(fruit));
		}
	}

	public static void createTripleComboExchangeRecipes(ItemStack seed, ItemStack sapling, ItemStack fruit, ResourceLocation name, boolean seedIsFruit, boolean requiresBonemeal){
		createDirtBucketExchangeRecipes(sapling, seed, true, "seedfromsapling", name);//Sapling <-> Seed exchange
		if (!seedIsFruit && fruit != null){
			createDirtBucketExchangeRecipes(seed, sapling, fruit, name, requiresBonemeal);
		}
	}

	public static void createDirtBucketExchangeRecipes(ItemStack saplingStack, ItemStack seedStack, boolean seedIsSapling, String suffix) {
		if(!seedStack.isEmpty() && seedStack.getItem() instanceof Seed) {
			Seed seed = (Seed) seedStack.getItem();
			createDirtBucketExchangeRecipes(saplingStack, seedStack, seedIsSapling, suffix, seed.getSpecies(seedStack).getRegistryName());
		}
	}

	public static void createDirtBucketExchangeRecipes(ItemStack saplingStack, ItemStack seedStack, boolean seedIsSapling, String suffix, ResourceLocation species) {
		System.out.println(species);
		if(!saplingStack.isEmpty() && !seedStack.isEmpty()) {

			String speciesPath = species.getResourcePath();
			String speciesDomain = species.getResourceDomain();

			//Create a seed from a sapling and dirt bucket
			GameRegistry.addShapelessRecipe(
					new ResourceLocation(speciesDomain, speciesPath + suffix),
					null,
					seedStack,
					Ingredient.fromStacks(saplingStack),
					Ingredient.fromItem(com.ferreusveritas.dynamictrees.ModItems.dirtBucket));

			if(seedIsSapling) {
				//Creates a vanilla sapling from a seed and dirt bucket
				GameRegistry.addShapelessRecipe(
						new ResourceLocation(speciesDomain, speciesPath + "sapling"),
						null,
						saplingStack,
						Ingredient.fromStacks(seedStack),
						Ingredient.fromItem(com.ferreusveritas.dynamictrees.ModItems.dirtBucket));

				//Register the seed in the ore dictionary as a sapling since we can convert for free anyway.
				OreDictionary.registerOre("treeSapling", seedStack);
			}
		}
	}

	public static void createDirtBucketExchangeRecipes(ItemStack seed, ItemStack sapling, ItemStack fruit, ResourceLocation name, boolean requiresBonemeal){
		if (!requiresBonemeal){
			GameRegistry.addShapelessRecipe(new ResourceLocation(name.getResourceDomain(), name.getResourcePath() + "seedfromfruit"), null, seed, Ingredient.fromStacks(fruit));
			GameRegistry.addShapelessRecipe(new ResourceLocation(name.getResourceDomain(), name.getResourcePath()+"saplingfromfruit"), null, sapling, Ingredient.fromStacks(fruit), Ingredient.fromItem(com.ferreusveritas.dynamictrees.ModItems.dirtBucket));//Fruit --> Sapling exchange
		} else {
			ItemStack bonemeal = new ItemStack(Items.DYE, 1, 15);
			GameRegistry.addShapelessRecipe(new ResourceLocation(name.getResourceDomain(), name.getResourcePath() + "seedfromfruitgerminate"), null, seed, Ingredient.fromStacks(fruit), Ingredient.fromStacks(bonemeal));
			GameRegistry.addShapelessRecipe(new ResourceLocation(name.getResourceDomain(), name.getResourcePath()+"saplingfromfruit"), null, sapling, Ingredient.fromStacks(fruit), Ingredient.fromStacks(bonemeal), Ingredient.fromItem(com.ferreusveritas.dynamictrees.ModItems.dirtBucket));//Fruit --> Sapling exchange
		}
	}

	/*
	private static void addTransformationPotion(Species species) {
		ItemStack outputStack = ModItems.dendroPotion.setTargetTree(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), species.getFamily());
		BrewingRecipeRegistry.addRecipe(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), species.getSeedStack(1), outputStack);
	}
	*/
	
}
