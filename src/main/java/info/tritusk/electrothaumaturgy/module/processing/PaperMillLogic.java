package info.tritusk.electrothaumaturgy.module.processing;

import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import info.tritusk.electrothaumaturgy.internal.SingleSlotItemHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;

import javax.annotation.Nullable;

public class PaperMillLogic extends TileEntity implements ITickable, IEnergySink, IAspectContainer, IEssentiaTransport {

    private static final int MAX_ENERGY = 8000, MAX_ESSENTIA = 500;

    private int energy, essentia;
    private SingleSlotItemHandler invnetory = new SingleSlotItemHandler();

    @Override
    public void update() {
        if (this.getWorld().isRemote) {
            return;
        }

        if (essentia >= 8 && energy >= 200) {
            essentia -= 8;
            energy -= 200;
            ItemStack currentContent = invnetory.getRawContent();
            if (currentContent.isEmpty()) {
                currentContent = new ItemStack(Items.PAPER);
            } else if (currentContent.getItem() == Items.PAPER) {
                currentContent.grow(1);
            }
            invnetory.setRawContent(currentContent);
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.invnetory);
        } else {
            return super.getCapability(capability, facing);
        }
    }

    @Override
    public double getDemandedEnergy() {
        return MAX_ENERGY - energy;
    }

    @Override
    public int getSinkTier() {
        return 1;
    }

    @Override
    public double injectEnergy(EnumFacing facing, double amount, double voltage) {
        this.energy = (int)Math.min(MAX_ENERGY, this.energy + amount);
        return 0;
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing facing) {
        return true;
    }

    @Override
    public AspectList getAspects() {
        return this.essentia == 0 ? null : new AspectList().add(Aspect.PLANT, essentia);
    }

    @Override
    public void setAspects(AspectList aspectList) {
        // TODO
    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return aspect == Aspect.PLANT;
    }

    @Override
    public int addToContainer(Aspect aspect, int increment) {
        if (aspect != Aspect.PLANT) {
            return increment;
        }

        this.essentia += increment;
        int diff = Math.max(0, essentia - MAX_ESSENTIA);
        if (diff > 0) {
            this.essentia = MAX_ESSENTIA;
        }
        return diff > 0 ? diff : 0;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int decrement) {
        // No taking out
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int amount) {
        return aspect == Aspect.PLANT && this.essentia >= amount;
    }

    @Override
    public int containerContains(Aspect aspect) {
        return aspect == Aspect.PLANT ? this.essentia : 0;
    }

    @Deprecated
    @Override
    public boolean takeFromContainer(AspectList aspectList) {
        return false;
    }

    @Deprecated
    @Override
    public boolean doesContainerContain(AspectList aspectList) {
        return false;
    }

    @Override
    public boolean isConnectable(EnumFacing facing) {
        return true;
    }

    @Override
    public boolean canInputFrom(EnumFacing facing) {
        // Input is always allowed
        return true;
    }

    @Override
    public boolean canOutputTo(EnumFacing facing) {
        // No output
        return false;
    }

    @Override
    public void setSuction(Aspect aspect, int i) {
        // No-op - fixed suction
    }

    @Override
    public Aspect getSuctionType(EnumFacing facing) {
        return Aspect.PLANT;
    }

    @Override
    public int getSuctionAmount(EnumFacing facing) {
        return 512;
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, EnumFacing facing) {
        // No essentia will be taken out of it
        return 0;
    }

    @Override
    public int addEssentia(Aspect aspect, int amount, EnumFacing facing) {
        return amount - this.addToContainer(aspect, amount);
    }

    @Override
    public Aspect getEssentiaType(EnumFacing facing) {
        return this.essentia > 0 ? Aspect.PLANT : null;
    }

    @Override
    public int getEssentiaAmount(EnumFacing facing) {
        return this.essentia;
    }

    @Override
    public int getMinimumSuction() {
        return 0;
    }
}
