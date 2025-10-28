package tornato.fletching.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import tornato.fletching.ExpandingTooltip;
import tornato.fletching.Fletching;
import tornato.fletching.FletchingScreenHandler;

import java.util.function.Function;

import static tornato.fletching.Fletching.FLETCHING_SCREEN;

public class FletchingClient implements ClientModInitializer {
    static {
        HandledScreens.register(FLETCHING_SCREEN, FletchingScreen::new);
    }

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Fletching.ARROW_ENTITY, FletchedArrowRenderer::new);
        ExpandingTooltip.INSTANCE = Screen::hasShiftDown;

        ModelLoadingPlugin.register(pluginContext -> {
            Function<String, Function<Item, Identifier>> toId = prefix -> item -> Fletching.id(prefix + item.getRegistryEntry().getKey().get().getValue().getPath());

            pluginContext.addModels(FletchingScreenHandler.FLETCHING.stream().map(toId.apply("item/fletching/")).toList());
            pluginContext.addModels(FletchingScreenHandler.POINTS.stream().map(toId.apply("item/point/")).toList());
            pluginContext.addModels(FletchingScreenHandler.SHAFTS.stream().map(toId.apply("item/shaft/")).toList());
        });
        BuiltinItemRendererRegistry.INSTANCE.register(Fletching.ARROW_ITEM, new FletchedArrowItemRenderer());
    }
}
