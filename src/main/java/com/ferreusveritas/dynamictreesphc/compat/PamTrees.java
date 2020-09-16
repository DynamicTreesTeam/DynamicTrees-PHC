package com.ferreusveritas.dynamictreesphc.compat;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.trees.Extra.SpeciesRedbud;
import com.ferreusveritas.dynamictreesphc.trees.Extra.TreeSpooky;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;


public class PamTrees {

    public static final String REDBUDTREE_MOD = "redbudtree";
    public static final String SPOOKYTREE_MOD = "spookytree";

    public static LeavesProperties redbudLeavesProperties;//, redbudPinkLeavesProperties;
    public static LeavesProperties spookyLeavesProperties;

    public static BlockDynamicLeaves leavesRedbud, leavesSpooky;

    public static TreeFamily treeSpooky;
    public static Species speciesRedbud;

    public static void treePreInit (){
        if (Loader.isModLoaded(REDBUDTREE_MOD)){
            TreeFamily oak = TreeRegistry.findSpeciesSloppy("oak").getFamily();
            speciesRedbud = new SpeciesRedbud(oak);
            speciesRedbud.getLeavesProperties().setTree(oak);
            Species.REGISTRY.register(speciesRedbud);
        }

        if (Loader.isModLoaded(SPOOKYTREE_MOD)){
            treeSpooky = new TreeSpooky();
            treeSpooky.registerSpecies(Species.REGISTRY);
        }
    }

    public static void blockPreInit (){

        if (Loader.isModLoaded(REDBUDTREE_MOD)){
            redbudLeavesProperties = new LeavesProperties(Blocks.AIR.getDefaultState()){
                @Override
                public IBlockState getPrimitiveLeaves() {
                    return Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(REDBUDTREE_MOD, "redbudtree_leaves"))).getDefaultState();
                }
                @Override
                public ItemStack getPrimitiveLeavesItemStack() {
                    return new ItemStack(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(REDBUDTREE_MOD, "redbudtree_leaves"))));
                }
            };
            leavesRedbud = new BlockDynamicLeaves();
            leavesRedbud.setDefaultNaming(ModConstants.MODID, "leaves_redbud");
            redbudLeavesProperties.setDynamicLeavesState(leavesRedbud.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
            leavesRedbud.setProperties(0, redbudLeavesProperties);
            leavesRedbud.setProperties(1, redbudLeavesProperties);
        }
        if (Loader.isModLoaded(SPOOKYTREE_MOD)){
            spookyLeavesProperties = new LeavesProperties(Blocks.AIR.getDefaultState()){
                @Override
                public IBlockState getPrimitiveLeaves() {
                    return Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(SPOOKYTREE_MOD, "spookytree_leaves"))).getDefaultState();
                }
                @Override
                public ItemStack getPrimitiveLeavesItemStack() {
                    return new ItemStack(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(SPOOKYTREE_MOD, "spookytree_leaves"))));
                }
            };
            leavesSpooky = new BlockDynamicLeaves();
            leavesSpooky.setDefaultNaming(ModConstants.MODID, "leaves_spooky");
            spookyLeavesProperties.setDynamicLeavesState(leavesSpooky.getDefaultState());
            leavesSpooky.setProperties(0, spookyLeavesProperties);
        }

    }
    public static void blockRegister(IForgeRegistry<Block> registry) {
        ArrayList<Block> treeBlocks = new ArrayList<>();
        if (Loader.isModLoaded(REDBUDTREE_MOD)){
            treeBlocks.add(leavesRedbud);
        }
        if (Loader.isModLoaded(SPOOKYTREE_MOD)){
            treeSpooky.getRegisterableBlocks(treeBlocks);
            treeBlocks.add(leavesSpooky);
        }

        registry.registerAll(treeBlocks.toArray(new Block[0]));
    }

    public static void itemRegister(IForgeRegistry<Item> registry) {
        ArrayList<Item> treeItems = new ArrayList<>();

        if (Loader.isModLoaded(REDBUDTREE_MOD)){
            treeItems.add(speciesRedbud.getSeed());
        }
        if (Loader.isModLoaded(SPOOKYTREE_MOD)){
            treeSpooky.getRegisterableItems(treeItems);
        }

        registry.registerAll(treeItems.toArray(new Item[0]));
    }

    public static void recipeRegister(IForgeRegistry<IRecipe> registry) {

        if (Loader.isModLoaded(REDBUDTREE_MOD)){
            Species redbudSpecies = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "redbud"));
            Block redbudSapling = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(REDBUDTREE_MOD, "redbudtree_sapling"));
            com.ferreusveritas.dynamictrees.ModRecipes.createDirtBucketExchangeRecipes(new ItemStack(redbudSapling), redbudSpecies.getSeedStack(1), true, "seedfromsapling");
        }
        if (Loader.isModLoaded(SPOOKYTREE_MOD)){
            Species spookySpecies = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "spooky"));
            Block appleSapling = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(SPOOKYTREE_MOD, "spookytree_sapling"));
            com.ferreusveritas.dynamictrees.ModRecipes.createDirtBucketExchangeRecipes(new ItemStack(appleSapling), spookySpecies.getSeedStack(1), true, "seedfromsapling");
        }


    }

    public static void modelRegister(ModelRegistryEvent event) {
        if (Loader.isModLoaded(REDBUDTREE_MOD)){
            ModelHelper.regModel(speciesRedbud.getSeed());
        }
        if (Loader.isModLoaded(SPOOKYTREE_MOD)){
            ModelHelper.regModel(treeSpooky.getDynamicBranch());
            ModelHelper.regModel(treeSpooky.getCommonSpecies().getSeed());
            ModelHelper.regModel(treeSpooky);
        }
    }

}
