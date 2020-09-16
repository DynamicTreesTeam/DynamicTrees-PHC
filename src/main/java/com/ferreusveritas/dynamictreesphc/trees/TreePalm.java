package com.ferreusveritas.dynamictreesphc.trees;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.util.BranchDestructionData;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.blocks.BlockDynamicLeavesPalm;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.HashMap;
import java.util.List;

public class TreePalm extends TreeFamilyPHC {

    public TreePalm (){
        this(new ResourceLocation(ModConstants.MODID, "palm"));

        this.canSupportCocoa = true;

        for (LeavesProperties properties : ModBlocks.palmLeavesProperties.values()){
            properties.setTree(this);
        }
    }
    public TreePalm(ResourceLocation name) {
        super(name);

        setPrimitiveLog(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE), new ItemStack(Blocks.LOG, 1, 3));

        addConnectableVanillaLeaves((state) -> state == Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE));
    }

    @Override
    public float getPrimaryThickness() {
        return 3.0f;
    }

    @Override
    public float getSecondaryThickness() {
        return 3.0f;
    }

    @Override
    public HashMap<BlockPos, IBlockState> getFellingLeavesClusters(BranchDestructionData destructionData) {

        if(destructionData.getNumEndpoints() < 1) {
            return null;
        }

        HashMap<BlockPos, IBlockState> leaves = new HashMap<>();
        BlockPos relPos = destructionData.getEndPointRelPos(0).up();//A palm tree is only supposed to have one endpoint at it's top.
        ILeavesProperties leavesProperties = getCommonSpecies().getLeavesProperties();

        leaves.put(relPos, leavesProperties.getDynamicLeavesState(4));//The barky overlapping part of the palm frond cluster
        leaves.put(relPos.up(), leavesProperties.getDynamicLeavesState(3));//The leafy top of the palm frond cluster

        //The 4 corners and 4 sides of the palm frond cluster
        for(int hydro = 1; hydro <= 2; hydro++) {
            IExtendedBlockState extState = (IExtendedBlockState) leavesProperties.getDynamicLeavesState(hydro);
            for(CoordUtils.Surround surr : BlockDynamicLeavesPalm.hydroSurroundMap[hydro]) {
                leaves.put(relPos.add(surr.getOpposite().getOffset()), extState.withProperty(BlockDynamicLeavesPalm.CONNECTIONS[surr.ordinal()], true));
            }
        }

        return leaves;
    }
}