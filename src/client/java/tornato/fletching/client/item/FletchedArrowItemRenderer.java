package tornato.fletching.client.item;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import tornato.fletching.item.ArrowComponent;
import tornato.fletching.Fletching;

import static tornato.fletching.Fletching.id;

public class FletchedArrowItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    private static final Identifier TIPPED_MODEL = id("item/tipped_arrow");

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5,0.5, 0.5);

        if (mode == ModelTransformationMode.GUI) {
            DiffuseLighting.enableGuiDepthLighting();
        }

        var component = stack.getOrDefault(Fletching.ARROW_COMPONENT, ArrowComponent.DEFAULT);
        renderLayer(stack, mode, matrices, vertexConsumers, light, overlay, component.fletchingPath());
        renderLayer(stack, mode, matrices, vertexConsumers, light, overlay, component.shaftPath());
        renderLayer(stack, mode, matrices, vertexConsumers, light, overlay, component.pointPath());
        if (component.hasEffect()) {
            renderLayer(stack, mode, matrices, vertexConsumers, light, overlay, TIPPED_MODEL);
        }

        if (mode == ModelTransformationMode.GUI) {
            DiffuseLighting.disableGuiDepthLighting();
        }
        matrices.pop();
    }

    private void renderLayer(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Identifier id) {
        var renderer = MinecraftClient.getInstance().getItemRenderer();
        var manager = renderer.getModels().getModelManager();

        renderer.renderItem(stack, mode, false, matrices, vertexConsumers, light, overlay, manager.getModel(id));
    }

    private void renderLayer(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, String path) {
        renderLayer(stack, mode, matrices, vertexConsumers, light, overlay, id("item/" + path));
    }
}
