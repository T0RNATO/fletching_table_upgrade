package tornato.fletching.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CyclingSlotIcon;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import tornato.fletching.Fletching;
import tornato.fletching.screen.FletchingScreenHandler;

import java.util.List;
import java.util.stream.Stream;

public class FletchingScreen extends ForgingScreen<FletchingScreenHandler> {
    private final CyclingSlotIcon fletchingIcon = new CyclingSlotIcon(0);
    private final CyclingSlotIcon shaftIcon = new CyclingSlotIcon(1);
    private final CyclingSlotIcon pointIcon = new CyclingSlotIcon(2);
    private final CyclingSlotIcon effectsIcon = new CyclingSlotIcon(3);
    private static final List<Identifier> fletchingIcons = Stream.of(
            "feather", "phantom_membrane"
    ).map(FletchingScreen::toId).toList();
    private static final List<Identifier> shaftIcons = Stream.of(
            "shaft"
    ).map(FletchingScreen::toId).toList();
    private static final List<Identifier> pointIcons = Stream.of(
            "amethyst_shard", "diamond", "echo_shard", "flint", "prismarine_shard"
    ).map(FletchingScreen::toId).toList();
    private static final List<Identifier> effectsIcons = Stream.of(
            "dust", "slime_ball", "splash_potion"
    ).map(FletchingScreen::toId).toList();

    private static Identifier toId(String string) {
        return Fletching.id("item/empty_slot/" + string);
    }

    public FletchingScreen(FletchingScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title, Fletching.id("textures/container/fletching.png"));
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        super.drawBackground(context, delta, mouseX, mouseY);
        this.fletchingIcon.render(this.handler, context, delta, x, y);
        this.shaftIcon.render(this.handler, context, delta, x, y);
        this.pointIcon.render(this.handler, context, delta, x, y);
        this.effectsIcon.render(this.handler, context, delta, x, y);
    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        this.fletchingIcon.updateTexture(fletchingIcons);
        this.shaftIcon.updateTexture(shaftIcons);
        this.pointIcon.updateTexture(pointIcons);
        this.effectsIcon.updateTexture(effectsIcons);
    }

    @Override
    protected void drawInvalidRecipeArrow(DrawContext context, int x, int y) {

    }
}
