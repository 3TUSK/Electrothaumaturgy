package info.tritusk.electrothaumaturgy.module.device;

import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import info.tritusk.electrothaumaturgy.internal.ElectricDeviceLogicBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidTank;
import thaumcraft.api.aura.AuraHelper;

public final class VisReplenisherLogic extends ElectricDeviceLogicBase implements ITickable, IEnergySink {

    private static final int MAX_ENERGY = 200000;

    private FluidTank tank = new FluidTank(8000);

    private int energy = 0;

    @Override
    public void update() {
        if (this.getWorld().isRemote) {
            return;
        }

        // 500 EU + 20 mB UU-Matter == 10% chance of restoring 1.0 vis.
        // TODO Higher price?
        // TODO: Do we need some flux leakage to balance it? After all, in theory, they are the same thing.
        if (energy >= 500 && tank.drain(20, false) != null) {
            energy -= 500;
            tank.drain(20, true);
            if (this.getWorld().rand.nextInt(10) == 0) {
                AuraHelper.addVis(this.getWorld(), this.getPos(), 1F);
            }
        }
    }

    @Override
    public double getDemandedEnergy() {
        return MAX_ENERGY - energy;
    }

    @Override
    public int getSinkTier() {
        return 4;
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

}
