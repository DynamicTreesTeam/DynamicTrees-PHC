package com.ferreusveritas.dynamictreesphc.blocks;

import com.ferreusveritas.dynamictrees.ModTabs;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.pam.harvestcraft.blocks.FruitRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class BlockVinePassionfruit extends BlockVine {

    private static final float baseFruitingChance = 0.002f;
    private static final float fruitGrowChance = 0.2f;
    private static final int maxLength = 3;

    public BlockVinePassionfruit (ResourceLocation name){
        setRegistryName(name);
        setUnlocalizedName(name.toString());
        setCreativeTab(ModTabs.dynamicTreesTab);
        setSoundType(SoundType.PLANT);
        setHardness(0.4f);
    }

    private float getFruitingChance (World world, BlockPos pos){
        float fruitFactor = SeasonHelper.globalSeasonalFruitProductionFactor(world, pos);
        return baseFruitingChance * Math.max((fruitFactor + 0.25f), 1);
    }

    private void changeVineWithProperties(World world, BlockPos pos, IBlockState stateWithWantedProperties){
        changeVineWithProperties(world, pos, getStateFromAge(0), stateWithWantedProperties);
    }
    private void changeVineWithProperties(World world, BlockPos pos, IBlockState baseState, IBlockState stateWithWantedProperties){
        IBlockState state = baseState
                .withProperty(UP, stateWithWantedProperties.getValue(UP))
                .withProperty(NORTH, stateWithWantedProperties.getValue(NORTH))
                .withProperty(WEST, stateWithWantedProperties.getValue(WEST))
                .withProperty(SOUTH, stateWithWantedProperties.getValue(SOUTH))
                .withProperty(EAST, stateWithWantedProperties.getValue(EAST));
        world.setBlockState(pos, state, 2);
    }

    private boolean recheckGrownSides(World worldIn, BlockPos pos, IBlockState state) {
        IBlockState iblockstate = state;

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            PropertyBool propertybool = getPropertyFor(enumfacing);

            if (state.getValue(propertybool) && !this.canAttachTo(worldIn, pos, enumfacing.getOpposite())) {
                IBlockState iblockstate1 = worldIn.getBlockState(pos.up());

                if (iblockstate1.getBlock() != this || !iblockstate1.getValue(propertybool)) {
                    state = state.withProperty(propertybool, false);
                }
            }
        }
        if (getNumGrownFaces(state) == 0) {
            return false;
        }
        else {
            if (iblockstate != state) {
                worldIn.setBlockState(pos, state, 2);
            }
            return true;
        }
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote && !this.recheckGrownSides(worldIn, pos, state))
        {
            spawnItemFruitIfRipe(worldIn, pos);
            worldIn.setBlockToAir(pos);
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            if (worldIn.rand.nextInt(4) == 0 && worldIn.isAreaLoaded(pos, 4)) { // Forge: check area to prevent loading unloaded chunks
                int vineLimit = 5;
                boolean tooManyVinesAround = false;

                tripleForBreak:
                for (int x = -4; x <= 4; ++x) {
                    for (int z = -4; z <= 4; ++z) {
                        for (int y = -1; y <= 1; ++y) {
                            if (worldIn.getBlockState(pos.add(x, y, z)).getBlock() instanceof BlockVinePassionfruit) {
                                --vineLimit;

                                if (vineLimit <= 0) {
                                    tooManyVinesAround = true;
                                    break tripleForBreak;
                                }
                            }
                        }
                    }
                }

                EnumFacing randomDir = EnumFacing.random(rand);
                BlockPos upPos = pos.up();

                //SPREAD UP
                if (randomDir == EnumFacing.UP && pos.getY() < 255 && worldIn.isAirBlock(upPos)) {
                    IBlockState newUpState = state;

                    for (EnumFacing dir : EnumFacing.Plane.HORIZONTAL) {
                        if (rand.nextBoolean() && this.canAttachTo(worldIn, upPos, dir.getOpposite())) {
                            newUpState = newUpState.withProperty(getPropertyFor(dir), true);
                        }
                        else {
                            newUpState = newUpState.withProperty(getPropertyFor(dir), false);
                        }
                    }

                    if (newUpState.getValue(NORTH) || newUpState.getValue(EAST) || newUpState.getValue(SOUTH) || newUpState.getValue(WEST)) {
                        // Up is always air so we spread age 0 vines
                        changeVineWithProperties(worldIn, upPos, newUpState);
                    }
                }
                //SPREAD SIDEWAYS
                else if (randomDir.getAxis().isHorizontal() && !state.getValue(getPropertyFor(randomDir))) {
                    if (!tooManyVinesAround) {
                        BlockPos randomDirPos = pos.offset(randomDir);
                        IBlockState randomDirState = worldIn.getBlockState(randomDirPos);
                        Block sideBlock = randomDirState.getBlock();

                        if (sideBlock.isAir(randomDirState, worldIn, randomDirPos)) {
                            EnumFacing randomDirRight = randomDir.rotateY();
                            EnumFacing randomDirLeft = randomDir.rotateYCCW();
                            boolean flag1 = state.getValue(getPropertyFor(randomDirRight));
                            boolean flag2 = state.getValue(getPropertyFor(randomDirLeft));
                            BlockPos randomRightPos = randomDirPos.offset(randomDirRight);
                            BlockPos randomLeftPos = randomDirPos.offset(randomDirLeft);

                            if (flag1 && this.canAttachTo(worldIn, randomRightPos.offset(randomDirRight), randomDirRight)) {
                                changeVineWithProperties(worldIn, randomDirPos, this.getDefaultState().withProperty(getPropertyFor(randomDirRight), true));
                            }
                            else if (flag2 && this.canAttachTo(worldIn, randomLeftPos.offset(randomDirLeft), randomDirLeft)) {
                                changeVineWithProperties(worldIn, randomDirPos, this.getDefaultState().withProperty(getPropertyFor(randomDirLeft), true));
                            }
                            else if (flag1 && worldIn.isAirBlock(randomRightPos) && this.canAttachTo(worldIn, randomRightPos, randomDir)) {
                                changeVineWithProperties(worldIn, randomRightPos, this.getDefaultState().withProperty(getPropertyFor(randomDir.getOpposite()), true));
                            }
                            else if (flag2 && worldIn.isAirBlock(randomLeftPos) && this.canAttachTo(worldIn, randomLeftPos, randomDir)) {
                                changeVineWithProperties(worldIn, randomLeftPos, this.getDefaultState().withProperty(getPropertyFor(randomDir.getOpposite()), true));
                            }
                        }
                        else if (randomDirState.getBlockFaceShape(worldIn, randomDirPos, randomDir) == BlockFaceShape.SOLID) {
                            //We change this block to cover said solid face
                            changeVineWithProperties(worldIn, pos, state, state.withProperty(getPropertyFor(randomDir), true));
                        }
                    }
                }
                //SPREAD DOWN
                else {
                    if (pos.getY() > 1 && !(worldIn.getBlockState(pos.up(maxLength)).getBlock() instanceof BlockVinePassionfruit)) {
                        BlockPos downPos = pos.down();
                        IBlockState downState = worldIn.getBlockState(downPos);

                        if (downState.getMaterial() == Material.AIR) {
                            IBlockState iblockstate1 = state;

                            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                                if (rand.nextBoolean())
                                {
                                    iblockstate1 = iblockstate1.withProperty(getPropertyFor(enumfacing), false);
                                }
                            }

                            if (iblockstate1.getValue(NORTH) || iblockstate1.getValue(EAST) || iblockstate1.getValue(SOUTH) || iblockstate1.getValue(WEST)) {
                                //down is air so we set age 0 vines
                                changeVineWithProperties(worldIn, downPos, iblockstate1);
                            }
                        }
                        else if (downState.getBlock() instanceof BlockVinePassionfruit) {
                            IBlockState newDownState = downState;

                            for (EnumFacing dir : EnumFacing.Plane.HORIZONTAL) {
                                PropertyBool propertySide = getPropertyFor(dir);

                                if (rand.nextBoolean() && state.getValue(propertySide)) {
                                    newDownState = newDownState.withProperty(propertySide, true);
                                }
                            }

                            if (newDownState.getValue(NORTH) || newDownState.getValue(EAST) || newDownState.getValue(SOUTH) || newDownState.getValue(WEST)) {
                                changeVineWithProperties(worldIn, downPos, downState, newDownState);
                            }
                        }
                    }
                }
            }
        }
        growFruit(worldIn, pos, state, rand);
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        Integer age = getAgeFromState(worldIn.getBlockState(pos));
        return age != null && age == 0;
    }

    protected void growFruit(World worldIn, BlockPos pos, IBlockState state, Random rand){
        Integer age = getAgeFromState(state);
        if (age == null) return;
        if ((age == 0 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextFloat() <= getFruitingChance(worldIn, pos))) ||
                (age > 0 && age < 4 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextFloat() <= fruitGrowChance))) {

            //We look for fruit blocks around. If there is more than two we cancel the fruit growth
            int fruitFoundAround = 0;
            for (EnumFacing dir : EnumFacing.values()){
                Integer sideAge = getAgeFromState(worldIn.getBlockState(pos.offset(dir)));
                if (sideAge != null && sideAge > 0){
                    fruitFoundAround++;
                }
            }
            if (fruitFoundAround >= 2){
                changeVineWithProperties(worldIn, pos, getStateFromAge(0), state);
                return;
            }
            changeVineWithProperties(worldIn, pos, getStateFromAge(age + 1), state);
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
        }
    }

    private Integer getAgeFromState (IBlockState state){
        Block block = state.getBlock();
        if (block == ModBlocks.passionfruitVine){
            return  0;
        } else if (block == ModBlocks.passionfruitVine0){
            return  1;
        } else if (block == ModBlocks.passionfruitVine1){
            return  2;
        } else if (block == ModBlocks.passionfruitVine2){
            return  3;
        } else if (block == ModBlocks.passionfruitVine3){
            return 4;
        } else {
            return null;
        }
    }
    @Nonnull
    private IBlockState getStateFromAge (int age){
        switch (age){
            case 0:
                return ModBlocks.passionfruitVine.getDefaultState();
            case 1:
                return ModBlocks.passionfruitVine0.getDefaultState();
            case 2:
                return ModBlocks.passionfruitVine1.getDefaultState();
            case 3:
                return ModBlocks.passionfruitVine2.getDefaultState();
            case 4:
                return ModBlocks.passionfruitVine3.getDefaultState();
            default:
                throw new IllegalArgumentException();
        }
    }

    private ItemStack getFruit(){
        return new ItemStack(FruitRegistry.getFood(FruitRegistry.PASSIONFRUIT));
    }

    private boolean spawnItemFruitIfRipe(World world, BlockPos pos){
        return spawnItemFruitIfRipe(world, pos, world.getBlockState(pos));
    }
    private boolean spawnItemFruitIfRipe(World world, BlockPos pos, IBlockState state){
        Integer age = getAgeFromState(state);
        if (!world.isRemote && age != null && age == 4){
            world.spawnEntity(new EntityItem(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, getFruit()));
            return true;
        }
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (spawnItemFruitIfRipe(worldIn, pos)){
            changeVineWithProperties(worldIn, pos, state);
            return true;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos) {
        if (player.getHeldItemMainhand().getItem() instanceof ItemShears){
            return 1;
        }
        return super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
    }

    @Override
    public boolean canAttachTo(World world, BlockPos pos, EnumFacing facing)
    {
        Block block = world.getBlockState(pos.up()).getBlock();
        return isAcceptableNeighbor(world, pos.offset(facing.getOpposite()), facing) && (block == Blocks.AIR || block instanceof BlockVinePassionfruit || TreeHelper.isBranch(block) || isAcceptableNeighbor(world, pos.up(), EnumFacing.UP));
    }
    public static boolean isAcceptableNeighbor(World world, BlockPos pos, EnumFacing facing)
    {
        IBlockState iblockstate = world.getBlockState(pos);
        if (TreeHelper.isBranch(iblockstate) && TreeHelper.getRadius(world, pos) == 8) return true;
        return iblockstate.getBlockFaceShape(world, pos, facing) == BlockFaceShape.SOLID && !isExceptBlockForAttaching(iblockstate.getBlock());
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        Integer age = getAgeFromState(state);
        if (age != null && age == 0)
            return new ItemStack(ModBlocks.passionfruitVine);
        else
            return getFruit();
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
    {
        List<ItemStack> stack = new LinkedList<>();
        stack.add(new ItemStack(ModBlocks.passionfruitVine));
        Integer age = getAgeFromState(world.getBlockState(pos));
        if (age != null && age == 4)
            stack.add(getFruit());
        return stack;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        spawnItemFruitIfRipe(worldIn, pos, state);
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS)
        {
            player.addStat(Objects.requireNonNull(StatList.getBlockStats(this)));
            spawnAsEntity(worldIn, pos, new ItemStack(ModBlocks.passionfruitVine));
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }

}
