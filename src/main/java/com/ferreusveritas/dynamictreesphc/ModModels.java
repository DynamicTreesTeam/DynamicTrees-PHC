package com.ferreusveritas.dynamictreesphc;

import com.ferreusveritas.dynamictrees.api.client.ModelHelper;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModModels {

	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event) {
		ModTrees.phcSpecies.values().forEach(species -> ModelHelper.regModel(species.getSeed()));
	}
	
}
