package com.ferreusveritas.dynamictreesphc;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictreesphc.blocks.BlockDynamicLeavesPalm;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModBlocks {

	public static BlockMapleSpile mapleSpile, mapleSpileWithBucket;

	public static LeavesProperties cinnamonLeavesProperties;
	public static LeavesProperties mapleLeavesProperties;
	public static LeavesProperties paperBarkLeavesProperties;
	public static LeavesProperties datePalmLeavesProperties;
	public static LeavesProperties papayaLeavesProperties;
	public static LeavesProperties bananaLeavesProperties;
	public static LeavesProperties coconutLeavesProperties;
	public static LeavesProperties dragonfruitLeavesProperties;

	public static LeavesProperties[] phcLeavesProperties;

	public static BlockDynamicLeavesPalm leavesPalm, leavesDragonfruit;

	public static Map<String, LeavesProperties> palmLeavesProperties = new HashMap<>();
	public static Map<String, BlockFruit> fruits = new HashMap<>();

	public static void preInit() {

		mapleSpile = new BlockMapleSpile(new ResourceLocation(ModConstants.MODID, "maplespile"));
		mapleSpileWithBucket = new BlockMapleSpileBucket(new ResourceLocation(ModConstants.MODID, "maplespilebucket"));

		leavesPalm = new BlockDynamicLeavesPalm("leaves_palm");
		leavesDragonfruit = new BlockDynamicLeavesPalm("leaves_dragonfruit");

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
		datePalmLeavesProperties = new LeavesProperties(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE), TreeRegistry.findCellKit("palm"));
		papayaLeavesProperties = new LeavesProperties(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE), TreeRegistry.findCellKit("palm"));
		dragonfruitLeavesProperties = new LeavesProperties(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE), TreeRegistry.findCellKit("palm")){
			// since dragonfruit is a cactus it shouldnt change color, so with stick with birch which doesnt change with biome
			@Override
			public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
				return ColorizerFoliage.getFoliageColorBirch();
			}
		};
		bananaLeavesProperties = new LeavesProperties(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE), TreeRegistry.findCellKit("palm"));
		coconutLeavesProperties = new LeavesProperties(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE), TreeRegistry.findCellKit("palm"));

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

		datePalmLeavesProperties.setDynamicLeavesState(leavesPalm.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		coconutLeavesProperties.setDynamicLeavesState(leavesPalm.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 1));
		leavesPalm.setProperties(0, datePalmLeavesProperties);
		leavesPalm.setProperties(1, coconutLeavesProperties);

		papayaLeavesProperties.setDynamicLeavesState(leavesPalm.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 2));
		bananaLeavesProperties.setDynamicLeavesState(leavesPalm.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 3));
		leavesPalm.setProperties(2, papayaLeavesProperties);
		leavesPalm.setProperties(3, bananaLeavesProperties);

		dragonfruitLeavesProperties.setDynamicLeavesState(leavesDragonfruit.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		leavesDragonfruit.setProperties(0, dragonfruitLeavesProperties);

		 palmLeavesProperties = new HashMap<String, LeavesProperties>(){{
			put(FruitRegistry.DATE, datePalmLeavesProperties);
			put(FruitRegistry.PAPAYA, papayaLeavesProperties);
			put(FruitRegistry.BANANA, bananaLeavesProperties);
			put(FruitRegistry.COCONUT, coconutLeavesProperties);
		}};

	}
	
	public static void register(IForgeRegistry<Block> registry) {
		ArrayList<Block> treeBlocks = new ArrayList<>();
		ModTrees.phcTrees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));

		fruits.values().forEach(registry::register);

		registry.registerAll(mapleSpile, mapleSpileWithBucket, leavesPalm, leavesDragonfruit);

		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(ModConstants.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[0]));
	}
	
}
