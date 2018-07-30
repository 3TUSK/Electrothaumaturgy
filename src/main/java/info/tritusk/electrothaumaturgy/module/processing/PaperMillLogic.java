package info.tritusk.electrothaumaturgy.module.processing;

import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import info.tritusk.electrothaumaturgy.internal.Directions;
import info.tritusk.electrothaumaturgy.internal.ElectricDeviceLogicBase;
import info.tritusk.electrothaumaturgy.internal.SingleSlotItemHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
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

public final class PaperMillLogic extends ElectricDeviceLogicBase implements ITickable, IEnergySink, IAspectContainer, IEssentiaTransport {

    private static final int MAX_ENERGY = 8000, MAX_ESSENTIA = 200, MAX_CATALYST = 200;

    private int energy, essentia, catalyst; // essentia is Herba, catalyst is Fabrico
    private SingleSlotItemHandler inventory = new SingleSlotItemHandler();

    @Override
    public void update() {
        if (this.getWorld().isRemote) {
            return;
        }

        if (essentia >= 8 && catalyst > 0 && energy >= 200) {
            essentia -= 8;
            catalyst--;
            energy -= 200;
            ItemStack currentContent = inventory.getRawContent();
            if (currentContent.isEmpty()) {
                currentContent = new ItemStack(Items.PAPER);
            } else if (currentContent.getItem() == Items.PAPER) {
                currentContent.grow(1);
            }
            inventory.setRawContent(currentContent);
        }

        boolean requireUpdate = false;

        for (EnumFacing direction : EnumFacing.HORIZONTALS) {
            if (essentia >= MAX_ESSENTIA) {
                break;
            }
            TileEntity tile = this.getWorld().getTileEntity(this.getPos().offset(direction));
            if (tile instanceof IEssentiaTransport) {
                IEssentiaTransport channel = (IEssentiaTransport) tile;
                EnumFacing opposite = direction.getOpposite();
                if (channel.canOutputTo(opposite)
                        && channel.getSuctionType(opposite) == Aspect.PLANT
                        && channel.getSuctionAmount(opposite) < this.getSuctionAmount(direction)
                        && this.getSuctionAmount(direction) >= channel.getMinimumSuction()) {
                    this.addToContainer(Aspect.PLANT, channel.takeEssentia(Aspect.PLANT, 1, opposite));
                    requireUpdate = true;
                    if (essentia >= MAX_ESSENTIA) {
                        break;
                    }
                }
            }
        }

        for (EnumFacing direction : Directions.VERTICALS) {
            if (catalyst >= MAX_CATALYST) {
                break;
            }
            TileEntity tile = this.getWorld().getTileEntity(this.getPos().offset(direction));
            if (tile instanceof IEssentiaTransport) {
                IEssentiaTransport channel = (IEssentiaTransport) tile;
                EnumFacing opposite = direction.getOpposite();
                if (channel.canOutputTo(opposite)
                        && channel.getSuctionType(opposite) == Aspect.CRAFT
                        && channel.getSuctionAmount(opposite) < this.getSuctionAmount(direction)
                        && this.getSuctionAmount(direction) >= channel.getMinimumSuction()) {
                    this.addToContainer(Aspect.CRAFT, channel.takeEssentia(Aspect.CRAFT, 1, opposite));
                    requireUpdate = true;
                    if (catalyst >= MAX_CATALYST) {
                        break;
                    }
                }
            }
        }

        if (requireUpdate) {
            this.getWorld().notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        data.setInteger("energy", energy);
        data.setInteger("essentia", essentia);
        data.setInteger("catalyst", catalyst);
        return super.writeToNBT(data);
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.energy = data.getInteger("energy");
        this.essentia = data.getInteger("essentia");
        this.catalyst = data.getInteger("catalyst");
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound data = new NBTTagCompound();
        data.setInteger("energy", energy);
        data.setInteger("essentia", essentia);
        data.setInteger("catalyst", catalyst);
        return new SPacketUpdateTileEntity(this.getPos(), 1, data);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        NBTTagCompound data = packet.getNbtCompound();
        this.energy = data.getInteger("energy");
        this.essentia = data.getInteger("essentia");
        this.catalyst = data.getInteger("catalyst");
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory);
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
        AspectList list = new AspectList();
        if (essentia > 0) {
            list.add(Aspect.PLANT, essentia);
        }
        if (catalyst > 0) {
            list.add(Aspect.CRAFT, catalyst);
        }
        return list;
    }

    @Override
    public void setAspects(AspectList aspectList) {
        // TODO
    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return aspect == Aspect.PLANT || aspect == Aspect.CRAFT;
    }

    @Override
    public int addToContainer(Aspect aspect, int increment) {
        if (aspect == Aspect.PLANT) {
            this.essentia += increment;
            int diff = Math.max(0, essentia - MAX_ESSENTIA);
            if (diff > 0) {
                this.essentia = MAX_ESSENTIA;
            }
            return diff > 0 ? diff : 0;
        } else if (aspect == Aspect.CRAFT) {
            this.catalyst += increment;
            int diff = Math.max(0, catalyst - MAX_CATALYST);
            if (diff > 0) {
                this.catalyst = MAX_CATALYST;
            }
            return diff > 0 ? diff : 0;
        } else {
            return increment;
        }
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int decrement) {
        // No taking out
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int amount) {
        if (aspect == Aspect.PLANT) {
            return essentia >= amount;
        } else if (aspect == Aspect.CRAFT) {
            return catalyst >= amount;
        } else {
            return false;
        }
    }

    @Override
    public int containerContains(Aspect aspect) {
        return aspect == Aspect.PLANT ? essentia : aspect == Aspect.CRAFT ? catalyst : 0;
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
        return facing.getAxis() == EnumFacing.Axis.Y ? Aspect.CRAFT : Aspect.PLANT;
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
        return aspect == Aspect.PLANT || aspect == Aspect.CRAFT ? amount - this.addToContainer(aspect, amount) : 0;
    }

    @Override
    public Aspect getEssentiaType(EnumFacing facing) {
        return facing.getAxis() == EnumFacing.Axis.Y ? Aspect.CRAFT : Aspect.PLANT;
    }

    @Override
    public int getEssentiaAmount(EnumFacing facing) {
        return facing.getAxis() == EnumFacing.Axis.Y ? catalyst : essentia;
    }

    @Override
    public int getMinimumSuction() {
        return 0;
    }
}
