package com.ferreusveritas.dynamictreesphc;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictreesphc.trees.SpeciesFruit;
import com.pam.harvestcraft.blocks.growables.BlockPamFruit;
import com.pam.harvestcraft.blocks.growables.BlockPamSapling;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.registries.IForgeRegistry;

public class ModRecipes {
	
	public static void register(IForgeRegistry<IRecipe> registry) {
		//Create dirt bucket exchange recipes
		ModTrees.phcSpecies.values().forEach(ModRecipes::speciesRecipes);
	}
	
	private static void speciesRecipes(Species species) {
		addSeedExchange(species);
		//addTransformationPotion(species); //Transformation potions only work at the TreeFamily level for now
	}
	
	private static void addSeedExchange(Species species) {
		
		if(species instanceof SpeciesFruit) {			
			SpeciesFruit speciesFruit = (SpeciesFruit)species;
			BlockPamFruit blockPamFruit = speciesFruit.getFruitBlock();
			BlockPamSapling saplingFruit = blockPamFruit.getSapling();
			Item fruitItem = blockPamFruit.getFruitItem();
			ItemStack saplingStack = new ItemStack(saplingFruit);
			ItemStack fruitStack = new ItemStack(fruitItem);
			ItemStack seedStack = species.getSeedStack(1);
			
			com.ferreusveritas.dynamictrees.ModRecipes.createDirtBucketExchangeRecipes(saplingStack, seedStack, true, "seedfromsapling");//Sapling <-> Seed exchange
			com.ferreusveritas.dynamictrees.ModRecipes.createDirtBucketExchangeRecipes(fruitStack, seedStack, false, "seedfromfruit");//Fruit --> Seed exchange
		}
	}
	
	/*
	private static void addTransformationPotion(Species species) {
		ItemStack outputStack = ModItems.dendroPotion.setTargetTree(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), species.getFamily());
		BrewingRecipeRegistry.addRecipe(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), species.getSeedStack(1), outputStack);
	}
	*/
	
}
