package tornato.fletching.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;

public class AmethystShrapnelEntity extends ProjectileEntity {
    public AmethystShrapnelEntity(EntityType<? extends AmethystShrapnelEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}
}
