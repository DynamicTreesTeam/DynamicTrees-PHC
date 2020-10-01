package com.ferreusveritas.dynamictreesphc.items;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.ModTabs;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.blocks.BlockDynamicSaplingPassionfruit;
import com.ferreusveritas.dynamictreesphc.blocks.BlockVinePassionfruit;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDynamicSeedPassionfruit extends Item {

    public ItemDynamicSeedPassionfruit(ResourceLocation name) {
        setRegistryName(name);
        setUnlocalizedName(name.toString());
        setCreativeTab(ModTabs.dynamicTreesTab);
    }

    public boolean doPlanting(World world, BlockPos pos, EntityPlayer planter, ItemStack seedStack) {
        if(plantSapling(world, pos)) {//Do the planting
            return true;
        }
        return false;
    }

    public boolean plantSapling(World world, BlockPos pos) {
        if(world.getBlockState(pos).getBlock().isReplaceable(world, pos) && BlockDynamicSaplingPassionfruit.canSaplingStay(world, pos)) {
            world.setBlockState(pos, ModBlocks.passionfruitSapling.getDefaultState());
            return true;
        }
        return false;
    }

//    public EnumActionResult onItemUseFlowerPot(EntityPlayer player, World world, BlockPos pos, EnumHand hand, ItemStack seedStack, EnumFacing facing, float hitX, float hitY, float hitZ) {
//        //Handle Flower Pot interaction
//        IBlockState emptyPotState = world.getBlockState(pos);
//        if(emptyPotState.getBlock() instanceof BlockFlowerPot && (emptyPotState == emptyPotState.getBlock().getDefaultState()) ) { //Empty Flower Pot of some kind
//            Species species = getSpecies(seedStack);
//            BlockBonsaiPot bonzaiPot = species.getBonzaiPot();
//            world.setBlockState(pos, bonzaiPot.getDefaultState());
//            if(bonzaiPot.setSpecies(world, species, pos) && bonzaiPot.setPotState(world, emptyPotState, pos)) {
//                seedStack.shrink(1);
//                return EnumActionResult.SUCCESS;
//            }
//        }
//
//        return EnumActionResult.PASS;
//    }

    public EnumActionResult onItemUsePlantSeed(EntityPlayer player, World world, BlockPos pos, EnumHand hand, ItemStack seedStack, EnumFacing facing, float hitX, float hitY, float hitZ) {

        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if(block.isReplaceable(world, pos)) {
            pos = pos.down();
            facing = EnumFacing.UP;
        }

        if (facing == EnumFacing.UP) {//Ensure this seed is only used on the top side of a block
            if (player.canPlayerEdit(pos, facing, seedStack) && player.canPlayerEdit(pos.up(), facing, seedStack)) {//Ensure permissions to edit block
                if(doPlanting(world, pos.up(), player, seedStack)) {
                    seedStack.shrink(1);
                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack seedStack = player.getHeldItem(hand);

//        //Handle Flower Pot interaction
//        if(onItemUseFlowerPot(player, world, pos, hand, seedStack, facing, hitX, hitY, hitZ) == EnumActionResult.SUCCESS) {
//            return EnumActionResult.SUCCESS;
//        }

        //Handle Planting Seed interaction
        if(onItemUsePlantSeed(player, world, pos, hand, seedStack, facing, hitX, hitY, hitZ) == EnumActionResult.SUCCESS) {
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

}
