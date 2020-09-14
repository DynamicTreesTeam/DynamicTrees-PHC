package com.ferreusveritas.dynamictreesphc.blocks;

import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModItems;
import com.pam.harvestcraft.blocks.FruitRegistry;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockMapleSpileBucket extends BlockMapleSpile {

    private static final int maxFilling = 3;
    public static final PropertyInteger FILLING = PropertyInteger.create("filling", 0, maxFilling);

    protected static final AxisAlignedBB SPILEBUCKET_EAST_AABB = new AxisAlignedBB(
            -1 /16f,0  /16f,4  /16f,
            9 /16f,9 /16f,12 /16f);
    protected static final AxisAlignedBB SPILEBUCKET_WEST_AABB = new AxisAlignedBB(
            7  /16f,0 /16f, 4  /16f,
            17 /16f,9 /16f,12 /16f);
    protected static final AxisAlignedBB SPILEBUCKET_NORTH_AABB = new AxisAlignedBB(
            4  /16f,0 /16f,7  /16f,
            12 /16f,9 /16f,17 /16f);
    protected static final AxisAlignedBB SPILEBUCKET_SOUTH_AABB = new AxisAlignedBB(
            4  /16f,0  /16f,-1 /16f,
            12 /16f,9 /16f,9 /16f);

    public BlockMapleSpileBucket(ResourceLocation name) {
        super(name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(FILLING, 0));
    }

    @Override
    protected void defaultState (){
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(FILLING, 0));
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FILLING, FACING);
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(FILLING, (meta & 15) >> 2);
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(FACING).getHorizontalIndex();
        i = i | state.getValue(FILLING) << 2;
        return i;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!this.canBlockStay(worldIn, pos, state)) {
            this.dropBlock(worldIn, pos, state);
        } else {
            int filling = worldIn.getBlockState(pos).getValue(FILLING);
            if (worldIn.rand.nextFloat() <= getSyrupChance(worldIn) && filling < maxFilling){
                worldIn.setBlockState(pos, state.withProperty(FILLING, filling + 1));
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (state.getProperties().containsKey(FILLING)){
            if (worldIn.getBlockState(pos).getValue(FILLING) == 0 && playerIn.isSneaking()){
                EnumFacing dir = state.getValue(FACING);
                worldIn.setBlockState(pos, ModBlocks.mapleSpile.getDefaultState().withProperty(FACING, dir));
                playerIn.addItemStackToInventory(new ItemStack(Items.BUCKET));
                return true;
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    protected boolean giveSyrup(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player){
        int filling = worldIn.getBlockState(pos).getValue(FILLING);
        if (filling > 0){
            if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots) {
                ItemStack drop = new ItemStack(FruitRegistry.getLog(FruitRegistry.MAPLE).getFruitItem());
                drop.setCount(filling + (filling == maxFilling ? 1 : 0)); //Adds one bonus syrup if collected when its full
                player.addItemStackToInventory(drop);
           }
            worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1, 1 + filling/4f, false);
            worldIn.setBlockState(pos, state.withProperty(FILLING, 0));
            return true;
        }
        return false;
    }

    @Override
    protected void dropBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        spawnAsEntity(worldIn, pos, new ItemStack(Items.BUCKET));
        spawnAsEntity(worldIn, pos, new ItemStack(Items.IRON_NUGGET));
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!player.isCreative()){
            spawnAsEntity(worldIn, pos, new ItemStack(Items.BUCKET));
            spawnAsEntity(worldIn, pos, new ItemStack(Items.IRON_NUGGET));
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Items.BUCKET);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(FILLING) * 5;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING))
        {
            case SOUTH:
                return SPILEBUCKET_SOUTH_AABB.setMaxY(12 /16f);
            case NORTH:
            default:
                return SPILEBUCKET_NORTH_AABB.setMaxY(12 /16f);
            case WEST:
                return SPILEBUCKET_WEST_AABB.setMaxY(12 /16f);
            case EAST:
                return SPILEBUCKET_EAST_AABB.setMaxY(12 /16f);
        }
    }
}
