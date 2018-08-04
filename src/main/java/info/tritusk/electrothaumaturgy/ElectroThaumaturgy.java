package info.tritusk.electrothaumaturgy;

import ic2.api.item.ElectricItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;

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

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ThaumcraftApi.registerResearchLocation(new ResourceLocation(MOD_ID, "research/electrothaumaturgy.json"));
        ResearchCategories.registerCategory(
                "ELECTROTHAUMATURGY",
                "ET_ORIGIN", // It must be a research defined in your research json data.
                new AspectList().add(Aspect.ENERGY, 10),
                new ResourceLocation(MOD_ID, "textures/items/drill_of_core.png"),
                new ResourceLocation("thaumcraft", "textures/gui/gui_research_back_4.jpg")
        );
    }
}
