package com.ferreusveritas.dynamictreesphc.trees;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.SpeciesRare;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.pam.harvestcraft.blocks.BlockRegistry;
import com.pam.harvestcraft.blocks.growables.BlockPamFruit;
import com.pam.harvestcraft.blocks.growables.BlockPamSapling.SaplingType;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesFruit extends Species {
	
	public final String fruitName;
	public final SaplingType saplingType;
	public IBlockState fruitBlockState;
	protected int fruitingRadius = 5;

	public SpeciesFruit(ResourceLocation name, TreeFamily treeFamily, ILeavesProperties leavesProperties, String fruitName, SaplingType saplingType) {
		super(name, treeFamily, leavesProperties);
		this.fruitName = fruitName;
		this.saplingType = saplingType;

		fruitTreeDefaults();

		setRequiresTileEntity(true);

		switch(saplingType) {
			default:
			case TEMPERATE:
				envFactor(Type.COLD, 0.75f);
				envFactor(Type.HOT, 0.75f);
				break;
			case COLD:
				envFactor(Type.HOT, 0.50f);
				break;
			case WARM:
				envFactor(Type.COLD, 0.50f);
				break;
		}
		generateSeed();

	}

	public void setFruitBlock (BlockFruit fruitBlock){
		fruitBlockState = fruitBlock.getDefaultState();
		addGenFeature(new FeatureGenFruit(fruitBlock).setRayDistance(4).setFruitingRadius(fruitingRadius));
	}

	@Override
	public ResourceLocation getSaplingName() {
		String dtModId = com.ferreusveritas.dynamictrees.ModConstants.MODID;
		
		switch(saplingType) {
			default:
			case TEMPERATE: return new ResourceLocation(dtModId, "oak");
			case COLD: return new ResourceLocation(dtModId, "spruce");
			case WARM: return new ResourceLocation(dtModId, "jungle");
		}
	}
	
	protected void fruitTreeDefaults() {
		setBasicGrowingParameters(0.3f, 8.0f, 1, 4, 1.0f, fruitingRadius);
	}
	
	protected SpeciesFruit setBasicGrowingParameters(float tapering, float energy, int upProbability, int lowestBranchHeight, float growthRate, int fruitingRadius) {
		setBasicGrowingParameters(tapering, energy, upProbability, lowestBranchHeight, growthRate);
		this.fruitingRadius = fruitingRadius;
		return this;
	}
	
	public BlockFruit getFruitBlock() {
		return (BlockFruit) fruitBlockState.getBlock();
	}
	
	@Override
	public void addJoCodes() {
		joCodeStore.addCodesFromFile(this, "assets/" + getRegistryName().getResourceDomain() + "/trees/fruit.txt");
	}
	
}
