package com.ferreusveritas.dynamictreesphc;

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
		+ REQAFTER + PHC_LATEST
		;

	public static final List<String> NOFRUIT = new LinkedList<>(Arrays.asList("cinnamon","paperbark","maple"));

	public static final List<String> PALMS = new LinkedList<>(Arrays.asList("date","papaya","dragonfruit","banana","coconut"));

}
