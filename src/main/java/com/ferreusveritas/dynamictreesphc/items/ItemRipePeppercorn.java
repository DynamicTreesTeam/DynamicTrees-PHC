package com.ferreusveritas.dynamictreesphc.items;

import java.util.List;

import com.pam.harvestcraft.HarvestCraft;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemRipePeppercorn extends Item {

    public ItemRipePeppercorn(ResourceLocation name) {
        setRegistryName(name);
        setUnlocalizedName(name.toString());
        setCreativeTab(HarvestCraft.modTab);
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag tooltipFlag) {
        super.addInformation(stack, world, tooltip, tooltipFlag);
        tooltip.add(new TextComponentTranslation("tooltip.dynamictreesphc:peppercornripeitem.name").getFormattedText());
    }

}
