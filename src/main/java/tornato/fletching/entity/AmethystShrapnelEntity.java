package tornato.fletching.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AmethystShrapnelEntity extends ThrownEntity {
    public AmethystShrapnelEntity(EntityType<? extends AmethystShrapnelEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        var entity = entityHitResult.getEntity();
        entity.damage(entity.getDamageSources().thrown(this, this.getOwner()), 3);
    }

    @Override
    public void tick() {
        super.tick();
        this.updateRotation();
        Vec3d velocity = this.getVelocity();
        if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
            //noinspection SuspiciousNameCombination
            this.setYaw((float)(MathHelper.atan2(velocity.x, velocity.z) * 180.0F / (float)Math.PI));
            this.setPitch((float)(MathHelper.atan2(velocity.y, velocity.horizontalLength()) * 180.0F / (float)Math.PI));
            this.prevYaw = this.getYaw();
            this.prevPitch = this.getPitch();
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        this.discard();
    }
}
