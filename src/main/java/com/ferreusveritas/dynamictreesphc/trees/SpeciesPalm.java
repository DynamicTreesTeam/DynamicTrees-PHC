package com.ferreusveritas.dynamictreesphc.trees;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorSeed;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeFindEnds;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.blocks.BlockPamFruitPalm;
import com.ferreusveritas.dynamictreesphc.dropcreators.FeatureGenFruitPalm;
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

import java.util.List;
import java.util.Random;

public class SpeciesPalm extends SpeciesFruit {

	public SpeciesPalm(ResourceLocation name, TreeFamily treeFamily, ILeavesProperties leavesProperties, String fruitName, SaplingType saplingType) {
		super(name, treeFamily, leavesProperties, fruitName, saplingType);

		addDropCreator(new DropCreatorSeed(3.0f) {
			@Override
			public List<ItemStack> getHarvestDrop(World world, Species species, BlockPos leafPos, Random random, List<ItemStack> dropList, int soilLife, int fortune) {
				if(random.nextInt(16) == 0) { // 1 in 4 chance to drop a seed on destruction..
					dropList.add(getSeedStack(1));
				}
				return dropList;
			}

//			private ItemStack getFruit (){
//				return new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(
//						new ResourceLocation("forestry", "fruits"))), 1, 5);
//			}

			@Override
			public List<ItemStack> getLeavesDrop(IBlockAccess access, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int fortune) {
				int chance = 16;
				if (fortune > 0) {
					chance -= fortune;
					if (chance < 3) {
						chance = 3;
					}
				}
				if (random.nextInt(chance) == 0) {
					dropList.add(getSeedStack(1));
				}
				return dropList;
			}
		});
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
		if (fruitName.equals(FruitRegistry.DRAGONFRUIT)){
			return super.getEnergy(world, pos) * biomeSuitability(world, pos) - (CoordUtils.coordHashCode(pos.up(month), 3) % 2);
		}
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

	@Override
	public void postGeneration(World world, BlockPos rootPos, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
		for (BlockPos endPoint : endPoints) {
			TreeHelper.ageVolume(world, endPoint, 1, 2, 3, safeBounds);
		}
	}

	public void setFruitBlock (BlockFruit fruitBlock){
		fruitBlockState = fruitBlock.getDefaultState();
		int growHeightOptions = 1;
		if (fruitName.equals(FruitRegistry.DRAGONFRUIT) || fruitName.equals(FruitRegistry.PAPAYA)){
			growHeightOptions = 2;
		}
		addGenFeature(new FeatureGenFruitPalm(fruitBlock, growHeightOptions, fruitBlock instanceof BlockPamFruitPalm && !fruitName.equals(FruitRegistry.COCONUT)));
	}

	@Override
	public boolean canBoneMeal() {
		if (fruitName.equals(FruitRegistry.DRAGONFRUIT)){
			return false;
		}
		return super.canBoneMeal();
	}

	@Override
	public ResourceLocation getSaplingName() {
		if (fruitName.equals(FruitRegistry.DRAGONFRUIT)){
			return new ResourceLocation(ModConstants.MODID, FruitRegistry.DRAGONFRUIT);
		}
		return super.getSaplingName();
	}

	@Override
	public SoundType getSaplingSound() {
		if (fruitName.equals(FruitRegistry.DRAGONFRUIT)){
			return SoundType.CLOTH;
		}
		return super.getSaplingSound();
	}

	@Override
	public AxisAlignedBB getSaplingBoundingBox() {
		if (fruitName.equals(FruitRegistry.DRAGONFRUIT)){
			return new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.5D, 0.625D);
		}
		return super.getSaplingBoundingBox();
	}

	@Override
	protected void fruitTreeDefaults(String name) {
		switch (name){
			case FruitRegistry.BANANA:
				setBasicGrowingParameters(0.8f, 5.0f, 1, 4, 1.0f, fruitingRadius);
				break;
			case FruitRegistry.DRAGONFRUIT:
				setBasicGrowingParameters(1.0f, 3.0f, 1, 4, 1.0f, fruitingRadius);
				break;
			default:
				setBasicGrowingParameters(0.4f, 8.0f, 1, 4, 0.8f, fruitingRadius);
		}
	}

}
