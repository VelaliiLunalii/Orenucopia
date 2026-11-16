package io.github.velaliilunalii.orenucopia.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;

public class OutcropVariantBlockSettings {
    public static final Codec<OutcropVariantBlockSettings> CODEC = RecordCodecBuilder.create((p_158307_) -> {
        return p_158307_.group(BlockState.CODEC.fieldOf("outcrop_variant_block").forGetter((p_158313_) -> {
            return p_158313_.outcropVariantBlock;
        }), Codec.INT.fieldOf("outcrop_variant_weight").orElse(1).forGetter((p_160850_) -> {
            return p_160850_.outcropVariantWeight;
        })).apply(p_158307_, OutcropVariantBlockSettings::new);
    });

    public final BlockState outcropVariantBlock;
    public final int outcropVariantWeight;

    public OutcropVariantBlockSettings(BlockState outcropVariantBlock, int outcropVariantWeight) {
        this.outcropVariantBlock = outcropVariantBlock;
        this.outcropVariantWeight = outcropVariantWeight;
    }
}
