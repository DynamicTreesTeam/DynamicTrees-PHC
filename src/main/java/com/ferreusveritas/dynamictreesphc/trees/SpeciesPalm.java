package com.ferreusveritas.dynamictreesphc.trees;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.pam.harvestcraft.blocks.growables.BlockPamSapling.SaplingType;
import net.minecraft.util.ResourceLocation;

public class SpeciesPalm extends SpeciesFruit {

	public SpeciesPalm(ResourceLocation name, TreeFamily treeFamily, ILeavesProperties leavesProperties, String fruitName, SaplingType saplingType) {
		super(name, treeFamily, leavesProperties, fruitName, saplingType);
	}

//	@Override
//	protected void setFruitBlock (){
//		BlockFruit fruitBlock = ModBlocks.fruits.get(fruitName);
//		fruitBlockState = fruitBlock.getDefaultState();
//		addGenFeature(new FeatureGenFruit(fruitBlock).setRayDistance(4).setFruitingRadius(fruitingRadius));
//	}

	@Override
	protected void fruitTreeDefaults() {
		setBasicGrowingParameters(0.3f, 8.0f, 1, 4, 1.0f, fruitingRadius);
	}

}
