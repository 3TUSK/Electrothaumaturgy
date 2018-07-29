package info.tritusk.electrothaumaturgy.module.device;

import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import info.tritusk.electrothaumaturgy.internal.ElectricDeviceLogicBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import thaumcraft.api.aura.AuraHelper;

public class FluxNormalizerLogic extends ElectricDeviceLogicBase implements ITickable, IEnergySink {

    private static final int MAX_ENERGY = 200000;

    private FluidTank tank = new FluidTank(1000);

    private float fluxReservoir = 0.0F;

    private int energy = 0;

    @Override
    public void update() {
        if (this.getWorld().isRemote) {
            return;
        }

        if (this.getWorld().rand.nextInt(20) == 0 && energy >= 10000) {
            float currentFlux = AuraHelper.getFlux(this.getWorld(), this.getPos());
            fluxReservoir += AuraHelper.drainFlux(this.getWorld(), this.getPos(), currentFlux / 2, false);
            energy -= 10000;
            if (fluxReservoir >= 20 && this.getWorld().rand.nextInt(5) == 0) {
                tank.fill(FluidRegistry.getFluidStack("ic2uu_matter", 1), true);
                fluxReservoir -= 20F;
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
