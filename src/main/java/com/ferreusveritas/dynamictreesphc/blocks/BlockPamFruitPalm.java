package com.ferreusveritas.dynamictreesphc.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.pam.harvestcraft.blocks.FruitRegistry;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPamFruitPalm extends BlockPamFruit {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    protected final float[] DATE_OFFSET = {6 /16f, 5.5f /16f, 4 /16f, 4 /16f};
    protected final float[] PAPAYA_OFFSET = {6 /16f, 5.5f /16f, 4 /16f, 4 /16f};
    protected final float[] COCONUT_OFFSET = {6 /16f, 5f /16f, 4 /16f, 4 /16f};

    public BlockPamFruitPalm(ResourceLocation name){
        super(name);
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(AGE, (meta & 15) >> 2);
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        boolean requiresFrondBase = fruitName.equals(FruitRegistry.COCONUT);
        EnumFacing dir = state.getValue(FACING);
        IBlockState offsetState = world.getBlockState(pos.offset(dir));
        IBlockState offsetUpState = world.getBlockState(pos.offset(dir).up());
        return offsetState.getBlock() instanceof BlockBranch && (!requiresFrondBase || offsetUpState.getBlock() instanceof BlockLeaves);
    }

    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(FACING).getHorizontalIndex();
        i = i | state.getValue(AGE) << 2;
        return i;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE, FACING);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess access, BlockPos pos) {
        EnumFacing dir = state.getValue(FACING);
        switch (fruitName){
            case "date":
                return offsetBoundingBox(DATE[state.getValue(AGE)], dir, DATE_OFFSET[state.getValue(AGE)]);
            case "papaya":
                return offsetBoundingBox(PAPAYA[state.getValue(AGE)], dir, PAPAYA_OFFSET[state.getValue(AGE)]);
            case "coconut":
                return offsetBoundingBox(COCONUT[state.getValue(AGE)], dir, COCONUT_OFFSET[state.getValue(AGE)]);
            default:
                return offsetBoundingBox(super.getBoundingBox(state, access, pos), dir, 6/16f);
        }
    }

    protected AxisAlignedBB offsetBoundingBox (AxisAlignedBB box, EnumFacing dir, float offset){
        return box.offset(dir.getFrontOffsetX() * offset, dir.getFrontOffsetY() * offset, dir.getFrontOffsetZ() * offset);
    }
}
