package info.tritusk.electrothaumaturgy.module.tools.elemental;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import thaumcraft.api.ThaumcraftMaterials;

import java.util.Collections;

public final class ElectricHoeOfGrowth extends AbstractElectricElementalTool implements IElectricItem {

    public ElectricHoeOfGrowth() {
        super(1F, 0F, ThaumcraftMaterials.TOOLMAT_ELEMENTAL, Collections.emptySet());
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return true;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack held = player.getHeldItem(hand);

        if (!player.canPlayerEdit(pos.offset(facing), facing, held)) {
            return EnumActionResult.FAIL;
        } else {
            UseHoeEvent eventUseHoe = new UseHoeEvent(player, held, world, pos);
            if (!MinecraftForge.EVENT_BUS.post(eventUseHoe)) {
                if (eventUseHoe.getResult() == Event.Result.ALLOW) {
                    // TODO Consume electricity
                    return EnumActionResult.SUCCESS;
                } else {
                    return EnumActionResult.FAIL;
                }
            }

            IBlockState block = world.getBlockState(pos);
            Block blockType = block.getBlock();

            if (blockType instanceof IPlantable) {
                BonemealEvent event = new BonemealEvent(player, world, pos, block, hand, held);
                if (!MinecraftForge.EVENT_BUS.post(event)) {
                    if (event.getResult() == Event.Result.ALLOW) {
                        // TODO Consume electricity
                        return EnumActionResult.SUCCESS;
                    } else {
                        return EnumActionResult.FAIL;
                    }
                }

                IGrowable plant = (IGrowable)blockType;
                if (plant.canGrow(world, pos, block, world.isRemote)) {
                    if (!world.isRemote) {
                        if (plant.canUseBonemeal(world, world.rand, pos, block)) {
                            plant.grow(world, world.rand, pos, block);
                        }
                        // TODO Consume electricity
                    }
                }
            }

            if (facing != EnumFacing.DOWN && world.isAirBlock(pos.up())) {
                // TODO 3x3 AoE tilling unless sneaking
                if (blockType == Blocks.GRASS || blockType == Blocks.GRASS_PATH || blockType == Blocks.DIRT) {
                    world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    if (!world.isRemote)
                    {
                        world.setBlockState(pos, Blocks.FARMLAND.getDefaultState(), 11);
                        // TODO Consume electricity
                    }
                    return EnumActionResult.SUCCESS;
                }
            }

            return EnumActionResult.PASS;
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack zeroCharge = new ItemStack(this);
            items.add(zeroCharge);
            ItemStack fullyCharged = zeroCharge.copy();
            ElectricItem.manager.charge(fullyCharged, Double.MAX_VALUE, Integer.MAX_VALUE, true, false);
            items.add(fullyCharged);
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
