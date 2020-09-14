package com.ferreusveritas.dynamictreesphc.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.pam.harvestcraft.blocks.FruitRegistry;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPamFruit extends BlockFruit {

    private final String name;

    protected final AxisAlignedBB[] CHERRY = new AxisAlignedBB[] {
            new AxisAlignedBB(7/16.0, 1f, 7/16.0, 9/16.0, 15/16.0, 9/16.0),
            new AxisAlignedBB(6.4/16.0, 1f, 6.4/16.0, 9.6/16.0, 13.6/16.0, 9.6/16.0),
            new AxisAlignedBB(6/16.0, 14.4/16.0, 6/16.0, 10/16.0, 11.2/16.0, 10/16.0),
            new AxisAlignedBB(5.2/16.0, 13.6/16.0, 5.2/16.0, 10.8/16.0, 10.4/16.0, 10.8/16.0),
    };

    public BlockPamFruit (ResourceLocation name){
        super(new ResourceLocation(name.getResourceDomain(), "fruit"+name.getResourcePath()).toString());
        this.name = name.getResourcePath();
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        if (name.equals(FruitRegistry.DRAGONFRUIT)){
            return world.getBlockState(pos.up()).getBlock() instanceof BlockLeaves || world.getBlockState(pos.up(2)).getBlock() instanceof BlockLeaves;
        }
        return super.canBlockStay(world,pos,state);
    }

    @Override @SideOnly(Side.CLIENT) public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess access, BlockPos pos) {
        switch (name){

            case "cherry":
                return CHERRY[state.getValue(AGE)];

            default:
                return super.getBoundingBox(state, access, pos);
        }
    }
}
