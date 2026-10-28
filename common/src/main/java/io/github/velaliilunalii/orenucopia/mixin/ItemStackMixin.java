package io.github.velaliilunalii.orenucopia.mixin;

import io.github.velaliilunalii.orenucopia.util.ItemStackUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    public void hurt(int $$0, RandomSource $$1, ServerPlayer $$2, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        if (!stack.isDamageableItem()) {
            cir.setReturnValue(false);
        } else {
            if ($$0 > 0) {
                int $$3 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack);
                int $$4 = 0;

                for (int $$5 = 0; $$3 > 0 && $$5 < $$0; $$5++) {
                    if (DigDurabilityEnchantment.shouldIgnoreDurabilityDrop(stack, $$3, $$1)) {
                        $$4++;
                    }
                }

                $$0 -= $$4;
                if ($$0 <= 0) {
                    cir.setReturnValue(false);
                }
            }

            int reinforcementValue = ItemStackUtil.getReinforcementValue(stack);

            int $$6 = reinforcementValue - $$0;
            if ($$6 >= 0) {
                ItemStackUtil.setReinforcementValue(stack, $$6);
                cir.setReturnValue(false);
            }else {
                $$0 = -$$6;
                if ($$0 > 0) {
                    int $$3 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack);
                    int $$4 = 0;

                    for (int $$5 = 0; $$3 > 0 && $$5 < $$0; $$5++) {
                        if (DigDurabilityEnchantment.shouldIgnoreDurabilityDrop(stack, $$3, $$1)) {
                            $$4++;
                        }
                    }

                    $$0 -= $$4;
                    if ($$0 <= 0) {
                        cir.setReturnValue(false);
                    }
                }

                if ($$2 != null && $$0 != 0) {
                    CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger($$2, stack, stack.getDamageValue() + $$0);
                }

                $$6 = stack.getDamageValue() + $$0;
                stack.setDamageValue($$6);
                cir.setReturnValue($$6 >= stack.getMaxDamage());
            }
        }
    }
}
