package io.github.velaliilunalii.orenucopia.mixin;

import io.github.velaliilunalii.orenucopia.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class OreItemMixin {
    @Inject(at = @At("HEAD"), method = "appendHoverText")
    public void appendHoverText(ItemStack stack, Level $$1, List<Component> tooltip, TooltipFlag $$3, CallbackInfo ci){
        if (stack.is(ModTags.Items.ORENUCOPIA_ITEM_ALLOWED)) {
            tooltip.add(Component.translatable("item.orenucopia.ore.description").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }
}
