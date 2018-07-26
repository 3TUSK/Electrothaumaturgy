package info.tritusk.electrothaumaturgy.internal.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public final class ModelUtil {

    private ModelUtil() {
        throw new UnsupportedOperationException();
    }

    public static void mapModel(Item item) {
        if (item == null) {
            return; // TODO Warn
        }
        ResourceLocation registryName = item.getRegistryName();
        if (registryName == null) {
            return; // TODO Warn
        }
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(registryName, "inventory"));
    }
}
