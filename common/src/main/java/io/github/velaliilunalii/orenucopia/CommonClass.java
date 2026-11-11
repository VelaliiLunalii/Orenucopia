package io.github.velaliilunalii.orenucopia;

import io.github.velaliilunalii.orenucopia.block.EchoTreasureBlock;
import io.github.velaliilunalii.orenucopia.block.LargeOutcropBlock;
import io.github.velaliilunalii.orenucopia.block.SmallOutcropBlock;
import io.github.velaliilunalii.orenucopia.enchantment.ProspectingEnchantment;
import io.github.velaliilunalii.orenucopia.item.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.function.Supplier;

public class CommonClass {
    public static CreativeModeTab ORENUCOPIA_TAB;

    public static Block ECHO_TREASURE_BLOCK;
    public static final Supplier<Block> ECHO_TREASURE_BLOCK_SUPPLIER = () ->
            new EchoTreasureBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE));

    public static Block IRON_OUTCROP;
    public static final Supplier<Block> IRON_OUTCROP_SUPPLIER = () ->
            new LargeOutcropBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE).lightLevel(blockstate -> 6).hasPostProcess(CommonClass::always).emissiveRendering(CommonClass::always));
    public static Block REDSTONE_OUTCROP;
    public static final Supplier<Block> REDSTONE_OUTCROP_SUPPLIER = () ->
            new LargeOutcropBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_BLOCK).lightLevel(blockstate -> 6).hasPostProcess(CommonClass::always).emissiveRendering(CommonClass::always));
    public static Block LAPIS_OUTCROP;
    public static final Supplier<Block> LAPIS_OUTCROP_SUPPLIER = () ->
            new LargeOutcropBlock(BlockBehaviour.Properties.copy(Blocks.LAPIS_ORE).lightLevel(blockstate -> 6).hasPostProcess(CommonClass::always).emissiveRendering(CommonClass::always));
    public static Block GOLD_OUTCROP;
    public static final Supplier<Block> GOLD_OUTCROP_SUPPLIER = () ->
            new LargeOutcropBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_ORE).lightLevel(blockstate -> 6).hasPostProcess(CommonClass::always).emissiveRendering(CommonClass::always));
    public static Block DIAMOND_OUTCROP;
    public static final Supplier<Block> DIAMOND_OUTCROP_SUPPLIER = () ->
            new SmallOutcropBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE).lightLevel(blockstate -> 6).hasPostProcess(CommonClass::always).emissiveRendering(CommonClass::always));
    public static Block COPPER_OUTCROP;
    public static final Supplier<Block> COPPER_OUTCROP_SUPPLIER = () ->
            new LargeOutcropBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_ORE).lightLevel(blockstate -> 6).hasPostProcess(CommonClass::always).emissiveRendering(CommonClass::always));
    public static Block COAL_OUTCROP;
    public static final Supplier<Block> COAL_OUTCROP_SUPPLIER = () ->
            new SmallOutcropBlock(BlockBehaviour.Properties.copy(Blocks.COAL_ORE).lightLevel(blockstate -> 6).hasPostProcess(CommonClass::always).emissiveRendering(CommonClass::always));
    public static Block EMERALD_OUTCROP;
    public static final Supplier<Block> EMERALD_OUTCROP_SUPPLIER = () ->
            new SmallOutcropBlock(BlockBehaviour.Properties.copy(Blocks.EMERALD_ORE).lightLevel(blockstate -> 6).hasPostProcess(CommonClass::always).emissiveRendering(CommonClass::always));
    public static Block NETHER_GOLD_OUTCROP;
    public static final Supplier<Block> NETHER_GOLD_OUTCROP_SUPPLIER = () ->
            new LargeOutcropBlock(BlockBehaviour.Properties.copy(Blocks.NETHER_GOLD_ORE).lightLevel(blockstate -> 6).hasPostProcess(CommonClass::always).emissiveRendering(CommonClass::always));
    public static Block QUARTZ_OUTCROP;
    public static final Supplier<Block> QUARTZ_OUTCROP_SUPPLIER = () ->
            new LargeOutcropBlock(BlockBehaviour.Properties.copy(Blocks.NETHER_QUARTZ_ORE).lightLevel(blockstate -> 6).hasPostProcess(CommonClass::always).emissiveRendering(CommonClass::always));

    public static OutcropSeedItem RAW_IRON_NUGGET;
    public static final Supplier<OutcropSeedItem> RAW_IRON_NUGGET_SUPPLIER = () ->
            new OutcropSeedItem(new Item.Properties());
    public static OutcropSeedItem REDSTONE_NUGGET;
    public static final Supplier<OutcropSeedItem> REDSTONE_NUGGET_SUPPLIER = () ->
            new OutcropSeedItem(new Item.Properties());
    public static OutcropSeedItem LAPIS_NUGGET;
    public static final Supplier<OutcropSeedItem> LAPIS_NUGGET_SUPPLIER = () ->
            new OutcropSeedItem(new Item.Properties());
    public static OutcropSeedItem RAW_GOLD_NUGGET;
    public static final Supplier<OutcropSeedItem> RAW_GOLD_NUGGET_SUPPLIER = () ->
            new OutcropSeedItem(new Item.Properties());
    public static OutcropSeedItem DIAMOND_NUGGET;
    public static final Supplier<OutcropSeedItem> DIAMOND_NUGGET_SUPPLIER = () ->
            new OutcropSeedItem(new Item.Properties());
    public static OutcropSeedItem RAW_COPPER_NUGGET;
    public static final Supplier<OutcropSeedItem> RAW_COPPER_NUGGET_SUPPLIER = () ->
            new OutcropSeedItem(new Item.Properties());
    public static OutcropSeedItem COAL_NUGGET;
    public static final Supplier<OutcropSeedItem> COAL_NUGGET_SUPPLIER = () ->
            new OutcropSeedItem(new Item.Properties());
    public static OutcropSeedItem EMERALD_NUGGET;
    public static final Supplier<OutcropSeedItem> EMERALD_NUGGET_SUPPLIER = () ->
            new OutcropSeedItem(new Item.Properties());
    public static OutcropSeedItem QUARTZ_NUGGET;
    public static final Supplier<OutcropSeedItem> QUARTZ_NUGGET_SUPPLIER = () ->
            new OutcropSeedItem(new Item.Properties());

    public static Map<OutcropSeedItem, Block> SEED_BLOCK_MAP = new HashMap<>();

    public static Item AURICHALCITE_CHUNK;
    public static final Supplier<Item> AURICHALCITE_CHUNK_SUPPLIER = () -> new Item(new Item.Properties());
    public static Item CUPRITE_CHUNK;
    public static final Supplier<Item> CUPRITE_CHUNK_SUPPLIER = () -> new Item(new Item.Properties());
    public static Item LAZULITE_CHUNK;
    public static final Supplier<Item> LAZULITE_CHUNK_SUPPLIER = () -> new Item(new Item.Properties());
    public static Item LAZURITE_CHUNK;
    public static final Supplier<Item> LAZURITE_CHUNK_SUPPLIER = () -> new Item(new Item.Properties());
    public static Item PERIDOTITE_CHUNK;
    public static final Supplier<Item> PERIDOTITE_CHUNK_SUPPLIER = () -> new Item(new Item.Properties());
    public static Item PYRITE_CHUNK;
    public static final Supplier<Item> PYRITE_CHUNK_SUPPLIER = () -> new Item(new Item.Properties());
    public static Item COAL_CHUNK;
    public static final Supplier<Item> COAL_CHUNK_SUPPLIER = () -> new Item(new Item.Properties());
    public static Item TUMBAGA_CHUNK;
    public static final Supplier<Item> TUMBAGA_CHUNK_SUPPLIER = () -> new Item(new Item.Properties());
    public static Item VERDANITE_CHUNK;
    public static final Supplier<Item> VERDANITE_CHUNK_SUPPLIER = () -> new Item(new Item.Properties());

    public static ExperienceDustItem EXPERIENCE_DUST;
    public static final Supplier<ExperienceDustItem> EXPERIENCE_DUST_SUPPLIER = () -> new ExperienceDustItem(new Item.Properties());
    public static CondensedExperienceItem CONDENSED_EXPERIENCE;
    public static final Supplier<CondensedExperienceItem> CONDENSED_EXPERIENCE_SUPPLIER = () -> new CondensedExperienceItem(new Item.Properties());

    public static final FoodProperties ROCK_CANDY_FOOD = new FoodProperties.Builder().nutrition(5).alwaysEat()
            .saturationMod(0.6F).effect(new MobEffectInstance(MobEffects.DIG_SPEED, 400, 1), 1.0F).fast().build();
    public static Item ROCK_CANDY;
    public static final Supplier<Item> ROCK_CANDY_SUPPLIER = () -> new Item(new Item.Properties().food(ROCK_CANDY_FOOD));

    public static ScrapsItem SCRAPS;
    public static final Supplier<ScrapsItem> SCRAPS_SUPPLIER = () -> new ScrapsItem(new Item.Properties());
    public static TourmalineItem TOURMALINE;
    public static final Supplier<TourmalineItem> TOURMALINE_SUPPLIER = () -> new TourmalineItem(new Item.Properties());
    public static BlankTourmalineItem BLANK_TOURMALINE;
    public static final Supplier<BlankTourmalineItem> BLANK_TOURMALINE_SUPPLIER = () -> new BlankTourmalineItem(new Item.Properties());

    public static OrenucopiaItem ORENUCOPIA;
    public static final Supplier<OrenucopiaItem> ORENUCOPIA_SUPPLIER = () -> new OrenucopiaItem(new Item.Properties().stacksTo(1));



    //--------------------------------------------Particles------------------------------------------------------------

    public static SimpleParticleType TRANSGENDER_PARTICLE;
    public static SimpleParticleType LESBIAN_PARTICLE;
    public static SimpleParticleType BISEXUAL_PARTICLE;
    public static SimpleParticleType ASEXUAL_PARTICLE;
    public static SimpleParticleType NON_BINARY_PARTICLE;

    public static Map<String, SimpleParticleType> BLOCK_PARTICLE_MAP = new HashMap<>();

    //------------------------------------------Enchantments-----------------------------------------------------------

    public static Enchantment PROSPECTING_ENCHANTMENT;
    public static final Supplier<Enchantment> PROSPECTING_ENCHANTMENT_SUPPLIER = ProspectingEnchantment::new;

    //---------------------------------------------------Sounds--------------------------------------------------------

    public static SoundEvent CHIME;
    public static final Supplier<SoundEvent> CHIME_SUPPLIER = () ->
            SoundEvent.createVariableRangeEvent(new ResourceLocation(Constants.MOD_ID, "chime"));

    public static SoundEvent CRUMBLE;
    public static final Supplier<SoundEvent> CRUMBLE_SUPPLIER = () ->
            SoundEvent.createVariableRangeEvent(new ResourceLocation(Constants.MOD_ID, "crumble"));

    public static SoundEvent METAL_HAMMER;
    public static final Supplier<SoundEvent> METAL_HAMMER_SUPPLIER = () ->
            SoundEvent.createVariableRangeEvent(new ResourceLocation(Constants.MOD_ID, "metal_hammer"));

    public static final Set<String> TOURMALINES = Set.of(
            "transgender",
            "lesbian",
            "bisexual",
            "asexual",
            "non_binary"
    );

    public static final ResourceLocation ABANDONED_MINESHAFT_STRUCTURE_CHEST_ID
            = new ResourceLocation("minecraft", "chests/abandoned_mineshaft");
    public static final ResourceLocation RUINED_PORTAL_STRUCTURE_CHEST_ID
            = new ResourceLocation("minecraft", "chests/ruined_portal");
    public static final ResourceLocation SIMPLE_DUNGEON_STRUCTURE_CHEST_ID
            = new ResourceLocation("minecraft", "chests/simple_dungeon");
    public static final ResourceLocation STRONGHOLD_LIBRARY_STRUCTURE_CHEST_ID
            = new ResourceLocation("minecraft", "chests/stronghold_library");

    public static void init() {
        SEED_BLOCK_MAP.put(RAW_IRON_NUGGET, IRON_OUTCROP);
        SEED_BLOCK_MAP.put(REDSTONE_NUGGET, REDSTONE_OUTCROP);
        SEED_BLOCK_MAP.put(LAPIS_NUGGET, LAPIS_OUTCROP);
        SEED_BLOCK_MAP.put(RAW_GOLD_NUGGET, GOLD_OUTCROP);
        SEED_BLOCK_MAP.put(DIAMOND_NUGGET, DIAMOND_OUTCROP);
        SEED_BLOCK_MAP.put(RAW_COPPER_NUGGET, COPPER_OUTCROP);
        SEED_BLOCK_MAP.put(COAL_NUGGET, COAL_OUTCROP);
        SEED_BLOCK_MAP.put(EMERALD_NUGGET, EMERALD_OUTCROP);
        SEED_BLOCK_MAP.put(QUARTZ_NUGGET, QUARTZ_OUTCROP);

        BLOCK_PARTICLE_MAP.put("transgender", TRANSGENDER_PARTICLE);
        BLOCK_PARTICLE_MAP.put("lesbian", LESBIAN_PARTICLE);
        BLOCK_PARTICLE_MAP.put("bisexual", BISEXUAL_PARTICLE);
        BLOCK_PARTICLE_MAP.put("asexual", ASEXUAL_PARTICLE);
        BLOCK_PARTICLE_MAP.put("non_binary", NON_BINARY_PARTICLE);
    }

    private static boolean always(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return true;
    }

}