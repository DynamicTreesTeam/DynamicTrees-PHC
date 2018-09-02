package com.ferreusveritas.dynamictreesphc;

import java.util.List;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeFindEnds;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.SpeciesRare;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.pam.harvestcraft.blocks.BlockRegistry;
import com.pam.harvestcraft.blocks.growables.BlockPamFruit;
import com.pam.harvestcraft.blocks.growables.BlockPamSapling.SaplingType;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesFruit extends SpeciesRare {
	
	public final String fruitName;
	public final SaplingType saplingType;
	private FeatureGenFruit fruitGen;
	public IBlockState fruitBlockState;
	private int fruitingRadius = 8;
	
	public SpeciesFruit(ResourceLocation name, TreeFamily treeFamily, ILeavesProperties leavesProperties, String fruitName, SaplingType saplingType) {
		super(name, treeFamily, leavesProperties);
		this.fruitName = fruitName;
		this.saplingType = saplingType;
		
		fruitTreeDefaults();
		
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
		
		Block fruitBlock = BlockRegistry.blocks.stream().filter(b -> b.getRegistryName().getResourcePath().equals("pam" + fruitName)).findFirst().get();
		fruitBlockState = fruitBlock.getDefaultState();
		fruitGen = new FeatureGenFruit(this, fruitBlockState).setRayDistance(4);
		
		setDynamicSapling(com.ferreusveritas.dynamictrees.ModBlocks.blockDynamicSaplingSpecies.getDefaultState());
	}
	
	protected void fruitTreeDefaults() {
		setBasicGrowingParameters(0.3f, 8.0f, 1, 4, 1.0f, 5);
	}
	
	protected SpeciesFruit setBasicGrowingParameters(float tapering, float energy, int upProbability, int lowestBranchHeight, float growthRate, int fruitingRadius) {
		setBasicGrowingParameters(tapering, energy, upProbability, lowestBranchHeight, growthRate);
		return setFruitingRadius(fruitingRadius);
	}
	
	public BlockPamFruit getFruitBlock() {
		return (BlockPamFruit) fruitBlockState.getBlock();
	}
	
	public SpeciesFruit setFruitingRadius(int fruitingRadius) {
		this.fruitingRadius = fruitingRadius;
		return this;
	}
	
	@Override
	public void postGeneration(World world, BlockPos rootPos, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds) {
		super.postGeneration(world, rootPos, biome, radius, endPoints, safeBounds);
		if(fruitBlockState.getBlock() instanceof BlockPamFruit) {
			fruitGen.setQuantity(10).setEnableHash(false).setFruit(fruitBlockState.withProperty(BlockPamFruit.AGE, 2)).gen(world, rootPos.up(), endPoints, safeBounds);
		}
	}
	
	@Override
	public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, int soilLife, boolean natural) {
		IBlockState blockState = world.getBlockState(treePos);
		BlockBranch branch = TreeHelper.getBranch(blockState);
			
		if(branch != null && branch.getRadius(blockState) >= fruitingRadius && natural) {
			if(fruitBlockState.getBlock() instanceof BlockPamFruit) {
				NodeFindEnds endFinder = new NodeFindEnds();
				TreeHelper.startAnalysisFromRoot(world, rootPos, new MapSignal(endFinder));
				fruitGen.setQuantity(1).setEnableHash(true).setFruit(fruitBlockState.withProperty(BlockPamFruit.AGE, 0)).gen(world, rootPos.up(), endFinder.getEnds(), SafeChunkBounds.ANY);
			}
		}
		
		return true;
	}
	
	@Override
	public void addJoCodes() {
		//Just use the codes from oak trees for now
		Species oak = TreeRegistry.findSpeciesSloppy("oak");
		joCodeStore.addCodesFromFile(this, "assets/" + oak.getRegistryName().getResourceDomain() + "/trees/"+ oak.getRegistryName().getResourcePath() + ".txt");
	}
	
}
