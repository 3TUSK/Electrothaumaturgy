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

    public static final Block ELECTROSTATIC_HARMONIZER = Blocks.AIR;

    public static final Block FLUX_NORMALIZER = Blocks.AIR;

    public static final Block VIS_REPLENISHER = Blocks.AIR;

    public static final Block MAGIC_HEAT_GENERATOR = Blocks.AIR;

    public static final Block MAGIC_KINETIC_GENERATOR = Blocks.AIR;

    public static final Block POTENTIA_GENERATOR = Blocks.AIR;

    public static final Block PAPER_MILL = Blocks.AIR;

    public static final Block ESSENTIA_COOLANT_INJECTOR = Blocks.AIR;

    public static final Item CHAINSAW_OF_STREAM = Items.AIR;

    public static final Item DRILL_OF_CORE = Items.AIR;

    public static final Item FROST_CONDENSATOR = Items.AIR;

    @GameRegistry.ObjectHolder("essentia_coolant_injector")
    public static final Item ESSENTIA_RCI = Items.AIR;

    @GameRegistry.ObjectHolder("magic_heat_generator")
    public static final Item MAGIC_HEAT_GENERATOR_ITEM = Items.AIR;

    @GameRegistry.ObjectHolder("magic_kinetic_generator")
    public static final Item MAGIC_KINETIC_GENERATOR_ITEM = Items.AIR;

}
