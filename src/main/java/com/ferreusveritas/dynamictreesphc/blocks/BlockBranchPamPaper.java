package com.ferreusveritas.dynamictreesphc.blocks;

import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.trees.TreePaperBark;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

import static com.ferreusveritas.dynamictreesphc.ModConstants.SEASON;

public class BlockBranchPamPaper extends BlockBranchPamSpecial {

    protected final float barkRegrowChance = 5f; //percent
    protected final float barkRegrowOutOfSeasonChance = 2f; //percent
    protected final int minStripRadius = 7;

    public BlockBranchPamPaper(String name, String logName, float yieldPerLog, boolean tick) {
        super(name, logName, yieldPerLog);
        setTickRandomly(tick);
    }

    public float getBarkRegrowChance(World world) {
        Float season = SeasonHelper.getSeasonValue(world);
        if (season == null || (season >= SEASON.FALL_START && season < SEASON.WINTER_START)){ //bark grows faster in autumn
            return Math.max(Math.min(barkRegrowChance / 100f, 1),0);
        }
        else return Math.max(Math.min(barkRegrowOutOfSeasonChance / 100f, 1),0);
    }

    public int getMinStripRadius() {
        return Math.max(Math.min(minStripRadius, 8), 1);
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        if (state.getBlock() == TreePaperBark.paperbarkCutBranch && worldIn.rand.nextFloat() <= getBarkRegrowChance(worldIn)){
            worldIn.setBlockState(pos, TreePaperBark.paperbarkBranch.getDefaultState().withProperty(RADIUS, state.getValue(RADIUS)));
        }
        super.randomTick(worldIn, pos, state, random);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.getHeldItem(hand).getItem() instanceof ItemAxe && state.getBlock() == TreePaperBark.paperbarkBranch && state.getValue(RADIUS) >= getMinStripRadius()){
            playerIn.addItemStackToInventory(new ItemStack(Items.PAPER, 1 + (int)(worldIn.rand.nextFloat()*3)));
            playerIn.getHeldItem(hand).damageItem(1, playerIn);
            worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, SoundCategory.BLOCKS, 0.2f, 1.5f + worldIn.rand.nextFloat(), false);
            worldIn.setBlockState(pos, TreePaperBark.paperbarkCutBranch.getDefaultState().withProperty(RADIUS, state.getValue(RADIUS)));
            return true;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public int getRadius(IBlockState state) {
        if (state.getBlock() == TreePaperBark.paperbarkCutBranch && state.getValue(RADIUS) >= getMinStripRadius()){
            return super.getRadius(state) - 1;
        }
        return super.getRadius(state);
    }
}
