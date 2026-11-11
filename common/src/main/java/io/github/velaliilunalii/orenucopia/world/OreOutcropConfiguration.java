package io.github.velaliilunalii.orenucopia.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public class OreOutcropConfiguration implements FeatureConfiguration {

    public static final Codec<OreOutcropConfiguration> CODEC = RecordCodecBuilder.create((p_160842_) -> {
        return p_160842_.group(ExtraCodecs.nonEmptyList(OreOutcropBlockSettings.CODEC.listOf()).fieldOf("blocks").forGetter((p_160868_) -> {
            return p_160868_.oreOutcropBlockSettings;
        })).apply(p_160842_, OreOutcropConfiguration::new);
    });

    public final List<OreOutcropBlockSettings> oreOutcropBlockSettings;

    public OreOutcropConfiguration(List<OreOutcropBlockSettings> oreOutcropBlockSettings) {
        this.oreOutcropBlockSettings = oreOutcropBlockSettings;
    }
}
