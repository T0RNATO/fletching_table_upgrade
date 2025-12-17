package tornato.fletching.mixin;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tornato.fletching.item.ArrowComponent;
import tornato.fletching.Fletching;

@Mixin(PersistentProjectileEntity.class)
public abstract class ProjectileMixin {
    @Shadow public abstract ItemStack getItemStack();

    @ModifyArg(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(DDD)D"), index = 0)
    private double applyDamageMultiplier(double value) {
        return value * this.getItemStack().getOrDefault(Fletching.ARROW_COMPONENT, ArrowComponent.DEFAULT).damageMultiplier();
    }
}
