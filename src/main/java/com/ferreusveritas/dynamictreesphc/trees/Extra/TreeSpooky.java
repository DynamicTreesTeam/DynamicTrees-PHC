package com.ferreusveritas.dynamictreesphc.trees.Extra;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.compat.PamTrees;
import com.ferreusveritas.dynamictreesphc.trees.TreeFamilyPHC;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.pam.spookytree.BlockRegistry;
import net.minecraft.util.math.MathHelper;

public class TreeSpooky extends TreeFamilyPHC {

    private static Block log;

    public class SpeciesSpooky extends Species {

        public SpeciesSpooky(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, PamTrees.spookyLeavesProperties);

            setBasicGrowingParameters(0.3f, 14.0f, 4, 4, 0.8f);

            generateSeed();

            setupStandardSeedDropping();
        }
    }

    public TreeSpooky() {
        super(new ResourceLocation(ModConstants.MODID, "spooky"));

        log = BlockRegistry.spookytreeLog;
        setPrimitiveLog(BlockRegistry.spookytreeLog.getDefaultState());

        PamTrees.spookyLeavesProperties.setTree(this);
    }

    @Override
    public ItemStack getPrimitiveLogItemStack(int qty) {
        ItemStack stack = new ItemStack(log, qty);
        stack.setCount(MathHelper.clamp(qty, 0, 64));
        return stack;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesSpooky(this));
    }
}
