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

public class TourmalineItem extends Item {
    public TourmalineItem(Properties $$0) {
        super($$0);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction clickAction, Player $player3) {
        if (slot.allowModification($player3) && clickAction == ClickAction.SECONDARY) {
            ItemStack itemStack = slot.getItem();
            if (!itemStack.isEmpty() && itemStack.isDamageableItem()) {
                $player3.playSound(CommonClass.CHIME, 1.5F, 0.4F + $player3.level().getRandom().nextFloat() * 0.2F);

                if (hasTourmaline(itemStack)){
                    ItemStack tourmaline = new ItemStack(CommonClass.TOURMALINE);
                    TourmalineItem.setTourmaline(tourmaline, TourmalineItem.removeTourmaline(itemStack));
                    ItemStackUtil.smartAddItem($player3, tourmaline);
                }

                setTourmaline(itemStack, getTourmaline(stack));
                stack.shrink(1);

                return true;
            }
        }

        return false;
    }

    public static boolean hasTourmaline(ItemStack stack) {
        return stack.getTag() != null && !stack.getTag().getString("Tourmaline").isEmpty();
    }

    public static String getTourmaline(ItemStack stack) {
        return stack.getTag() == null ? "" : stack.getTag().getString("Tourmaline");
    }

    public static void setTourmaline(ItemStack stack, String tourmaline) {
        stack.getOrCreateTag().putString("Tourmaline", tourmaline);
    }

    public static String removeTourmaline(ItemStack stack) {
        String tourmaline = getTourmaline(stack);
        stack.getOrCreateTag().remove("Tourmaline");
        return tourmaline;
    }

    public void appendHoverText(ItemStack $$0, Level $$1, List<Component> $$2, TooltipFlag $$3){
        $$2.add(Component.translatable("item.orenucopia.tourmaline.description.1").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        $$2.add(Component.translatable("item.orenucopia.tourmaline.description.2").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        super.appendHoverText($$0, $$1, $$2, $$3);
    }
}