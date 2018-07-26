package info.tritusk.electrothaumaturgy.module.tools.elemental;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import info.tritusk.electrothaumaturgy.internal.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class ChainsawOfStream extends AbstractElectricElementalTool implements IElectricItem {

    public ChainsawOfStream() {
        super(6F, -3F, ThaumcraftMaterials.TOOLMAT_ELEMENTAL, Collections.emptySet());
        setHarvestLevel("axe", 4);
        setHarvestLevel("sword", 4);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        Material material = state.getMaterial();
        return (material == Material.WOOD || material == Material.LEAVES || material == Material.WEB) && ElectricItem.manager.canUse(stack, 100);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return ElectricItem.manager.canUse(stack, 100) ? this.canHarvestBlock(state, stack) ? this.efficiency : super.getDestroySpeed(stack, state) : 1F;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        ElectricItem.manager.use(stack, 100, attacker);
        return true;
    }

    // TODO Handler of Burrowing enchantment is not calling this method, causing chainsaw does not consume electricity when breaking stuff
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase userEntity) {
        if (!worldIn.isRemote && state.getBlockHardness(worldIn, pos) != 0F) {
            if (userEntity != null) {
                ElectricItem.manager.use(stack, 100, userEntity);
            } else {
                ElectricItem.manager.discharge(stack, 100, 2, false, false, false);
            }
        }
        return true;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack item, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if (player.world.isRemote) {
            return false;
        }

        if (target instanceof IShearable) {
            IShearable shearableTarget = (IShearable)target;
            BlockPos pos = target.getPosition();
            if (shearableTarget.isShearable(item, target.world, pos)) {
                List<ItemStack> result = shearableTarget.onSheared(item, target.world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, item));
                Random rand = player.getRNG();
                for (ItemStack drop : result) {
                    EntityItem droppedItem = target.entityDropItem(drop, 0.5F);
                    if (droppedItem == null) {
                        continue;
                    }
                    droppedItem.motionX = (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                    droppedItem.motionY = -1.0;
                    droppedItem.motionZ = (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                }
                ElectricItem.manager.use(item, 100, player);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack item, BlockPos pos, EntityPlayer player) {
        if (player.world.isRemote) {
            return false;
        }

        World world = player.world;
        Block target = world.getBlockState(pos).getBlock();
        if (target instanceof IShearable) {
            IShearable shearableTarget = (IShearable)target;
            if (shearableTarget.isShearable(item, world, pos)) {
                List<ItemStack> result = shearableTarget.onSheared(item, world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, item));
                Random rand = player.getRNG();
                for (ItemStack drop : result) {
                    ItemUtil.dropItem(world, pos, drop);
                }
                ElectricItem.manager.use(item, 100, player);
                player.addStat(StatList.getBlockStats(target));
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 8 | 2 | 1);
            }
            return true;
        }
        return false;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return 30000;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return 1;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return 32;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack zeroCharge = new ItemStack(this, 1, 0, null);
            EnumInfusionEnchantment.addInfusionEnchantment(zeroCharge, EnumInfusionEnchantment.COLLECTOR, 1);
            EnumInfusionEnchantment.addInfusionEnchantment(zeroCharge, EnumInfusionEnchantment.BURROWING, 1);
            items.add(zeroCharge);

            ItemStack fullyCharged = zeroCharge.copy();
            ElectricItem.manager.charge(fullyCharged, Double.MAX_VALUE, Integer.MAX_VALUE, true, false);
            items.add(fullyCharged);
        }
    }
}
