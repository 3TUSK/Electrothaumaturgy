package info.tritusk.electrothaumaturgy.module.generator;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public final class MagicHeatGenerator extends Block {

    public MagicHeatGenerator() {
        super(Material.IRON, MapColor.OBSIDIAN);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new MagicHeatGeneratorLogic();
    }
}
