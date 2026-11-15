package me.drex.fafpatch.mixin.mod.entity;

import com.faboslav.friendsandfoes.common.entity.CrabEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * F&F only advances the climbing pose progress on the client (isClientSide). On a server-side
 * (Polymer) setup nothing advances it, so the crab never shows its wall climbing pose.
 */
@Mixin(CrabEntity.class)
public abstract class CrabEntityMixin {
    @Shadow
    private float climbProgress;
    @Shadow
    private float prevClimbProgress;

    @Inject(method = "tick", at = @At("TAIL"))
    private void fafpatch$serverSideClimbProgress(CallbackInfo ci) {
        CrabEntity self = (CrabEntity) (Object) this;
        if (self.level().isClientSide()) {
            return;
        }
        this.prevClimbProgress = this.climbProgress;
        float speed = 1.0F / 10.0F;
        if (self.isClimbingWall()) {
            this.climbProgress = Math.min(1.0F, this.climbProgress + speed);
        } else {
            this.climbProgress = Math.max(0.0F, this.climbProgress - speed);
        }
    }
}
