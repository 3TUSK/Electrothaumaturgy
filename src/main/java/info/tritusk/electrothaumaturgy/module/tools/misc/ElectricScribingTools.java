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
        return 32;
    }

    static final class ElectricScribingToolManager implements IElectricItemManager {

        static final ElectricScribingToolManager INSTANCE = new ElectricScribingToolManager();

        private ElectricScribingToolManager() {}

        @Override
        public double charge(ItemStack itemStack, double v, int i, boolean b, boolean b1) {
            // TODO Also mutate item damage
            return 0;
        }

        @Override
        public double discharge(ItemStack itemStack, double v, int i, boolean b, boolean b1, boolean b2) {
            return ElectricItem.rawManager.discharge(itemStack, v, i, b, b1, b2);
        }

        @Override
        public double getCharge(ItemStack itemStack) {
            return ElectricItem.rawManager.getCharge(itemStack);
        }

        @Override
        public double getMaxCharge(ItemStack itemStack) {
            return ElectricItem.rawManager.getMaxCharge(itemStack);
        }

        @Override
        public boolean canUse(ItemStack itemStack, double v) {
            return false; // No-op
        }

        @Override
        public boolean use(ItemStack itemStack, double v, EntityLivingBase entityLivingBase) {
            return false; // No-op
        }

        @Override
        public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entityLivingBase) {
            // No-op
        }

        @Override
        public String getToolTip(ItemStack itemStack) {
            return ElectricItem.rawManager.getToolTip(itemStack);
        }

        @Override
        public int getTier(ItemStack itemStack) {
            return ElectricItem.rawManager.getTier(itemStack);
        }
    }
}
