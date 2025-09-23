package tornato.fletching;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

public class FletchedArrowEntity extends PersistentProjectileEntity {
    public static final ExplosionBehavior EXPLOSION_BEHAVIOR = new ExplosionBehavior();

    private int bounceCount = 0;

    public FletchedArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(Fletching.ARROW_ENTITY, x, y, z, world, stack, shotFrom);
    }

    public FletchedArrowEntity(EntityType<? extends FletchedArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public FletchedArrowEntity(World world, LivingEntity shooter, ItemStack itemStack, @Nullable ItemStack shotFrom) {
        super(Fletching.ARROW_ENTITY, shooter, world, itemStack, shotFrom);
    }

    private ArrowComponent getComponent() {
        return this.getItemStack().getOrDefault(Fletching.ARROW_COMPONENT, ArrowComponent.DEFAULT);
    }

    @Override
    public boolean updateMovementInFluid(TagKey<Fluid> tag, double speed) {
        if (this.getComponent().noWaterPenalty()) return false;
        return super.updateMovementInFluid(tag, speed);
    }

    @Override
    public void tick() {
        super.tick();

        var noGrav = this.getComponent().noGrav();
        this.setNoGravity(noGrav);

        if (noGrav && this.age > 400 && !this.inGround) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (this.getWorld().isClient()) { // todo: sync the component to the client so I don't need this check?
            super.onCollision(hitResult); return;
        }
        if (
            hitResult.getType() == HitResult.Type.BLOCK &&
            bounceCount < 5 &&
            this.getComponent().bouncy()
        ) {
            Direction.Axis surfaceNormal = ((BlockHitResult) hitResult).getSide().getAxis();
            Vec3d velocity = this.getVelocity();
            switch (surfaceNormal) {
                case X -> velocity = velocity.rotateX(MathHelper.PI);
                case Y -> velocity = velocity.rotateY(MathHelper.PI);
                case Z -> velocity = velocity.rotateZ(MathHelper.PI);
            }
            this.setVelocity(velocity.multiply(this.getComponent().noGrav() ? -1 : -0.6));
            bounceCount++;
        } else {
            super.onCollision(hitResult);
        }
        if (
            hitResult.getType() != HitResult.Type.MISS &&
            bounceCount < 1 &&
            this.getComponent().explodesOnHit()
        ) {
            var world = this.getWorld();
            world.createExplosion(this, world.getDamageSources().explosion(this, this.getOwner()), EXPLOSION_BEHAVIOR, hitResult.getPos(), 1, false, World.ExplosionSourceType.MOB);
            bounceCount++;
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
        return new ItemStack(Fletching.ARROW_ITEM);
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