package info.tritusk.electrothaumaturgy.module.tools.elemental;

import ic2.api.item.IElectricItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import thaumcraft.api.ThaumcraftMaterials;

import java.util.Collections;

public final class ElectricHoeOfGrowth extends ItemTool implements IElectricItem {

    public ElectricHoeOfGrowth() {
        super(1F, 4F, ThaumcraftMaterials.TOOLMAT_ELEMENTAL, Collections.emptySet());
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return 1;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return 32;
    }

}
