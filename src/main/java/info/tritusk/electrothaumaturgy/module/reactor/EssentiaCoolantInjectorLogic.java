package info.tritusk.electrothaumaturgy.module.reactor;

import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric;
import info.tritusk.electrothaumaturgy.ElectricDeviceLogicBase;
import info.tritusk.electrothaumaturgy.ElectroThaumoObjects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;

public final class EssentiaCoolantInjectorLogic extends ElectricDeviceLogicBase
        implements ITickable, IEnergySink, IAspectContainer {

    private static final int MAX_ENERGY = 32000, MAX_ESSENTIA = 1000;

    private int essentiaAmount = 0;

    // TODO IReactor lacks the method to give either: 1. its x-dimension and y-dimension 2. a view-only List<ItemStack> or equivalents that represents its contents
    private TileEntityNuclearReactorElectric attachedReactor;

    private int energy = 0;

    @Override
    public void update() {
        if (this.getWorld().isRemote) {
            return;
        }

        if (this.attachedReactor != null) {
            for (ItemStack reactorComponent : attachedReactor.reactorSlot) {
                if (essentiaAmount < 1) {
                    break;
                }
                if (reactorComponent.getItem() == ElectroThaumoObjects.FROST_CONDENSATOR) {
                    AspectList componentStorage = ((FrostCondensator)reactorComponent.getItem()).getAspects(reactorComponent);
                    if (componentStorage == null) {
                        componentStorage = new AspectList();
                    }
                    int toInject = Math.max(FrostCondensator.MAX_ESSENTIA_AMOUNT, FrostCondensator.MAX_ESSENTIA_AMOUNT - componentStorage.getAmount(Aspect.COLD));
                    toInject = Math.min(this.essentiaAmount, toInject);
                    this.essentiaAmount -= toInject;
                    componentStorage.add(Aspect.COLD, toInject);
                    ((FrostCondensator)reactorComponent.getItem()).setAspects(reactorComponent, componentStorage);
                }
            }
        }
    }

    @Override
    public double getDemandedEnergy() {
        return MAX_ENERGY - energy;
    }

    @Override
    public int getSinkTier() {
        return 2;
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
        return new AspectList().add(Aspect.COLD, essentiaAmount);
    }

    @Override
    public void setAspects(AspectList aspectList) {

    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return aspect == Aspect.COLD;
    }

    @Override
    public int addToContainer(Aspect aspect, int increment) {
        if (aspect != Aspect.COLD) {
            return increment;
        }

        this.essentiaAmount += increment;
        int diff = Math.max(0, essentiaAmount - MAX_ESSENTIA);
        if (diff > 0) {
            this.essentiaAmount = MAX_ESSENTIA;
        }
        return diff > 0 ? diff : 0;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int amount) {
        // You cannot take essentia out of this container.
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int i) {
        return aspect == Aspect.COLD && essentiaAmount >= i;
    }

    @Override
    public int containerContains(Aspect aspect) {
        return aspect == Aspect.COLD ? essentiaAmount : 0;
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

}
