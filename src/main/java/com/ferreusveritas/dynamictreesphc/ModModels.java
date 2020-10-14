package com.ferreusveritas.dynamictreesphc;

import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictreesphc.trees.TreePaperBark;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModModels {

	@SideOnly(Side.CLIENT)
	public static void register(ModelRegistryEvent event) {
		for (TreeFamily tree : ModTrees.phcTrees) {
			ModelHelper.regModel(tree.getDynamicBranch());
			ModelHelper.regModel(tree.getCommonSpecies().getSeed());
			ModelHelper.regModel(tree);
		}
		ModelHelper.regModel(TreePaperBark.paperbarkCutBranch);
		ModelHelper.regModel(ModItems.passionfruitVine);
		ModelHelper.regModel(ModItems.passionfruitSeed);
		ModelHelper.regModel(ModItems.ripePeppercorn);

		ModTrees.phcFruitSpecies.values().stream().filter(s -> s.getSeed() != Seed.NULLSEED).forEach(s -> ModelHelper.regModel(s.getSeed()));//Register Seed Item Models
	}
	
}
