package com.ferreusveritas.dynamictreesphc.dropcreators;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictreesphc.blocks.BlockPamFruitPalm;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;


public class FeatureGenFruitPalm implements IPostGenFeature, IPostGrowFeature {

    BlockFruit fruitPod;
    int allowedSize, frondHeight;
    boolean isSided;
    Integer forceHeight = null;
    protected int fruitingRadius = 6;

    public FeatureGenFruitPalm(BlockFruit fruitPod, int size, boolean isSided, boolean forceHeightDown){
        this(fruitPod, size, isSided);
        this.forceHeight = forceHeightDown?1:0;
    }

    public FeatureGenFruitPalm(BlockFruit fruitPod, int size, boolean isSided, int frondHeight){
        this.fruitPod = fruitPod;
        allowedSize = Math.max(size, 1);
        this.isSided = isSided;
        this.frondHeight = frondHeight;
    }

    public FeatureGenFruitPalm(BlockFruit fruitPod, int size, boolean isSided){
        this(fruitPod, size, isSided, 20);
    }

    public void setFruitingRadius(int fruitingRadius) {
        this.fruitingRadius = fruitingRadius;
    }

    private int getRandPosition (World world){
        if (forceHeight != null){
            return forceHeight;
        }
        return world.rand.nextInt(allowedSize);
    }

    @Override
    public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, Species species, int soilLife, boolean natural) {
        if((TreeHelper.getRadius(world, rootPos.up()) >= fruitingRadius) && natural && world.rand.nextInt() % 16 == 0) {
            addFruit(world, rootPos, getLeavesHeight(rootPos, world).down(getRandPosition(world)), false);
        }
        return false;
    }

    private BlockPos getLeavesHeight (BlockPos rootPos, World world){
        for (int y= 1; y < frondHeight; y++){
            BlockPos testPos = rootPos.up(y);
            if ((world.getBlockState(testPos).getBlock() instanceof BlockLeaves)){
                return testPos;
            }
        }
        return rootPos;
    }

    @Override
    public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
        boolean placed = false;
        for (int i=0;i<8;i++){
            if(world.rand.nextInt() % 4 == 0) {
                addFruit(world, rootPos, getLeavesHeight(rootPos, world).down(getRandPosition(world)),true);
                placed = true;
            }
        }
        return placed;
    }

    private void addFruit(World world, BlockPos rootPos, BlockPos leavesPos, boolean worldGen) {
        if (rootPos.getY() == leavesPos.getY()){
            return;
        }
        if (isSided){
            EnumFacing placeDir = EnumFacing.HORIZONTALS[world.rand.nextInt(4)];
            leavesPos = leavesPos.down(); //we move the pos down so the fruit can stick to the trunk
            if (world.isAirBlock(leavesPos.offset(placeDir))){
                world.setBlockState(leavesPos.offset(placeDir), fruitPod.getDefaultState().withProperty(BlockPamFruitPalm.FACING, placeDir.getOpposite()).withProperty(BlockPamFruitPalm.AGE, worldGen?(world.rand.nextInt(3)):0));
            }
        } else {
            CoordUtils.Surround placeDir = CoordUtils.Surround.values()[world.rand.nextInt(8)];
            if (world.isAirBlock(leavesPos.add(placeDir.getOffset()))){
                world.setBlockState(leavesPos.add(placeDir.getOffset()), fruitPod.getDefaultState());
            }
        }
    }

}