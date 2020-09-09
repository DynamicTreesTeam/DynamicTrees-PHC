package com.ferreusveritas.dynamictreesphc.trees;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.SpeciesRare;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.pam.harvestcraft.blocks.BlockRegistry;
import com.pam.harvestcraft.blocks.growables.BlockPamFruit;
import com.pam.harvestcraft.blocks.growables.BlockPamSapling.SaplingType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesPalm extends SpeciesFruit {

	private int fruitingRadius = 5;

	public SpeciesPalm(ResourceLocation name, TreeFamily treeFamily, ILeavesProperties leavesProperties, String fruitName, SaplingType saplingType) {
		super(name, treeFamily, leavesProperties, fruitName, saplingType);
	}

	@Override
	protected void setFruitBlock (){
		Block fruitBlock = BlockRegistry.blocks.stream().filter(b -> b.getRegistryName().getResourcePath().equals("pam" + fruitName)).findFirst().get();
		IBlockState ripeFruit = fruitBlock.getDefaultState().withProperty(BlockPamFruit.AGE, 2);
		IBlockState unripeFruit = fruitBlock.getDefaultState().withProperty(BlockPamFruit.AGE, 0);
		fruitBlockState = fruitBlock.getDefaultState();
		addGenFeature(new FeatureGenFruit(unripeFruit, ripeFruit).setRayDistance(4).setFruitingRadius(fruitingRadius));
	}

	@Override
	protected void fruitTreeDefaults() {
		setBasicGrowingParameters(0.3f, 8.0f, 1, 4, 1.0f, fruitingRadius);
	}
	
}
