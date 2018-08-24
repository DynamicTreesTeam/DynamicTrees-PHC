package com.ferreusveritas.dtphc;

public class ModConstants {
	
	public static final String MODID = "dynamictrees-phc";
	public static final String NAME = "Dynamic Trees PHC";
	public static final String VERSIONDEV = "1.12.2-9.9.9z";
	public static final String VERSIONAUTO = "@VERSION@";
	public static final String VERSION = VERSIONAUTO;
	
	public static final String AFTER = "after:";
	public static final String BEFORE = "before:";
	public static final String NEXT = ";";
	
	public static final String PHC_LATEST = com.pam.harvestcraft.Reference.MODID + "@[" + com.pam.harvestcraft.Reference.VERSION + ",)";
	
	public static final String DEPENDENCIES
		= AFTER + com.ferreusveritas.dynamictrees.ModConstants.DYNAMICTREES_LATEST
		+ NEXT
		+ AFTER + PHC_LATEST
		;
	
}
