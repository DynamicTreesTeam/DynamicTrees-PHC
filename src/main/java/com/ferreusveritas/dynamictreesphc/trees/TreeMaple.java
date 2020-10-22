package com.ferreusveritas.dynamictreesphc.trees;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.blocks.BlockBranchPamSpecial;
import com.ferreusveritas.dynamictreesphc.dropcreators.FeatureGenSyrup;
import com.ferreusveritas.dynamictreesphc.items.ItemDynamicSeedMaple;
import com.pam.harvestcraft.blocks.FruitRegistry;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeMaple extends TreeFamilyPHC {
	public static final String speciesName = FruitRegistry.MAPLE;
	
	//Species need not be created as a nested class.  They can be created after the tree has already been constructed.
	public class TreeMapleSpecies extends Species {
		
		public TreeMapleSpecies(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModBlocks.mapleLeavesProperties);
			
			//Setup the same as biomes o' plenty addon
			setBasicGrowingParameters(0.15f, 14.0f, 4, 4, 1.05f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);

			setSeedStack(new ItemStack(new ItemDynamicSeedMaple()));

			setupStandardSeedDropping();
			
			//Add species features
			addGenFeature(new FeatureGenSyrup(0.05f, 0.001f));
		}

		@Override
		public boolean useDefaultWailaBody() {
			return false;
		}

		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.JUNGLE);
		}
		
	}
		
	public TreeMaple() {
		super(new ResourceLocation(ModConstants.MODID, speciesName));

		setPrimitiveLog(FruitRegistry.getLog(speciesName).getDefaultState());

		ModBlocks.mapleLeavesProperties.setTree(this);
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new TreeMapleSpecies(this));
	}

	@Override
	public BlockBranch createBranch() {
		return new BlockBranchPamSpecial(
				getName()+"branch",
				speciesName,
				0.2f){
			@Override
			public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
				if (playerIn.getHeldItem(hand).getItem() == Items.IRON_INGOT &&
						worldIn.getBlockState(pos.offset(facing)).getBlock().isReplaceable(worldIn, pos.offset(facing)) &&
						TreeHelper.getRadius(worldIn, pos) >= 7){
					worldIn.setBlockState(pos.offset(facing), ModBlocks.mapleSpile.getDefaultState().withProperty(BlockHorizontal.FACING, facing));
					if (!playerIn.isCreative()){
						playerIn.getHeldItem(hand).shrink(1);
					}
					worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, SoundCategory.BLOCKS, 1, 1.5f, false);
					return true;
				}
				return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
			}
		};
	}
}
