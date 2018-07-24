package info.tritusk.electrothaumaturgy.module.tools.elemental;

import ic2.api.item.IElectricItem;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import java.util.Set;

abstract class AbstractElectricElementalTool extends ItemTool implements IElectricItem {
    AbstractElectricElementalTool(float attackDamage, float attackSpeed, ToolMaterial material, Set<Block> effectiveBlocks) {
        super(attackDamage, attackSpeed, material, effectiveBlocks);
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }
}
