package io.github.velaliilunalii.orenucopia.item;

import io.github.velaliilunalii.orenucopia.CommonClass;
import io.github.velaliilunalii.orenucopia.util.ItemStackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ScrapsItem extends Item {
    public ScrapsItem(Properties $$0) {
        super($$0);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction clickAction, Player $player3) {
        if (slot.allowModification($player3) && clickAction == ClickAction.SECONDARY) {
            ItemStack itemStack = slot.getItem();

            if (!itemStack.isEmpty() && itemStack.isDamageableItem() && !ItemStackUtil.hasMaxReinforcement(itemStack)) {
                $player3.playSound(CommonClass.METAL_HAMMER, 3F, 1.0F + $player3.level().getRandom().nextFloat() * 0.4F);

                int remainingDamage = itemStack.getDamageValue() - 64;
                itemStack.setDamageValue(remainingDamage);

                int toReinforce = -remainingDamage/2;
                int reinforcementValue = ItemStackUtil.getReinforcementValue(itemStack);
                if (toReinforce > 0 && reinforcementValue < ItemStackUtil.getMaxReinforcementValue(itemStack)){
                    stack.shrink(1);
                    ItemStackUtil.setReinforcementValue(itemStack, reinforcementValue + toReinforce);
                    return true;
                }

                if (remainingDamage > 0){
                    stack.shrink(1);
                    return true;
                }
            }
        }

        return false;
    }

    public void appendHoverText(ItemStack $$0, Level $$1, List<Component> $$2, TooltipFlag $$3){
        $$2.add(Component.translatable("item.orenucopia.scraps.description").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        super.appendHoverText($$0, $$1, $$2, $$3);
    }
}
