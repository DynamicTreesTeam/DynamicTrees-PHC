package com.ferreusveritas.dynamictreesphc.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModConfigs;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.blocks.BlockBranchPamSpecial;
import com.pam.harvestcraft.blocks.FruitRegistry;
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
			
			setBasicGrowingParameters(0.1f, 14.0f, 12, getLowestBranchHeight(), 1.8f);

			generateSeed();

			setupStandardSeedDropping();
		}

		@Override
		public boolean useDefaultWailaBody() {
			return false;
		}

		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.JUNGLE);
		}

		@Override
		public int maxBranchRadius() {
			return 4;
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
				ModConfigs.treeCinnamonYieldPerLog);
	}
}
