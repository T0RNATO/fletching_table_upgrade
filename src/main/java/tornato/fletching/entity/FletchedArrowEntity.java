package tornato.fletching.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;
import tornato.fletching.item.ArrowComponent;
import tornato.fletching.Fletching;

public class FletchedArrowEntity extends PersistentProjectileEntity {
    private static final TrackedData<ItemStack> ITEM = DataTracker.registerData(FletchedArrowEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    public static final ExplosionBehavior EXPLOSION_BEHAVIOR = new ExplosionBehavior();
    public static final ExplosionBehavior FIRE_BEHAVIOUR = new ExplosionBehavior() {
        @Override public boolean shouldDamage(Explosion explosion, Entity entity) { return false; }
    };

    private int bounceCount = 0;

    public FletchedArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(Fletching.ARROW_ENTITY, x, y, z, world, stack, shotFrom);
        updateDataTracker(stack);
    }

    public FletchedArrowEntity(EntityType<? extends FletchedArrowEntity> entityType, World world) {
        super(entityType, world);
        updateDataTracker(getDefaultItemStack());
    }

    public FletchedArrowEntity(World world, LivingEntity shooter, ItemStack itemStack, @Nullable ItemStack shotFrom) {
        super(Fletching.ARROW_ENTITY, shooter, world, itemStack, shotFrom);
        updateDataTracker(itemStack);
    }

    public ArrowComponent getComponent() {
        return this.getDataTracker().get(ITEM).getOrDefault(Fletching.ARROW_COMPONENT, ArrowComponent.DEFAULT);
    }

    @Override
    protected void setStack(ItemStack stack) {
        super.setStack(stack);
        updateDataTracker(stack);
    }

    protected void updateDataTracker(ItemStack stack) {
        this.getDataTracker().set(ITEM, stack.copyWithCount(1));
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ITEM, this.getDefaultItemStack());
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
            bounceCount < 1
        ) {
            var world = this.getWorld();
            if (!world.isClient()) {
                if (this.getComponent().explodesOnHit()) {
                    world.createExplosion(this, world.getDamageSources().explosion(this, this.getEffectCause()), EXPLOSION_BEHAVIOR, hitResult.getPos(), 1, false, World.ExplosionSourceType.MOB);
                }
                if (this.getComponent().onFire()) {
                    world.createExplosion(this, world.getDamageSources().explosion(this, this.getEffectCause()), FIRE_BEHAVIOUR, hitResult.getPos(), 1.5f, true, World.ExplosionSourceType.MOB);
                }
            }
            bounceCount++;
        }
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        var component = this.getComponent();

        if (component.effect().isPresent()) {
            var effects = component.effect().get().value().getEffects();
            for (var effect : effects) {
                target.addStatusEffect(
                        new StatusEffectInstance(
                                effect.getEffectType(),
                                Math.max(effect.mapDuration(i -> i / 8), 1),
                                effect.getAmplifier(),
                                effect.isAmbient(),
                                effect.shouldShowParticles()
                        ),
                        target
                );
            }
        } else if (component.spectral()) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200, 0), this.getEffectCause());
        }
        if (component.appliesDarkness()) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 100, 0), this.getEffectCause());
        }
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Fletching.ARROW_ITEM);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.updateDataTracker(ItemStack.fromNbt(this.getRegistryManager(), nbt.getCompound("Item")).orElseGet(this::getDefaultItemStack));
    }
}