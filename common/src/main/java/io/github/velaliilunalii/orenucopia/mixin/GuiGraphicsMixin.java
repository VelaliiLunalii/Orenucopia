package io.github.velaliilunalii.orenucopia.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import io.github.velaliilunalii.orenucopia.util.ItemStackUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Inject(at = @At("HEAD"), method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V")
    public void renderGuiItemDecorations(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
        GuiGraphics guiGraphics = (GuiGraphics) (Object) this;

        if (!stack.isEmpty()) {
            if (ItemStackUtil.isReinforcementBarVisible(stack)) {
                guiGraphics.pose().pushPose();
                int l = ItemStackUtil.getReinforcementBarWidth(stack);
                int i = Mth.hsvToRgb(185/360f, 0.55f, 0.9f);
                int j = x + 2;
                int k = y + 13;
                guiGraphics.fill(RenderType.guiOverlay(), j, k, j + 13, k + 2, -16777216);
                guiGraphics.fill(RenderType.guiOverlay(), j, k, j + l, k + 1, i | -16777216);
                guiGraphics.pose().popPose();
            }
        }
    }
}
