package com.ferreusveritas.dynamictreesphc;

import java.util.ArrayList;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.pam.harvestcraft.blocks.FruitRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

	public static LeavesProperties cinnamonLeavesProperties;
	public static LeavesProperties mapleLeavesProperties;
	public static LeavesProperties paperBarkLeavesProperties;
	
	public static LeavesProperties[] phcLeavesProperties;

	public static Block primCinnamonLog;
	public static Block primMapleLog;
	public static Block primPaperBarkLog;
	
	public static void preInit() {
		
		//Set up primitive leaves. This controls what is dropped on shearing, leaves replacement, etc.
		cinnamonLeavesProperties = new LeavesProperties(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE));
		mapleLeavesProperties = new LeavesProperties(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE));
		paperBarkLeavesProperties = new LeavesProperties(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE));
		
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
		
		primCinnamonLog = FruitRegistry.getLog(FruitRegistry.CINNAMON);
		primPaperBarkLog = FruitRegistry.getLog(FruitRegistry.PAPERBARK);
		primMapleLog = FruitRegistry.getLog(FruitRegistry.MAPLE);
	}
	
	public static void registerBlocks(IForgeRegistry<Block> registry) {
		ArrayList<Block> treeBlocks = new ArrayList<>();
		ModTrees.phcTrees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		
		treeBlocks.addAll(TreeHelper.getLeavesMapForModId(ModConstants.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[0]));
	}
	
}
