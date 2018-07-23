package info.tritusk.electrothaumaturgy.module.tools.elemental;

import ic2.api.item.IElectricItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.NonNullList;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

import java.util.Collections;

public final class ChainsawOfStream extends ItemTool implements IElectricItem {

    public ChainsawOfStream() {
        super(10F, 1F, ThaumcraftMaterials.TOOLMAT_ELEMENTAL, Collections.emptySet());
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
        return 1;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return 32;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack item = new ItemStack(this, 1, 0, null);
            EnumInfusionEnchantment.addInfusionEnchantment(item, EnumInfusionEnchantment.COLLECTOR, 1);
            EnumInfusionEnchantment.addInfusionEnchantment(item, EnumInfusionEnchantment.BURROWING, 1);
            items.add(item);
        }
    }
}
