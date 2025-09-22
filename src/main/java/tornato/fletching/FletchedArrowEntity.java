package tornato.fletching;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FletchedArrowEntity extends PersistentProjectileEntity {
    public FletchedArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(Fletching.ARROW_ENTITY, x, y, z, world, stack, shotFrom);
    }

    public FletchedArrowEntity(EntityType<? extends FletchedArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public FletchedArrowEntity(World world, LivingEntity shooter, ItemStack itemStack, @Nullable ItemStack shotFrom) {
        super(Fletching.ARROW_ENTITY, shooter, world, itemStack, shotFrom);
    }

    private ArrowComponent component() {
        return this.getItemStack().getOrDefault(Fletching.ARROW_COMPONENT, ArrowComponent.DEFAULT);
    }

    @Override
    public void tick() {
        super.tick();

        var noGrav = this.component().noGrav();
        this.setNoGravity(noGrav);

        if (noGrav && this.age > 400 && !this.inGround) {
            this.remove(RemovalReason.DISCARDED);
        }

        if (this.getWorld().isClient) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                }
            } else {
            }
        }
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        Entity entity = this.getEffectCause();

//        PotionContentsComponent potionContentsComponent = this.getPotionContents();
//        if (potionContentsComponent.potion().isPresent()) {
//            for (StatusEffectInstance statusEffectInstance : ((Potion)((RegistryEntry)potionContentsComponent.potion().get()).value()).getEffects()) {
//                target.addStatusEffect(
//                        new StatusEffectInstance(
//                                statusEffectInstance.getEffectType(),
//                                Math.max(statusEffectInstance.mapDuration(i -> i / 8), 1),
//                                statusEffectInstance.getAmplifier(),
//                                statusEffectInstance.isAmbient(),
//                                statusEffectInstance.shouldShowParticles()
//                        ),
//                        entity
//                );
//            }
//        }
//
//        for (StatusEffectInstance statusEffectInstance : potionContentsComponent.customEffects()) {
//            target.addStatusEffect(statusEffectInstance, entity);
//        }
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 0) {
//            int i = this.getColor();
//            if (i != -1) {
//                float f = (i >> 16 & 0xFF) / 255.0F;
//                float g = (i >> 8 & 0xFF) / 255.0F;
//                float h = (i >> 0 & 0xFF) / 255.0F;
//
//                for (int j = 0; j < 20; j++) {
//                    this.getWorld()
//                            .addParticle(
//                                    EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, f, g, h),
//                                    this.getParticleX(0.5),
//                                    this.getRandomBodyY(),
//                                    this.getParticleZ(0.5),
//                                    0.0,
//                                    0.0,
//                                    0.0
//                            );
//                }
//            }
        } else {
            super.handleStatus(status);
        }
    }
}