package io.github.velaliilunalii.orenucopia.util;


import io.github.velaliilunalii.orenucopia.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> ORENUCOPIA_ITEM_ALLOWED = tag("orenucopia_item_allowed");

        private static TagKey<Item> tag(String name) {
            return TagKey.create(Registries.ITEM, new ResourceLocation(Constants.MOD_ID, name));
        }
    }
}