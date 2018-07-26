package info.tritusk.electrothaumaturgy.module.reactor;

import ic2.api.tile.IWrenchable;
import info.tritusk.electrothaumaturgy.ElectroThaumoObjects;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public final class EssentiaCoolantInjector extends BlockHorizontal implements IWrenchable {

    public EssentiaCoolantInjector() {
        super(Material.IRON, MapColor.WHITE_STAINED_HARDENED_CLAY);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.HORIZONTALS[meta]);
    }

    @Override
    public EnumFacing getFacing(World world, BlockPos pos) {
        return world.getBlockState(pos).getValue(FACING);
    }

    @Override
    public boolean setFacing(World world, BlockPos pos, EnumFacing facing, EntityPlayer player) {
        if (facing.getAxis() == EnumFacing.Axis.Y) {
            return false;
        }
        world.setBlockState(pos, world.getBlockState(pos).withProperty(FACING, facing));
        return true;
    }

    @Override
    public boolean wrenchCanRemove(World world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    public List<ItemStack> getWrenchDrops(World world, BlockPos pos, IBlockState state, TileEntity tile, EntityPlayer player, int fortune) {
        // TODO Leak essentia as flux?
        return Collections.singletonList(new ItemStack(ElectroThaumoObjects.ESSENTIA_RCI));
    }
}
