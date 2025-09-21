package tornato.fletching.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import tornato.fletching.Fletching;
import tornato.fletching.FletchingScreenHandler;

public class FletchingScreen extends ForgingScreen<FletchingScreenHandler> {
    public FletchingScreen(FletchingScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title, Fletching.id("textures/container/fletching.png"));
    }

    @Override
    protected void drawInvalidRecipeArrow(DrawContext context, int x, int y) {

    }
}
