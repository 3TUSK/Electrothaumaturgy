package info.tritusk.electrothaumaturgy.module.device;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public final class VisReplenisher extends Block {

    public VisReplenisher() {
        super(Material.IRON, MapColor.OBSIDIAN);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new VisReplenisherLogic();
    }
}
