package io.github.velaliilunalii.orenucopia.world;

import io.github.velaliilunalii.orenucopia.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class ModFeatures {
    public static final Feature<NoneFeatureConfiguration> ORE_OUTCROP = new OreOutcropFeature(NoneFeatureConfiguration.CODEC);
    public static final Feature<NoneFeatureConfiguration> ECHO_TREASURE_GEODE = new EchoTreasureGeodeFeature(NoneFeatureConfiguration.CODEC);

    public static void registerModFeatures(){
        Registry.register(BuiltInRegistries.FEATURE, new ResourceLocation(Constants.MOD_ID, "ore_outcrop"), ORE_OUTCROP);
        Registry.register(BuiltInRegistries.FEATURE, new ResourceLocation(Constants.MOD_ID, "echo_treasure_geode"), ECHO_TREASURE_GEODE);
    }
}