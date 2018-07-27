package info.tritusk.electrothaumaturgy.module.device;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.crafting.IInfusionStabiliser;

public class ElectrostaticHarmonizer extends Block implements IInfusionStabiliser {

    public ElectrostaticHarmonizer() {
        super(Material.IRON, MapColor.LIGHT_BLUE);
    }

    @Override
    public boolean canStabaliseInfusion(World world, BlockPos pos) {
        // Only effective when it's full moon
        if (world.provider.getMoonPhase(world.getWorldTime()) == 0) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof ElectrostaticHarmonizerLogic) {
                return ((ElectrostaticHarmonizerLogic) tile).getDemandedEnergy() < 0;
            }
        }
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new ElectrostaticHarmonizerLogic();
    }
}
