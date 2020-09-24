package com.ferreusveritas.dynamictreesphc;

import com.pam.harvestcraft.blocks.FruitRegistry;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ModConstants extends com.ferreusveritas.dynamictrees.ModConstants {
	
	public static final String MODID = "dynamictreesphc";
	public static final String NAME = "Dynamic Trees PHC";
	public static final String VERSION = "1.12.2-9999.9999.9999z";//Maxed out version to satisfy dependencies during dev, Assigned from gradle during build, do not change
	
	//Other Mods
	public static final String PHC_MODID = com.pam.harvestcraft.Reference.MODID;
	
	//Other Mod Versions
	public static final String PHC_LATEST = PHC_MODID + AT + com.pam.harvestcraft.Reference.VERSION + ORGREATER;

	public static final String DEPENDENCIES
			= REQAFTER + DYNAMICTREES_LATEST
			+ NEXT
			+ REQAFTER + PHC_LATEST;

	public static final List<String> NOFRUIT = new LinkedList<>(Arrays.asList(FruitRegistry.CINNAMON,FruitRegistry.PAPERBARK,FruitRegistry.MAPLE));

	public static final List<String> FALLINGFRUIT = new LinkedList<>(Arrays.asList(FruitRegistry.COCONUT, FruitRegistry.DURIAN, FruitRegistry.JACKFRUIT, FruitRegistry.BREADFRUIT));

	public static final List<String> SEEDISFRUIT = new LinkedList<>(Arrays.asList(FruitRegistry.COCONUT));
	//Coconut is a nut too but we exclude it cause this is for recipes
	public static final List<String> NUTS = new LinkedList<>(Arrays.asList(FruitRegistry.ALMOND, FruitRegistry.CASHEW, FruitRegistry.CHESTNUT, FruitRegistry.HAZELNUT, FruitRegistry.NUTMEG, FruitRegistry.PECAN, FruitRegistry.PISTACHIO, FruitRegistry.WALNUT));

	public static final List<String> PALMS = new LinkedList<>(Arrays.asList(FruitRegistry.DATE,FruitRegistry.PAPAYA,FruitRegistry.DRAGONFRUIT,FruitRegistry.BANANA,FruitRegistry.COCONUT));

	//Fruits that have a direction property, for being placed on the side of blocks.
	public static final List<String> SIDEDFRUIT = new LinkedList<>(Arrays.asList(FruitRegistry.DATE,FruitRegistry.PAPAYA,FruitRegistry.COCONUT));

}
