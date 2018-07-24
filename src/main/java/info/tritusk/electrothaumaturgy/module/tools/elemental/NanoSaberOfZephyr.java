package info.tritusk.electrothaumaturgy.module.tools.elemental;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

import java.util.Collections;

public class NanoSaberOfZephyr extends AbstractElectricElementalTool implements IElectricItem {

    public NanoSaberOfZephyr() {
        super(6F, 1.5F, ThaumcraftMaterials.TOOLMAT_ELEMENTAL, Collections.emptySet());
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> attributes = HashMultimap.create();

        if (slot == EntityEquipmentSlot.MAINHAND) {
            if (ElectricItem.manager.canUse(stack, 32)) {
                attributes.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 24F, 0));
            } else {
                attributes.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) this.attackDamage, 0));
            }
            attributes.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", attackSpeed, 0));
        }

        return attributes;
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return 160000;
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
            EnumInfusionEnchantment.addInfusionEnchantment(item, EnumInfusionEnchantment.ARCING, 2);
            items.add(item);
        }
    }
}
