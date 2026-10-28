package io.github.velaliilunalii.orenucopia.mixin;

import io.github.velaliilunalii.orenucopia.CommonClass;
import io.github.velaliilunalii.orenucopia.item.CondensedExperienceItem;
import io.github.velaliilunalii.orenucopia.item.TourmalineItem;
import io.github.velaliilunalii.orenucopia.recipe.ForgeCrushingRecipe;
import io.github.velaliilunalii.orenucopia.util.ItemStackUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Optional;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(at = @At("HEAD"), method = "overrideStackedOnOther", cancellable = true)
    public void overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player, CallbackInfoReturnable<Boolean> cir){
        ItemStack itemStack = slot.getItem();
        if (slot.allowModification(player) && action == ClickAction.SECONDARY && !itemStack.isEmpty()) {
            if (stack.getItem() instanceof PickaxeItem) {
                Level level = player.level();

                SimpleContainer inputContainer = new SimpleContainer(1);
                SimpleContainer outputContainer = new SimpleContainer(3);

                inputContainer.addItem(itemStack.copy());

                Optional<ForgeCrushingRecipe> recipe = level.getRecipeManager()
                        .getRecipeFor(ForgeCrushingRecipe.Type.INSTANCE, inputContainer, level);

                if (recipe.isPresent()) {
                    player.playSound(CommonClass.CRUMBLE, 0.5F, 0.6F + player.level().getRandom().nextFloat() * 0.4F);
                    ForgeCrushingRecipe forgeRecipe = recipe.get();

                    if (forgeRecipe.matches(inputContainer, level)) {

                        inputContainer.removeItem(0, 1);
                        itemStack.shrink(1);

                        for (ItemStack result : forgeRecipe.getResults()) {
                            for (int i = 0; i < outputContainer.getContainerSize(); i++) {
                                ItemStack existing = outputContainer.getItem(i);
                                if (existing.isEmpty()) {
                                    outputContainer.setItem(i, result.copy());
                                    break;
                                } else if (ItemStack.isSameItem(existing, result) && existing.getCount() + result.getCount() <= existing.getMaxStackSize()) {
                                    existing.grow(result.getCount());
                                    break;
                                }
                            }
                        }
                    }

                    for (int i = 0; i < outputContainer.getContainerSize(); i++) {
                        ItemStack newStack = outputContainer.removeItem(i, outputContainer.getItem(i).getCount());
                        ItemStackUtil.smartAddItem(player, newStack);
                    }

                    cir.setReturnValue(true);
                    return;
                } else if (itemStack.is(CommonClass.CONDENSED_EXPERIENCE)) {
                    itemStack.shrink(1);

                    ItemStack givenStack = CondensedExperienceItem.getLoot();
                    ItemStackUtil.smartAddItem(player, givenStack);

                    if (givenStack.is(CommonClass.BLANK_TOURMALINE)) {
                        player.playSound(CommonClass.CHIME, 1.5F, 0.6F + player.level().getRandom().nextFloat() * 0.2F);
                    } else
                        player.playSound(CommonClass.CRUMBLE, 0.5F, 0.6F + player.level().getRandom().nextFloat() * 0.4F);

                    if (level instanceof ServerLevel serverLevel) {
                        ExperienceOrb.award(serverLevel, player.position(), 1 + player.getRandom().nextInt(2));
                    }

                    cir.setReturnValue(true);
                    return;
                } else if (itemStack.isDamageableItem()) {
                    player.playSound(CommonClass.METAL_HAMMER, 3F, 0.6F + player.level().getRandom().nextFloat() * 0.2F);

                    int scrapsToGive = Math.floorDiv(itemStack.getMaxDamage() - itemStack.getDamageValue() - 1, 128) + 1;
                    ItemStackUtil.smartAddItem(player, new ItemStack(CommonClass.SCRAPS, scrapsToGive));
                    if (level instanceof ServerLevel serverLevel) {

                        Map<Enchantment, Integer> enchantmentsMap = EnchantmentHelper.getEnchantments(itemStack);
                        for (Enchantment enchantment : enchantmentsMap.keySet()) {
                            ExperienceOrb.award(serverLevel, player.position(),
                                    enchantment.getMinCost(enchantmentsMap.get(enchantment)) / 5
                                            + 1 + player.getRandom().nextInt(2));
                        }
                    }
                    itemStack.shrink(1);

                    cir.setReturnValue(true);
                    return;
                } else if (itemStack.is(CommonClass.AURICHALCITE_CHUNK)) {
                    player.playSound(CommonClass.CRUMBLE, 0.5F, 0.6F + player.level().getRandom().nextFloat() * 0.4F);
                    itemStack.shrink(1);
                    ItemStackUtil.smartAddItem(player, new ItemStack(Items.RAW_COPPER, 12));

                    cir.setReturnValue(true);
                    return;
                }
            }

            if (stack.getItem() instanceof ShearsItem && TourmalineItem.hasTourmaline(itemStack) && !itemStack.is(CommonClass.TOURMALINE)) {
                player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);

                ItemStack tourmaline = new ItemStack(CommonClass.TOURMALINE);
                TourmalineItem.setTourmaline(tourmaline, TourmalineItem.removeTourmaline(itemStack));
                ItemStackUtil.smartAddItem(player, tourmaline);

                cir.setReturnValue(true);
                return;
            }
        }
        cir.setReturnValue(false);
    }
}
