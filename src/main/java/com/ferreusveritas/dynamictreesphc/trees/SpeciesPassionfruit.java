package com.ferreusveritas.dynamictreesphc.trees;

import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenVine;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.pam.harvestcraft.blocks.FruitRegistry;
import net.minecraft.util.ResourceLocation;

public class SpeciesPassionfruit extends Species {

    public SpeciesPassionfruit (TreeFamily treeFamily){
        super(new ResourceLocation(ModConstants.MODID, FruitRegistry.PASSIONFRUIT), treeFamily);

        addGenFeature(new FeatureGenVine().setVineBlock(ModBlocks.passionfruitVine).setQuantity(16));
    }

    @Override
    public void addJoCodes() {
        joCodeStore.addCodesFromFile(this, "assets/" + getRegistryName().getResourceDomain() + "/trees/fruit.txt");
    }

    @Override
    public ResourceLocation getSaplingName() {
        return new ResourceLocation(com.ferreusveritas.dynamictrees.ModConstants.MODID, "jungle");
    }

}
