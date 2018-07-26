package info.tritusk.electrothaumaturgy;

import ic2.api.item.ElectricItem;
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
            ItemStack icon = new ItemStack(ElectroThaumoObjects.DRILL_OF_CORE);
            ElectricItem.manager.charge(icon, Double.MAX_VALUE, Integer.MAX_VALUE, true, false);
            return icon;
        }
    };
}
