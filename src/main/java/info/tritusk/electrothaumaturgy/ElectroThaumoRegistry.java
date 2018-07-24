package info.tritusk.electrothaumaturgy;

import info.tritusk.electrothaumaturgy.module.tools.elemental.ChainsawOfStream;
import info.tritusk.electrothaumaturgy.module.tools.elemental.DrillOfCore;
import info.tritusk.electrothaumaturgy.module.tools.elemental.ElectricHoeOfGrowth;
import info.tritusk.electrothaumaturgy.module.tools.elemental.NanoSaberOfZephyr;
import info.tritusk.electrothaumaturgy.module.tools.misc.ElectricGoggle;
import info.tritusk.electrothaumaturgy.module.tools.misc.ElectricScribingTools;
import info.tritusk.electrothaumaturgy.module.tools.misc.ElectricTravellerBoots;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ElectroThaumaturgy.MOD_ID)
public final class ElectroThaumoRegistry {

    private ElectroThaumoRegistry() {
        throw new UnsupportedOperationException();
    }

    @SubscribeEvent
    public static void onItemRegistration(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new ElectricGoggle()
                        .setCreativeTab(ElectroThaumaturgy.TAB)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "electric_goggle")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "electric_goggle"),
                new ElectricTravellerBoots()
                        .setCreativeTab(ElectroThaumaturgy.TAB)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "electric_traveller_boots")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "electric_traveller_boots"),
                new ElectricScribingTools()
                        .setCreativeTab(ElectroThaumaturgy.TAB)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "electric_scribing_tools")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "electric_scribing_tools"),

                new DrillOfCore()
                        .setCreativeTab(ElectroThaumaturgy.TAB)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "drill_of_core")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "drill_of_core"),
                new ChainsawOfStream()
                        .setCreativeTab(ElectroThaumaturgy.TAB)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "chainsaw_of_stream")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "chainsaw_of_stream"),
                new ElectricHoeOfGrowth()
                        .setCreativeTab(ElectroThaumaturgy.TAB)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "electric_hoe_of_growth")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "electric_hoe_of_growth"),
                new NanoSaberOfZephyr()
                        .setCreativeTab(ElectroThaumaturgy.TAB)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "nanosaber_of_zephyr")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "nanosaber_of_zephyr")
        );
    }
}
