package info.tritusk.electrothaumaturgy;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(ElectroThaumaturgy.MOD_ID)
public final class ElectroThaumoObjects {

    private ElectroThaumoObjects() {
        throw new UnsupportedOperationException();
    }

    @GameRegistry.ObjectHolder("vis_replenisher")
    public static final Block VIS_REPLENISHER = Blocks.AIR;

}
