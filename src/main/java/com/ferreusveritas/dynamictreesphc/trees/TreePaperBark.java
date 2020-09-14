package com.ferreusveritas.dynamictreesphc.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.blocks.BlockBranchPamPaper;
import com.pam.harvestcraft.blocks.FruitRegistry;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import java.util.List;

public class TreePaperBark extends TreeFamilyPHC {
	
	public static final String speciesName = FruitRegistry.PAPERBARK;
	
	//Species need not be created as a nested class.  They can be created after the tree has already been constructed.
	public class TreePaperBarkSpecies extends Species {
		
		public TreePaperBarkSpecies(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModBlocks.paperBarkLeavesProperties);
			
			setBasicGrowingParameters(0.2f, 16.0f, 4, 5, 1.0f);

			generateSeed();

			setupStandardSeedDropping();
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.JUNGLE);
		}
		
	}

	public static BlockBranchPamPaper paperbarkBranch, paperbarkCutBranch;

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
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		paperbarkCutBranch = new BlockBranchPamPaper(
				getName()+"cutbranch",
				speciesName,
				2f,
				true
		);
		paperbarkCutBranch.setFamily(this);
		blockList.add(paperbarkCutBranch);
		return super.getRegisterableBlocks(blockList);
	}

	@Override
	public BlockBranch createBranch() {
		paperbarkBranch = new BlockBranchPamPaper(
				getName()+"branch",
				speciesName,
				2f,
				false
		);
		return paperbarkBranch;
	}
}
