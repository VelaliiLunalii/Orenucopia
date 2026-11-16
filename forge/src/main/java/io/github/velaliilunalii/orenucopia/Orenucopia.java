package io.github.velaliilunalii.orenucopia;

import io.github.velaliilunalii.orenucopia.block.ThemedOutcropBlock;
import io.github.velaliilunalii.orenucopia.block.entity.EchoTreasureBlockEntity;
import io.github.velaliilunalii.orenucopia.item.*;
import io.github.velaliilunalii.orenucopia.particle.ColoredTerrainParticle;
import io.github.velaliilunalii.orenucopia.recipe.ForgeCrushingRecipe;
import io.github.velaliilunalii.orenucopia.world.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static io.github.velaliilunalii.orenucopia.CommonClass.*;

@Mod(Constants.MOD_ID)
public class Orenucopia {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Constants.MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Constants.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(Registries.ENCHANTMENT, Constants.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Constants.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MOD_ID);
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, Constants.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);


    public static final RegistryObject<Feature<OreOutcropConfiguration>> ORE_OUTCROP =
            FEATURES.register("ore_outcrop", () -> new OreOutcropFeature(OreOutcropConfiguration.CODEC));
    public static final RegistryObject<Feature<OreOutcropConfiguration>> NETHER_ORE_OUTCROP =
            FEATURES.register("nether_ore_outcrop", () -> new OreOutcropFeature(OreOutcropConfiguration.CODEC));


    public static final RegistryObject<Enchantment> PROSPECTING_ENCHANTMENT =
            ENCHANTMENTS.register("prospecting", CommonClass.PROSPECTING_ENCHANTMENT_SUPPLIER);

    public static final RegistryObject<CreativeModeTab> ORENUCOPIA_TAB =
            CREATIVE_MODE_TABS.register("orenucopia_tab", () ->
                    CreativeModeTab.builder()
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

    public static final RegistryObject<SimpleParticleType> TRANSGENDER_PARTICLE =
            PARTICLE_TYPES.register("transgender_particle", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> LESBIAN_PARTICLE =
            PARTICLE_TYPES.register("lesbian_particle", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BISEXUAL_PARTICLE =
            PARTICLE_TYPES.register("bisexual_particle", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> ASEXUAL_PARTICLE =
            PARTICLE_TYPES.register("asexual_particle", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> NON_BINARY_PARTICLE =
            PARTICLE_TYPES.register("non_binary_particle", () -> new SimpleParticleType(false));

    public static final RegistryObject<Block> IRON_OUTCROP = registerOutcropBlockWithItem(
            "iron_outcrop", "raw_iron_nugget", CommonClass.IRON_OUTCROP_SUPPLIER);
    public static final RegistryObject<Block> REDSTONE_OUTCROP = registerOutcropBlockWithItem(
            "redstone_outcrop", "redstone_nugget", CommonClass.REDSTONE_OUTCROP_SUPPLIER);
    public static final RegistryObject<Block> LAPIS_OUTCROP = registerOutcropBlockWithItem(
            "lapis_outcrop", "lapis_nugget", CommonClass.LAPIS_OUTCROP_SUPPLIER);
    public static final RegistryObject<Block> GOLD_OUTCROP = registerOutcropBlockWithItem(
            "gold_outcrop", "raw_gold_nugget", CommonClass.GOLD_OUTCROP_SUPPLIER);
    public static final RegistryObject<Block> DIAMOND_OUTCROP = registerOutcropBlockWithItem(
            "diamond_outcrop", "diamond_nugget", CommonClass.DIAMOND_OUTCROP_SUPPLIER);
    public static final RegistryObject<Block> COPPER_OUTCROP = registerOutcropBlockWithItem(
            "copper_outcrop", "raw_copper_nugget", CommonClass.COPPER_OUTCROP_SUPPLIER);
    public static final RegistryObject<Block> COAL_OUTCROP = registerOutcropBlockWithItem(
            "coal_outcrop", "coal_nugget", CommonClass.COAL_OUTCROP_SUPPLIER);
    public static final RegistryObject<Block> EMERALD_OUTCROP = registerOutcropBlockWithItem(
            "emerald_outcrop", "emerald_nugget", CommonClass.EMERALD_OUTCROP_SUPPLIER);
    public static final RegistryObject<Block> NETHER_GOLD_OUTCROP =
            BLOCKS.register("nether_gold_outcrop", CommonClass.GOLD_OUTCROP_SUPPLIER);
    public static final RegistryObject<Block> QUARTZ_OUTCROP = registerOutcropBlockWithItem(
            "quartz_outcrop", "quartz_nugget", CommonClass.QUARTZ_OUTCROP_SUPPLIER);

    public static final RegistryObject<Block> INFECTED_IRON_OUTCROP =
            BLOCKS.register("infected_iron_outcrop", () ->
                    new ThemedOutcropBlock(IRON_OUTCROP.get(), BlockBehaviour.Properties.copy(Blocks.IRON_ORE).lightLevel(blockstate -> 6).hasPostProcess(CommonClass::always).emissiveRendering(CommonClass::always)));

    public static final RegistryObject<Block> ECHO_TREASURE_BLOCK =
            BLOCKS.register("echo_treasure", CommonClass.ECHO_TREASURE_BLOCK_SUPPLIER);

    public static final RegistryObject<BlockEntityType<EchoTreasureBlockEntity>> ECHO_TREASURE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("echo_treasure_block_entity", () ->
                    BlockEntityType.Builder.of(
                            EchoTreasureBlockEntity::new,
                            ECHO_TREASURE_BLOCK.get()
                    ).build(null)
            );

    public static final RegistryObject<Item> AURICHALCITE_CHUNK =
            ITEMS.register("aurichalcite_chunk", CommonClass.AURICHALCITE_CHUNK_SUPPLIER);
    public static final RegistryObject<Item> CUPRITE_CHUNK =
            ITEMS.register("cuprite_chunk", CommonClass.CUPRITE_CHUNK_SUPPLIER);
    public static final RegistryObject<Item> LAZULITE_CHUNK =
            ITEMS.register("lazulite_chunk", CommonClass.LAZULITE_CHUNK_SUPPLIER);
    public static final RegistryObject<Item> LAZURITE_CHUNK =
            ITEMS.register("lazurite_chunk", CommonClass.LAZURITE_CHUNK_SUPPLIER);
    public static final RegistryObject<Item> PERIDOTITE_CHUNK =
            ITEMS.register("peridotite_chunk", CommonClass.PERIDOTITE_CHUNK_SUPPLIER);
    public static final RegistryObject<Item> PYRITE_CHUNK =
            ITEMS.register("pyrite_chunk", CommonClass.PYRITE_CHUNK_SUPPLIER);
    public static final RegistryObject<Item> COAL_CHUNK =
            ITEMS.register("coal_chunk", CommonClass.COAL_CHUNK_SUPPLIER);
    public static final RegistryObject<Item> TUMBAGA_CHUNK =
            ITEMS.register("tumbaga_chunk", CommonClass.TUMBAGA_CHUNK_SUPPLIER);
    public static final RegistryObject<Item> VERDANITE_CHUNK =
            ITEMS.register("verdanite_chunk", CommonClass.VERDANITE_CHUNK_SUPPLIER);

    public static final RegistryObject<ExperienceDustItem> EXPERIENCE_DUST =
            ITEMS.register("experience_dust", CommonClass.EXPERIENCE_DUST_SUPPLIER);
    public static final RegistryObject<CondensedExperienceItem> CONDENSED_EXPERIENCE =
            ITEMS.register("condensed_experience", CommonClass.CONDENSED_EXPERIENCE_SUPPLIER);

    public static final RegistryObject<Item> ROCK_CANDY =
            ITEMS.register("rock_candy", CommonClass.ROCK_CANDY_SUPPLIER);

    public static final RegistryObject<ScrapsItem> SCRAPS =
            ITEMS.register("scraps", CommonClass.SCRAPS_SUPPLIER);

    public static final RegistryObject<BlankTourmalineItem> BLANK_TOURMALINE =
            ITEMS.register("blank_tourmaline", CommonClass.BLANK_TOURMALINE_SUPPLIER);
    public static final RegistryObject<TourmalineItem> TOURMALINE =
            ITEMS.register("tourmaline", CommonClass.TOURMALINE_SUPPLIER);

    public static final RegistryObject<OrenucopiaItem> ORENUCOPIA =
            ITEMS.register("orenucopia", CommonClass.ORENUCOPIA_SUPPLIER);

    public static final RegistryObject<RecipeType<?>> FORGE_CRUSHING_TYPE =
            RECIPE_TYPES.register("forge_crushing", () -> ForgeCrushingRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeSerializer<?>> FORGE_CRUSHING_SERIALIZER =
            RECIPE_SERIALIZERS.register("forge_crushing", () -> ForgeCrushingRecipe.Serializer.INSTANCE);

    public static final RegistryObject<SoundEvent> CHIME =
            SOUND_EVENTS.register("chime", CommonClass.CHIME_SUPPLIER);
    public static final RegistryObject<SoundEvent> CRUMBLE =
            SOUND_EVENTS.register("crumble", CommonClass.CRUMBLE_SUPPLIER);
    public static final RegistryObject<SoundEvent> METAL_HAMMER =
            SOUND_EVENTS.register("metal_hammer", CommonClass.METAL_HAMMER_SUPPLIER);
    public static final RegistryObject<SoundEvent> SCULK_ECHO =
            SOUND_EVENTS.register("sculk_echo", CommonClass.SCULK_ECHO_SUPPLIER);

    public Orenucopia() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ITEMS.register(modBus);
        BLOCKS.register(modBus);
        PARTICLE_TYPES.register(modBus);
        SOUND_EVENTS.register(modBus);
        CREATIVE_MODE_TABS.register(modBus);
        ENCHANTMENTS.register(modBus);
        RECIPE_TYPES.register(modBus);
        RECIPE_SERIALIZERS.register(modBus);
        FEATURES.register(modBus);
        BLOCK_ENTITIES.register(modBus);

        modBus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CommonClass.PROSPECTING_ENCHANTMENT = PROSPECTING_ENCHANTMENT.get();

            CommonClass.ORENUCOPIA_TAB = ORENUCOPIA_TAB.get();

            CommonClass.TRANSGENDER_PARTICLE = TRANSGENDER_PARTICLE.get();
            CommonClass.LESBIAN_PARTICLE = LESBIAN_PARTICLE.get();
            CommonClass.BISEXUAL_PARTICLE = BISEXUAL_PARTICLE.get();
            CommonClass.ASEXUAL_PARTICLE = ASEXUAL_PARTICLE.get();
            CommonClass.NON_BINARY_PARTICLE = NON_BINARY_PARTICLE.get();

            CommonClass.IRON_OUTCROP = IRON_OUTCROP.get();
            CommonClass.REDSTONE_OUTCROP = REDSTONE_OUTCROP.get();
            CommonClass.LAPIS_OUTCROP = LAPIS_OUTCROP.get();
            CommonClass.GOLD_OUTCROP = GOLD_OUTCROP.get();
            CommonClass.DIAMOND_OUTCROP = DIAMOND_OUTCROP.get();
            CommonClass.COPPER_OUTCROP = COPPER_OUTCROP.get();
            CommonClass.COAL_OUTCROP = COAL_OUTCROP.get();
            CommonClass.EMERALD_OUTCROP = EMERALD_OUTCROP.get();
            CommonClass.NETHER_GOLD_OUTCROP = NETHER_GOLD_OUTCROP.get();
            CommonClass.QUARTZ_OUTCROP = QUARTZ_OUTCROP.get();

            CommonClass.INFECTED_IRON_OUTCROP = INFECTED_IRON_OUTCROP.get();
            CommonClass.ECHO_TREASURE_BLOCK = ECHO_TREASURE_BLOCK.get();

            CommonClass.ECHO_TREASURE_BLOCK_ENTITY = ECHO_TREASURE_BLOCK_ENTITY.get();

            CommonClass.AURICHALCITE_CHUNK = AURICHALCITE_CHUNK.get();
            CommonClass.CUPRITE_CHUNK = CUPRITE_CHUNK.get();
            CommonClass.LAZULITE_CHUNK = LAZULITE_CHUNK.get();
            CommonClass.LAZURITE_CHUNK = LAZURITE_CHUNK.get();
            CommonClass.PERIDOTITE_CHUNK = PERIDOTITE_CHUNK.get();
            CommonClass.PYRITE_CHUNK = PYRITE_CHUNK.get();
            CommonClass.COAL_CHUNK = COAL_CHUNK.get();
            CommonClass.TUMBAGA_CHUNK = TUMBAGA_CHUNK.get();
            CommonClass.VERDANITE_CHUNK = VERDANITE_CHUNK.get();

            CommonClass.EXPERIENCE_DUST = EXPERIENCE_DUST.get();
            CommonClass.CONDENSED_EXPERIENCE = CONDENSED_EXPERIENCE.get();

            CommonClass.ROCK_CANDY = ROCK_CANDY.get();

            CommonClass.SCRAPS = SCRAPS.get();

            CommonClass.BLANK_TOURMALINE = BLANK_TOURMALINE.get();
            CommonClass.TOURMALINE = TOURMALINE.get();

            CommonClass.ORENUCOPIA = ORENUCOPIA.get();

            CommonClass.LESBIAN_PARTICLE= LESBIAN_PARTICLE.get();
            CommonClass.TRANSGENDER_PARTICLE= TRANSGENDER_PARTICLE.get();
            CommonClass.BISEXUAL_PARTICLE= BISEXUAL_PARTICLE.get();
            CommonClass.ASEXUAL_PARTICLE= ASEXUAL_PARTICLE.get();
            CommonClass.NON_BINARY_PARTICLE= NON_BINARY_PARTICLE.get();

            CommonClass.CHIME = CHIME.get();
            CommonClass.CRUMBLE = CRUMBLE.get();
            CommonClass.METAL_HAMMER = METAL_HAMMER.get();
            CommonClass.SCULK_ECHO = SCULK_ECHO.get();

            CommonClass.init();
        });
    }

    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation id = event.getName();

        if(ABANDONED_MINESHAFT_STRUCTURE_CHEST_ID.equals(id) || RUINED_PORTAL_STRUCTURE_CHEST_ID.equals(id)
                || SIMPLE_DUNGEON_STRUCTURE_CHEST_ID.equals(id) || STRONGHOLD_LIBRARY_STRUCTURE_CHEST_ID.equals(id)) {
            event.getTable().addPool(
                    LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .when(LootItemRandomChanceCondition.randomChance(0.5f))
                            .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK))
                            .apply(new SetEnchantmentsFunction.Builder()
                                    .withEnchantment(CommonClass.PROSPECTING_ENCHANTMENT, ConstantValue.exactly(1)))
                            .build()
            );
        }
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class DataGenerators {
        @SubscribeEvent
        public static void gatherData(GatherDataEvent event) {
            DataGenerator generator = event.getGenerator();
            PackOutput packOutput = generator.getPackOutput();
            CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

            generator.addProvider(event.includeServer(), new ModWorldGenProvider(packOutput, lookupProvider));
        }
    }

    public static RegistryObject<Block> registerOutcropBlockWithItem(String blockName, String itemName, Supplier<Block> supplier){
        RegistryObject<Block> block = BLOCKS.register(blockName, supplier);
        ITEMS.register(itemName, () -> new OutcropSeedItem(block.get(), new Item.Properties()));
        return block;
    }
}