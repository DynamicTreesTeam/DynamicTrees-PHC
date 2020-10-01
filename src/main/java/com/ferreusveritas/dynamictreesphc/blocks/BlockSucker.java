package com.ferreusveritas.dynamictreesphc.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.pam.harvestcraft.blocks.FruitRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSucker extends BlockHorizontal {

    private static ItemStack droppedStack;

    public BlockSucker(ResourceLocation name) {
        super(Material.LEAVES);
        setRegistryName(name);
        setUnlocalizedName(name.toString());
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        setSoundType(SoundType.PLANT);
    }

    protected static final AxisAlignedBB SUCKER_EAST_AABB = new AxisAlignedBB(
            0 /16f,0  /16f,6  /16f,
            4 /16f,15 /16f,10 /16f);
    protected static final AxisAlignedBB SUCKER_WEST_AABB = new AxisAlignedBB(
            12  /16f,0 /16f, 6  /16f,
            16 /16f,15 /16f,10 /16f);
    protected static final AxisAlignedBB SUCKER_NORTH_AABB = new AxisAlignedBB(
            6  /16f,0  /16f,12  /16f,
            10 /16f,15 /16f,16 /16f);
    protected static final AxisAlignedBB SUCKER_SOUTH_AABB = new AxisAlignedBB(
            6  /16f,0  /16f,0 /16f,
            10 /16f,15 /16f,4 /16f);

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }

    @Override public IBlockState getStateFromMeta(int meta) {
        EnumFacing face = EnumFacing.HORIZONTALS[meta % 4];
        return getDefaultState().withProperty(FACING, face.getOpposite());
    }
    @Override public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getOpposite().getHorizontalIndex();
    }

    //////////////////////////////////////////
    // DROPS
    //////////////////////////////////////////

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!this.canBlockStay(worldIn, pos, state))
        {
            this.dropBlock(worldIn, pos, state);
        }
    }

    protected ItemStack getDroppedItem(){
        if (droppedStack == null){
            Species species = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, FruitRegistry.BANANA));
            return species.getSeedStack(1);
        } else return droppedStack;
    }

    protected void dropBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        spawnAsEntity(worldIn, pos, getDroppedItem());
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!player.isCreative()){
            spawnAsEntity(worldIn, pos, getDroppedItem());
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return getDroppedItem();
    }

    //////////////////////////////////////////
    // RENDERING
    //////////////////////////////////////////

    @Override public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }
    @Override public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override public boolean isFullCube(IBlockState state) { return false; }
    @Override public boolean isOpaqueCube(IBlockState state) { return false; }
    @Override public BlockRenderLayer getBlockLayer() { return BlockRenderLayer.CUTOUT; }

    @Override
    public boolean isFoliage(IBlockAccess world, BlockPos pos) {
        return true;
    }
    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.DESTROY;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            case SOUTH:
                return SUCKER_SOUTH_AABB;
            case NORTH:
            default:
                return SUCKER_NORTH_AABB;
            case WEST:
                return SUCKER_WEST_AABB;
            case EAST:
                return SUCKER_EAST_AABB;
        }
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }
}
