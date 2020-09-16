package com.ferreusveritas.dynamictreesphc.trees.Extra;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.compat.PamTrees;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class SpeciesRedbud extends Species {

    public SpeciesRedbud(TreeFamily treeFamily) {
        super(new ResourceLocation(ModConstants.MODID, "redbud"), treeFamily, PamTrees.redbudLeavesProperties);

        this.setBasicGrowingParameters(0.4F, 10.0F, 1, 4, 0.7F);

        setRequiresTileEntity(true);

        generateSeed();

        setupStandardSeedDropping();
    }

}
