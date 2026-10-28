package io.github.velaliilunalii.orenucopia.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import static io.github.velaliilunalii.orenucopia.Constants.MOD_ID;

public class ModWorldGeneration {
    public static final ResourceKey<PlacedFeature> ORE_OUTCROP_PLACED_KEY =
            ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(MOD_ID, "ore_outcrop"));

    public static void registerWorldGeneration() {
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ORE_OUTCROP_PLACED_KEY
        );
    }
}
