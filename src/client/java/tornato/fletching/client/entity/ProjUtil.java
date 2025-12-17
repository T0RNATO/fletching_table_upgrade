package tornato.fletching.client.entity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.math.RotationAxis;

public class ProjUtil {
    static void renderLayer(MatrixStack.Entry entry, VertexConsumer vertexConsumer, MatrixStack stack, int i, int color) {
        // Two perpendicular two-sided planes
        for (int u = 0; u < 4; u++) {
            stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
            vertex(entry, vertexConsumer, -8, -2, 0.0F, 0.0F, i, color);
            vertex(entry, vertexConsumer, 8, -2, 0.5F, 0.0F, i, color);
            vertex(entry, vertexConsumer, 8, 2, 0.5F, 0.15625F, i, color);
            vertex(entry, vertexConsumer, -8, 2, 0.0F, 0.15625F, i, color);
        }
    }

    static void renderLayer(MatrixStack.Entry entry, VertexConsumer vertexConsumer, MatrixStack stack, int i) {
        renderLayer(entry, vertexConsumer, stack, i, Colors.WHITE);
    }

    private static void vertex(MatrixStack.Entry matrix, VertexConsumer vertexConsumer, float x, float y, float u, float v, int light, int color) {
        vertexConsumer.vertex(matrix, x, y, 0)
                .color(color)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(matrix, 0, 1, 0);
    }
}
