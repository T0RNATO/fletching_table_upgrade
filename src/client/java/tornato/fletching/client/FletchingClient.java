package tornato.fletching.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

import static tornato.fletching.Fletching.FLETCHING_SCREEN;

public class FletchingClient implements ClientModInitializer {
    static {
        HandledScreens.register(FLETCHING_SCREEN, FletchingScreen::new);
    }

    @Override
    public void onInitializeClient() {

    }
}
