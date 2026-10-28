package io.github.velaliilunalii.orenucopia.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.nio.file.Path;

public class ItemStackUtil {
    public static BlockPos getTreasurePos(ItemStack stack){
        int[] ints = stack.getOrCreateTag().getIntArray("TreasurePos");
        return ints.length >0 ? new BlockPos(ints[0], ints[1], ints[2]) : null;
    }

    public static void smartAddItem(Player player, ItemStack itemStack){
        boolean hasSpace = player.getInventory().getSlotWithRemainingSpace(itemStack) != -1
                || player.getInventory().getFreeSlot() != -1;
        if (hasSpace) player.addItem(itemStack);
        else {
            ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), itemStack);
            itemEntity.setDefaultPickUpDelay();
            player.level().addFreshEntity(itemEntity);
        }
    }

    public static int getReinforcementValue(ItemStack stack) {
        return stack.getTag() == null ? 0 : stack.getTag().getInt("Reinforcement");
    }

    public static void setReinforcementValue(ItemStack stack, int $$0) {
        stack.getOrCreateTag().putInt("Reinforcement", Math.min(getMaxReinforcementValue(stack), $$0));
    }

    public static int getMaxReinforcementValue(ItemStack stack) {
        return 512;
    }

    public static boolean hasMaxReinforcement(ItemStack stack) {
        return getReinforcementValue(stack) >= getMaxReinforcementValue(stack);
    }

    public static boolean isReinforcementBarVisible(ItemStack $$0) {
        return $$0.isDamageableItem() && getReinforcementValue($$0) > 0;
    }

    public static int getReinforcementBarWidth(ItemStack $$0) {
        return Math.round((float)getReinforcementValue($$0) * 13.0F / (float) getMaxReinforcementValue($$0));
    }

    public static int getReinforcementBarColor(ItemStack $$0) {
        float $$1 = Math.max(0.0F, ((float)$$0.getItem().getMaxDamage() - (float)getReinforcementValue($$0)) / (float)$$0.getItem().getMaxDamage());
        return Mth.hsvToRgb($$1 / 3.0F, 1.0F, 1.0F);
    }
}
