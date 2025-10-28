package tornato.fletching.client.mixin;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tornato.fletching.Fletching;
import tornato.fletching.FletchingScreenHandler;

import java.util.Map;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {
    @Shadow protected abstract void loadInventoryVariantItemModel(Identifier id);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/ModelLoader;loadItemModel(Lnet/minecraft/client/util/ModelIdentifier;)V", ordinal = 0))
    private void foo(BlockColors blockColors, Profiler profiler, Map jsonUnbakedModels, Map blockStates, CallbackInfo ci) {
        for (Item item : FletchingScreenHandler.FLETCHING) {
            this.loadInventoryVariantItemModel(Fletching.id("fletching/" + item.getRegistryEntry().getKey().get().getValue().getPath()));
        }
        for (Item item : FletchingScreenHandler.SHAFTS) {
            this.loadInventoryVariantItemModel(Fletching.id("shaft/" + item.getRegistryEntry().getKey().get().getValue().getPath()));
        }
        for (Item item : FletchingScreenHandler.POINTS) {
            this.loadInventoryVariantItemModel(Fletching.id("point/" + item.getRegistryEntry().getKey().get().getValue().getPath()));
        }
    }
}
