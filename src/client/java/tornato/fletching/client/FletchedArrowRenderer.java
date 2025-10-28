package tornato.fletching.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import tornato.fletching.FletchedArrowEntity;
import tornato.fletching.Fletching;

public class FletchedArrowRenderer extends ProjectileEntityRenderer<FletchedArrowEntity> {
    public FletchedArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public Identifier getTexture(FletchedArrowEntity arrowEntity) {
        return null;
    }

    public Identifier fletchingTexture(FletchedArrowEntity arrowEntity) {
        return Fletching.id("textures/entity/" + arrowEntity.getComponent().fletchingPath() + ".png");
    }
    public Identifier shaftTexture(FletchedArrowEntity arrowEntity) {
        return Fletching.id("textures/entity/" + arrowEntity.getComponent().shaftPath() + ".png");
    }
    public Identifier pointTexture(FletchedArrowEntity arrowEntity) {
        return Fletching.id("textures/entity/" + arrowEntity.getComponent().pointPath() + ".png");
    }

    @Override
    public void render(FletchedArrowEntity persistentProjectileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        matrixStack.push();
        matrixStack.multiply(
                RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, persistentProjectileEntity.prevYaw, persistentProjectileEntity.getYaw()) - 90.0F)
        );
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, persistentProjectileEntity.prevPitch, persistentProjectileEntity.getPitch())));
        float s = persistentProjectileEntity.shake - g;
        if (s > 0.0F) {
            float t = -MathHelper.sin(s * 3.0F) * s;
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(t));
        }

        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45.0F));
        matrixStack.scale(0.05625F, 0.05625F, 0.05625F);
        matrixStack.translate(-4.0F, 0.0F, 0.0F);
        MatrixStack.Entry entry = matrixStack.peek();

        VertexConsumer vertexConsumer;

        vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(fletchingTexture(persistentProjectileEntity)));
        renderLayer(entry, vertexConsumer, matrixStack, light);
        vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(shaftTexture(persistentProjectileEntity)));
        renderLayer(entry, vertexConsumer, matrixStack, light);
        vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(pointTexture(persistentProjectileEntity)));
        renderLayer(entry, vertexConsumer, matrixStack, light);

        matrixStack.pop();
    }

    private void renderLayer(MatrixStack.Entry entry, VertexConsumer vertexConsumer, MatrixStack stack, int i) {
        vertex(entry, vertexConsumer, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, i);
        vertex(entry, vertexConsumer, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, i);
        vertex(entry, vertexConsumer, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, i);
        vertex(entry, vertexConsumer, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, i);
        vertex(entry, vertexConsumer, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, i);
        vertex(entry, vertexConsumer, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, i);
        vertex(entry, vertexConsumer, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, i);
        vertex(entry, vertexConsumer, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, i);

        for (int u = 0; u < 4; u++) {
            stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
            vertex(entry, vertexConsumer, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, i);
            vertex(entry, vertexConsumer, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, i);
            vertex(entry, vertexConsumer, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, i);
            vertex(entry, vertexConsumer, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, i);
        }
    }
}
