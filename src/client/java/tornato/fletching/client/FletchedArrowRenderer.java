package tornato.fletching.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import tornato.fletching.ArrowComponent;
import tornato.fletching.FletchedArrowEntity;

public class FletchedArrowRenderer extends ProjectileEntityRenderer<FletchedArrowEntity> {
    public static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/projectiles/arrow.png");

    public FletchedArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public Identifier getTexture(FletchedArrowEntity arrowEntity) {
        return TEXTURE;
    }

    @Override
    public void render(FletchedArrowEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);
//        System.out.println(entity.getComponent().effect_item().isPresent());
    }
}
