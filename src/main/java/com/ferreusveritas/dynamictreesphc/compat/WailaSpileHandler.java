package com.ferreusveritas.dynamictreesphc.compat;

import com.ferreusveritas.dynamictrees.compat.WailaBranchHandler;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.blocks.BlockMapleSpile;
import com.ferreusveritas.dynamictreesphc.blocks.BlockMapleSpileBucket;
import com.pam.harvestcraft.blocks.FruitRegistry;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.SpecialChars;
import mcp.mobius.waila.config.FormattingConfig;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import java.util.List;

public class WailaSpileHandler implements IWailaDataProvider {

    @Nonnull
    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        tooltip.clear();
        tooltip.add(String.format(FormattingConfig.blockFormat, new TextComponentTranslation(accessor.getBlock().getLocalizedName()).getFormattedText()));
        return tooltip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        IBlockState state = accessor.getWorld().getBlockState(accessor.getPosition());

        StringBuilder renderString = new StringBuilder();

        ItemStack syrup = new ItemStack(FruitRegistry.getLog(FruitRegistry.MAPLE).getFruitItem());

        if (state.getProperties().containsKey(BlockMapleSpile.FILLED)){
            int count = state.getValue(BlockMapleSpile.FILLED) ? 1:0;
            renderString.append(SpecialChars.getRenderString("waila.stack", "1", syrup.getItem().getRegistryName().toString(), String.valueOf(count), String.valueOf(syrup.getItemDamage())));
        } else if (state.getProperties().containsKey(BlockMapleSpileBucket.FILLING)){
            int count = state.getValue(BlockMapleSpileBucket.FILLING);
            count += count==3?1:0;
            renderString.append(SpecialChars.getRenderString("waila.stack", "1", syrup.getItem().getRegistryName().toString(), String.valueOf(count), String.valueOf(syrup.getItemDamage())));
        }

        tooltip.add(renderString.toString());

        return tooltip;
    }

    @Nonnull
    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        tooltip.clear();
        tooltip.add(String.format(FormattingConfig.modNameFormat, ModConstants.NAME));
        return tooltip;
    }
}
