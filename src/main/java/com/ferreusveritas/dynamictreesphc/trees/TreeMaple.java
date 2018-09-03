package com.ferreusveritas.dynamictreesphc.trees;

import java.util.List;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModConstants;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeMaple extends TreeFamily {
	public static final String speciesName = "maple";
	
	//Species need not be created as a nested class.  They can be created after the tree has already been constructed.
	public class TreeMapleSpecies extends Species {
		
		public TreeMapleSpecies(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModBlocks.mapleLeavesProperties);
			
			//Setup the same as biomes o' plenty addon
			setBasicGrowingParameters(0.15f, 14.0f, 4, 4, 1.05f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);
			
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
		
	public TreeMaple() {
		super(new ResourceLocation(ModConstants.MODID, speciesName));
		
		//Set up primitive log. This controls what is dropped on harvest.
		setPrimitiveLog(ModBlocks.primMapleLog.getDefaultState());

		ModBlocks.mapleLeavesProperties.setTree(this);
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new TreeMapleSpecies(this));
		getCommonSpecies().generateSeed();
	}

	//Since we created a DynamicSapling in the common species we need to let it out to be registered.
	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(getCommonSpecies().getDynamicSapling().getBlock());
		return super.getRegisterableBlocks(blockList);
	}
}
