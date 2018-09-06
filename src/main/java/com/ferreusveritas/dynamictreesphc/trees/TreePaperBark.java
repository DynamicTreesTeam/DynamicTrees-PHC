package com.ferreusveritas.dynamictreesphc.trees;

import java.util.List;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModConstants;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreePaperBark extends TreeFamilyPHC {
	
	public static final String speciesName = "paperbark";
	
	//Species need not be created as a nested class.  They can be created after the tree has already been constructed.
	public class TreePaperBarkSpecies extends Species {
		
		public TreePaperBarkSpecies(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModBlocks.paperBarkLeavesProperties);
			
			setBasicGrowingParameters(0.2f, 10.0f, getUpProbability(), getLowestBranchHeight(), 1.0f);
			setDynamicSapling(speciesName);
			setupStandardSeedDropping();
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.JUNGLE);
		}
		
		@Override
		public void addJoCodes() {
			//Disable adding of JoCodes
		}
		
	}
	
	public TreePaperBark() {
		super(new ResourceLocation(ModConstants.MODID, speciesName));
		
		//Set up primitive log. This controls what is dropped on harvest.
		setPrimitiveLog(ModBlocks.primPaperBarkLog.getDefaultState());
		
		ModBlocks.paperBarkLeavesProperties.setTree(this);
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new TreePaperBarkSpecies(this));
		getCommonSpecies().generateSeed();
	}
	
	//Since we created a DynamicSapling in the common species we need to let it out to be registered.
	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(getCommonSpecies().getDynamicSapling().getBlock());
		return super.getRegisterableBlocks(blockList);
	}
	
	//This mod registers all of the seeds externally so we'll only provide the dynamic branch block here
	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		//Register an itemBlock for the branch block
		itemList.add(new ItemBlock(getDynamicBranch()).setRegistryName(getDynamicBranch().getRegistryName()));
		return itemList;
	}
	
}
