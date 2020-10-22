package com.ferreusveritas.dynamictreesphc.dropcreators;

import com.ferreusveritas.dynamictrees.api.network.INodeInspector;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.blocks.BlockMapleSpile;
import com.ferreusveritas.dynamictreesphc.blocks.BlockMapleSpileBucket;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NodeDripSyrup implements INodeInspector {
	
	private boolean finished = false;
	private int bucketsVisited = 0;
	public int maxBuckets = 4;
	
	@Override
	public boolean run(IBlockState blockState, World world, BlockPos pos, EnumFacing fromDir) {
		
		if(!finished) {
			if(fromDir != EnumFacing.DOWN) {//If we turn then we're no longer in the main trunk where the buckets are
				finished = true;
				return false;
			}
			for(EnumFacing face : EnumFacing.HORIZONTALS) { //Check all sides of this block
				BlockPos offPos = pos.offset(face);
				IBlockState state = world.getBlockState(offPos);
				if(state.getBlock() == ModBlocks.mapleSpileWithBucket) { //Found a bucket
					bucketsVisited++;
					if(bucketsVisited <= maxBuckets) {
						int filling = state.getValue(BlockMapleSpileBucket.FILLING);
						if (filling < BlockMapleSpileBucket.maxFilling) {//Fill it up a little bit if there's room
							world.setBlockState(offPos, state.withProperty(BlockMapleSpileBucket.FILLING, filling + 1));
							finished = true;
							return false;
						}
					} else {
						finished = true; //We've hit our limit of buckets we can visit
					}
				}
				else if(state.getBlock() == ModBlocks.mapleSpile) {
					boolean filled = state.getValue(BlockMapleSpile.FILLED);
					if(!filled) {
						world.setBlockState(offPos, state.withProperty(BlockMapleSpile.FILLED, true));
					}
					finished = true; //A spile without a bucket simply drips wastefully.
					return false;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public boolean returnRun(IBlockState blockState, World world, BlockPos pos, EnumFacing fromDir) {
		return false;
	}
	
}
