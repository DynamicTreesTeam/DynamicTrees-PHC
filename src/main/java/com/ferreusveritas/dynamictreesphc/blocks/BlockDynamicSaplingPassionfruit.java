package com.ferreusveritas.dynamictreesphc.blocks;

import java.util.Random;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.ModTabs;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeTwinkle;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDynamicSaplingPassionfruit extends Block {

    public BlockDynamicSaplingPassionfruit (ResourceLocation name){
        super(Material.LEAVES);
        setRegistryName(name);
        setUnlocalizedName(name.toString());
        setCreativeTab(ModTabs.dynamicTreesTab);
        setSoundType(SoundType.PLANT);
        setTickRandomly(true);
    }

    public static boolean canSaplingStay(World world, BlockPos pos) {
        //Ensure theres a solid block on the sides
        boolean flatSide = false;
        for(EnumFacing dir: EnumFacing.HORIZONTALS) {
            if (BlockVinePassionfruit.isAcceptableNeighbor(world, pos.offset(dir.getOpposite()), dir)){
                flatSide = true;
            }
        }

        //Air above and oak's acceptable soil below
        Species oak = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oak"));
        return flatSide && world.isAirBlock(pos.up()) && oak.isAcceptableSoil(world, pos.down(), world.getBlockState(pos.down()));
    }

    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        return canSaplingStay(world, pos);
    }

    public boolean transitionToTree(World world, BlockPos pos) {
        //Ensure planting conditions are right
        boolean n = false,s = false,e = false,w = false;
        for(EnumFacing dir: EnumFacing.HORIZONTALS) {
            if (BlockVinePassionfruit.isAcceptableNeighbor(world, pos.offset(dir), dir.getOpposite())){
                switch (dir){
                    case NORTH:
                    default:
                        n = true;
                        break;
                    case SOUTH:
                        s = true;
                        break;
                    case EAST:
                        e = true;
                        break;
                    case WEST:
                        w = true;
                }
            }
        }
        world.setBlockState(pos, ModBlocks.passionfruitVine.getDefaultState()
                .withProperty(BlockVine.NORTH, n)
                .withProperty(BlockVine.SOUTH, s)
                .withProperty(BlockVine.EAST, e)
                .withProperty(BlockVine.WEST, w));
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        ItemStack heldItem = player.getHeldItem(hand);

        //Make a special case for the potion of burgeoning.  Since it only makes sense.
        if (!heldItem.isEmpty()) {//Something in the hand
            //Use substance provider interface if it's available
            if(heldItem.getItem() == Items.DYE && heldItem.getMetadata() == 15) {
                grow(world, world.rand, pos, state);
                if (!player.isCreative()){
                    heldItem.shrink(1);
                }
                NodeTwinkle.spawnParticles(world, EnumParticleTypes.VILLAGER_HAPPY, pos.getX(), pos.getY(), pos.getZ(), 8, world.rand);
                return true;
            }
        }

        return false;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        grow(world, rand, pos, state);
    }

    public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
        if(canBlockStay(world, pos, state)) {
            transitionToTree(world, pos);
        } else {
            dropBlock(world, state, pos);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.canBlockStay(world, pos, state)) {
            dropBlock(world, state, pos);
        }
    }

    protected void dropBlock(World world, IBlockState state, BlockPos pos) {
        dropBlockAsItem(world, pos, state, 0);
        world.setBlockToAir(pos);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);
            drops.add(new ItemStack(ModItems.passionfruitSeed));
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;//The sapling block itself is not obtainable
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(ModItems.passionfruitSeed);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess access, BlockPos pos) {
        return new AxisAlignedBB(0.25f, 0.0f, 0.25f, 0.75f, 0.75f, 0.75f);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;//This prevents fences and walls from attempting to connect to saplings.
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

}
