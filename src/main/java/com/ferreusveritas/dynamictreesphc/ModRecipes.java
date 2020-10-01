package com.ferreusveritas.dynamictreesphc;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
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
		Block appleSapling = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("harvestcraft", "apple_sapling"));
		com.ferreusveritas.dynamictrees.ModRecipes.createDirtBucketExchangeRecipes(new ItemStack(appleSapling), appleSpecies.getSeedStack(1), true, "seedfromsapling");

		String[] specialSpecies = new String[]{FruitRegistry.CINNAMON, FruitRegistry.MAPLE, FruitRegistry.PAPERBARK};
		for (String name : specialSpecies){
			Species species = TreeRegistry.findSpecies(new ResourceLocation(com.ferreusveritas.dynamictreesphc.ModConstants.MODID, name));
			Block sapling = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("harvestcraft", name+"_sapling"));
			com.ferreusveritas.dynamictrees.ModRecipes.createDirtBucketExchangeRecipes(new ItemStack(sapling), species.getSeedStack(1), true, "seedfromsapling");

		}

		//We add passionfruit recipes separately since the whole thing is handled in a different way
		ItemStack passionfruitSeed = new ItemStack(ModItems.passionfruitSeed);
		ItemStack passionfruit = new ItemStack(FruitRegistry.getFood(FruitRegistry.PASSIONFRUIT));
		ItemStack passionfruitSapling = new ItemStack(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("harvestcraft", "passionfruit_sapling"))));
		GameRegistry.addShapelessRecipe(new ResourceLocation(ModConstants.MODID, "passionfruitseedfromsapling"), null, passionfruitSeed, Ingredient.fromStacks(passionfruitSapling), Ingredient.fromItem(com.ferreusveritas.dynamictrees.ModItems.dirtBucket));
		GameRegistry.addShapelessRecipe(new ResourceLocation(ModConstants.MODID, "passionfruitseedfromfruit"), null, passionfruitSeed, Ingredient.fromStacks(passionfruit), Ingredient.fromItem(com.ferreusveritas.dynamictrees.ModItems.dirtBucket));
		GameRegistry.addShapelessRecipe(new ResourceLocation(ModConstants.MODID, "passionfruitsapling"), null, passionfruitSapling, Ingredient.fromStacks(passionfruitSeed), Ingredient.fromItem(com.ferreusveritas.dynamictrees.ModItems.dirtBucket));
		GameRegistry.addShapelessRecipe(new ResourceLocation(ModConstants.MODID, "passionfruitseedfromfruitdirect"), null, passionfruitSeed, Ingredient.fromStacks(passionfruit));
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
			if (saplingBlock == null){
				return;
			}
			ItemStack saplingStack = new ItemStack(saplingBlock);
			ItemStack fruitStack = new ItemStack(fruitItem);
			ItemStack seedStack = species.getSeedStack(1);
			String fruit = species.getRegistryName().getResourcePath();
			ItemStack bonemeal = new ItemStack(Items.DYE, 1, 15);

			com.ferreusveritas.dynamictrees.ModRecipes.createDirtBucketExchangeRecipes(saplingStack, seedStack, true, "seedfromsapling");//Sapling <-> Seed exchange
			if (!ModConstants.SEEDISFRUIT.contains(fruit)){
				com.ferreusveritas.dynamictrees.ModRecipes.createDirtBucketExchangeRecipes(fruitStack, seedStack, false, "seedfromfruit");//Fruit --> Seed exchange

				if (!ModConstants.NUTS.contains(fruit)){ //nut recipes include bonemeal to "germinate" them
					GameRegistry.addShapelessRecipe(new ResourceLocation(species.getRegistryName().getResourceDomain(), fruit + "seedfromfruitdirect"), (ResourceLocation)null, seedStack, Ingredient.fromStacks(fruitStack));
				} else {
					GameRegistry.addShapelessRecipe(new ResourceLocation(species.getRegistryName().getResourceDomain(), fruit + "seedfromfruitgerminate"), (ResourceLocation)null, seedStack, Ingredient.fromStacks(fruitStack), Ingredient.fromStacks(bonemeal));
				}
			}
		}
	}
	
	/*
	private static void addTransformationPotion(Species species) {
		ItemStack outputStack = ModItems.dendroPotion.setTargetTree(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), species.getFamily());
		BrewingRecipeRegistry.addRecipe(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), species.getSeedStack(1), outputStack);
	}
	*/
	
}
