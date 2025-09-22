package tornato.fletching.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import tornato.fletching.FletchedArrowEntity;

public class FletchedArrowRenderer extends ProjectileEntityRenderer<FletchedArrowEntity> {
    public static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/projectiles/arrow.png");

    public FletchedArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public Identifier getTexture(FletchedArrowEntity arrowEntity) {
        return TEXTURE;
    }
}
