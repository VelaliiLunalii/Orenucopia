package io.github.velaliilunalii.orenucopia.world;

import com.mojang.serialization.Codec;
import io.github.velaliilunalii.orenucopia.CommonClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import static io.github.velaliilunalii.orenucopia.block.OutcropBlock.*;
import static net.minecraft.world.level.block.Blocks.DEEPSLATE;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class EchoTreasureGeodeFeature extends Feature<NoneFeatureConfiguration> {

    public EchoTreasureGeodeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();

        int placed = 0;

        Block outcrop = CommonClass.randomOutcrop();
        BlockPos newPos;
        for (Direction direction : Direction.values()){
            newPos = origin.relative(direction);
            BlockState state = level.getBlockState(newPos);

            if (state.is(DEEPSLATE)){
                level.setBlock(newPos, outcrop.defaultBlockState()
                        .setValue(AGE, 1)
                        .setValue(AGING, false)
                        .setValue(BlockStateProperties.FACING, direction.getOpposite())
                        .setValue(ROTATION_FACING, getRandomOrthogonalDirection(direction.getOpposite()))
                        .setValue(WATERLOGGED, false), 2);
                placed ++;
            }
        }

        return placed > 0;
    }
}
