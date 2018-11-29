package com.ferreusveritas.dynamictreesphc;

import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModModels {

	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event) {
		
		//Register Meshers for Branches
		for(TreeFamily tree: ModTrees.phcTrees) {
			ModelHelper.regModel(tree.getDynamicBranch());//Register Branch itemBlock
			ModelHelper.regModel(tree);//Register custom state mapper for branch
		}
		
		//Register GrowingLeavesBlocks Meshers and Colorizers
		for(BlockDynamicLeaves leaves: LeavesPaging.getLeavesMapForModId(ModConstants.MODID).values()) {
			ModelHelper.regModel(Item.getItemFromBlock(leaves));
		}
		
		ModTrees.phcSpecies.values().forEach(species -> ModelHelper.regModel(species.getSeed()));//Register Seed Item Models
	}
	
}
