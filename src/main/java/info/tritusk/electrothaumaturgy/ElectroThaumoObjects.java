package info.tritusk.electrothaumaturgy;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(ElectroThaumaturgy.MOD_ID)
public final class ElectroThaumoObjects {

    private ElectroThaumoObjects() {
        throw new UnsupportedOperationException();
    }

    public static final Block VIS_REPLENISHER = Blocks.AIR;

    public static final Item CHAINSAW_OF_STREAM = Items.AIR;

    public static final Item DRILL_OF_CORE = Items.AIR;

}
