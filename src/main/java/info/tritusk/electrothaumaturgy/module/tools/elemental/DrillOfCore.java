package info.tritusk.electrothaumaturgy.module.tools.elemental;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

import java.util.Collections;

public final class DrillOfCore extends AbstractElectricElementalTool implements IElectricItem {

    public DrillOfCore() {
        super(1.0F, -3F, ThaumcraftMaterials.TOOLMAT_ELEMENTAL, Collections.emptySet());
        setHarvestLevel("pickaxe", 4);
        setHarvestLevel("shovel", 4);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        Material material = state.getMaterial();
        return (material == Material.ROCK || material == Material.IRON || material == Material.ANVIL || material == Material.SNOW || super.canHarvestBlock(state, stack)) && ElectricItem.manager.canUse(stack, 100);
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
    public double getMaxCharge(ItemStack itemStack) {
        return 30000;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return 2;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return 128;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack zeroCharge = new ItemStack(this, 1, 0, null);
            EnumInfusionEnchantment.addInfusionEnchantment(zeroCharge, EnumInfusionEnchantment.REFINING, 3);
            EnumInfusionEnchantment.addInfusionEnchantment(zeroCharge, EnumInfusionEnchantment.SOUNDING, 1);
            items.add(zeroCharge);

            ItemStack fullyCharged = zeroCharge.copy();
            ElectricItem.manager.charge(fullyCharged, Double.MAX_VALUE, Integer.MAX_VALUE, true, false);
            items.add(fullyCharged);
        }
    }
}
