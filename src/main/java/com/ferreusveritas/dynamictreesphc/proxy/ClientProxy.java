package com.ferreusveritas.dynamictreesphc.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.ModTrees;
import com.ferreusveritas.dynamictreesphc.items.ItemDynamicSeedMaple;
import com.ferreusveritas.dynamictreesphc.renderer.RenderMapleSeed;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
		registerEntityRenderers();
	}
	
	@Override
	public void init() {
		super.init();
		registerColorHandlers();
	}
	
	public void registerColorHandlers() {
				
		try {
			ResourceLocation loc = new ResourceLocation(ModConstants.MODID, "models/item/seedcolors.json");
			InputStream in;
			in = Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			Gson gson = new Gson();
			JsonElement je = gson.fromJson(reader, JsonElement.class);
			JsonObject json = je.getAsJsonObject();
			for(Entry<String, JsonElement> entry : json.entrySet()) {
				String speciesName = entry.getKey();
				Species species = ModTrees.phcFruitSpecies.get(speciesName);
				if(species != null) {
					Seed seed = species.getSeed();
					JsonArray colors = entry.getValue().getAsJsonArray();
					List<Integer> colorArray = new ArrayList<>(4);
					colors.forEach(i -> colorArray.add(Integer.parseInt(i.getAsString(), 16)));
					if(colors != null) {
						ModelHelper.regColorHandler(seed, new IItemColor() {
							@Override
							public int colorMultiplier(ItemStack stack, int tintIndex) {
								return tintIndex >= 0 && tintIndex < colorArray.size() ? colorArray.get(tintIndex) : 0xFFFFFF;
							}
						});
					}

				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		final int magenta = 0x00FF00FF;//for errors.. because magenta sucks.
		
		//Register GrowingLeavesBlocks Colorizers
		for(BlockDynamicLeaves leaves: LeavesPaging.getLeavesMapForModId(ModConstants.MODID).values()) {
			ModelHelper.regColorHandler(leaves, new IBlockColor() {
				@Override
				public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
					Block block = state.getBlock();
					if(TreeHelper.isLeaves(block)) {
						return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
					}
					return magenta;
				}
			});
		}
		
	}

	public void registerEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(ItemDynamicSeedMaple.EntityItemMapleSeed.class, new RenderMapleSeed.Factory());
	}
	
}
