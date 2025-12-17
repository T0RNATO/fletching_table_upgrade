package tornato.fletching.client.entity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import tornato.fletching.entity.FletchedArrowEntity;
import tornato.fletching.Fletching;

import static tornato.fletching.client.entity.ProjUtil.*;

public class FletchedArrowRenderer extends ProjectileEntityRenderer<FletchedArrowEntity> {
    private static final RenderLayer EFFECT_TIP_LAYER = RenderLayer.getEntityCutout(Fletching.id("textures/entity/effect_tip.png"));

    public FletchedArrowRenderer(EntityRendererFactory.Context context) { super(context);    }
    public Identifier getTexture(FletchedArrowEntity arrowEntity) {  return null; }

    private Identifier texture(String path) {
        return Fletching.id("textures/entity/" + path + ".png");
    }

    @Override
    public void render(FletchedArrowEntity entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch())));
        float shake = entity.shake - tickDelta;
        if (shake > 0.0F) {
            float t = -MathHelper.sin(shake * 3.0F) * shake;
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(t));
        }

        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45.0F));
        matrixStack.scale(0.05625F, 0.05625F, 0.05625F);
        matrixStack.translate(-4.0F, 0.0F, 0.0F);
        MatrixStack.Entry entry = matrixStack.peek();

        VertexConsumer vertexConsumer;
        var component = entity.getComponent();
        // Fletching
        vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(texture(component.fletchingPath())));
        renderLayer(entry, vertexConsumer, matrixStack, light);
        renderFletching(entry, vertexConsumer, light);
        // Shaft
        vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(texture(component.shaftPath())));
        renderLayer(entry, vertexConsumer, matrixStack, light);
        // Point
        vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(texture(component.pointPath())));
        renderLayer(entry, vertexConsumer, matrixStack, light);
        if (component.hasEffect()) {
            vertexConsumer = vertexConsumerProvider.getBuffer(EFFECT_TIP_LAYER);
            renderLayer(entry, vertexConsumer, matrixStack, light, component.getTint());
        }

        matrixStack.pop();
    }

    private void renderFletching(MatrixStack.Entry entry, VertexConsumer vertexConsumer, int i) {
        // Perpendicular plane at the fletching end of the arrow
        vertex(entry, vertexConsumer, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, i);
        vertex(entry, vertexConsumer, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, i);
        vertex(entry, vertexConsumer, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, i);
        vertex(entry, vertexConsumer, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, i);
        vertex(entry, vertexConsumer, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, i);
        vertex(entry, vertexConsumer, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, i);
        vertex(entry, vertexConsumer, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, i);
        vertex(entry, vertexConsumer, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, i);
    }
}
