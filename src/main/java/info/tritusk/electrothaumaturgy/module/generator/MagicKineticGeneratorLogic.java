package info.tritusk.electrothaumaturgy.module.generator;

import ic2.api.energy.tile.IKineticSource;
import info.tritusk.electrothaumaturgy.internal.Directions;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;

public class MagicKineticGeneratorLogic extends TileEntity implements ITickable, IAspectContainer, IEssentiaTransport, IKineticSource {

    private static final int MAX_MOTUS = 200, MAX_MACHINA = 100;

    private int motusAmount, machinaAmount;

    private int rotateTimeRemain = 0;

    @Override
    public void update() {
        if (this.getWorld().isRemote) {
            return;
        }

        if (rotateTimeRemain > 0) {
            rotateTimeRemain--;
        } else if (motusAmount >= 20 && machinaAmount >= 10) {
            motusAmount -= 20;
            machinaAmount -= 10;
            rotateTimeRemain += 200;
        }

        for (EnumFacing direction : EnumFacing.HORIZONTALS) {
            TileEntity tile = this.getWorld().getTileEntity(this.getPos().offset(direction));
            if (tile instanceof IEssentiaTransport) {
                IEssentiaTransport channel = (IEssentiaTransport) tile;
                EnumFacing opposite = direction.getOpposite();
                if (channel.canOutputTo(opposite)
                        && channel.getSuctionType(opposite) == Aspect.MOTION
                        && channel.getSuctionAmount(opposite) < this.getSuctionAmount(direction)
                        && this.getSuctionAmount(direction) >= channel.getMinimumSuction()) {
                    this.addToContainer(Aspect.MOTION, channel.takeEssentia(Aspect.MOTION, 10, opposite));
                    if (motusAmount >= MAX_MOTUS) {
                        break;
                    }
                }
            }
        }

        for (EnumFacing direction : Directions.VERTICALS) {
            TileEntity tile = this.getWorld().getTileEntity(this.getPos().offset(direction));
            if (tile instanceof IEssentiaTransport) {
                IEssentiaTransport channel = (IEssentiaTransport) tile;
                EnumFacing opposite = direction.getOpposite();
                if (channel.canOutputTo(opposite)
                        && channel.getSuctionType(opposite) == Aspect.MECHANISM
                        && channel.getSuctionAmount(opposite) < this.getSuctionAmount(direction)
                        && this.getSuctionAmount(direction) >= channel.getMinimumSuction()) {
                    this.addToContainer(Aspect.MECHANISM, channel.takeEssentia(Aspect.MECHANISM, 10, opposite));
                    if (machinaAmount >= MAX_MACHINA) {
                        break;
                    }
                }
            }
        }
    }

    @Deprecated
    @Override
    public int maxrequestkineticenergyTick(EnumFacing side) {
        return getConnectionBandwidth(side);
    }

    @Deprecated
    @Override
    public int requestkineticenergy(EnumFacing side, int amount) {
        return 0;
    }

    @Override
    public int getConnectionBandwidth(EnumFacing side) {
        return 32;
    }

    @Override
    public int drawKineticEnergy(EnumFacing side, int request, boolean simulate) {
        return rotateTimeRemain > 0 ? 32 : 0;
    }

    @Override
    public AspectList getAspects() {
        AspectList list = new AspectList();
        if (motusAmount > 0) {
            list.add(Aspect.MOTION, motusAmount);
        }
        if (machinaAmount > 0) {
            list.add(Aspect.MECHANISM, machinaAmount);
        }
        return list;
    }

    @Override
    public void setAspects(AspectList aspectList) {
        // TODO
    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return aspect == Aspect.MOTION || aspect == Aspect.MECHANISM;
    }

    @Override
    public int addToContainer(Aspect aspect, int increment) {
        if (aspect == Aspect.MOTION) {
            this.motusAmount += increment;
            int diff = Math.max(0, motusAmount - MAX_MOTUS);
            if (diff > 0) {
                this.motusAmount = MAX_MOTUS;
            }
            return diff > 0 ? diff : 0;
        } else if (aspect == Aspect.MECHANISM) {
            this.machinaAmount += increment;
            int diff = Math.max(0, machinaAmount - MAX_MACHINA);
            if (diff > 0) {
                this.machinaAmount = MAX_MACHINA;
            }
            return diff > 0 ? diff : 0;
        } else {
            return increment;
        }
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int decrement) {
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int amount) {
        if (aspect == Aspect.MOTION) {
            return motusAmount >= amount;
        } else if (aspect == Aspect.MECHANISM) {
            return machinaAmount >= amount;
        } else {
            return false;
        }
    }

    @Override
    public int containerContains(Aspect aspect) {
        return aspect == Aspect.MOTION ? motusAmount : aspect == Aspect.MECHANISM ? machinaAmount : 0;
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
        // Allow input from any direction.
        return true;
    }

    @Override
    public boolean canOutputTo(EnumFacing facing) {
        // No essentia taken out.
        return false;
    }

    @Override
    public void setSuction(Aspect aspect, int suction) {
        // No-op - fixed suction
    }

    @Override
    public Aspect getSuctionType(EnumFacing facing) {
        return facing.getAxis() == EnumFacing.Axis.Y ? Aspect.MECHANISM : Aspect.MOTION;
    }

    @Override
    public int getSuctionAmount(EnumFacing facing) {
        return 914; // Reference to SCP-914
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, EnumFacing facing) {
        // No essentia taken out.
        return 0;
    }

    @Override
    public int addEssentia(Aspect aspect, int amount, EnumFacing facing) {
        return aspect == Aspect.MOTION || aspect == Aspect.MECHANISM ? amount - this.addToContainer(aspect, amount) : 0;
    }

    @Override
    public Aspect getEssentiaType(EnumFacing facing) {
        return facing.getAxis() == EnumFacing.Axis.Y ? Aspect.MECHANISM : Aspect.MOTION;
    }

    @Override
    public int getEssentiaAmount(EnumFacing facing) {
        return facing.getAxis() == EnumFacing.Axis.Y ? machinaAmount : motusAmount;
    }

    @Override
    public int getMinimumSuction() {
        return 0;
    }
}
