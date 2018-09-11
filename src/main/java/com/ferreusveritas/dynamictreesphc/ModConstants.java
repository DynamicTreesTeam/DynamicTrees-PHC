package com.ferreusveritas.dynamictreesphc;

public class ModConstants extends com.ferreusveritas.dynamictrees.ModConstants {
	
	public static final String MODID = "dynamictreesphc";
	public static final String NAME = "Dynamic Trees PHC";
	public static final String VERSIONAUTO = "@VERSION@";
	public static final String VERSION = VERSIONAUTO;//Change to VERSIONDEV in development, VERSIONAUTO for release
	
	//Other Mods
	public static final String PHC_MODID = com.pam.harvestcraft.Reference.MODID;
	
	//Other Mod Versions
	public static final String PHC_LATEST = PHC_MODID + AT + com.pam.harvestcraft.Reference.VERSION + ORGREATER;
	
	public static final String DEPENDENCIES
		= REQAFTER + DYNAMICTREES_LATEST
		+ NEXT
		+ REQAFTER + PHC_LATEST
		;
	
}
