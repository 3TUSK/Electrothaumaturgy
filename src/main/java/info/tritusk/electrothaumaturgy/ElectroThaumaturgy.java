package info.tritusk.electrothaumaturgy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

@Mod(
        modid = ElectroThaumaturgy.MOD_ID,
        name = ElectroThaumaturgy.MOD_NAME,
        version = "@VERSION@",
        useMetadata = true,
        dependencies = "required-after:ic2;required-after:thaumcraft"
)
public final class ElectroThaumaturgy {

    public static final String MOD_ID = "electrothaumaturgy";
    public static final String MOD_NAME = "ElectroThaumaturgy";

    public static final CreativeTabs TAB = new CreativeTabs(MOD_ID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ElectroThaumoObjects.DRILL_OF_CORE);
        }
    };
}
