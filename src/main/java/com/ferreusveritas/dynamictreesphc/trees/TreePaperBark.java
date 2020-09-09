package com.ferreusveritas.dynamictreesphc.trees;

import java.util.List;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.blocks.BlockBranchPamPaper;
import com.ferreusveritas.dynamictreesphc.blocks.BlockBranchPamSpecial;
import com.ferreusveritas.dynamictreesphc.dropcreators.DropCreatorFruitLogProduct;

import com.pam.harvestcraft.blocks.FruitRegistry;
import com.pam.harvestcraft.blocks.growables.BlockPamFruitLog;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreePaperBark extends TreeFamilyPHC {
	
	public static final String speciesName = FruitRegistry.PAPERBARK;
	
	//Species need not be created as a nested class.  They can be created after the tree has already been constructed.
	public class TreePaperBarkSpecies extends Species {
		
		public TreePaperBarkSpecies(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModBlocks.paperBarkLeavesProperties);
			
			setBasicGrowingParameters(0.2f, 10.0f, 4, 5, 1.0f);

			generateSeed();

			setupStandardSeedDropping();
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.JUNGLE);
		}
		
	}
	
	public TreePaperBark() {
		super(new ResourceLocation(ModConstants.MODID, speciesName));

		setPrimitiveLog(FruitRegistry.getLog(speciesName).getDefaultState());

		ModBlocks.paperBarkLeavesProperties.setTree(this);
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new TreePaperBarkSpecies(this));
	}

	@Override
	public BlockBranch createBranch() {
		return new BlockBranchPamPaper(
				getName()+"branch",
				speciesName,
				1.5f);
	}
}
