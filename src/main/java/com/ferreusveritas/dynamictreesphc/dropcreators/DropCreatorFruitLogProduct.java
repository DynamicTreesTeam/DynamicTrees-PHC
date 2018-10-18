package com.ferreusveritas.dynamictreesphc.dropcreators;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictreesphc.trees.TreeFamilyPHC;
import com.pam.harvestcraft.blocks.growables.BlockPamFruitLog;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DropCreatorFruitLogProduct extends DropCreator {
	
	private final TreeFamilyPHC treeFamily;
	private final float yieldPerLog;
	
	public DropCreatorFruitLogProduct(TreeFamilyPHC treeFamily) {
		this(treeFamily, 2.0f);
	}
	
	public DropCreatorFruitLogProduct(TreeFamilyPHC treeFamily, float yieldPerLog) {
		super(new ResourceLocation(ModConstants.MODID, "fruitlog"));
		this.treeFamily = treeFamily;
		this.yieldPerLog = yieldPerLog;
	}

	@Override
	public List<ItemStack> getLogsDrop(World world, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, float volume) {
		
		Block primLogBlock = treeFamily.getPrimitiveLog().getBlock();
		
		if(primLogBlock instanceof BlockPamFruitLog) {
			BlockPamFruitLog fruitLog = (BlockPamFruitLog) primLogBlock;
			Item fruit = fruitLog.getFruitItem();
			
			int numFruits = (int) (volume * yieldPerLog);
			int maxStackSize = fruit.getItemStackLimit();
			while(numFruits > 0) {
				dropList.add(new ItemStack(fruit, numFruits >= maxStackSize ? maxStackSize : numFruits));
				numFruits -= maxStackSize;
			}
		}
		
		return dropList;
	}
	
}
