package com.ferreusveritas.dynamictreesphc.proxy;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictreesphc.ModBlocks;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.ModItems;
import com.ferreusveritas.dynamictreesphc.items.ItemDynamicSeedMaple;
import com.ferreusveritas.dynamictreesphc.models.ModelLoaderBlockPalmFronds;
import com.ferreusveritas.dynamictreesphc.renderer.RenderMapleSeed;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
		registerEntityRenderers();

		ModelLoaderRegistry.registerLoader(new ModelLoaderBlockPalmFronds("dynamicpalmfrondsphcsmall",0));
		ModelLoaderRegistry.registerLoader(new ModelLoaderBlockPalmFronds("dynamicpalmfrondsphcmedium",1));
		ModelLoaderRegistry.registerLoader(new ModelLoaderBlockPalmFronds("dynamicpalmfrondsphclarge",2));
	}
	
	@Override
	public void init() {
		super.init();
		registerColorHandlers();
	}
	
	public void registerColorHandlers() {
		
		final int magenta = 0x00FF00FF;//for errors.. because magenta sucks.
		
		//Register GrowingLeavesBlocks Colorizers
		List<BlockDynamicLeaves> leavesList = new LinkedList<>(LeavesPaging.getLeavesMapForModId(ModConstants.MODID).values());
		leavesList.add(ModBlocks.leavesPalm);
		leavesList.add(ModBlocks.leavesDragonfruit);
		for(BlockDynamicLeaves leaves: leavesList) {
			ModelHelper.regColorHandler(leaves, (state, worldIn, pos, tintIndex) -> {
				Block block = state.getBlock();
				if(TreeHelper.isLeaves(block)) {
					return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
				}
				return magenta;
			});
		}

		List<Block> coloredBlocks = new LinkedList<>();
		coloredBlocks.add(ModBlocks.bananaSucker);
		coloredBlocks.add(ModBlocks.passionfruitVine0);
		coloredBlocks.add(ModBlocks.passionfruitVine1);
		coloredBlocks.add(ModBlocks.passionfruitVine2);
		coloredBlocks.add(ModBlocks.passionfruitVine3);
		coloredBlocks.add(ModBlocks.passionfruitSapling);
		for(Block block: coloredBlocks) {
			ModelHelper.regColorHandler(block, (state, worldIn, pos, tintIndex) -> worldIn != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic());
		}
		ModelHelper.regColorHandler(ModItems.passionfruitVine, (stack, tintIndex) -> {
			IBlockState iblockstate = ((ItemBlock)stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
			return Minecraft.getMinecraft().getBlockColors().colorMultiplier(iblockstate, null, null, tintIndex);
		});

	}

	public void registerEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(ItemDynamicSeedMaple.EntityItemMapleSeed.class, new RenderMapleSeed.Factory());
	}
	
}
