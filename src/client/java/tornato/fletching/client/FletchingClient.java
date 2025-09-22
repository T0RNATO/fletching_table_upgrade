package tornato.fletching.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import tornato.fletching.ExpandingTooltip;
import tornato.fletching.Fletching;

import static tornato.fletching.Fletching.FLETCHING_SCREEN;

public class FletchingClient implements ClientModInitializer {
    static {
        HandledScreens.register(FLETCHING_SCREEN, FletchingScreen::new);
    }

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Fletching.ARROW_ENTITY, FletchedArrowRenderer::new);
        ExpandingTooltip.INSTANCE = Screen::hasShiftDown;
    }
}
