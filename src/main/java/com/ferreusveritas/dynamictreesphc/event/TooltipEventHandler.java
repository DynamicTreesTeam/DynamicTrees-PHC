package com.ferreusveritas.dynamictreesphc.event;

import com.ferreusveritas.dynamictrees.client.TooltipHandler;
import com.ferreusveritas.dynamictreesphc.ModItems;

import net.minecraft.item.Item;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TooltipEventHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onItemTooltipAdded(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        if (Loader.isModLoaded("sereneseasons") && item.equals(ModItems.passionfruitSeed)){
            TooltipHandler.applySeasonalTooltips(event.getToolTip(), 2);
        }
    }

}
