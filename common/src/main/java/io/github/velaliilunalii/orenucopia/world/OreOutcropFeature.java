package io.github.velaliilunalii.orenucopia.world;

import com.mojang.serialization.Codec;
import io.github.velaliilunalii.orenucopia.CommonClass;
import io.github.velaliilunalii.orenucopia.block.OutcropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.Random;

import static io.github.velaliilunalii.orenucopia.CommonClass.isIn;
import static io.github.velaliilunalii.orenucopia.block.OutcropBlock.*;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class OreOutcropFeature extends Feature<NoneFeatureConfiguration> {

    public OreOutcropFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();

        int chunkX = SectionPos.blockToSectionCoord(origin.getX());
        int chunkZ = SectionPos.blockToSectionCoord(origin.getZ());
        int minX = SectionPos.sectionToBlockCoord(chunkX);
        int minZ = SectionPos.sectionToBlockCoord(chunkZ);
        int maxX = minX + 15;
        int maxZ = minZ + 15;
        int[][] sectionBounds = {{minX, maxX}, {minZ, maxZ}};

        int placed = 0;

        for (BlockPos pos : BlockPos.betweenClosed(
                new BlockPos(minX, level.getMinBuildHeight(), minZ),
                new BlockPos(maxX, level.getMaxBuildHeight(), maxZ))) {

            BlockState state = level.getBlockState(pos);

            placed += placeOutcrops(context, state, pos, sectionBounds, CommonClass.COPPER_OUTCROP_BASES, (OutcropBlock) CommonClass.COPPER_OUTCROP);
            placed += placeOutcrops(context, state, pos, sectionBounds, CommonClass.IRON_OUTCROP_BASES, (OutcropBlock) CommonClass.IRON_OUTCROP);
            placed += placeOutcrops(context, state, pos, sectionBounds, CommonClass.GOLD_OUTCROP_BASES, (OutcropBlock) CommonClass.GOLD_OUTCROP);
            placed += placeOutcrops(context, state, pos, sectionBounds, CommonClass.REDSTONE_OUTCROP_BASES, (OutcropBlock) CommonClass.REDSTONE_OUTCROP);
            placed += placeOutcrops(context, state, pos, sectionBounds, CommonClass.LAPIS_OUTCROP_BASES, (OutcropBlock) CommonClass.LAPIS_OUTCROP);
            placed += placeOutcrops(context, state, pos, sectionBounds, CommonClass.DIAMOND_OUTCROP_BASES, (OutcropBlock) CommonClass.DIAMOND_OUTCROP);
            placed += placeOutcrops(context, state, pos, sectionBounds, CommonClass.COAL_OUTCROP_BASES, (OutcropBlock) CommonClass.COAL_OUTCROP);
            placed += placeOutcrops(context, state, pos, sectionBounds, CommonClass.EMERALD_OUTCROP_BASES, (OutcropBlock) CommonClass.EMERALD_OUTCROP);
        }

        return placed > 0;
    }

    public static int placeOutcrops(FeaturePlaceContext<NoneFeatureConfiguration> context, BlockState state, BlockPos pos, int[][] sectionBounds, Block[] bases, OutcropBlock outcrop){
        WorldGenLevel level = context.level();
        int placed = 0;

        if (isIn(bases, state)) {
            for (Direction direction : Direction.values()) {
                BlockPos adjacentPos = pos.relative(direction);

                if (adjacentPos.getX() < sectionBounds[0][0] || adjacentPos.getX() > sectionBounds[0][1] ||
                        adjacentPos.getZ() < sectionBounds[1][0] || adjacentPos.getZ() > sectionBounds[1][1]) {
                    continue;
                }

                BlockState adjacentState = level.getBlockState(adjacentPos);

                if (adjacentState.isAir() || adjacentState.is(Blocks.WATER)) {
                    FluidState fluidState = level.getFluidState(adjacentPos);
                    boolean waterlogged = fluidState.getType() == Fluids.WATER;
                    Random random = new Random();
                    float randomFloat = random.nextFloat();
                    if (outcrop.getMaxAge() > 1) {
                        if (randomFloat < 0.3f && !otherInRange(level, pos, outcrop, 4, 2)) {
                            level.setBlock(adjacentPos, outcrop.defaultBlockState()
                                    .setValue(AGE, 2)
                                    .setValue(AGING, false)
                                    .setValue(BlockStateProperties.FACING, direction)
                                    .setValue(ROTATION_FACING, getRandomOrthogonalDirection(direction))
                                    .setValue(WATERLOGGED, waterlogged), 2);
                            placed += 1 + placeTrailingOutcrops(level, adjacentPos, bases, outcrop,
                                    new Random().nextInt(3));
                        } else if (randomFloat < 0.1f) {
                            level.setBlock(adjacentPos, outcrop.defaultBlockState()
                                    .setValue(AGE, 1)
                                    .setValue(AGING, false)
                                    .setValue(BlockStateProperties.FACING, direction)
                                    .setValue(ROTATION_FACING, getRandomOrthogonalDirection(direction))
                                    .setValue(WATERLOGGED, waterlogged), 2);
                            placed++;
                        }
                    }else if (randomFloat < 0.3f && !otherInRange(level, pos, outcrop, 4, 1)){
                        level.setBlock(adjacentPos, outcrop.defaultBlockState()
                                .setValue(AGE, 1)
                                .setValue(AGING, false)
                                .setValue(BlockStateProperties.FACING, direction)
                                .setValue(ROTATION_FACING, getRandomOrthogonalDirection(direction))
                                .setValue(WATERLOGGED, waterlogged), 2);
                        placed ++;
                    }
                }
            }
        }
        return placed;
    }

    public static boolean otherInRange(WorldGenLevel level, BlockPos pos, OutcropBlock outcrop, int length, int age){
        int halfLength = length/2;
        for (BlockPos blockPos : BlockPos.betweenClosed(
                pos.offset(-halfLength, -halfLength, -halfLength),
                pos.offset(halfLength, halfLength, halfLength))) {
            BlockState state = level.getBlockState(blockPos);
            if (state.is(outcrop) && state.getValue(AGE) == age && !blockPos.equals(pos)) return true;
        }

        return false;
    }

    public static int placeTrailingOutcrops(WorldGenLevel level, BlockPos pos, Block[] bases, Block outcrop, int count){
        if (count <= 0) return 0;
        count --;

        int placed = 0;
        ArrayList<BlockPos> suitablesPos = new ArrayList<>();
        ArrayList<Direction> suitablesDirection = new ArrayList<>();

        for (BlockPos blockPos : BlockPos.betweenClosed(
                pos.offset(-1, -1, -1),
                pos.offset(1, 1, 1))) {
            BlockState state = level.getBlockState(blockPos);
            if (state.isAir() || state.is(Blocks.WATER)){
                for (Direction direction1 : Direction.values()){
                    BlockPos adjacentPos = blockPos.relative(direction1);
                    BlockState adjacentState = level.getBlockState(adjacentPos);
                    if (isIn(bases, adjacentState)) {
                        suitablesPos.add(adjacentPos);
                        suitablesDirection.add(direction1.getOpposite());
                    }
                    if (new Random().nextFloat() < 0.15 && (adjacentState.is(BlockTags.BASE_STONE_OVERWORLD) || adjacentState.is(BlockTags.BASE_STONE_NETHER))){
                        suitablesPos.add(adjacentPos);
                        suitablesDirection.add(direction1.getOpposite());
                    }
                }
            }
        }

        if (suitablesPos.isEmpty()) return placed;

        int random = new Random().nextInt(suitablesPos.size());
        BlockPos newPos = suitablesPos.get(random);
        Direction newDirection = suitablesDirection.get(random);
        FluidState fluidState = level.getFluidState(newPos.relative(newDirection));
        boolean waterlogged = fluidState.getType() == Fluids.WATER;

        level.setBlock(newPos.relative(newDirection), outcrop.defaultBlockState()
                .setValue(AGE, 1)
                .setValue(AGING, false)
                .setValue(BlockStateProperties.FACING, newDirection)
                .setValue(ROTATION_FACING, getRandomOrthogonalDirection(newDirection))
                .setValue(WATERLOGGED, waterlogged), 2);

        return placed + placeTrailingOutcrops(level, newPos, bases, outcrop, count);
    }
}
