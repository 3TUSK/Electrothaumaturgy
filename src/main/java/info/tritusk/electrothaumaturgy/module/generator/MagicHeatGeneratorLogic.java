package info.tritusk.electrothaumaturgy.module.generator;

import ic2.api.energy.tile.IHeatSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;

public final class MagicHeatGeneratorLogic extends TileEntity implements ITickable, IAspectContainer, IEssentiaTransport, IHeatSource {

    private static final int MAX_ESSENTIA = 200;

    private int heatTimeRemain, essentia;
    
    @Override
    public void update() {
        if (this.getWorld().isRemote) {
            return;
        }

        if (heatTimeRemain > 0) {
            heatTimeRemain--;
        } else if (essentia >= 20) {
            essentia -= 20;
            heatTimeRemain += 200;
        }

        for (EnumFacing direction : EnumFacing.HORIZONTALS) {
            TileEntity tile = this.getWorld().getTileEntity(this.getPos().offset(direction));
            if (tile instanceof IEssentiaTransport) {
                IEssentiaTransport channel = (IEssentiaTransport) tile;
                EnumFacing opposite = direction.getOpposite();
                if (channel.canOutputTo(opposite)
                        && channel.getSuctionType(opposite) == Aspect.FIRE
                        && channel.getSuctionAmount(opposite) < this.getSuctionAmount(direction)
                        && this.getSuctionAmount(direction) >= channel.getMinimumSuction()) {
                    this.addToContainer(Aspect.FIRE, channel.takeEssentia(Aspect.FIRE, 10, opposite));
                    if (essentia >= MAX_ESSENTIA) {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public int getConnectionBandwidth(EnumFacing side) {
        return 32;
    }

    @Override
    public int drawHeat(EnumFacing side, int request, boolean simulate) {
        return heatTimeRemain > 0 ? 32 : 0;
    }

    @Deprecated
    @Override
    public int maxrequestHeatTick(EnumFacing facing) {
        return getConnectionBandwidth(facing);
    }

    @Deprecated
    @Override
    public int requestHeat(EnumFacing facing, int i) {
        return 0;
    }

    @Override
    public AspectList getAspects() {
        return essentia > 0 ? new AspectList().add(Aspect.FIRE, essentia) : null;
    }

    @Override
    public void setAspects(AspectList aspectList) {
        // TODO
    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return aspect == Aspect.FIRE;
    }

    @Override
    public int addToContainer(Aspect aspect, int increment) {
        if (aspect == Aspect.FIRE) {
            this.essentia += increment;
            int diff = Math.max(0, essentia - MAX_ESSENTIA);
            if (diff > 0) {
                this.essentia = MAX_ESSENTIA;
            }
            return diff > 0 ? diff : 0;
        } else {
            return increment;
        }
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int decrement) {
        // You cannot take essentia out of this container.
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int amount) {
        return aspect == Aspect.FIRE && this.essentia >= amount;
    }

    @Override
    public int containerContains(Aspect aspect) {
        return aspect == Aspect.FIRE ? this.essentia : 0;
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
        return true;
    }

    @Override
    public boolean canOutputTo(EnumFacing facing) {
        return false;
    }

    @Override
    public void setSuction(Aspect aspect, int i) {
        // No-op
    }

    @Override
    public Aspect getSuctionType(EnumFacing facing) {
        return Aspect.FIRE;
    }

    @Override
    public int getSuctionAmount(EnumFacing facing) {
        return 943;
    }

    @Override
    public int takeEssentia(Aspect aspect, int i, EnumFacing facing) {
        // No-op
        return 0;
    }

    @Override
    public int addEssentia(Aspect aspect, int amount, EnumFacing facing) {
        return aspect == Aspect.FIRE ? amount - this.addToContainer(aspect, amount) : 0;
    }

    @Override
    public Aspect getEssentiaType(EnumFacing facing) {
        return essentia > 0 ? Aspect.FIRE : null;
    }

    @Override
    public int getEssentiaAmount(EnumFacing facing) {
        return essentia;
    }

    @Override
    public int getMinimumSuction() {
        return 0;
    }
}
