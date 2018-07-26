package info.tritusk.electrothaumaturgy.module.reactor;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaContainerItem;

public final class FrostCondensator extends Item implements IEssentiaContainerItem, IReactorComponent {

    static final int MAX_ESSENTIA_AMOUNT = 250;

    @Override
    public void processChamber(ItemStack item, IReactor reactor, int x, int y, boolean b) {
        // No-op
    }

    @Override
    public boolean acceptUraniumPulse(ItemStack item, IReactor reactor, ItemStack pulseSource, int x, int y, int pulseX, int pulseY, boolean heatRun) {
        return false;
    }

    @Override
    public boolean canStoreHeat(ItemStack item, IReactor reactor, int x, int y) {
        return false;
    }

    @Override
    public int getMaxHeat(ItemStack item, IReactor reactor, int x, int y) {
        return 0;
    }

    @Override
    public int getCurrentHeat(ItemStack item, IReactor reactor, int x, int y) {
        return 0;
    }

    @Override
    public int alterHeat(ItemStack item, IReactor reactor, int x, int y, int heat) {
        if (heat < -273) {
            return heat;
        }

        AspectList currentAspects = new AspectList();
        NBTTagCompound data = item.getTagCompound();
        if (data == null) {
            data = new NBTTagCompound();
            item.setTagCompound(data);
        }
        currentAspects.readFromNBT(data);
        int quantityGelumEssentia = currentAspects.getAmount(Aspect.COLD);
        int tempDecrement = Math.min(heat, quantityGelumEssentia);
        heat -= tempDecrement;
        currentAspects.reduce(Aspect.COLD, tempDecrement);
        currentAspects.writeToNBT(data);
        return heat;
    }

    @Override
    public float influenceExplosion(ItemStack item, IReactor reactor) {
        return 0;
    }

    @Override
    public boolean canBePlacedIn(ItemStack itemStack, IReactor iReactor) {
        return true;
    }

    @Override
    public AspectList getAspects(ItemStack item) {
        if (item.hasTagCompound()) {
            AspectList list = new AspectList();
            list.readFromNBT(item.getTagCompound());
            return list.size() > 0 ? list : null;
        } else {
            return null;
        }
    }

    @Override
    public void setAspects(ItemStack item, AspectList aspectList) {
        int quantityGelumEssentia = aspectList.getAmount(Aspect.COLD);

        NBTTagCompound data = item.getTagCompound();
        if (data == null) {
            data = new NBTTagCompound();
            item.setTagCompound(data);
        }
        AspectList newList = new AspectList();
        newList.add(Aspect.COLD, quantityGelumEssentia);
        newList.writeToNBT(data);
    }

    @Override
    public boolean ignoreContainedAspects() {
        return false;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.isRemote) {
            return EnumActionResult.PASS;
        }

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IAspectContainer) {
            int quantityAvailable = ((IAspectContainer) tile).containerContains(Aspect.COLD);
            int quantityDrained = Math.min(quantityAvailable, MAX_ESSENTIA_AMOUNT);
            if (((IAspectContainer) tile).takeFromContainer(Aspect.COLD, quantityDrained)) {
                this.setAspects(player.getHeldItem(hand), new AspectList().add(Aspect.COLD, quantityDrained));
                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.FAIL;
            }
        }
        return EnumActionResult.PASS;
    }
}
