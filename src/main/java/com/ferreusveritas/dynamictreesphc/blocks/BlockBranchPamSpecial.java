package com.ferreusveritas.dynamictreesphc.blocks;

import java.util.List;
import java.util.stream.Collectors;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.entities.EntityFallingTree;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.BranchDestructionData;
import com.pam.harvestcraft.blocks.FruitRegistry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockBranchPamSpecial extends BlockBranchBasic {

    private String logName;
    private float dropYield;

    public BlockBranchPamSpecial(String name, String logName, float yieldPerLog) {
        super(name);
        this.logName = logName;
        dropYield = yieldPerLog;
    }

    @Override public void futureBreak(IBlockState state, World world, BlockPos cutPos, EntityLivingBase entity) {

        //Try to get the face being pounded on
        final double reachDistance = entity instanceof EntityPlayerMP ? entity.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() : 5.0D;
        RayTraceResult rtResult = playerRayTrace(entity, reachDistance, 1.0F);
        EnumFacing toolDir = rtResult != null ? (entity.isSneaking() ? rtResult.sideHit.getOpposite() : rtResult.sideHit) : EnumFacing.DOWN;

        //Do the actual destruction
        BranchDestructionData destroyData = destroyBranchFromNode(world, cutPos, toolDir, false);

        //Get all of the wood drops
        ItemStack heldItem = entity.getHeldItemMainhand();
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, heldItem);
        boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, heldItem) != 0;
        float fortuneFactor = 1.0f + 0.25f * fortune;
        float woodVolume = destroyData.woodVolume;// The amount of wood calculated from the body of the tree network
        List<ItemStack> woodItems;
        if (silkTouch){
            woodItems = getLogDrops(world, cutPos, destroyData.species, woodVolume * fortuneFactor, true);
        } else {
            woodItems = getLogDrops(world, cutPos, destroyData.species, woodVolume * fortuneFactor, false);
        }

        float chance = 1.0f;
        //Fire the block harvesting event.  For An-Sar's PrimalCore mod :)
        if (entity instanceof EntityPlayer)
        {
            chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(woodItems, world, cutPos, state, fortune, chance, false, (EntityPlayer) entity);
        }
        final float finalChance = chance;

        //Build the final wood drop list taking chance into consideration
        List<ItemStack> woodDropList = woodItems.stream().filter(i -> world.rand.nextFloat() <= finalChance).collect(Collectors.toList());

        //This will drop the EntityFallingTree into the world
        EntityFallingTree.dropTree(world, destroyData, woodDropList, EntityFallingTree.DestroyType.HARVEST);

        //Damage the axe by a prescribed amount
        damageAxe(entity, heldItem, getRadius(state), woodVolume);
    }
    @Override protected void sloppyBreak(World world, BlockPos cutPos, EntityFallingTree.DestroyType destroyType) {
        //Do the actual destruction
        BranchDestructionData destroyData = destroyBranchFromNode(world, cutPos, EnumFacing.DOWN, false);
        //float burntWoodVolume = 0;

        //Get all of the wood drops
        List<ItemStack> woodDropList = getLogDrops(world, cutPos, destroyData.species, destroyData.woodVolume, false);

        //This will drop the EntityFallingTree into the world
        EntityFallingTree.dropTree(world, destroyData, woodDropList, destroyType);
    }
    public List<ItemStack> getLogDrops(World world, BlockPos pos, Species species, float volume, boolean silkTouch) {
        List<ItemStack> ret = new java.util.ArrayList<>();//A list for storing all the dead tree guts
        volume *= ModConfigs.treeHarvestMultiplier;// For cheaters.. you know who you are.
        if (silkTouch) {
            ItemStack drop = new ItemStack(FruitRegistry.getLog(logName));
            drop.setCount((int)volume);
            ret.add(drop);
        } else {
            ItemStack drop = new ItemStack(FruitRegistry.getLog(logName).getFruitItem());
            drop.setCount((int)(volume * dropYield));
            ret.add(drop);
        }
        return species.getLogsDrops(world, pos, ret, volume);
    }

}
