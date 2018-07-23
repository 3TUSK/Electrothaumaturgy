package info.tritusk.electrothaumaturgy;

import info.tritusk.electrothaumaturgy.module.tools.misc.ElectricGoggle;
import info.tritusk.electrothaumaturgy.module.tools.misc.ElectricScribingTools;
import info.tritusk.electrothaumaturgy.module.tools.misc.ElectricTravellerBoots;
import net.minecraft.creativetab.CreativeTabs;
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
                        .setCreativeTab(CreativeTabs.COMBAT)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "electric_goggle")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "electric_goggle"),
                new ElectricTravellerBoots()
                        .setCreativeTab(CreativeTabs.COMBAT)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "electric_traveller_boots")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "electric_traveller_boots"),
                new ElectricScribingTools()
                        .setCreativeTab(CreativeTabs.MISC)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "electric_scribing_tools")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "electric_scribing_tools")
        );
    }
}
