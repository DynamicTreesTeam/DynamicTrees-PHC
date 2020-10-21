package com.ferreusveritas.dynamictreesphc;

import com.pam.harvestcraft.blocks.FruitRegistry;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.*;

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

	public static final List<String> FRUITISNOTSEED = new LinkedList<>(Arrays.asList(FruitRegistry.COCONUT, FruitRegistry.BANANA));
	public static final List<String> FRUITDROPSSEED = new LinkedList<>(Arrays.asList(FruitRegistry.COCONUT));
	//Coconut is a nut too but we exclude it cause this is for recipes
	public static final List<String> NUTS = new LinkedList<>(Arrays.asList(FruitRegistry.ALMOND, FruitRegistry.CASHEW, FruitRegistry.CHESTNUT, FruitRegistry.HAZELNUT, FruitRegistry.NUTMEG, FruitRegistry.PECAN, FruitRegistry.PISTACHIO, FruitRegistry.WALNUT));

	public static final List<String> PALMS = new LinkedList<>(Arrays.asList(FruitRegistry.DATE,FruitRegistry.PAPAYA,FruitRegistry.DRAGONFRUIT,FruitRegistry.BANANA,FruitRegistry.COCONUT));

	//Fruits that have a direction property, for being placed on the side of blocks.
	public static final List<String> SIDEDFRUIT = new LinkedList<>(Arrays.asList(FruitRegistry.DATE,FruitRegistry.PAPAYA,FruitRegistry.COCONUT));

	public static class fruitBoxes {

		protected static final AxisAlignedBB flower = createBox(1,1,0, 16);
		protected static final AxisAlignedBB firstStage = createBox(1,2,0, 16);

		public static final AxisAlignedBB[] FRUIT_SMALL = new AxisAlignedBB[] {
				flower,
				firstStage,
				createBox(2f,4,0),
				createBox(2f,4,1.25f)
		};
		public static final AxisAlignedBB[] FRUIT_THIN = new AxisAlignedBB[] {
				flower,
				firstStage,
				createBox(2f,5,0),
				createBox(2f,5,1.25f)
		};
		public static final AxisAlignedBB[] FRUIT_LONG = new AxisAlignedBB[] {
				flower,
				firstStage,
				createBox(2f,6,0),
				createBox(2f,6,1.25f)
		};
		public static final AxisAlignedBB[] FRUIT_BERRY = new AxisAlignedBB[] {
				flower,
				createBox(2,3,0),
				createBox(3.3f,4,1),
				createBox(3.3f,4,2)
		};

		public static final AxisAlignedBB[] BANANA = new AxisAlignedBB[] {
				flower,
				createBox(2.5f,10,0),
				createBox(7f,20,0),
				createBox(7f,13,0)
		};
		public static final AxisAlignedBB[] BREADFRUIT = new AxisAlignedBB[] {
				flower,
				firstStage,
				createBox(2.5f,5,0),
				createBox(3f,7,1.25f)
		};
		public static final AxisAlignedBB[] CASHEW = new AxisAlignedBB[] {
				flower,
				createBox(2,7,0),
				createBox(2,8,0),
				createBox(2,8,1.25f)
		};
		public static final AxisAlignedBB[] DRAGONFRUIT = new AxisAlignedBB[] {
				createBox(1,1,0, 16),
				createBox(1,4,0, 16),
				createBox(2f,8,0),
				createBox(2f,9,1.25f)
		};
		public static final AxisAlignedBB[] DURIAN = new AxisAlignedBB[] {
				flower,
				firstStage,
				createBox(2.5f,5,3),
				createBox(3.5f,7,5)
		};
		public static final AxisAlignedBB[] MANGO = new AxisAlignedBB[] {
				flower,
				firstStage,
				createBox(2.5f,6,0),
				createBox(2.5f,6,1.25f)
		};
		public static final AxisAlignedBB[] CHERRY = new AxisAlignedBB[] {
				flower,
				createBox(2,3,0),
				createBox(2.5f,4,2),
				createBox(3.5f,4,3)
		};
		public static final AxisAlignedBB[] DATE = new AxisAlignedBB[] {
				createBox(1,1,1, 16),
				createBox(4,8,7, 16),
				createBox(5,10,6, 16),
				createBox(5,10,6, 16)
		};
		public static final AxisAlignedBB[] PAPAYA = new AxisAlignedBB[] {
				createBox(1,1,3),
				createBox(1.5f,4,4),
				createBox(2,6,4),
				createBox(2.5f,10,5)
		};
		public static final AxisAlignedBB[] COCONUT = new AxisAlignedBB[] {
				flower,
				createBox(2.5f,5,1, 16),
				createBox(4,8,1, 16),
				createBox(4,8,2, 16)
		};
		public static final AxisAlignedBB[] GRAPEFRUIT = new AxisAlignedBB[] {
				flower,
				firstStage,
				createBox(2.5f,5,0),
				createBox(3f,6,1.25f)
		};
		public static final AxisAlignedBB[] HAZELNUT = new AxisAlignedBB[] {
				flower,
				createBox(1,2,0),
				createBox(1.5f,3,0),
				createBox(1.5f,3,1.25f)
		};
		public static final AxisAlignedBB[] JACKFRUIT = new AxisAlignedBB[] {
				flower,
				createBox(2.5f,6,0),
				createBox(3f,8,0),
				createBox(3.5f,9,1.25f)
		};
		public static final AxisAlignedBB[] PEPPERCORN = new AxisAlignedBB[] {
				createBox(1,5,0),
				createBox(1,5,0),
				createBox(1.5f,8,1.25f),
				createBox(1.5f,8,2.5f)
		};
		public static final AxisAlignedBB[] PISTACHIO = new AxisAlignedBB[] {
				createBox(1.8f,4,0),
				createBox(1,3,0),
				createBox(2.8f,4,1),
				createBox(2.8f,4,2)
		};
		public static final AxisAlignedBB[] RAMBUTAN = new AxisAlignedBB[] {
				flower,
				createBox(2, (8/3f),0, 16),
				createBox(4.6f,5.5f,1.5f),
				createBox(4.6f,6,2)
		};
		public static final AxisAlignedBB[] SOURSOP = new AxisAlignedBB[] {
				flower,
				createBox(1, (8/3f),0, 16),
				createBox(2.5f,8,0),
				createBox(2.5f,8,1.25f)
		};
		public static final AxisAlignedBB[] SPIDERWEB = new AxisAlignedBB[] {
				createBox(1,5,0, 16),
				createBox(4.5f,8,0, 16),
				createBox(4.5f,12,0, 16),
				createBox(6,15,0, 16)
		};
		public static final AxisAlignedBB[] TAMARIND = new AxisAlignedBB[] {
				flower,
				createBox(1,4,0),
				createBox(1.5f,8,0),
				createBox(1.5f,10,1.25f)
		};
		public static final AxisAlignedBB[] VANILLABEAN = new AxisAlignedBB[] {
				flower,
				createBox(1.5f,4,1),
				createBox(1.5f,6,1),
				createBox(1.5f,7,2)
		};

		private static AxisAlignedBB createBox (float radius, float height, float stemLength){
			return createBox(radius, height, stemLength, 20);
		}
		private static AxisAlignedBB createBox (float radius, float height, float stemLength, float fraction){
			float topHeight = fraction - stemLength;
			float bottomHeight = topHeight - height;
			return new AxisAlignedBB(
					((fraction/2) - radius)/fraction, topHeight/fraction, ((fraction/2) - radius)/fraction,
					((fraction/2) + radius)/fraction, bottomHeight/fraction, ((fraction/2) + radius)/fraction);
		}

	}

	//A null offset means the fruit grows the same way all year round.
	public static Map<String, Float> fruitOffset = new HashMap<String, Float>() {{
		put(FruitRegistry.ALMOND, 0.5f); //summer-autumn
		put(FruitRegistry.APRICOT, 0f); //summer
		put(FruitRegistry.AVOCADO, 0f); //summer
		put(FruitRegistry.BANANA, null); //all year round
		put(FruitRegistry.CASHEW, 2f); //winter
		put(FruitRegistry.CHERRY, 0f); //summer
		put(FruitRegistry.CHESTNUT, 0.5f); //summer-autumn
		put(FruitRegistry.COCONUT, null); //all year round
		put(FruitRegistry.DATE, 1.5f); //autumn-winter
		put(FruitRegistry.DRAGONFRUIT, 0f); //summer
		put(FruitRegistry.DURIAN, 0f); //summer
		put(FruitRegistry.FIG, 0.5f); //summer-autumn
		put(FruitRegistry.GOOSEBERRY, 0f); //summer
		put(FruitRegistry.GRAPEFRUIT, 3f); //spring
		put(FruitRegistry.LEMON, 2.5f); //winter-spring
		put(FruitRegistry.LIME, 0f); //summer
		put(FruitRegistry.MANGO, 0f); //summer
		put(FruitRegistry.NUTMEG, 0f); //summer
		put(FruitRegistry.OLIVE, 1f); //autumn
		put(FruitRegistry.ORANGE, 2.5f); //winter-spring
		put(FruitRegistry.PAPAYA, null); //all year round
		put(FruitRegistry.PEACH, 0f); //summer
		put(FruitRegistry.PEAR, 0f); //summer
		put(FruitRegistry.PECAN, 1f); //autumn
		put(FruitRegistry.PEPPERCORN, 0f); //summer
		put(FruitRegistry.PERSIMMON, 1.5f); //autumn-winter
		put(FruitRegistry.PISTACHIO, 0f); //summer
		put(FruitRegistry.PLUM, 0f); //summer
		put(FruitRegistry.POMEGRANATE, 1.5f); //autumn-winter
		put(FruitRegistry.STARFRUIT, 1f); //autumn
		put(FruitRegistry.VANILLABEAN, 2f); //winter
		put(FruitRegistry.WALNUT, 0.5f); //summer-autumn
		put(FruitRegistry.SPIDERWEB, null); //all year round, i guess?
		put(FruitRegistry.HAZELNUT, 1f); //autumn
		put(FruitRegistry.PAWPAW, 0f); //summer
		put(FruitRegistry.SOURSOP, 1f); //autumn
		put(FruitRegistry.BREADFRUIT, 3f); //spring
		put(FruitRegistry.GUAVA, 3f); //spring
		put(FruitRegistry.JACKFRUIT, 0.5f); //summer-autumn
		put(FruitRegistry.LYCHEE, 0f); //summer
		put(FruitRegistry.PASSIONFRUIT, 0f); //summer
		put(FruitRegistry.RAMBUTAN, 0.5f); //summer-autumn
		put(FruitRegistry.TAMARIND, 3.5f); //spring-summer
	}};

}
