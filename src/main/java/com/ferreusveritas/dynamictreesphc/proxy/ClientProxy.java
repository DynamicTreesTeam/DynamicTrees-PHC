package com.ferreusveritas.dynamictreesphc.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.ModTrees;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ClientProxy extends CommonProxy {
	
	public void init() {
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
				Species species = ModTrees.phcSpecies.get(speciesName);
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
		
	}

}
