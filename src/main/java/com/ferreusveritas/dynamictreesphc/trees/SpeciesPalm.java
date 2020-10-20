package com.ferreusveritas.dynamictreesphc.trees;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorSeed;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeFindEnds;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.blocks.BlockPamFruitPalm;
import com.ferreusveritas.dynamictreesphc.dropcreators.FeatureGenFruitPalm;
import com.ferreusveritas.dynamictreesphc.dropcreators.FeatureGenSuckers;
import com.pam.harvestcraft.blocks.FruitRegistry;
import com.pam.harvestcraft.blocks.growables.BlockPamSapling.SaplingType;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SpeciesPalm extends SpeciesFruit {

	public SpeciesPalm(ResourceLocation name, TreeFamily treeFamily, ILeavesProperties leavesProperties, String fruitName, SaplingType saplingType) {
		super(name, treeFamily, leavesProperties, fruitName, saplingType);

		if (fruitName.equals(FruitRegistry.BANANA)){
			addGenFeature(new FeatureGenSuckers(ModBlocks.bananaSucker));
		}
	}

	@Override
	protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
		EnumFacing originDir = signal.dir.getOpposite();

		// Alter probability map for direction change
		probMap[0] = 0; // Down is always disallowed for palm
		probMap[1] = 10;
		probMap[2] = probMap[3] = probMap[4] = probMap[5] =  0;
		probMap[originDir.ordinal()] = 0; // Disable the direction we came from

		return probMap;
	}

	@Override
	public float getEnergy(World world, BlockPos pos) {
		long day = world.getWorldTime() / 24000L;
		int month = (int) day / 30; // Change the hashs every in-game month
		return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (CoordUtils.coordHashCode(pos.up(month), 3) % 3); // Vary the height energy by a psuedorandom hash function
	}

	@Override
	public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, int soilLife, boolean natural) {
		IBlockState trunkBlockState = world.getBlockState(treePos);
		BlockBranch branch = TreeHelper.getBranch(trunkBlockState);
		NodeFindEnds endFinder = new NodeFindEnds();
		MapSignal signal = new MapSignal(endFinder);
		branch.analyse(trunkBlockState, world, treePos, EnumFacing.DOWN, signal);
		List<BlockPos> endPoints = endFinder.getEnds();

		for (BlockPos endPoint: endPoints) {
			TreeHelper.ageVolume(world, endPoint, 2, 3, 3, SafeChunkBounds.ANY);
		}

		// Make sure the bottom block is always just a little thicker that the block above it.
		int radius = branch.getRadius(world.getBlockState(treePos.up()));
		if (radius != 0) {
			branch.setRadius(world, treePos, radius + 1, null);
		}

		return super.postGrow(world, rootPos, treePos, soilLife, natural);
	}

	public boolean transitionToTree(World world, BlockPos pos) {
		//Ensure planting conditions are right
		TreeFamily family = getFamily();
		if(world.isAirBlock(pos.up()) && isAcceptableSoil(world, pos.down(), world.getBlockState(pos.down()))) {
			family.getDynamicBranch().setRadius(world, pos, (int)family.getPrimaryThickness(), null);//set to a single branch with 1 radius
			world.setBlockState(pos.up(), getLeavesProperties().getDynamicLeavesState());//Place 2 leaf blocks on top
			world.setBlockState(pos.up(2), getLeavesProperties().getDynamicLeavesState().withProperty(BlockDynamicLeaves.HYDRO, 3));
			placeRootyDirtBlock(world, pos.down(), 15);//Set to fully fertilized rooty dirt underneath
			return true;
		}
		return false;
	}

	@Override
	public void postGeneration(World world, BlockPos rootPos, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
		for (BlockPos endPoint : endPoints) {
			TreeHelper.ageVolume(world, endPoint, 1, 2, 3, safeBounds);
		}
	}

	@Override
	public int maxBranchRadius() {
		return 6;
	}

	public void setFruitBlock (BlockFruit fruitBlock){
		fruitBlockState = fruitBlock.getDefaultState();
		int growHeightOptions = 1;
		if (fruitName.equals(FruitRegistry.PAPAYA)){
			growHeightOptions = 2;
		}
		FeatureGenFruitPalm fruitFeatureGen;
		if (fruitName.equals(FruitRegistry.BANANA)) {
			fruitFeatureGen = new FeatureGenFruitPalm(fruitBlock, growHeightOptions, fruitBlock instanceof BlockPamFruitPalm, true);
		} else {
			fruitFeatureGen = new FeatureGenFruitPalm(fruitBlock, growHeightOptions, fruitBlock instanceof BlockPamFruitPalm);
		}
		fruitFeatureGen.setFruitingRadius(fruitingRadius);
		addGenFeature(fruitFeatureGen);

		addDropCreator(new DropCreatorSeed() {
			@Override public List<ItemStack> getHarvestDrop(World world, Species species, BlockPos leafPos, Random random, List<ItemStack> dropList, int soilLife, int fortune) {
				float rarity = getHarvestRarity();
				rarity *= (fortune + 1) / 16f;
				rarity *= Math.min(species.seasonalSeedDropFactor(world, leafPos) + 0.15f, 1.0);
				if(rarity > random.nextFloat()) dropList.add(getFruit (species)); //1 in 16 chance to drop a fruit on destruction..
				return dropList;
			}

			private ItemStack getFruit (Species species){
				if (fruitName.equals(FruitRegistry.COCONUT)) //coconut seeds are whole coconuts while the food is an open coconut
					return species.getSeedStack(1);
				return new ItemStack(FruitRegistry.getFood(fruitName));
			}

			@Override public List<ItemStack> getLeavesDrop(IBlockAccess access, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int fortune) {
				int chance = 20; //See BlockLeaves#getSaplingDropChance(state);
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
	protected void fruitTreeDefaults(String name) {
		setBasicGrowingParameters(0.4f, 8.0f, 1, 4, 0.3f, 5);
	}

	@Override
	public void addJoCodes() {
		joCodeStore.addCodesFromFile(this, "assets/" + getRegistryName().getResourceDomain() + "/trees/palm.txt");
	}

}
