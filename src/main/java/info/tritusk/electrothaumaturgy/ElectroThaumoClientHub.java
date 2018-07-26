package info.tritusk.electrothaumaturgy;

import info.tritusk.electrothaumaturgy.internal.client.ModelUtil;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = ElectroThaumaturgy.MOD_ID, value = Side.CLIENT)
public final class ElectroThaumoClientHub {

    private ElectroThaumoClientHub() {
        throw new UnsupportedOperationException();
    }

    @SubscribeEvent
    public static void onModelRegistryWindowOpened(final ModelRegistryEvent event) {
        ModelUtil.mapModel(ElectroThaumoObjects.DRILL_OF_CORE);
        ModelUtil.mapModel(ElectroThaumoObjects.CHAINSAW_OF_STREAM);
    }
}
