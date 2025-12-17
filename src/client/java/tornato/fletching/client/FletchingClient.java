package tornato.fletching.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import tornato.fletching.*;
import tornato.fletching.client.entity.AmethystShrapnelRenderer;
import tornato.fletching.client.entity.FletchedArrowRenderer;
import tornato.fletching.client.item.FletchedArrowItemRenderer;
import tornato.fletching.client.screen.FletchingScreen;
import tornato.fletching.item.ArrowComponent;
import tornato.fletching.screen.FletchingScreenHandler;

import java.util.function.Function;

import static tornato.fletching.Fletching.FLETCHING_SCREEN;

public class FletchingClient implements ClientModInitializer {
    static {
        HandledScreens.register(FLETCHING_SCREEN, FletchingScreen::new);
    }

    @Override
    public void onInitializeClient() {
        ExpandingTooltip.INSTANCE = Screen::hasShiftDown;
        // Entity renderers
        EntityRendererRegistry.register(Fletching.ARROW_ENTITY, FletchedArrowRenderer::new);
        EntityRendererRegistry.register(Fletching.AMETHYST_SHRAPNEL_ENTITY, AmethystShrapnelRenderer::new);

        // Item models
        ModelLoadingPlugin.register(pluginContext -> {
            Function<String, Function<Item, Identifier>> toId = prefix -> item -> Fletching.id(prefix + item.getRegistryEntry().getKey().get().getValue().getPath());

            pluginContext.addModels(FletchingScreenHandler.FLETCHING.stream().map(toId.apply("item/fletching/")).toList());
            pluginContext.addModels(FletchingScreenHandler.POINTS.stream().map(toId.apply("item/point/")).toList());
            pluginContext.addModels(FletchingScreenHandler.SHAFTS.stream().map(toId.apply("item/shaft/")).toList());
            pluginContext.addModels(Fletching.id("item/tipped_arrow"));
        });

        // Fletched arrow item rendering
        BuiltinItemRendererRegistry.INSTANCE.register(Fletching.ARROW_ITEM, new FletchedArrowItemRenderer());
        ColorProviderRegistry.ITEM.register(
            (stack, tintIndex) ->
                tintIndex == 1 ? stack.getOrDefault(Fletching.ARROW_COMPONENT, ArrowComponent.DEFAULT).getTint(): -1,
            Fletching.ARROW_ITEM
        );
    }
}
