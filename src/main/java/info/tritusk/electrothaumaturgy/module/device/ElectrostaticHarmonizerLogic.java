package info.tritusk.electrothaumaturgy.module.device;

import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.info.Info;
import info.tritusk.electrothaumaturgy.ElectricDeviceLogicBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import thaumcraft.api.crafting.IStabilizable;

import java.util.List;

public final class ElectrostaticHarmonizerLogic extends ElectricDeviceLogicBase implements IEnergySink, ITickable {

    private int energy;

    @Override
    public void update() {
        if (this.getWorld().isRemote) {
            return;
        }

        if ((energy -= 32) <= 0) {
            energy = 0;
            return;
        }

        if (this.getWorld().rand.nextInt(10) > 0) {
            return;
        }

        for (BlockPos probing : BlockPos.getAllInBox(this.getPos().add(-4, -4, -4), this.getPos().add(4, 4, 4))) {
            TileEntity tile = this.getWorld().getTileEntity(probing);
            if (tile instanceof IStabilizable) {
                IStabilizable target = (IStabilizable) tile;
                if (this.getWorld().rand.nextInt(20) == 0) {
                    float sinResult = MathHelper.sin((float) Math.toRadians(this.world.getWorldTime() % 360));
                    if (MathHelper.abs(sinResult) < 1E-5F) {
                        if (this.getWorld().rand.nextInt(100) == 0 && this.getWorld().provider.getMoonPhase(this.getWorld().getWorldTime()) == 0) {
                            target.addStability(100);
                        } else {
                            target.addStability(8);
                        }
                    } else if (sinResult > 0) {
                        target.addStability(1);
                    } else {
                        List<? extends EntityLivingBase> victims = this.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(probing.add(-5, -5, -5), probing.add(5, 5, 5)));
                        for (EntityLivingBase victim : victims) {
                            victim.attackEntityFrom(Info.DMG_ELECTRIC, MathHelper.cos((float) Math.toRadians(this.world.getWorldTime() % 360)));
                        }
                    }
                }
            }
        }
    }

    @Override
    public double getDemandedEnergy() {
        return 0;
    }

    @Override
    public int getSinkTier() {
        return 1;
    }

    @Override
    public double injectEnergy(EnumFacing facing, double v, double v1) {
        return 0;
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter iEnergyEmitter, EnumFacing enumFacing) {
        return true;
    }

}
