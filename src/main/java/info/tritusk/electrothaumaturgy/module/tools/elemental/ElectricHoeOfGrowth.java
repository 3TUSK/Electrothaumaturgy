package info.tritusk.electrothaumaturgy.module.tools.elemental;

import ic2.api.item.IElectricItem;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.ForgeEventFactory;
import thaumcraft.api.ThaumcraftMaterials;

import java.util.Collections;

public final class ElectricHoeOfGrowth extends AbstractElectricElementalTool implements IElectricItem {

    public ElectricHoeOfGrowth() {
        super(1F, 4F, ThaumcraftMaterials.TOOLMAT_ELEMENTAL, Collections.emptySet());
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return true;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack held = player.getHeldItem(hand);

        if (!player.canPlayerEdit(pos.offset(facing), facing, held)) {
            return EnumActionResult.FAIL;
        } else {
            int usingHoe = ForgeEventFactory.onHoeUse(held, player, worldIn, pos);
            if (usingHoe != 0) {
                return usingHoe > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
            }

            IBlockState block = worldIn.getBlockState(pos);
            Block blockType = block.getBlock();

            if (blockType instanceof IPlantable) {
                int usingBonemeal = ForgeEventFactory.onApplyBonemeal(player, worldIn, pos, block, held, hand);
                if (usingBonemeal != 0) {
                    return usingBonemeal > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
                }

                IGrowable plant = (IGrowable)blockType;
                if (plant.canGrow(worldIn, pos, block, worldIn.isRemote)) {
                    if (!worldIn.isRemote) {
                        if (plant.canUseBonemeal(worldIn, worldIn.rand, pos, block)) {
                            plant.grow(worldIn, worldIn.rand, pos, block);
                        }
                        // TODO Consume electricity
                    }
                }
            }

            if (facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up())) {
                // TODO 3x3 AoE tilling unless sneaking
                if (blockType == Blocks.GRASS || blockType == Blocks.GRASS_PATH || blockType == Blocks.DIRT) {
                    worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    if (!worldIn.isRemote)
                    {
                        worldIn.setBlockState(pos, Blocks.FARMLAND.getDefaultState(), 11);
                        // TODO Consume electricity
                    }
                    return EnumActionResult.SUCCESS;
                }
            }

            return EnumActionResult.PASS;
        }
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return 1;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return 32;
    }

}
