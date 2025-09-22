package tornato.fletching.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tornato.fletching.ArrowComponent;
import tornato.fletching.FletchedArrowItem;
import tornato.fletching.Fletching;

@Debug(export = true)
@Mixin(RangedWeaponItem.class)
public class RangedWeaponMixin {
    @ModifyExpressionValue(method = "method_18817", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private static boolean canUseAsAmmo(boolean original, @Local(argsOnly = true) ItemStack stack) {
        return original || stack.isOf(Fletching.ARROW_ITEM);
    }

    @WrapOperation(method = "createArrowEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"))
    private PersistentProjectileEntity replaceArrowEntity(ArrowItem instance, World world, ItemStack projectileStack, LivingEntity shooter, ItemStack weaponStack, Operation<PersistentProjectileEntity> original) {
        return projectileStack.isOf(Fletching.ARROW_ITEM) ?
                ((FletchedArrowItem)Fletching.ARROW_ITEM).createArrow(world, projectileStack, shooter, weaponStack):
                original.call(instance, world, projectileStack, shooter, weaponStack);
    }

    @ModifyArg(method = "shootAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/RangedWeaponItem;shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;IFFFLnet/minecraft/entity/LivingEntity;)V"), index = 3)
    private float multiplyVelocity(float speed, @Local(ordinal = 1) ItemStack projectileStack) {
        return speed * projectileStack.getOrDefault(Fletching.ARROW_COMPONENT, ArrowComponent.DEFAULT).velocityMultiplier();
    }
}
