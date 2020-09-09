package com.ferreusveritas.dynamictreesphc.blocks;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.entities.EntityFallingTree;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.BranchDestructionData;
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

import java.util.List;
import java.util.stream.Collectors;

public class BlockBranchPamPaper extends BlockBranchPamSpecial {

    public BlockBranchPamPaper(String name, String logName, float yieldPerLog) {
        super(name, logName, yieldPerLog);
    }

}
