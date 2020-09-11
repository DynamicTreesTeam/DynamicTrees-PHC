package com.ferreusveritas.dynamictreesphc;

import java.util.*;

import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictreesphc.blocks.BlockMapleSpile;
import com.ferreusveritas.dynamictreesphc.blocks.BlockMapleSpileBucket;
import com.pam.harvestcraft.blocks.FruitRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

	public static BlockMapleSpile mapleSpile, mapleSpileWithBucket;

	public static LeavesProperties cinnamonLeavesProperties;
	public static LeavesProperties mapleLeavesProperties;
	public static LeavesProperties paperBarkLeavesProperties;
	
	public static LeavesProperties[] phcLeavesProperties, palmLeavesProperties, largePalmLeavesProperties;

	public static Map<String, BlockFruit> fruits = new HashMap<>();

	public static void preInit() {

		mapleSpile = new BlockMapleSpile(new ResourceLocation(ModConstants.MODID, "maplespile"));
		mapleSpileWithBucket = new BlockMapleSpileBucket(new ResourceLocation(ModConstants.MODID, "maplespilebucket"));

		//Set up primitive leaves. This controls what is dropped on shearing, leaves replacement, etc.
		cinnamonLeavesProperties = new LeavesProperties(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE));
		mapleLeavesProperties = new LeavesProperties(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE)){
			// we make maple leaves have oak-like coloring despite being spruce. Makes it look more interesting
			@Override
			public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
				return world.getBiome(pos).getFoliageColorAtPos(pos);
			}
		};
		paperBarkLeavesProperties = new LeavesProperties(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE)){
			// we make paperbark leaves have birch-like coloring despite being spruce. Makes it look more interesting
			@Override
			public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
				return ColorizerFoliage.getFoliageColorBirch();
			}
		};
		
		//For this mod it is vital that these are never reordered.  If a leaves properties is removed from the
		//mod then there should be a LeavesProperties.NULLPROPERTIES used as a placeholder.
		phcLeavesProperties = new LeavesProperties[] {
			cinnamonLeavesProperties,
			mapleLeavesProperties,
			paperBarkLeavesProperties
		};
		
		for(LeavesProperties lp : phcLeavesProperties) {
			LeavesPaging.getNextLeavesBlock(ModConstants.MODID, lp);
		}

	}
	
	public static void register(IForgeRegistry<Block> registry) {
		ArrayList<Block> treeBlocks = new ArrayList<>();
		ModTrees.phcTrees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));

		fruits.values().forEach(registry::register);

		registry.registerAll(mapleSpile, mapleSpileWithBucket);

		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(ModConstants.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[0]));
	}
	
}
