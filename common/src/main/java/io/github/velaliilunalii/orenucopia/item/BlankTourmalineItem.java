package io.github.velaliilunalii.orenucopia.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class BlankTourmalineItem extends Item {
    public BlankTourmalineItem(Properties $$0) {
        super($$0);
    }

    public void appendHoverText(ItemStack $$0, Level $$1, List<Component> $$2, TooltipFlag $$3){
        $$2.add(Component.translatable("item.orenucopia.blank_tourmaline.description").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        super.appendHoverText($$0, $$1, $$2, $$3);
    }
}
