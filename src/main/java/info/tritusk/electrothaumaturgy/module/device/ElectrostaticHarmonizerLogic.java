package info.tritusk.electrothaumaturgy.module.device;

import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.info.Info;
import info.tritusk.electrothaumaturgy.internal.ElectricDeviceLogicBase;
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

    boolean charged;

    @Override
    public void update() {
        if (this.getWorld().isRemote) {
            return;
        }

        if (!charged) {
            return;
        }

        charged = false;

        if (this.getWorld().rand.nextInt(10) > 0) {
            return;
        }

        // Special effect is only available during night time
        if (this.getWorld().isDaytime()) {
            return;
        }

        // 5% chance to either add a bit stability, or causing electric shock to nearby entities
        // The if check comes first so that we can save some time
        if (this.getWorld().rand.nextInt(20) == 0) {
            for (BlockPos probing : BlockPos.getAllInBox(this.getPos().add(-4, -4, -4), this.getPos().add(4, 4, 4))) {
                TileEntity tile = this.getWorld().getTileEntity(probing);
                if (tile instanceof IStabilizable) {
                    IStabilizable target = (IStabilizable) tile;
                    float sinResult = MathHelper.sin((float) Math.toRadians(this.world.getWorldTime() % 360));
                    if (MathHelper.abs(sinResult) < 1E-8F) { // epsilon equals to 0F
                        if (this.getWorld().rand.nextInt(100) == 0) { // additional 1%, builds up to 0.05%
                            // Requiring a sunny night with full-moon in order to get bonus stability.
                            // TODO What about new moon? 1/4? 3/4? Crescent?
                            if (this.getWorld().provider.getMoonPhase(this.getWorld().getWorldTime()) == 0
                                    && !this.getWorld().isRaining()
                                    && !this.getWorld().isThundering()
                                    && !this.getWorld().isDaytime()) {
                                target.addStability(100);
                            } else {
                                target.addStability(8);
                            }
                        }
                    } else if (sinResult > 0) {
                        target.addStability(1);
                    } else {
                        // You know, we create some weird energy field around the target in order to stabilize it.
                        // It perfectly makes sense that such energy field may cause damage to nearby living entities.
                        // TODO Using my own damage source?
                        List<? extends EntityLivingBase> victims = this.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(probing.add(-5, -5, -5), probing.add(5, 5, 5)));
                        for (EntityLivingBase victim : victims) {
                            victim.attackEntityFrom(Info.DMG_ELECTRIC, (float) Math.exp(2 * MathHelper.cos((float) Math.toRadians(this.world.getWorldTime() % 360))));
                        }
                    }
                }
            }
        }
    }

    @Override
    public double getDemandedEnergy() {
        return 128D;
    }

    @Override
    public int getSinkTier() {
        return 2;
    }

    @Override
    public double injectEnergy(EnumFacing facing, double amount, double voltage) {
        this.charged = true;
        return 0;
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing sideFrom) {
        return true;
    }

}
