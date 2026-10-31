package io.github.velaliilunalii.orenucopia.item;

import io.github.velaliilunalii.orenucopia.CommonClass;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.*;

public class CondensedExperienceItem extends Item {
    public CondensedExperienceItem(Properties $$0) {
        super($$0);
    }

    public void appendHoverText(ItemStack $$0, Level $$1, List<Component> $$2, TooltipFlag $$3){
        $$2.add(Component.translatable("item.orenucopia.ore.description").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        super.appendHoverText($$0, $$1, $$2, $$3);
    }

    public static ItemStack getLoot(){
        Set<LootInstance> lootInstances = Set.of(
                new LootInstance(Items.COAL, 4, 2, 4),
                new LootInstance(Items.RAW_COPPER, 4, 2, 6),
                new LootInstance(Items.RAW_IRON, 3, 1, 3),
                new LootInstance(Items.RAW_GOLD, 3, 1, 3),
                new LootInstance(Items.REDSTONE, 3, 2, 6),
                new LootInstance(Items.LAPIS_LAZULI, 3, 2, 6),
                new LootInstance(Items.EMERALD, 2, 1, 2),
                new LootInstance(Items.DIAMOND, 1, 1, 2),
                new LootInstance(Items.QUARTZ, 2, 1, 2),
                new LootInstance(CommonClass.COAL_CHUNK, 1, 1, 2),
                new LootInstance(CommonClass.CUPRITE_CHUNK, 1, 1, 3),
                new LootInstance(CommonClass.LAZULITE_CHUNK, 1, 1, 3),
                new LootInstance(CommonClass.LAZURITE_CHUNK, 1, 1, 2),
                new LootInstance(CommonClass.PERIDOTITE_CHUNK, 1, 1, 1),
                new LootInstance(CommonClass.PYRITE_CHUNK, 1, 1, 2),
                new LootInstance(CommonClass.TUMBAGA_CHUNK, 1, 1, 2),
                new LootInstance(CommonClass.VERDANITE_CHUNK, 1, 1, 2),
                new LootInstance(CommonClass.AURICHALCITE_CHUNK, 1, 1, 3),
                new LootInstance(CommonClass.BLANK_TOURMALINE, 1, 1, 1)
        );

        int totalWeight = lootInstances.stream().map(LootInstance::getWeight).reduce(0, Integer::sum);

        int chosenPosition = new Random().nextInt(totalWeight);

        int position = 0;
        for (LootInstance lootInstance: lootInstances){
            position = position + lootInstance.getWeight();
            if (chosenPosition < position) return lootInstance.getStack();
        }

        return null;
    }

    public static class LootInstance{
        private final Item item;
        private final int weight;
        private final int lowBound;
        private final int highBound;

        public LootInstance(Item item, int weight, int lowBound, int highBound){
            this.item = item;
            this.weight = weight;
            this.lowBound = lowBound;
            this.highBound = highBound;
        }

        public int getWeight() {
            return weight;
        }

        public ItemStack getStack(){
            int count = new Random().nextInt(lowBound, highBound +1);
            return new ItemStack(item, count);
        }
    }
}
