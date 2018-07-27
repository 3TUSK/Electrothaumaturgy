package info.tritusk.electrothaumaturgy.module.generator;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.MinecraftForge;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;

public final class PotentiaGeneratorLogic extends TileEntity
        implements ITickable, IEnergySource, IAspectContainer, IEssentiaTransport {

    private static final int MAX_ESSENTIA = 250;

    private int essentiaStorage;
    // TODO Do we really need a buffer? we could do some "lingering" design instead.
    private int energyBuffer;

    private boolean isInEnergyNet;

    @Override
    public void update() {
        if (this.getWorld().isRemote) {
            return;
        }

        if (essentiaStorage >= 10) {
            essentiaStorage -= 10;
            energyBuffer += 32;
        }

        if (essentiaStorage >= MAX_ESSENTIA) {
            return;
        }

        for (EnumFacing direction : EnumFacing.VALUES) {
            TileEntity tile = this.getWorld().getTileEntity(this.getPos().offset(direction));
            if (tile instanceof IEssentiaTransport) {
                IEssentiaTransport channel = (IEssentiaTransport) tile;
                EnumFacing opposite = direction.getOpposite();
                if (channel.canOutputTo(opposite)
                        && channel.getSuctionType(opposite) == Aspect.ENERGY
                        && channel.getSuctionAmount(opposite) < this.getSuctionAmount(direction)
                        && this.getSuctionAmount(direction) >= channel.getMinimumSuction()) {
                    this.addToContainer(Aspect.ENERGY, channel.takeEssentia(Aspect.ENERGY, 10, opposite));
                    if (essentiaStorage >= MAX_ESSENTIA) {
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void invalidate() {
        if (!getWorld().isRemote && isInEnergyNet) {
            isInEnergyNet = false;
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }
        super.invalidate();
    }

    @Override
    public void onLoad() {
        if (!getWorld().isRemote && !isInEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            isInEnergyNet = true;
        }
    }

    @Override
    public double getOfferedEnergy() {
        return Math.min(energyBuffer, 32);
    }

    @Override
    public void drawEnergy(double amount) {
        this.energyBuffer -= amount;
        if (this.energyBuffer < 0) {
            this.energyBuffer = 0;
        }
    }

    @Override
    public int getSourceTier() {
        return 1;
    }

    @Override
    public boolean emitsEnergyTo(IEnergyAcceptor acceptor, EnumFacing facing) {
        return true;
    }

    @Override
    public AspectList getAspects() {
        if (this.essentiaStorage < 1) {
            return null;
        }
        return new AspectList().add(Aspect.ENERGY, this.essentiaStorage);
    }

    @Override
    public void setAspects(AspectList aspectList) {
        // TODO
    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return aspect == Aspect.ENERGY;
    }

    @Override
    public int addToContainer(Aspect aspect, int increment) {
        if (aspect != Aspect.ENERGY) {
            return increment;
        }

        this.essentiaStorage += increment;
        int diff = Math.max(0, essentiaStorage - MAX_ESSENTIA);
        if (diff > 0) {
            this.essentiaStorage = MAX_ESSENTIA;
        }
        return diff > 0 ? diff : 0;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int decrement) {
        // You cannot take essentia out of this container.
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int amount) {
        return aspect == Aspect.ENERGY && this.essentiaStorage >= amount;
    }

    @Override
    public int containerContains(Aspect aspect) {
        return aspect == Aspect.ENERGY ? this.essentiaStorage : 0;
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
        // Input from any direction
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
        return Aspect.ENERGY;
    }

    @Override
    public int getSuctionAmount(EnumFacing facing) {
        return 943;
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, EnumFacing facing) {
        // No essentia will be taken out of it
        return 0;
    }

    @Override
    public int addEssentia(Aspect aspect, int amount, EnumFacing facing) {
        return aspect == Aspect.ENERGY ? amount - this.addToContainer(aspect, amount) : 0;
    }

    @Override
    public Aspect getEssentiaType(EnumFacing facing) {
        return this.essentiaStorage == 0 ? null : Aspect.ENERGY;
    }

    @Override
    public int getEssentiaAmount(EnumFacing facing) {
        return this.essentiaStorage;
    }

    @Override
    public int getMinimumSuction() {
        return 0;
    }
}
