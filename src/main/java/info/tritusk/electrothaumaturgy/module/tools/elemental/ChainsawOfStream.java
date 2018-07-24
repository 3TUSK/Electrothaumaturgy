package info.tritusk.electrothaumaturgy.module.tools.elemental;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

import java.util.Collections;

public final class ChainsawOfStream extends AbstractElectricElementalTool implements IElectricItem {

    public ChainsawOfStream() {
        super(10F, 1F, ThaumcraftMaterials.TOOLMAT_ELEMENTAL, Collections.emptySet());
        setHarvestLevel("axe", 4);
        setHarvestLevel("sword", 4);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        Material material = state.getMaterial();
        return (material == Material.WOOD || material == Material.LEAVES || material == Material.WEB) && ElectricItem.manager.canUse(stack, 100);
    }


    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        // TODO Consume electricity
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (!worldIn.isRemote && state.getBlockHardness(worldIn, pos) != 0F) {
            // TODO Consume electricity
        }
        return true;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        // TODO Consume electricity
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        // TODO Consume electricity
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return 30000;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return 1;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return 32;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack item = new ItemStack(this, 1, 0, null);
            EnumInfusionEnchantment.addInfusionEnchantment(item, EnumInfusionEnchantment.COLLECTOR, 1);
            EnumInfusionEnchantment.addInfusionEnchantment(item, EnumInfusionEnchantment.BURROWING, 1);
            items.add(item);
        }
    }
}
