package me.drex.fafpatch.mixin.mod.entity;

import com.faboslav.friendsandfoes.common.entity.ai.brain.task.penguin.PenguinSwimWithPlayerTask;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Penguins give "Penguin's Glide" (boat speed) to players they swim with. That bonus is applied in
 * the boat movement code which runs on the client for the controlling player, so it cannot work on a
 * server-side (Polymer) setup. Stop giving the effect; the penguin behaviour itself is unchanged.
 */
@Mixin(PenguinSwimWithPlayerTask.class)
public abstract class PenguinSwimWithPlayerTaskMixin {
    @WrapOperation(
        method = {
            "start(Lnet/minecraft/server/level/ServerLevel;Lcom/faboslav/friendsandfoes/common/entity/PenguinEntity;J)V",
            "tick(Lnet/minecraft/server/level/ServerLevel;Lcom/faboslav/friendsandfoes/common/entity/PenguinEntity;J)V"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z"
        )
    )
    private boolean fafpatch$noPenguinsGlide(LivingEntity instance, MobEffectInstance effect, Entity source, Operation<Boolean> original) {
        return false;
    }
}
