package com.ferreusveritas.dynamictreesphc.dropcreators;

import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.trees.Species;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class FeatureGenSyrup implements IPostGrowFeature {
	
	private final float baseSyrupChance;
	private final float outOfSeasonSyrupChance;
	
	public FeatureGenSyrup(float baseSyrupChance, float outOfSeasonSyrupChance) {
		this.baseSyrupChance = baseSyrupChance;
		this.outOfSeasonSyrupChance = outOfSeasonSyrupChance;
	}
	
	@Override
	public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, Species species, int soilLife, boolean natural) {
		if(natural && (TreeHelper.getRadius(world, treePos) >= 7) && (world.rand.nextFloat() <= getSyrupChance(world))) {
			dripSyrup(world, rootPos);
		}
		return false;
	}
	
	//Update syrup extract rate depending on seasons
	public double getSyrupChance (World world){
		Float season = SeasonHelper.getSeasonValue(world);
		return MathHelper.clamp(season == null || SeasonHelper.isSeasonBetween(season, SeasonHelper.WINTER + 0.5f, SeasonHelper.SPRING + 0.5f) ? baseSyrupChance : outOfSeasonSyrupChance, 0.0f, 1.0f);
	}
	
	private void dripSyrup(World world, BlockPos rootPos) {
		TreeHelper.startAnalysisFromRoot(world, rootPos, new MapSignal(new NodeDripSyrup()));
	}
	
}
