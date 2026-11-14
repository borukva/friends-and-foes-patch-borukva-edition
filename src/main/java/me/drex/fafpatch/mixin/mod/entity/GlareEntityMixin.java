package me.drex.fafpatch.mixin.mod.entity;

import com.faboslav.friendsandfoes.common.entity.GlareEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * F&F only moves the current eye offset towards the target on the client (isClientSide).
 * On a server-side (Polymer) setup the eyes would therefore never move.
 */
@Mixin(GlareEntity.class)
public abstract class GlareEntityMixin {
    @Shadow
    private Vec2 currentEyesPositionOffset;
    @Shadow
    private Vec2 targetEyesPositionOffset;

    @Inject(method = "updateEyesPositionOffset", at = @At("HEAD"))
    private void fafpatch$serverSideEyes(CallbackInfo ci) {
        GlareEntity self = (GlareEntity) (Object) this;
        if (self.level().isClientSide()) {
            return;
        }
        float speedX = Math.abs(this.targetEyesPositionOffset.x - this.currentEyesPositionOffset.x) / GlareEntity.MIN_EYE_ANIMATION_TICK_AMOUNT;
        float speedY = Math.abs(this.targetEyesPositionOffset.y - this.currentEyesPositionOffset.y) / GlareEntity.MIN_EYE_ANIMATION_TICK_AMOUNT;
        this.currentEyesPositionOffset = new Vec2(
            Mth.approach(this.currentEyesPositionOffset.x, this.targetEyesPositionOffset.x, speedX),
            Mth.approach(this.currentEyesPositionOffset.y, this.targetEyesPositionOffset.y, speedY)
        );
    }
}
