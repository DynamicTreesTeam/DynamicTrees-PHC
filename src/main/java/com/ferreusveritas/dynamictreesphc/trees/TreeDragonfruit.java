package com.ferreusveritas.dynamictreesphc.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.blocks.BlockPamFruitPalm;
import com.ferreusveritas.dynamictreesphc.dropcreators.FeatureGenFruitPalm;
import com.pam.harvestcraft.blocks.FruitRegistry;
import com.pam.harvestcraft.blocks.growables.BlockPamSapling;
import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TreeDragonfruit extends TreePalm {

    public class SpeciesDragonfruit extends SpeciesPalm {

        public SpeciesDragonfruit(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModBlocks.dragonfruitLeavesProperties, FruitRegistry.DRAGONFRUIT, BlockPamSapling.SaplingType.WARM);
        }

        @Override
        public float getEnergy(World world, BlockPos pos) {
            long day = world.getWorldTime() / 24000L;
            int month = (int) day / 30; // Change the hashs every in-game month
            return super.getEnergy(world, pos) * biomeSuitability(world, pos) - (CoordUtils.coordHashCode(pos.up(month), 3) % 2); // Vary the height energy by a psuedorandom hash function
        }

        public void setFruitBlock (BlockFruit fruitBlock){
            fruitBlockState = fruitBlock.getDefaultState();
            FeatureGenFruitPalm featureGen = new FeatureGenFruitPalm(fruitBlock, 2, fruitBlock instanceof BlockPamFruitPalm);
            featureGen.setFruitingRadius(fruitingRadius);
            addGenFeature(featureGen);
        }

        @Override
        public boolean canBoneMeal() {
            return false;
        }

        @Override
        public ResourceLocation getSaplingName() {
            return new ResourceLocation(ModConstants.MODID, FruitRegistry.DRAGONFRUIT);
        }

        @Override
        public SoundType getSaplingSound() {
            return SoundType.CLOTH;
        }

        @Override
        public AxisAlignedBB getSaplingBoundingBox() {
            return new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.5D, 0.625D);
        }

        @Override
        protected void fruitTreeDefaults(String name) {
            setBasicGrowingParameters(1.0f, 2.0f, 1, 4, 0.2f, 2);
        }

        @Override
        public void addJoCodes() {
            joCodeStore.addCodesFromFile(this, "assets/" + getRegistryName().getResourceDomain() + "/trees/dragonfruit.txt");
        }

    }

    public TreeDragonfruit() {
        super(new ResourceLocation(ModConstants.MODID, "dragonfruit"));

        ModBlocks.dragonfruitLeavesProperties.setTree(this);
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesDragonfruit(this));
    }


}