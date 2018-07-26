package info.tritusk.electrothaumaturgy.module.tools.elemental;

import ic2.api.item.IBoxable;
import ic2.api.item.IElectricItem;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import java.util.Set;

abstract class AbstractElectricElementalTool extends ItemTool implements IElectricItem, IBoxable {

    AbstractElectricElementalTool(float attackDamage, float attackSpeed, ToolMaterial material, Set<Block> effectiveBlocks) {
        super(attackDamage, attackSpeed, material, effectiveBlocks);
        this.setNoRepair();
        this.setMaxDamage(0);
        this.setHasSubtypes(false);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean canBeStoredInToolbox(ItemStack itemStack) {
        return true;
    }
}
