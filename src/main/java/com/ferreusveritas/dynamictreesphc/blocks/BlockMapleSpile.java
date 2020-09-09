package com.ferreusveritas.dynamictreesphc.blocks;

import akka.japi.pf.FI;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.pam.harvestcraft.blocks.FruitRegistry;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockMapleSpile extends BlockHorizontal {

    public static final PropertyBool FILLED = PropertyBool.create("filled");

    public static final float syrupChance = 0.1f;

    protected static final AxisAlignedBB SPILE_EAST_AABB = new AxisAlignedBB(
            -1 /16f,10  /16f,7  /16f,
            6 /16f,12 /16f,9 /16f);
    protected static final AxisAlignedBB SPILE_WEST_AABB = new AxisAlignedBB(
            10  /16f,10 /16f, 7  /16f,
            17 /16f,12 /16f,9 /16f);
    protected static final AxisAlignedBB SPILE_NORTH_AABB = new AxisAlignedBB(
            7  /16f,10  /16f,10  /16f,
            9 /16f,12 /16f,17 /16f);
    protected static final AxisAlignedBB SPILE_SOUTH_AABB = new AxisAlignedBB(
            7  /16f,10  /16f,-1 /16f,
            9 /16f,12 /16f,6 /16f);

    public BlockMapleSpile(ResourceLocation name) {
        super(Material.IRON);
        defaultState();
        setRegistryName(name);
        setUnlocalizedName(name.toString());
        setSoundType(new SoundType(
                1.0F,
                1.5F,
                SoundEvents.BLOCK_WOOD_PLACE, //block broken
                SoundEvents.BLOCK_METAL_STEP,
                SoundEvents.BLOCK_WOOD_BREAK, //block placed
                SoundEvents.BLOCK_METAL_HIT,
                SoundEvents.BLOCK_METAL_FALL));
        setHardness(0.1f);
        setTickRandomly(true);
    }

    protected void defaultState (){
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(FILLED, false));
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FILLED, FACING);
    }

    @Override public IBlockState getStateFromMeta(int meta) {
        boolean isFilled = meta >= 8;
        EnumFacing face = EnumFacing.HORIZONTALS[meta % 4];
        return getDefaultState().withProperty(FACING, face.getOpposite()).withProperty(FILLED, isFilled);
    }
    @Override public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getOpposite().getHorizontalIndex() + (state.getValue(FILLED) ? 8 : 0);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!this.canBlockStay(worldIn, pos, state)) {
            this.dropBlock(worldIn, pos, state);
        } else {
            if (worldIn.rand.nextFloat() <= syrupChance){
                worldIn.setBlockState(pos, state.withProperty(FILLED, true));
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (state.getProperties().containsKey(FILLED) && playerIn.getHeldItemMainhand().getItem() == Items.BUCKET){
            EnumFacing dir = state.getValue(FACING);
            worldIn.setBlockState(pos, ModBlocks.mapleSpileWithBucket.getDefaultState()
                    .withProperty(FACING, dir)
                    .withProperty(BlockMapleSpileBucket.FILLING, state.getValue(FILLED)?1:0));
            if (!playerIn.isCreative()){
                playerIn.getHeldItemMainhand().shrink(1);
            }
            return true;
        }
        if (dropSyrup(worldIn, pos, state)){
            return true;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    protected boolean dropSyrup (World worldIn, BlockPos pos, IBlockState state){
        if (state.getValue(FILLED)){
            if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots) {
                ItemStack drop = new ItemStack(FruitRegistry.getLog(FruitRegistry.MAPLE).getFruitItem());
                spawnAsEntity(worldIn, pos, drop);
                System.out.println(drop);
            }
            worldIn.setBlockState(pos, state.withProperty(FILLED, false));
            return true;
        }
        return false;
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!this.canBlockStay(worldIn, pos, state))
        {
            this.dropBlock(worldIn, pos, state);
        }
    }

    protected void dropBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        this.dropBlockAsItem(worldIn, pos, state, 0);
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        BlockPos offsetPos = pos.offset(state.getValue(FACING).getOpposite());
        IBlockState offsetState = worldIn.getBlockState(offsetPos);
        if (TreeHelper.isBranch(offsetState)){
            BlockBranch branch = TreeHelper.getBranch(offsetState);
            return branch.getFamily().getPrimitiveLog().getBlock() == FruitRegistry.getLog(FruitRegistry.MAPLE) &&
                    branch.getRadius(offsetState) >= 7;
        }
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {

        IBlockState thisState = worldIn.getBlockState(pos);
        if (!thisState.getBlock().isReplaceable(worldIn, pos)) return false;

        IBlockState logState = worldIn.getBlockState(pos.offset(side.getOpposite()));
        if (TreeHelper.isBranch(logState)){
            BlockBranch branch = TreeHelper.getBranch(logState);
            return branch.getFamily().getPrimitiveLog().getBlock() == FruitRegistry.getLog(FruitRegistry.MAPLE) &&
                    branch.getRadius(logState) >= 7;
        }
        return false;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        if (!player.isCreative()){
            this.dropBlockAsItem(worldIn, pos, state, 0);
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (facing.getAxis() == EnumFacing.Axis.Y) facing = EnumFacing.NORTH;
        return this.getDefaultState().withProperty(FACING, facing);
    }

    @Override public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }
    @Override public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    @Override public boolean isFullCube(IBlockState state) { return false; }
    @Override public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING))
        {
            case SOUTH:
                return SPILE_SOUTH_AABB;
            case NORTH:
            default:
                return SPILE_NORTH_AABB;
            case WEST:
                return SPILE_WEST_AABB;
            case EAST:
                return SPILE_EAST_AABB;
        }
    }
}
