package com.ferreusveritas.dynamictreesphc.event;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.client.TooltipHandler;
import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.ModItems;
import com.pam.harvestcraft.blocks.FruitRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;

public class TooltipEventHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onItemTooltipAdded(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        if (item.equals(ModItems.passionfruitSeed)){
            EntityPlayer player = event.getEntityPlayer();
            if(player != null) {
                TooltipHandler.applySeasonalTooltips(new LinkedList<>(), 2);
            }
        }
    }

}
