package io.github.velaliilunalii.orenucopia.world;

import com.mojang.serialization.Codec;
import io.github.velaliilunalii.orenucopia.block.GrowingOutcropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.Random;

import static io.github.velaliilunalii.orenucopia.block.GrowingOutcropBlock.*;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class OreOutcropFeature extends Feature<OreOutcropConfiguration> {

    public OreOutcropFeature(Codec<OreOutcropConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<OreOutcropConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        OreOutcropConfiguration outcropConfiguration = context.config();

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

            for(OreOutcropBlockSettings oreOutcropBlockSettings : outcropConfiguration.oreOutcropBlockSettings){
                placed += placeTest(oreOutcropBlockSettings, level, state, pos, sectionBounds);
            }
        }

        return placed > 0;
    }

    public static int placeTest(OreOutcropBlockSettings oreOutcropBlockSettings, WorldGenLevel level, BlockState state, BlockPos pos, int[][] sectionBounds){
        int placed = 0;

        BlockState outcrop = oreOutcropBlockSettings.outcropBlock;
        TagKey<Block> baseBlocks = oreOutcropBlockSettings.baseBlocks;
        float grownOutcropChance = oreOutcropBlockSettings.grownOutcropChance;
        float growingOutcropChance = oreOutcropBlockSettings.growingOutcropChance;
        float trailChance = oreOutcropBlockSettings.trailChance;
        int maxTrailLength = oreOutcropBlockSettings.maxTrailLength;

        if (state.is(baseBlocks) && outcrop.getBlock() instanceof GrowingOutcropBlock growingOutcropBlock) {
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
                    int maxAge = Math.min(growingOutcropBlock.getMaxAge(), 2);
                    boolean noOtherInRange = !otherInRange(level, pos, growingOutcropBlock, 4, maxAge);

                    if (maxAge > 1) {
                        if (randomFloat < grownOutcropChance && noOtherInRange) {
                            level.setBlock(adjacentPos, outcrop
                                    .setValue(AGE, maxAge)
                                    .setValue(AGING, false)
                                    .setValue(BlockStateProperties.FACING, direction)
                                    .setValue(ROTATION_FACING, getRandomOrthogonalDirection(direction))
                                    .setValue(WATERLOGGED, waterlogged), 2);
                            placed += 1 + placeTrailingOutcropsTest(level, adjacentPos, baseBlocks, outcrop,
                                    new Random().nextInt(maxTrailLength + 1), trailChance);
                        } else if (randomFloat < growingOutcropChance) {
                            level.setBlock(adjacentPos, outcrop
                                    .setValue(AGE, 1)
                                    .setValue(AGING, false)
                                    .setValue(BlockStateProperties.FACING, direction)
                                    .setValue(ROTATION_FACING, getRandomOrthogonalDirection(direction))
                                    .setValue(WATERLOGGED, waterlogged), 2);
                            placed++;
                        }
                    }else if (randomFloat < grownOutcropChance && noOtherInRange){
                        level.setBlock(adjacentPos, outcrop
                                .setValue(AGE, maxAge)
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

    public static int placeTrailingOutcropsTest(WorldGenLevel level, BlockPos pos, TagKey<Block> baseBlocks, BlockState outcrop, int maxTrailLength, float trailChance){
        if (maxTrailLength < 1) return 0;
        maxTrailLength --;

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
                    if (adjacentState.is(baseBlocks) ||
                            (new Random().nextFloat() < trailChance && (adjacentState.is(BlockTags.BASE_STONE_OVERWORLD) || adjacentState.is(BlockTags.BASE_STONE_NETHER)))) {
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

        level.setBlock(newPos.relative(newDirection), outcrop
                .setValue(AGE, 1)
                .setValue(AGING, false)
                .setValue(BlockStateProperties.FACING, newDirection)
                .setValue(ROTATION_FACING, getRandomOrthogonalDirection(newDirection))
                .setValue(WATERLOGGED, waterlogged), 2);

        return placed + placeTrailingOutcropsTest(level, newPos, baseBlocks, outcrop, maxTrailLength, trailChance);
    }

    public static boolean otherInRange(WorldGenLevel level, BlockPos pos, GrowingOutcropBlock outcrop, int length, int age){
        int halfLength = length/2;
        for (BlockPos blockPos : BlockPos.betweenClosed(
                pos.offset(-halfLength, -halfLength, -halfLength),
                pos.offset(halfLength, halfLength, halfLength))) {
            BlockState state = level.getBlockState(blockPos);
            if (state.is(outcrop) && state.getValue(AGE) == age && !blockPos.equals(pos)) return true;
        }

        return false;
    }
}
