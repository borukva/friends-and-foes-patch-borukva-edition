package me.drex.fafpatch.mixin.mod.registry;

import com.faboslav.friendsandfoes.common.init.FriendsAndFoesRecipes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Holder;
import org.apache.logging.log4j.util.TriConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * The Glide effect changes friction inside LivingEntity.travelInAir, which for players runs on the
 * client. On a server-side (Polymer) setup it has no effect on players, so the potions of gliding
 * would be useless. The potions stay registered (so existing items keep loading) but can no longer
 * be brewed.
 */
@Mixin(FriendsAndFoesRecipes.class)
public abstract class FriendsAndFoesRecipesMixin {
    @WrapOperation(
        method = "registerBrewingRecipes",
        at = @At(
            value = "INVOKE",
            target = "Lorg/apache/logging/log4j/util/TriConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V"
        )
    )
    private static void fafpatch$skipGlidingPotionRecipes(TriConsumer<?, ?, ?> instance, Object input, Object ingredient, Object output, Operation<Void> original) {
        if (output instanceof Holder<?> holder && holder.unwrapKey().map(key -> key.identifier().getPath().endsWith("gliding")).orElse(false)) {
            return;
        }
        original.call(instance, input, ingredient, output);
    }
}
