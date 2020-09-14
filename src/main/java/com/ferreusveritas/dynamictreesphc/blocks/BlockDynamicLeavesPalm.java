package com.ferreusveritas.dynamictreesphc.blocks;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.util.CoordUtils.Surround;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockDynamicLeavesPalm extends BlockDynamicLeaves {

    public static final Surround[][] hydroSurroundMap = new Surround[][] {
            {}, //Hydro 0
            {Surround.NE, Surround.SE, Surround.SW, Surround.NW}, //Hydro 1
            {Surround.N, Surround.E, Surround.S, Surround.W}, //Hydro 2
            {}, //Hydro 3
            {} //Hydro 4
    };

    public static final IUnlistedProperty<Boolean>[] CONNECTIONS;

    static {
        CONNECTIONS = new Properties.PropertyAdapter[Surround.values().length];

        for (Surround surr : Surround.values()) {
            CONNECTIONS[surr.ordinal()] = new Properties.PropertyAdapter<Boolean>(PropertyBool.create("conn_" + surr.toString()));
        }
    }

    public BlockDynamicLeavesPalm(String name) {
        setDefaultState(this.blockState.getBaseState().withProperty(HYDRO, 4));
        setRegistryName(ModConstants.MODID, name);
        setUnlocalizedName(name);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[] {HYDRO, TREE}, CONNECTIONS);
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        if (state.getBlock() == this && getProperties(state) == ModBlocks.dragonfruitLeavesProperties){
            return SoundType.CLOTH;
        }
        return super.getSoundType(state, world, pos, entity);
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (state.getBlock() == this && getProperties(state) == ModBlocks.dragonfruitLeavesProperties){
            entityIn.attackEntityFrom(DamageSource.CACTUS, 1.0F);
        }
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {

        if(ModConfigs.canopyCrash && entity instanceof EntityLivingBase) { //We are only interested in Living things crashing through the canopy.
            entity.fallDistance--;

            AxisAlignedBB aabb = entity.getEntityBoundingBox();

            int minX = MathHelper.floor(aabb.minX + 0.001D);
            int minZ = MathHelper.floor(aabb.minZ + 0.001D);
            int maxX = MathHelper.floor(aabb.maxX - 0.001D);
            int maxZ = MathHelper.floor(aabb.maxZ - 0.001D);

            boolean crushing = true;
            boolean hasLeaves = true;

            SoundType stepSound = this.getSoundType(world.getBlockState(pos), world, pos, entity);
            float volume = MathHelper.clamp(stepSound.getVolume() / 16.0f * fallDistance, 0, 3.0f);
            world.playSound(entity.posX, entity.posY, entity.posZ, stepSound.getBreakSound(), SoundCategory.BLOCKS, volume, stepSound.getPitch(), false);

            for(int iy = 0; (entity.fallDistance > 3.0f) && crushing && ((pos.getY() - iy) > 0); iy++) {
                if(hasLeaves) {//This layer has leaves that can help break our fall
                    entity.fallDistance *= 0.66f;//For each layer we are crushing break the momentum
                    hasLeaves = false;
                }
                for(int ix = minX; ix <= maxX; ix++) {
                    for(int iz = minZ; iz <= maxZ; iz++) {
                        BlockPos iPos = new BlockPos(ix, pos.getY() - iy, iz);
                        IBlockState state = world.getBlockState(iPos);
                        if(TreeHelper.isLeaves(state)) {
                            hasLeaves = true;//This layer has leaves
                            DynamicTrees.proxy.crushLeavesBlock(world, iPos, state, entity);
                            world.setBlockToAir(iPos);
                        } else
                        if (!world.isAirBlock(iPos)) {
                            crushing = false;//We hit something solid thus no longer crushing leaves layers
                        }
                    }
                }
            }
        }
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess access, BlockPos pos) {

        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState extState = (IExtendedBlockState) state;
            for(Surround surr : hydroSurroundMap[state.getValue(BlockDynamicLeaves.HYDRO)]) {
                IBlockState scanState = access.getBlockState(pos.add(surr.getOffset()));
                if(scanState.getBlock() == this) {
                    if( scanState.getValue(BlockDynamicLeaves.HYDRO) == 3 ) {
                        extState = extState.withProperty(CONNECTIONS[surr.ordinal()], true);
                    }
                }
            }

            return extState;
        }

        return state;
    }

    @Override
    public int getRadiusForConnection(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, BlockBranch from, EnumFacing side, int fromRadius) {
        return side == EnumFacing.UP && from.getFamily().isCompatibleDynamicLeaves(blockState, blockAccess, pos) ? fromRadius : 0;
    }

    @Override
    public int branchSupport(IBlockState blockState, IBlockAccess blockAccess, BlockBranch branch, BlockPos pos, EnumFacing dir, int radius) {
        return branch.getFamily() == getFamily(blockState, blockAccess, pos) ? BlockBranch.setSupport(0, 1) : 0;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        AxisAlignedBB base = super.getBoundingBox(state, source, pos);
        base.expand(1, 0, 1);
        base.expand(-1,-0,-1);
        return base;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }
}