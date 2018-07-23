package info.tritusk.electrothaumaturgy.module.tools.elemental;

import ic2.api.item.IElectricItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.NonNullList;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

import java.util.Collections;

public final class DrillOfCore extends ItemTool implements IElectricItem {
    public DrillOfCore() {
        super(6.0F, 1.0F, ThaumcraftMaterials.TOOLMAT_ELEMENTAL, Collections.emptySet());
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
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
            ItemStack item = new ItemStack(this, 1, 0, null);
            EnumInfusionEnchantment.addInfusionEnchantment(item, EnumInfusionEnchantment.REFINING, 3);
            EnumInfusionEnchantment.addInfusionEnchantment(item, EnumInfusionEnchantment.SOUNDING, 1);
            items.add(item);
        }
    }
}
