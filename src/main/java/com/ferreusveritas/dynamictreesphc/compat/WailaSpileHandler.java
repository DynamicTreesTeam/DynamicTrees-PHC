package com.ferreusveritas.dynamictreesphc.compat;

import com.ferreusveritas.dynamictrees.compat.WailaBranchHandler;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.blocks.BlockMapleSpile;
import com.ferreusveritas.dynamictreesphc.blocks.BlockMapleSpileBucket;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.List;

public class WailaSpileHandler implements IWailaDataProvider {

    @Nonnull
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return new ItemStack(Items.IRON_INGOT);
    }

    @Nonnull
    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        tooltip.add(ModConstants.NAME);
        return tooltip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        IBlockState state = accessor.getWorld().getBlockState(accessor.getPosition());

        if (state.getProperties().containsKey(BlockMapleSpile.FILLED)){

        } else if (state.getProperties().containsKey(BlockMapleSpileBucket.FILLING)){

        }

        System.out.println("AAAAAA");

        tooltip.add(ModConstants.NAME);

        return tooltip;
    }

    @Nonnull
    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        tooltip.add(ModConstants.NAME);
        return tooltip;
    }
}
