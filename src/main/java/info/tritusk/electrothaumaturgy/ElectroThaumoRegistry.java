package info.tritusk.electrothaumaturgy;

import info.tritusk.electrothaumaturgy.module.device.ElectrostaticHarmonizer;
import info.tritusk.electrothaumaturgy.module.device.ElectrostaticHarmonizerLogic;
import info.tritusk.electrothaumaturgy.module.device.FluxNormalizer;
import info.tritusk.electrothaumaturgy.module.device.FluxNormalizerLogic;
import info.tritusk.electrothaumaturgy.module.device.VisReplenisher;
import info.tritusk.electrothaumaturgy.module.device.VisReplenisherLogic;
import info.tritusk.electrothaumaturgy.module.generator.MagicHeatGenerator;
import info.tritusk.electrothaumaturgy.module.generator.MagicHeatGeneratorLogic;
import info.tritusk.electrothaumaturgy.module.generator.MagicKineticGenerator;
import info.tritusk.electrothaumaturgy.module.generator.MagicKineticGeneratorLogic;
import info.tritusk.electrothaumaturgy.module.generator.PotentiaGenerator;
import info.tritusk.electrothaumaturgy.module.generator.PotentiaGeneratorLogic;
import info.tritusk.electrothaumaturgy.module.processing.PaperMill;
import info.tritusk.electrothaumaturgy.module.processing.PaperMillLogic;
import info.tritusk.electrothaumaturgy.module.reactor.EssentiaCoolantInjector;
import info.tritusk.electrothaumaturgy.module.reactor.EssentiaCoolantInjectorLogic;
import info.tritusk.electrothaumaturgy.module.reactor.FrostCondensator;
import info.tritusk.electrothaumaturgy.module.tools.elemental.ChainsawOfStream;
import info.tritusk.electrothaumaturgy.module.tools.elemental.DrillOfCore;
import info.tritusk.electrothaumaturgy.module.tools.elemental.ElectricHoeOfGrowth;
import info.tritusk.electrothaumaturgy.module.tools.elemental.NanoSaberOfZephyr;
import info.tritusk.electrothaumaturgy.module.tools.misc.ElectricGoggle;
import info.tritusk.electrothaumaturgy.module.tools.misc.ElectricScribingTools;
import info.tritusk.electrothaumaturgy.module.tools.misc.ElectricTravellerBoots;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = ElectroThaumaturgy.MOD_ID)
public final class ElectroThaumoRegistry {

    private ElectroThaumoRegistry() {
        throw new UnsupportedOperationException();
    }

    @SubscribeEvent
    public static void onBlockRegistration(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                new ElectrostaticHarmonizer()
                        .setCreativeTab(ElectroThaumaturgy.TAB)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "electrostatic_harmonizer")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "electrostatic_harmonizer"),
                new FluxNormalizer()
                        .setCreativeTab(ElectroThaumaturgy.TAB)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "flux_normalizer")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "flux_normalizer"),
                new VisReplenisher()
                        .setCreativeTab(ElectroThaumaturgy.TAB)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "vis_replenisher")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "vis_replenisher"),

                new MagicHeatGenerator()
                        .setCreativeTab(ElectroThaumaturgy.TAB)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "magic_heat_generator")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "magic_heat_generator"),
                new MagicKineticGenerator()
                        .setCreativeTab(ElectroThaumaturgy.TAB)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "magic_kinetic_generator")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "magic_kinetic_generator"),
                new PotentiaGenerator()
                        .setCreativeTab(ElectroThaumaturgy.TAB)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "potentia_generator")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "potentia_generator"),

                new PaperMill()
                        .setCreativeTab(ElectroThaumaturgy.TAB)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "paper_mill")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "paper_mill"),

                new EssentiaCoolantInjector()
                        .setCreativeTab(ElectroThaumaturgy.TAB)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "essentia_coolant_injector")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "essentia_coolant_injector")
        );

        GameRegistry.registerTileEntity(ElectrostaticHarmonizerLogic.class, new ResourceLocation(ElectroThaumaturgy.MOD_ID, "electrostatic_harmonizer"));
        GameRegistry.registerTileEntity(FluxNormalizerLogic.class, new ResourceLocation(ElectroThaumaturgy.MOD_ID, "flux_normalizer"));
        GameRegistry.registerTileEntity(VisReplenisherLogic.class, new ResourceLocation(ElectroThaumaturgy.MOD_ID, "vis_replenisher"));

        GameRegistry.registerTileEntity(MagicHeatGeneratorLogic.class, new ResourceLocation(ElectroThaumaturgy.MOD_ID, "magic_heat_generator"));
        GameRegistry.registerTileEntity(MagicKineticGeneratorLogic.class, new ResourceLocation(ElectroThaumaturgy.MOD_ID, "magic_kinetic_generator"));
        GameRegistry.registerTileEntity(PotentiaGeneratorLogic.class, new ResourceLocation(ElectroThaumaturgy.MOD_ID, "potentia_generator"));

        GameRegistry.registerTileEntity(PaperMillLogic.class, new ResourceLocation(ElectroThaumaturgy.MOD_ID, "paper_mill"));

        GameRegistry.registerTileEntity(EssentiaCoolantInjectorLogic.class, new ResourceLocation(ElectroThaumaturgy.MOD_ID, "essentia_coolant_injector"));
    }

    @SubscribeEvent
    public static void onItemRegistration(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new ItemBlock(ElectroThaumoObjects.VIS_REPLENISHER)
                        .setCreativeTab(ElectroThaumaturgy.TAB)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "vis_replenisher")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "vis_replenisher"),
                new ItemBlock(ElectroThaumoObjects.POTENTIA_GENERATOR)
                        .setCreativeTab(ElectroThaumaturgy.TAB)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "potentia_generator")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "potentia_generator"),

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
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "nanosaber_of_zephyr"),

                new FrostCondensator()
                        .setCreativeTab(ElectroThaumaturgy.TAB)
                        .setUnlocalizedName(ElectroThaumaturgy.MOD_ID + '.' + "frost_condensator")
                        .setRegistryName(ElectroThaumaturgy.MOD_ID, "frost_condensator")
        );
    }
}
