package info.tritusk.electrothaumaturgy.module.tools.misc;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.items.IScribeTools;

public class ElectricScribingTools extends Item implements IElectricItem, ISpecialElectricItem, IScribeTools {

    @Override
    public void setDamage(ItemStack stack, int damage) {
        // TODO We gonna have to figure out how to trick Thaumcraft to consume electricity, not item durability
        // TODO Probably, we need to file an issue ticket, if we really want a nice and clean implementation
        int disChargeAmount = 100 * (damage - this.getDamage(stack));
        if (disChargeAmount > 0) {
            ElectricItem.manager.discharge(stack, disChargeAmount, 1, false, false, false);
        }
        super.setDamage(stack, damage);
    }

    @Override
    public int getDamage(ItemStack item) {
        return 100 - (int)(ElectricItem.rawManager.getCharge(item) / 100);
    }

    public int getMaxDamage(ItemStack item) {
        return 100;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public IElectricItemManager getManager(ItemStack itemStack) {
        return ElectricScribingToolManager.INSTANCE;
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
        return 50;
    }

    static final class ElectricScribingToolManager implements IElectricItemManager {

        static final ElectricScribingToolManager INSTANCE = new ElectricScribingToolManager();

        private ElectricScribingToolManager() {}

        @Override
        public double charge(ItemStack item, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
            double charged = ElectricItem.rawManager.charge(item, amount, tier, ignoreTransferLimit, simulate);
            if (charged > 0) {
                item.setItemDamage(item.getItemDamage() - (int)(charged / 100));
            }
            return charged;
        }

        @Override
        public double discharge(ItemStack item, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
            return ElectricItem.rawManager.discharge(item, amount, tier, ignoreTransferLimit, externally, simulate);
        }

        @Override
        public double getCharge(ItemStack item) {
            return ElectricItem.rawManager.getCharge(item);
        }

        @Override
        public double getMaxCharge(ItemStack item) {
            return ElectricItem.rawManager.getMaxCharge(item);
        }

        @Override
        public boolean canUse(ItemStack item, double amount) {
            return false; // No-op
        }

        @Override
        public boolean use(ItemStack item, double amount, EntityLivingBase entity) {
            return false; // No-op
        }

        @Override
        public void chargeFromArmor(ItemStack item, EntityLivingBase entity) {
            // No-op
        }

        @Override
        public String getToolTip(ItemStack item) {
            return ElectricItem.rawManager.getToolTip(item);
        }

        @Override
        public int getTier(ItemStack item) {
            return ElectricItem.rawManager.getTier(item);
        }
    }
}
