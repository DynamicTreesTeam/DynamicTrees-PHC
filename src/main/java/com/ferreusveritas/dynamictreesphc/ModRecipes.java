package com.ferreusveritas.dynamictreesphc;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictreesphc.trees.SpeciesFruit;
import com.pam.harvestcraft.blocks.growables.BlockPamFruit;
import com.pam.harvestcraft.blocks.growables.BlockPamSapling;

import com.pam.harvestcraft.item.items.ItemPamFood;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class ModRecipes {
	
	public static void register(IForgeRegistry<IRecipe> registry) {
		//Create dirt bucket exchange recipes
		ModTrees.phcFruitSpecies.values().forEach(ModRecipes::speciesRecipes);

		Species appleSpecies = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "apple"));
		Block sapling = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("harvestcraft", "apple_sapling"));
		com.ferreusveritas.dynamictrees.ModRecipes.createDirtBucketExchangeRecipes(new ItemStack(sapling), appleSpecies.getSeedStack(1), true, "seedfromsapling");
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
