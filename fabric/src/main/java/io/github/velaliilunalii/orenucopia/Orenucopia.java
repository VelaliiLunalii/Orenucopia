package io.github.velaliilunalii.orenucopia;

import io.github.velaliilunalii.orenucopia.item.OutcropSeedItem;
import io.github.velaliilunalii.orenucopia.item.OrenucopiaItem;
import io.github.velaliilunalii.orenucopia.item.TourmalineItem;
import io.github.velaliilunalii.orenucopia.recipe.ForgeCrushingRecipe;
import io.github.velaliilunalii.orenucopia.util.ModLootTableModifiers;
import io.github.velaliilunalii.orenucopia.world.ModFeatures;
import io.github.velaliilunalii.orenucopia.world.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

import static io.github.velaliilunalii.orenucopia.CommonClass.*;
import static io.github.velaliilunalii.orenucopia.CommonClass.ASEXUAL_PARTICLE;
import static io.github.velaliilunalii.orenucopia.CommonClass.AURICHALCITE_CHUNK;
import static io.github.velaliilunalii.orenucopia.CommonClass.AURICHALCITE_CHUNK_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.BISEXUAL_PARTICLE;
import static io.github.velaliilunalii.orenucopia.CommonClass.BLANK_TOURMALINE;
import static io.github.velaliilunalii.orenucopia.CommonClass.BLANK_TOURMALINE_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.COAL_CHUNK;
import static io.github.velaliilunalii.orenucopia.CommonClass.COAL_CHUNK_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.COAL_OUTCROP;
import static io.github.velaliilunalii.orenucopia.CommonClass.COAL_OUTCROP_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.CONDENSED_EXPERIENCE;
import static io.github.velaliilunalii.orenucopia.CommonClass.CONDENSED_EXPERIENCE_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.COPPER_OUTCROP;
import static io.github.velaliilunalii.orenucopia.CommonClass.COPPER_OUTCROP_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.CUPRITE_CHUNK;
import static io.github.velaliilunalii.orenucopia.CommonClass.CUPRITE_CHUNK_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.DIAMOND_OUTCROP;
import static io.github.velaliilunalii.orenucopia.CommonClass.DIAMOND_OUTCROP_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.ECHO_TREASURE_BLOCK;
import static io.github.velaliilunalii.orenucopia.CommonClass.ECHO_TREASURE_BLOCK_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.EMERALD_OUTCROP;
import static io.github.velaliilunalii.orenucopia.CommonClass.EMERALD_OUTCROP_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.EXPERIENCE_DUST;
import static io.github.velaliilunalii.orenucopia.CommonClass.EXPERIENCE_DUST_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.GOLD_OUTCROP;
import static io.github.velaliilunalii.orenucopia.CommonClass.GOLD_OUTCROP_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.IRON_OUTCROP;
import static io.github.velaliilunalii.orenucopia.CommonClass.IRON_OUTCROP_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.LAPIS_OUTCROP;
import static io.github.velaliilunalii.orenucopia.CommonClass.LAPIS_OUTCROP_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.LAZULITE_CHUNK;
import static io.github.velaliilunalii.orenucopia.CommonClass.LAZULITE_CHUNK_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.LAZURITE_CHUNK;
import static io.github.velaliilunalii.orenucopia.CommonClass.LAZURITE_CHUNK_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.NON_BINARY_PARTICLE;
import static io.github.velaliilunalii.orenucopia.CommonClass.PERIDOTITE_CHUNK;
import static io.github.velaliilunalii.orenucopia.CommonClass.PERIDOTITE_CHUNK_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.PYRITE_CHUNK;
import static io.github.velaliilunalii.orenucopia.CommonClass.PYRITE_CHUNK_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.REDSTONE_OUTCROP;
import static io.github.velaliilunalii.orenucopia.CommonClass.REDSTONE_OUTCROP_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.ROCK_CANDY;
import static io.github.velaliilunalii.orenucopia.CommonClass.ROCK_CANDY_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.SCRAPS;
import static io.github.velaliilunalii.orenucopia.CommonClass.SCRAPS_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.TOURMALINE;
import static io.github.velaliilunalii.orenucopia.CommonClass.TOURMALINE_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.TUMBAGA_CHUNK;
import static io.github.velaliilunalii.orenucopia.CommonClass.TUMBAGA_CHUNK_SUPPLIER;
import static io.github.velaliilunalii.orenucopia.CommonClass.VERDANITE_CHUNK;
import static io.github.velaliilunalii.orenucopia.CommonClass.VERDANITE_CHUNK_SUPPLIER;

public class Orenucopia implements ModInitializer {

    @Override
    public void onInitialize() {
        registerItems();
        registerBlocks();
        registerParticles();
        registerSounds();
        registerRecipes();
        registerEnchantments();

        ModFeatures.registerModFeatures();
        ModWorldGeneration.registerWorldGeneration();
        ModLootTableModifiers.modifyLootTables();

        registerCreativeModeTab();

        CommonClass.init();
    }

    public static void registerEnchantments(){
        PROSPECTING_ENCHANTMENT = Registry.register(
                BuiltInRegistries.ENCHANTMENT,
                new ResourceLocation(Constants.MOD_ID, "prospecting"),
                CommonClass.PROSPECTING_ENCHANTMENT_SUPPLIER.get()
        );
    }

    public static void registerCreativeModeTab(){
        CommonClass.ORENUCOPIA_TAB = Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                new ResourceLocation(Constants.MOD_ID, "orenucopia_tab"),
                CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                        .title(Component.translatable("creative_mode_tab.orenucopia.orenucopia_tab"))
                        .icon(OrenucopiaItem::getFilledOrenucopia)
                        .displayItems((parameters, output) -> {
                            output.accept(CommonClass.IRON_OUTCROP);
                            output.accept(CommonClass.REDSTONE_OUTCROP);
                            output.accept(CommonClass.LAPIS_OUTCROP);
                            output.accept(CommonClass.GOLD_OUTCROP);
                            output.accept(CommonClass.DIAMOND_OUTCROP);
                            output.accept(CommonClass.COPPER_OUTCROP);
                            output.accept(CommonClass.COAL_OUTCROP);
                            output.accept(CommonClass.EMERALD_OUTCROP);
                            output.accept(CommonClass.QUARTZ_OUTCROP);

                            output.accept(CommonClass.AURICHALCITE_CHUNK);
                            output.accept(CommonClass.CUPRITE_CHUNK);
                            output.accept(CommonClass.LAZULITE_CHUNK);
                            output.accept(CommonClass.LAZURITE_CHUNK);
                            output.accept(CommonClass.PERIDOTITE_CHUNK);
                            output.accept(CommonClass.PYRITE_CHUNK);
                            output.accept(CommonClass.COAL_CHUNK);
                            output.accept(CommonClass.TUMBAGA_CHUNK);
                            output.accept(CommonClass.VERDANITE_CHUNK);

                            output.accept(CommonClass.EXPERIENCE_DUST);
                            output.accept(CommonClass.CONDENSED_EXPERIENCE);
                            output.accept(CommonClass.ROCK_CANDY);
                            output.accept(CommonClass.SCRAPS);
                            output.accept(CommonClass.BLANK_TOURMALINE);

                            output.accept(CommonClass.ORENUCOPIA);
                        })
                        .build()
        );
    }

    public static void registerParticles() {
        TRANSGENDER_PARTICLE = Registry.register(
                BuiltInRegistries.PARTICLE_TYPE,
                new ResourceLocation(Constants.MOD_ID, "transgender_particle"),
                FabricParticleTypes.simple()
        );
        LESBIAN_PARTICLE = Registry.register(
                BuiltInRegistries.PARTICLE_TYPE,
                new ResourceLocation(Constants.MOD_ID, "lesbian_particle"),
                FabricParticleTypes.simple()
        );
        BISEXUAL_PARTICLE = Registry.register(
                BuiltInRegistries.PARTICLE_TYPE,
                new ResourceLocation(Constants.MOD_ID, "bisexual_particle"),
                FabricParticleTypes.simple()
        );
        ASEXUAL_PARTICLE = Registry.register(
                BuiltInRegistries.PARTICLE_TYPE,
                new ResourceLocation(Constants.MOD_ID, "asexual_particle"),
                FabricParticleTypes.simple()
        );
        NON_BINARY_PARTICLE = Registry.register(
                BuiltInRegistries.PARTICLE_TYPE,
                new ResourceLocation(Constants.MOD_ID, "non_binary_particle"),
                FabricParticleTypes.simple()
        );
    }

    public static void registerBlocks() {
        IRON_OUTCROP = registerOutcropBlockWithItem("iron_outcrop", "raw_iron_nugget", IRON_OUTCROP_SUPPLIER);
        REDSTONE_OUTCROP = registerOutcropBlockWithItem("redstone_outcrop", "redstone_nugget", REDSTONE_OUTCROP_SUPPLIER);
        LAPIS_OUTCROP = registerOutcropBlockWithItem("lapis_outcrop", "lapis_nugget", LAPIS_OUTCROP_SUPPLIER);
        GOLD_OUTCROP = registerOutcropBlockWithItem("gold_outcrop", "raw_gold_nugget", GOLD_OUTCROP_SUPPLIER);
        DIAMOND_OUTCROP = registerOutcropBlockWithItem("diamond_outcrop", "diamond_nugget", DIAMOND_OUTCROP_SUPPLIER);
        COPPER_OUTCROP = registerOutcropBlockWithItem("copper_outcrop", "raw_copper_nugget", COPPER_OUTCROP_SUPPLIER);
        COAL_OUTCROP = registerOutcropBlockWithItem("coal_outcrop", "coal_nugget", COAL_OUTCROP_SUPPLIER);
        EMERALD_OUTCROP = registerOutcropBlockWithItem("emerald_outcrop", "emerald_nugget", EMERALD_OUTCROP_SUPPLIER);
        NETHER_GOLD_OUTCROP = registerBlock("nether_gold_outcrop", NETHER_GOLD_OUTCROP_SUPPLIER);
        QUARTZ_OUTCROP = registerOutcropBlockWithItem("quartz_outcrop", "quartz_nugget", QUARTZ_OUTCROP_SUPPLIER);

        INFECTED_IRON_OUTCROP = registerBlock("infected_iron_outcrop", INFECTED_IRON_OUTCROP_SUPPLIER);

        ECHO_TREASURE_BLOCK = registerBlock("echo_treasure", ECHO_TREASURE_BLOCK_SUPPLIER);
    }

    public static void registerItems() {

        AURICHALCITE_CHUNK = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, "aurichalcite_chunk"),
                AURICHALCITE_CHUNK_SUPPLIER.get());
        CUPRITE_CHUNK = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, "cuprite_chunk"),
                CUPRITE_CHUNK_SUPPLIER.get());
        LAZULITE_CHUNK = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, "lazulite_chunk"),
                LAZULITE_CHUNK_SUPPLIER.get());
        LAZURITE_CHUNK = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, "lazurite_chunk"),
                LAZURITE_CHUNK_SUPPLIER.get());
        PERIDOTITE_CHUNK = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, "peridotite_chunk"),
                PERIDOTITE_CHUNK_SUPPLIER.get());
        PYRITE_CHUNK = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, "pyrite_chunk"),
                PYRITE_CHUNK_SUPPLIER.get());
        COAL_CHUNK = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, "coal_chunk"),
                COAL_CHUNK_SUPPLIER.get());
        TUMBAGA_CHUNK = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, "tumbaga_chunk"),
                TUMBAGA_CHUNK_SUPPLIER.get());
        VERDANITE_CHUNK = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, "verdanite_chunk"),
                VERDANITE_CHUNK_SUPPLIER.get());

        EXPERIENCE_DUST = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, "experience_dust"),
                EXPERIENCE_DUST_SUPPLIER.get());
        CONDENSED_EXPERIENCE = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, "condensed_experience"),
                CONDENSED_EXPERIENCE_SUPPLIER.get());

        ROCK_CANDY = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, "rock_candy"),
                ROCK_CANDY_SUPPLIER.get());

        SCRAPS = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, "scraps"),
                SCRAPS_SUPPLIER.get());

        BLANK_TOURMALINE = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, "blank_tourmaline"),
                BLANK_TOURMALINE_SUPPLIER.get());
        TOURMALINE = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, "tourmaline"),
                TOURMALINE_SUPPLIER.get());
        registerTransgenderTourmalineProperty(TOURMALINE);
        registerLesbianTourmalineProperty(TOURMALINE);
        registerBisexualTourmalineProperty(TOURMALINE);
        registerAsexualTourmalineProperty(TOURMALINE);
        registerNonBinaryTourmalineProperty(TOURMALINE);

        ORENUCOPIA = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, "orenucopia"),
                ORENUCOPIA_SUPPLIER.get());
        registerOrenucopiaItemProperty(ORENUCOPIA);
    }

    public static void registerRecipes(){
        Registry.register(
                BuiltInRegistries.RECIPE_TYPE,
                new ResourceLocation("orenucopia", "forge_crushing"),
                ForgeCrushingRecipe.Type.INSTANCE
        );

        Registry.register(
                BuiltInRegistries.RECIPE_SERIALIZER,
                new ResourceLocation("orenucopia", "forge_crushing"),
                ForgeCrushingRecipe.Serializer.INSTANCE
        );
    }

    public static void registerSounds() {
        CHIME = Registry.register(BuiltInRegistries.SOUND_EVENT, new ResourceLocation(Constants.MOD_ID, "chime"),
                CHIME_SUPPLIER.get());
        CRUMBLE = Registry.register(BuiltInRegistries.SOUND_EVENT, new ResourceLocation(Constants.MOD_ID, "crumble"),
                CRUMBLE_SUPPLIER.get());
        METAL_HAMMER = Registry.register(BuiltInRegistries.SOUND_EVENT, new ResourceLocation(Constants.MOD_ID, "metal_hammer"),
                METAL_HAMMER_SUPPLIER.get());
    }

    public static Block registerBlock(String name, Supplier<Block> supplier){
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Constants.MOD_ID, name), supplier.get());
    }

    public static Block registerOutcropBlockWithItem(String blockName, String itemName, Supplier<Block> supplier){
        Block block = registerBlock(blockName, supplier);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, itemName),
                new OutcropSeedItem(block, new Item.Properties()));
        return block;
    }

    private static void registerTransgenderTourmalineProperty(Item item) {
        ItemProperties.register(item,
                new ResourceLocation(Constants.MOD_ID, "transgender"),
                (stack, level, entity, seed) -> {
                    return TourmalineItem.getTourmaline(stack).equals("transgender") ? 1 : 0;
                }
        );
    }

    private static void registerLesbianTourmalineProperty(Item item) {
        ItemProperties.register(item,
                new ResourceLocation(Constants.MOD_ID, "lesbian"),
                (stack, level, entity, seed) -> {
                    return TourmalineItem.getTourmaline(stack).equals("lesbian") ? 1 : 0;
                }
        );
    }

    private static void registerBisexualTourmalineProperty(Item item) {
        ItemProperties.register(item,
                new ResourceLocation(Constants.MOD_ID, "bisexual"),
                (stack, level, entity, seed) -> {
                    return TourmalineItem.getTourmaline(stack).equals("bisexual") ? 1 : 0;
                }
        );
    }

    private static void registerAsexualTourmalineProperty(Item item) {
        ItemProperties.register(item,
                new ResourceLocation(Constants.MOD_ID, "asexual"),
                (stack, level, entity, seed) -> {
                    return TourmalineItem.getTourmaline(stack).equals("asexual") ? 1 : 0;
                }
        );
    }

    private static void registerNonBinaryTourmalineProperty(Item item) {
        ItemProperties.register(item,
                new ResourceLocation(Constants.MOD_ID, "non_binary"),
                (stack, level, entity, seed) -> {
                    return TourmalineItem.getTourmaline(stack).equals("non_binary") ? 1 : 0;
                }
        );
    }

    private static void registerOrenucopiaItemProperty(Item item) {
        ItemProperties.register(item,
                new ResourceLocation(Constants.MOD_ID, "orenucopia"),
                (stack, level, entity, seed) -> {
                    return OrenucopiaItem.hasContent(stack) ? 1 : 0;
                }
        );
    }
}
