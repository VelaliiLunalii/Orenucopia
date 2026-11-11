package io.github.velaliilunalii.orenucopia.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class OreOutcropBlockSettings {
    public static final Codec<OreOutcropBlockSettings> CODEC = RecordCodecBuilder.create((p_158307_) -> {
        return p_158307_.group(BlockState.CODEC.fieldOf("outcrop_block").forGetter((p_158313_) -> {
            return p_158313_.outcropBlock;
        }), TagKey.hashedCodec(Registries.BLOCK).fieldOf("base_blocks").forGetter((p_158313_) -> {
            return p_158313_.baseBlocks;
        }), Codec.FLOAT.fieldOf("grown_outcrop_chance").orElse(0.3F).forGetter((p_160850_) -> {
            return p_160850_.grownOutcropChance;
        }), Codec.FLOAT.fieldOf("growing_outcrop_chance").orElse(0.1F).forGetter((p_160850_) -> {
            return p_160850_.growingOutcropChance;
        }), Codec.FLOAT.fieldOf("trail_chance").orElse(0.15F).forGetter((p_160850_) -> {
            return p_160850_.trailChance;
        }), Codec.INT.fieldOf("max_trail_length").orElse(2).forGetter((p_160850_) -> {
            return p_160850_.maxTrailLength;
        })).apply(p_158307_, OreOutcropBlockSettings::new);
    });

    public final BlockState outcropBlock;
    public final TagKey<Block> baseBlocks;
    public final float grownOutcropChance;
    public final float growingOutcropChance;
    public final float trailChance;
    public final int maxTrailLength;

    public OreOutcropBlockSettings(BlockState outcropBlock, TagKey<Block> baseBlocks, float grownOutcropChance, float growingOutcropChance, float trailChance, int maxTrailLength) {
        this.outcropBlock = outcropBlock;
        this.baseBlocks = baseBlocks;
        this.grownOutcropChance = grownOutcropChance;
        this.growingOutcropChance = growingOutcropChance;
        this.trailChance = trailChance;
        this.maxTrailLength = maxTrailLength;
    }
}
