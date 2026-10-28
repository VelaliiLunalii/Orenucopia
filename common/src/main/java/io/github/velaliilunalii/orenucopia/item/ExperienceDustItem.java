package io.github.velaliilunalii.orenucopia.item;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.velaliilunalii.orenucopia.CommonClass;
import io.github.velaliilunalii.orenucopia.util.ItemStackUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;

public class ExperienceDustItem extends Item {
    public ExperienceDustItem(Properties $$0) {
        super($$0);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction clickAction, Player $player3) {
        if (slot.allowModification($player3) && clickAction == ClickAction.SECONDARY) {
            ItemStack itemStack = slot.getItem();
            if (!itemStack.isEmpty() && itemStack.is(CommonClass.BLANK_TOURMALINE)) {
                $player3.playSound(CommonClass.CHIME, 1.5F, 1.0F + $player3.level().getRandom().nextFloat() * 0.2F);

                giveTourmaline($player3);

                itemStack.shrink(1);
                stack.shrink(1);

                return true;
            }
        }

        return false;
    }

    public static void giveTourmaline(Player player){
        ArrayList<String> fullList = new ArrayList<>(CommonClass.TOURMALINES);

        //Check duplicates
        ArrayList<String> subtractedList = new ArrayList<>(fullList);
        for (ItemStack stack : player.getInventory().items){
            if (stack.is(CommonClass.TOURMALINE)) subtractedList.remove(TourmalineItem.getTourmaline(stack));
        }
        if (subtractedList.isEmpty()){
            subtractedList = fullList;
        }

        String tourmalineType = subtractedList.get(player.getRandom().nextInt(subtractedList.size()));

        ItemStack newStack = new ItemStack(CommonClass.TOURMALINE);
        TourmalineItem.setTourmaline(newStack, tourmalineType);
        ItemStackUtil.smartAddItem(player, newStack);
    }
}