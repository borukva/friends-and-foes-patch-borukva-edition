package me.drex.fafpatch.mixin.mod.entity;

import com.faboslav.friendsandfoes.common.entity.PenguinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * F&F only advances the swim pose progress on the client (isClientSide). On a server-side
 * (Polymer) setup nothing advances it, so the penguin never shows its swimming pose.
 */
@Mixin(PenguinEntity.class)
public abstract class PenguinEntityMixin {
    @Shadow
    private float swimProgress;
    @Shadow
    private float prevSwimProgress;

    @Inject(method = "tick", at = @At("TAIL"))
    private void fafpatch$serverSideSwimProgress(CallbackInfo ci) {
        PenguinEntity self = (PenguinEntity) (Object) this;
        if (self.level().isClientSide()) {
            return;
        }
        this.prevSwimProgress = this.swimProgress;
        float speed = 1.0F / 10.0F;
        if (self.isInWater()) {
            this.swimProgress = Math.min(1.0F, this.swimProgress + speed);
        } else {
            this.swimProgress = Math.max(0.0F, this.swimProgress - speed);
        }
    }
}
