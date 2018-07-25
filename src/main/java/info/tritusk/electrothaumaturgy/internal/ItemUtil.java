package info.tritusk.electrothaumaturgy.internal;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class ItemUtil {

    private ItemUtil() {
        throw new UnsupportedOperationException();
    }

    public static void dropItem(final World world, final BlockPos dropPos, final ItemStack item) {
        EntityItem entityItem = new EntityItem(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), item);
        entityItem.motionX = world.rand.nextGaussian() * 0.05D;
        entityItem.motionY = world.rand.nextGaussian() * 0.05D + 0.2D;
        entityItem.motionZ = world.rand.nextGaussian() * 0.05D;
        world.spawnEntity(entityItem);
    }

}
