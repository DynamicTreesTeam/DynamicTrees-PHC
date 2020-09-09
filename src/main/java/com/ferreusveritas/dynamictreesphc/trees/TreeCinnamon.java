package com.ferreusveritas.dynamictreesphc.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.blocks.BlockBranchPamSpecial;
import com.ferreusveritas.dynamictreesphc.dropcreators.DropCreatorFruitLogProduct;

import com.pam.harvestcraft.blocks.FruitRegistry;
import com.pam.harvestcraft.blocks.growables.BlockPamFruitLog;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeCinnamon extends TreeFamilyPHC {
	
	public static final String speciesName = FruitRegistry.CINNAMON;
	
	//Species need not be created as a nested class.  They can be created after the tree has already been constructed.
	public class TreeCinnamonSpecies extends Species {
		
		public TreeCinnamonSpecies(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModBlocks.cinnamonLeavesProperties);
			
			setBasicGrowingParameters(0.1f, 14.0f, 12, getLowestBranchHeight(), 1.0f);

			generateSeed();

			setupStandardSeedDropping();
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.JUNGLE);
		}

		@Override
		public int maxBranchRadius() {
			return 6;
		}
	}

	public TreeCinnamon() {
		super(new ResourceLocation(ModConstants.MODID, speciesName));

		setPrimitiveLog(FruitRegistry.getLog(speciesName).getDefaultState());

		ModBlocks.cinnamonLeavesProperties.setTree(this);
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new TreeCinnamonSpecies(this));
	}

	@Override
	public BlockBranch createBranch() {
		return new BlockBranchPamSpecial(
				getName()+"branch",
				speciesName,
				1);
	}
}
