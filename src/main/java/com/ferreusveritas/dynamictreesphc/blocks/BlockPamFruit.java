package com.ferreusveritas.dynamictreesphc.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictreesphc.ModConstants;
import com.ferreusveritas.dynamictreesphc.ModSounds;
import com.google.common.collect.Lists;
import com.pam.harvestcraft.blocks.FruitRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockPamFruit extends BlockFruit {

    public static final DamageSource FALLING_COCONUT = new DamageSource("fallingFruitCoconut");
    public static final DamageSource FALLING_DURIAN = new DamageSource("fallingFruitDurian");
    public static final DamageSource FALLING_JACKFRUIT = new DamageSource("fallingFruitJackfruit");
    public static final DamageSource FALLING_BREADFRUIT = new DamageSource("fallingFruitBreadfruit");

    protected final String fruitName;

    public static double randomFruitFallChance = 0.005D;
    private static final float distanceFromPlayerToFall = 10;

    public BlockPamFruit (ResourceLocation name){
        super(new ResourceLocation(name.getResourceDomain(), "fruit"+name.getResourcePath()).toString());
        this.fruitName = name.getResourcePath();
    }

    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        if (state.getValue(AGE) >= 3) {
            ItemStack toDrop = this.getFruitDrop();
            if (fruitName.equals(FruitRegistry.BANANA) && world instanceof World) {
                toDrop.setCount(1 + ((World)world).rand.nextInt(4));
            }
            if (!toDrop.isEmpty()) {
                drops.add(toDrop);
            }
        }
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        if (fruitName.equals(FruitRegistry.DRAGONFRUIT) || fruitName.equals(FruitRegistry.BANANA)){
            return world.getBlockState(pos.up()).getBlock() instanceof BlockLeaves || world.getBlockState(pos.up(2)).getBlock() instanceof BlockLeaves;
        }
        return super.canBlockStay(world,pos,state);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (ModConstants.FALLINGFRUIT.contains(fruitName) && state.getValue(AGE) == 3){
            if (!this.canBlockStay(worldIn, pos, state)) {
                this.blockFall(worldIn, pos, state);
            }
        } else {
            super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (ModConstants.FALLINGFRUIT.contains(fruitName) && state.getValue(AGE) == 3 && worldIn.rand.nextFloat() <= randomFruitFallChance)
            this.blockFall(worldIn, pos, state);
        else
            super.updateTick(worldIn, pos, state, rand);
    }

    private void blockFall(World worldIn, BlockPos pos, IBlockState state) {
        if (worldIn.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), distanceFromPlayerToFall, false) == null){
            return;
        }
        if ((worldIn.isAirBlock(pos.down()) || BlockFalling.canFallThrough(worldIn.getBlockState(pos.down()))) && pos.getY() >= 0)
        {
            if (worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32)))
            {
                if (!worldIn.isRemote)
                {
                    EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldIn, (double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, state){
                        public void onUpdate() {
                            Block block = state.getBlock();
                            if (state.getMaterial() == Material.AIR) { this.setDead(); }
                            else
                            {
                                this.prevPosX = this.posX;
                                this.prevPosY = this.posY;
                                this.prevPosZ = this.posZ;
                                if (this.fallTime++ == 0) {
                                    BlockPos blockpos = new BlockPos(this);
                                    if (this.world.getBlockState(blockpos).getBlock() == block)
                                    {
                                        this.world.setBlockToAir(blockpos);
                                    }
                                    else if (!this.world.isRemote)
                                    {
                                        this.setDead();
                                        return;
                                    }
                                }
                                if (!this.hasNoGravity())
                                {
                                    this.motionY -= 0.04D;
                                }
                                this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                                if (!this.world.isRemote) {
                                    BlockPos blockpos1 = new BlockPos(this);
                                    if (!this.onGround) {
                                        if (this.fallTime > 100 && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.fallTime > 600) {
                                            if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops")) {
                                                this.entityDropItem(new ItemStack(block, 1, block.damageDropped(state)), 0.0F);
                                            }
                                            this.setDead();
                                        }
                                    } else {
                                        if (this.world.isAirBlock(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ))) //Forge: Don't indent below.
                                            if (BlockFalling.canFallThrough(this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ)))) {
                                                this.onGround = false;
                                                return;
                                            }
                                        this.motionX *= 0.67D;
                                        this.motionZ *= 0.67D;
                                        this.motionY *= -0.5D;

                                        this.entityDropItem(((BlockPamFruit)block).droppedFruit, 0.0F);
                                        this.setDead();
                                        return;
                                    }
                                }
                                this.motionX *= 0.98D;
                                this.motionY *= 0.98D;
                                this.motionZ *= 0.98D;
                            }
                        }

                        public void fall(float distance, float damageMultiplier)
                        {
                            int i = MathHelper.ceil(distance - 1.0F);
                            if (i > 0)
                            {
                                DamageSource source;
                                switch (fruitName){
                                    case FruitRegistry.COCONUT:
                                        source = FALLING_COCONUT;
                                        break;
                                    case FruitRegistry.DURIAN:
                                        source = FALLING_DURIAN;
                                        break;
                                    case FruitRegistry.JACKFRUIT:
                                        source = FALLING_JACKFRUIT;
                                        break;
                                    case FruitRegistry.BREADFRUIT:
                                        source = FALLING_BREADFRUIT;
                                        break;
                                    default:
                                        source = DamageSource.FALLING_BLOCK;
                                }

                                List<Entity> list = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
                                for (Entity entity : list)
                                {
                                    if (entity instanceof EntityLivingBase){
                                        entity.attackEntityFrom(source, (float)Math.min(MathHelper.floor((float)i * 1.2), 10));
                                        entity.getEntityWorld().playSound(null, entity.posX, entity.posY, entity.posZ, ModSounds.BLOCK_FRUIT_BONK, SoundCategory.BLOCKS, 1, 1);
                                    }
                                }
                            }
                        }


                    };
                    worldIn.spawnEntity(entityfallingblock);
                }
            }
        }
    }

    @Override @SideOnly(Side.CLIENT) public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess access, BlockPos pos) {
        switch (fruitName){
            case FruitRegistry.ALMOND:
            case FruitRegistry.STARFRUIT:
            case FruitRegistry.LEMON:
            case FruitRegistry.LIME:
            case FruitRegistry.FIG:
            case FruitRegistry.PAWPAW:
                return ModConstants.fruitBoxes.FRUIT_THIN[state.getValue(AGE)];
            case FruitRegistry.APRICOT:
            case FruitRegistry.GUAVA:
            case FruitRegistry.POMEGRANATE:
            case FruitRegistry.PERSIMMON:
                return ModConstants.fruitBoxes.FRUIT_SMALL[state.getValue(AGE)];
            case FruitRegistry.AVOCADO:
            case FruitRegistry.PEAR:
                return ModConstants.fruitBoxes.FRUIT_LONG[state.getValue(AGE)];
            case FruitRegistry.GOOSEBERRY:
            case FruitRegistry.LYCHEE:
            case FruitRegistry.OLIVE:
                return ModConstants.fruitBoxes.FRUIT_BERRY[state.getValue(AGE)];

            case FruitRegistry.BANANA:
                return ModConstants.fruitBoxes.BANANA[state.getValue(AGE)];
            case FruitRegistry.CASHEW:
                return ModConstants.fruitBoxes.CASHEW[state.getValue(AGE)];
            case FruitRegistry.BREADFRUIT:
                return ModConstants.fruitBoxes.BREADFRUIT[state.getValue(AGE)];
            case FruitRegistry.DRAGONFRUIT:
                return ModConstants.fruitBoxes.DRAGONFRUIT[state.getValue(AGE)];
            case FruitRegistry.DURIAN:
                return ModConstants.fruitBoxes.DURIAN[state.getValue(AGE)];
            case FruitRegistry.GRAPEFRUIT:
                return ModConstants.fruitBoxes.GRAPEFRUIT[state.getValue(AGE)];
            case FruitRegistry.HAZELNUT:
                return ModConstants.fruitBoxes.HAZELNUT[state.getValue(AGE)];
            case FruitRegistry.JACKFRUIT:
                return ModConstants.fruitBoxes.JACKFRUIT[state.getValue(AGE)];
            case FruitRegistry.PEPPERCORN:
                return ModConstants.fruitBoxes.PEPPERCORN[state.getValue(AGE)];
            case FruitRegistry.MANGO:
                return ModConstants.fruitBoxes.MANGO[state.getValue(AGE)];
            case FruitRegistry.CHERRY:
                return ModConstants.fruitBoxes.CHERRY[state.getValue(AGE)];
            case FruitRegistry.PISTACHIO:
                return ModConstants.fruitBoxes.PISTACHIO[state.getValue(AGE)];
            case FruitRegistry.RAMBUTAN:
                return ModConstants.fruitBoxes.RAMBUTAN[state.getValue(AGE)];
            case FruitRegistry.SOURSOP:
                return ModConstants.fruitBoxes.SOURSOP[state.getValue(AGE)];
            case FruitRegistry.SPIDERWEB:
                return ModConstants.fruitBoxes.SPIDERWEB[state.getValue(AGE)];
            case FruitRegistry.TAMARIND:
                return ModConstants.fruitBoxes.TAMARIND[state.getValue(AGE)];
            case FruitRegistry.VANILLABEAN:
                return ModConstants.fruitBoxes.VANILLABEAN[state.getValue(AGE)];
            default:
                return super.getBoundingBox(state, access, pos);
        }
    }
}
