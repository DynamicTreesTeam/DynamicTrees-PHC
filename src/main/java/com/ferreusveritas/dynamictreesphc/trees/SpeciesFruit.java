package com.ferreusveritas.dynamictreesphc.trees;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorSeed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.pam.harvestcraft.blocks.FruitRegistry;
import com.pam.harvestcraft.blocks.growables.BlockPamSapling.SaplingType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;

import java.util.List;
import java.util.Random;

public class SpeciesFruit extends Species {
	
	public final String fruitName;
	public final SaplingType saplingType;
	public IBlockState fruitBlockState;
	protected int fruitingRadius = 5;

	public SpeciesFruit(ResourceLocation name, TreeFamily treeFamily, ILeavesProperties leavesProperties, String fruitName, SaplingType saplingType) {
		super(name, treeFamily, leavesProperties);
		this.fruitName = fruitName;
		this.saplingType = saplingType;

		fruitTreeDefaults(fruitName);

		setRequiresTileEntity(true);

		switch(saplingType) {
			default:
			case TEMPERATE:
				envFactor(Type.COLD, 0.75f);
				envFactor(Type.HOT, 0.75f);
				break;
			case COLD:
				envFactor(Type.HOT, 0.50f);
				break;
			case WARM:
				envFactor(Type.COLD, 0.50f);
				break;
		}
		generateSeed();
	}

	public void setFruitBlock (BlockFruit fruitBlock){
		fruitBlockState = fruitBlock.getDefaultState();
		addGenFeature(new FeatureGenFruit(fruitBlock).setRayDistance(4).setFruitingRadius(fruitingRadius));
		addDropCreator(new DropCreatorSeed() {
			@Override public List<ItemStack> getHarvestDrop(World world, Species species, BlockPos leafPos, Random random, List<ItemStack> dropList, int soilLife, int fortune) {
				float rarity = getHarvestRarity();
				rarity *= (fortune + 1) / 128f; //Extra rare so players are incentivized to get fruits from growing instead of chopping
				rarity *= Math.min(species.seasonalSeedDropFactor(world, leafPos) + 0.15f, 1.0);
				if(rarity > random.nextFloat()) dropList.add(getFruit (species)); //1 in 128 chance to drop a fruit on destruction..
				return dropList;
			}

			private ItemStack getFruit (Species species){
				if (fruitName.equals(FruitRegistry.SPIDERWEB)){
					return species.getSeedStack(1);
				}
				return new ItemStack(FruitRegistry.getFood(fruitName));
			}

			@Override public List<ItemStack> getLeavesDrop(IBlockAccess access, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int fortune) {
				int chance = 40;
				//Hokey fortune stuff here to match Vanilla logic.
				if (fortune > 0) {
					chance -= 2 << fortune;
					if (chance < 10) chance = 10;
				}
				float seasonFactor = 1.0f;
				if(access instanceof World) {
					World world = (World) access;
					if(!world.isRemote) seasonFactor = species.seasonalSeedDropFactor(world, breakPos);
				}
				if(random.nextInt((int) (chance / getLeavesRarity())) == 0)
					if(seasonFactor > random.nextFloat())
						dropList.add(getFruit(species));
				return dropList;
			}
		});
	}

	@Override
	public ResourceLocation getSaplingName() {
		String dtModId = com.ferreusveritas.dynamictrees.ModConstants.MODID;
		
		switch(saplingType) {
			default:
			case TEMPERATE: return new ResourceLocation(dtModId, "oak");
			case COLD: return new ResourceLocation(dtModId, "spruce");
			case WARM: return new ResourceLocation(dtModId, "jungle");
		}
	}
	
	protected void fruitTreeDefaults(String name) {
				setBasicGrowingParameters(0.3f, 8.0f, 1, 4, 1.0f, fruitingRadius);
	}
	
	protected SpeciesFruit setBasicGrowingParameters(float tapering, float energy, int upProbability, int lowestBranchHeight, float growthRate, int fruitingRadius) {
		setBasicGrowingParameters(tapering, energy, upProbability, lowestBranchHeight, growthRate);
		this.fruitingRadius = fruitingRadius;
		return this;
	}
	
	public BlockFruit getFruitBlock() {
		return (BlockFruit) fruitBlockState.getBlock();
	}
	
	@Override
	public void addJoCodes() {
		joCodeStore.addCodesFromFile(this, "assets/" + getRegistryName().getResourceDomain() + "/trees/fruit.txt");
	}
	
}
