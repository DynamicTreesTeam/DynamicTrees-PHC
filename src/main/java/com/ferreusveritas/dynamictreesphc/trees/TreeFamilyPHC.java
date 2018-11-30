package com.ferreusveritas.dynamictreesphc.trees;

import java.util.List;

import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

public class TreeFamilyPHC extends TreeFamily {
	
	public TreeFamilyPHC(ResourceLocation resourceLocation) {
		super(resourceLocation);
	}
	
	//This mod registers all of the seeds externally so we'll only provide the dynamic branch block here
	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		//Register an itemBlock for the branch block
		itemList.add(new ItemBlock(getDynamicBranch()).setRegistryName(getDynamicBranch().getRegistryName()));
		return itemList;
	}
	
}
