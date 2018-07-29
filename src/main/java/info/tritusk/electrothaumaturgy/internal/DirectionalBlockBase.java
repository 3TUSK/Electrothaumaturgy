package info.tritusk.electrothaumaturgy.internal;

import ic2.api.tile.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public abstract class DirectionalBlockBase extends Block implements IWrenchable {

    protected DirectionalBlockBase(Material material, MapColor mapColor) {
        super(material, mapColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDirectional.FACING, EnumFacing.EAST));
    }

    /**
     * A helper method that intend to reduce Block -> Item lookup overhand, and to
     * prepare for 1.13 update.
     * @return Its corresponding Item object, if exists.
     */
    public abstract Item getItemForm();

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockDirectional.FACING);
    }

    // Remove in 1.13
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockDirectional.FACING).getIndex();
    }

    // Remove in 1.13
    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.VALUES[meta]);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(BlockDirectional.FACING, placer.getHorizontalFacing().getOpposite()));
    }

    @Override
    public EnumFacing getFacing(World world, BlockPos pos) {
        return world.getBlockState(pos).getValue(BlockDirectional.FACING);
    }

    @Override
    public boolean setFacing(World world, BlockPos pos, EnumFacing newDirection, EntityPlayer player) {
        world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockDirectional.FACING, newDirection), 2);
        return true;
    }

    @Override
    public boolean wrenchCanRemove(World world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    public List<ItemStack> getWrenchDrops(World world, BlockPos pos, IBlockState state, TileEntity tile, EntityPlayer player, int fortune) {
        return Collections.singletonList(new ItemStack(this.getItemForm(), 1, state.getBlock().damageDropped(state)));
    }
}
