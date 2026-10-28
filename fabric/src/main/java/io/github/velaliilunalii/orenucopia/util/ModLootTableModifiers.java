package io.github.velaliilunalii.orenucopia.util;

import io.github.velaliilunalii.orenucopia.CommonClass;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import static io.github.velaliilunalii.orenucopia.CommonClass.*;

public class ModLootTableModifiers {
    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if(ABANDONED_MINESHAFT_STRUCTURE_CHEST_ID.equals(id) || RUINED_PORTAL_STRUCTURE_CHEST_ID.equals(id)
            || SIMPLE_DUNGEON_STRUCTURE_CHEST_ID.equals(id) || STRONGHOLD_LIBRARY_STRUCTURE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .conditionally(LootItemRandomChanceCondition.randomChance(0.5f).build())
                        .with(LootItem.lootTableItem(Items.ENCHANTED_BOOK).build())
                        .apply(new SetEnchantmentsFunction.Builder()
                                .withEnchantment(CommonClass.PROSPECTING_ENCHANTMENT, ConstantValue.exactly(1)));
                tableBuilder.pool(poolBuilder.build());
            }
        });
    }
}
