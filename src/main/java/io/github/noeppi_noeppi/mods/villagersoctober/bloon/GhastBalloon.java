package io.github.noeppi_noeppi.mods.villagersoctober.bloon;

import io.github.noeppi_noeppi.mods.villagersoctober.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class GhastBalloon extends Entity {

    public static final double MAX_TIE_LENGTH = 6;
    
    private static final EntityDataAccessor<BlockPos> TIE_POS = SynchedEntityData.defineId(GhastBalloon.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Float> LOOK_ANGLE = SynchedEntityData.defineId(GhastBalloon.class, EntityDataSerializers.FLOAT);

    private BlockPos tiePos;
    private Vec3 tieVec;
    
    private float targetLookAngle;
    
    private int ticksWithoutFence = 0;
    
    public GhastBalloon(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.tiePos = BlockPos.ZERO;
    }

    public BlockPos getTiePos() {
        return tiePos;
    }

    @Nullable
    public Vec3 getTieVec() {
        return tieVec;
    }

    public void setTiePos(BlockPos tiePos) {
        this.tiePos = tiePos.immutable();
        this.tieVec = new Vec3(tiePos.getX() + 0.5, tiePos.getY() + 0.65, tiePos.getZ() + 0.5);
        this.entityData.set(TIE_POS, this.tiePos);
    }
    
    public void setLookAngle(float angle) {
        this.setLookAngleDeg((float) Math.toDegrees(angle));
    }
    
    public void setLookAngleDeg(float angle) {
        this.targetLookAngle = Mth.wrapDegrees(angle);
        if (!this.isAddedToWorld() && !this.level.isClientSide) {
            this.setYRot(this.targetLookAngle);
        }
        this.entityData.set(LOOK_ANGLE, this.targetLookAngle);
    }

    @Override
    public boolean isPushable() {
        return !this.isRemoved();
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public boolean isInvulnerableTo(@Nonnull DamageSource source) {
        if (super.isInvulnerableTo(source)) return true;
        return source.isFall() || source == DamageSource.CRAMMING;
    }
    
    @Override
    public boolean hurt(@Nonnull DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            this.markHurt();
            if (amount > 0) {
                if (source.getEntity() instanceof LivingEntity living) {
                    living.awardKillScore(this, 0, source);
                }
                this.die();
            }
            return true;
        }
    }

    @Override
    public void tick() {
        super.tick();
        
        if (this.tieVec != null) {
            Vec3 tieVec = this.position().vectorTo(this.tieVec);
            if (tieVec.lengthSqr() > (MAX_TIE_LENGTH * MAX_TIE_LENGTH) * (1.5 * 1.5)) {
                double l = tieVec.length();
                double f = (l - MAX_TIE_LENGTH) / l;
                this.setPos(this.position().add(tieVec.normalize().multiply(f, f, f)));
                this.setDeltaMovement(Vec3.ZERO);
            } else {
                Vec3 velocity = this.getDeltaMovement();
                double xm = velocity.x == 0 ? ((this.random.nextDouble() - 0.5) * 0.05) : velocity.x * 0.9;
                double ym = velocity.y == 0 ? 0.01 : velocity.y;
                double zm = velocity.z == 0 ? ((this.random.nextDouble() - 0.5) * 0.05) : velocity.z * 0.9;
                if (tieVec.lengthSqr() < (MAX_TIE_LENGTH * MAX_TIE_LENGTH)) {
                    ym += 0.05;
                } else {
                    double tension = ((tieVec.length() - MAX_TIE_LENGTH) / MAX_TIE_LENGTH) * 0.15;
                    ym -= tension;
                }
                if (tieVec.x * tieVec.x + tieVec.z * tieVec.z > 0.4) {
                    xm += tieVec.x * 0.04;
                    zm += tieVec.z * 0.04;
                }
                this.setDeltaMovement((3 * velocity.x + xm) / 4, (3 * velocity.y + ym) / 4, (3 * velocity.z + zm) / 4);
            }
        }
        
        float rotDiff = Mth.wrapDegrees(this.targetLookAngle - this.getYRot());
        float newRot = this.getYRot();
        if (Math.abs(rotDiff) <= 0.5) {
            if (!this.level.isClientSide) this.findNewLookTarget();
        } else {
            float rotChange = Mth.clamp(rotDiff, -2, 2);
            newRot = Mth.wrapDegrees(newRot + rotChange);
        }
        
        this.setOldPosAndRot();
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setYRot(newRot);
        
        Vec3 position = this.position();
        this.setPos(position.x, position.y, position.z);
        
        this.pushEntities();
        
        if (!this.level.isClientSide) {
            //noinspection deprecation
            if (this.isAddedToWorld() && this.level.isAreaLoaded(this.blockPosition(), 1) && this.tieVec != null) {
                if (this.getY() < this.tieVec.y || !this.level.getBlockState(this.tiePos).is(BlockTags.FENCES)) {
                    ticksWithoutFence += 1;
                    if (ticksWithoutFence >= 8) this.die();
                } else {
                    ticksWithoutFence = 0;
                }
            }
        }
    }

    private void die() {
        if (!this.level.isClientSide) {
            this.spawnAtLocation(ModItems.ghastBalloon);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    private void pushEntities() {
        List<Entity> entities = this.level.getEntities(this, this.getBoundingBox(), EntitySelector.pushableBy(this));
        for (Entity entity : entities) {
             entity.push(this);
        }
    }
    
    private void findNewLookTarget() {
        List<Entity> entities = this.level.getEntities(this, this.getBoundingBox().inflate(10, 20, 10), e -> e instanceof LivingEntity && !e.isSpectator());
        Entity closest = null;
        double horDistanceSqr = Double.POSITIVE_INFINITY;
        for (Entity entity : entities) {
            double xDiff = entity.getX() - this.getX();
            double zDiff = entity.getZ() - this.getZ();
            double distSqr = (xDiff * xDiff) + (zDiff * zDiff);
            if (distSqr < horDistanceSqr) {
                closest = entity;
                horDistanceSqr = distSqr;
            }
        }
        if (closest != null) {
            double xDiff = closest.getX() - this.getX();
            double zDiff = closest.getZ() - this.getZ();
            this.setLookAngle((float) (Math.atan2(zDiff, xDiff) - (Math.PI / 2)));
        } else {
            this.setLookAngleDeg(this.getYRot());
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(TIE_POS, BlockPos.ZERO);
        this.entityData.define(LOOK_ANGLE, 0f);
    }

    @Override
    public void onSyncedDataUpdated(@Nonnull EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (TIE_POS.equals(key) && this.level.isClientSide) {
            this.setTiePos(this.entityData.get(TIE_POS));
        } else if (LOOK_ANGLE.equals(key) && this.level.isClientSide) {
            this.setLookAngleDeg(this.entityData.get(LOOK_ANGLE));
        }
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }
    
    @Nullable
    @Override
    protected PortalInfo findDimensionEntryPoint(@Nonnull ServerLevel level) {
        return null;
    }
    
    @Override
    protected void readAdditionalSaveData(@Nonnull CompoundTag nbt) {
        this.setTiePos(NbtUtils.readBlockPos(nbt.getCompound("TiePos")));
        this.setLookAngleDeg(nbt.getFloat("TargetLookAngle"));
    }

    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundTag nbt) {
        nbt.put("TiePos", NbtUtils.writeBlockPos(this.tiePos));
        nbt.putFloat("TargetLookAngle", this.targetLookAngle);
    }

    @Nonnull
    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.ghastBalloon);
    }
}
