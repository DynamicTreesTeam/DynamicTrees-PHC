package com.ferreusveritas.dynamictreesphc.dropcreators;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictreesphc.blocks.BlockPamFruitPalm;
import com.ferreusveritas.dynamictreesphc.blocks.BlockSucker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;


public class FeatureGenSuckers implements IPostGenFeature, IPostGrowFeature {

    BlockSucker sucker;

    public FeatureGenSuckers(BlockSucker fruitPod){
        sucker = fruitPod;
    }

    @Override
    public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, Species species, int soilLife, boolean natural) {
        if(world.rand.nextInt() % 16 == 0) {
            addSucker(world, rootPos, false);
        }
        return false;
    }

    @Override
    public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
        boolean placed = false;
        for (int i=0;i<8;i++){
            if(world.rand.nextInt() % 4 == 0) {
                addSucker(world, rootPos, true);
                placed = true;
            }
        }
        return placed;
    }

    private BlockPos getGroundPos (World world, BlockPos pos){
        for (int i=0; i < 3; i++){
            BlockPos testPos = pos.add(0, i,0);
            if (world.isBlockLoaded(testPos) &&
                    world.getBlockState(testPos).getBlock().isReplaceable(world, testPos) &&
                    world.getBlockState(testPos.down()).getBlock() != sucker &&
                    world.getBlockState(testPos.down()).isSideSolid(world, testPos, EnumFacing.UP)){
                return testPos;
            }
        }
        return null;
    }

    private void addSucker(World world, BlockPos rootPos, boolean worldGen) {
        EnumFacing dir = EnumFacing.HORIZONTALS[world.rand.nextInt(4)];
        BlockPos ground = getGroundPos(world, rootPos.offset(dir));
        if (ground == null){
            return;
        }
        world.setBlockState(ground, sucker.getDefaultState().withProperty(BlockSucker.FACING, dir));
    }

}