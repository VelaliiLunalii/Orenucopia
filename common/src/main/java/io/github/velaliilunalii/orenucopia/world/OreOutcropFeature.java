package io.github.velaliilunalii.orenucopia.world;

import com.mojang.serialization.Codec;
import io.github.velaliilunalii.orenucopia.block.GrowingOutcropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.*;

import static io.github.velaliilunalii.orenucopia.block.GrowingOutcropBlock.*;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class OreOutcropFeature extends Feature<OreOutcropConfiguration> {
    private final int variantYThreshold = 0;
    private final int minY = -62;
    private final int averageMaxY = 62;

    public OreOutcropFeature(Codec<OreOutcropConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<OreOutcropConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        List<OreOutcropBlockSettings> oreOutcropBlockSettingsList = context.config().oreOutcropBlockSettings;

        int chunkX = SectionPos.blockToSectionCoord(origin.getX());
        int chunkZ = SectionPos.blockToSectionCoord(origin.getZ());
        int minX = SectionPos.sectionToBlockCoord(chunkX);
        int minZ = SectionPos.sectionToBlockCoord(chunkZ);
        int maxX = minX + 15;
        int maxZ = minZ + 15;
        int[][] sectionBounds = {{minX, maxX}, {minZ, maxZ}};

        int placed = 0;

        for (BlockPos pos : BlockPos.betweenClosed(
                new BlockPos(minX, minY, minZ),
                new BlockPos(maxX, variantYThreshold, maxZ))) {

            BlockState state = level.getBlockState(pos);

            for (OreOutcropBlockSettings oreOutcropBlockSettings : oreOutcropBlockSettingsList) {
                TagKey<Block> baseBlocks = oreOutcropBlockSettings.baseBlocks;
                float grownChance = oreOutcropBlockSettings.grownOutcropChance;
                float growingChance = oreOutcropBlockSettings.growingOutcropChance;
                float randomChance = level.getRandom().nextFloat();
                int maxAge = Math.min(((GrowingOutcropBlock) oreOutcropBlockSettings.outcropBlock.getBlock()).getMaxAge(), 2);
                if (state.is(baseBlocks)) {
                    if (randomChance < grownChance) placed += placeOutcropVariant(oreOutcropBlockSettings, level, pos, sectionBounds, maxAge);
                    else if (randomChance < growingChance + grownChance && maxAge > 1) placed += placeGrowingOutcrop(oreOutcropBlockSettings, level, pos, sectionBounds);
                }
            }
        }

        BlockPos sectionMaxYPos = new BlockPos(minX + 7, averageMaxY, minZ + 7);
        BlockState sectionMaxYState = level.getBlockState(sectionMaxYPos);
        while (sectionMaxYState.isAir() || sectionMaxYState.is(Blocks.WATER)){
            sectionMaxYPos = sectionMaxYPos.below();
            sectionMaxYState = level.getBlockState(sectionMaxYPos);
        }
        while (!(sectionMaxYState.isAir() || sectionMaxYState.is(Blocks.WATER))){
            sectionMaxYPos = sectionMaxYPos.above();
            sectionMaxYState = level.getBlockState(sectionMaxYPos);
        }

        for (BlockPos pos : BlockPos.betweenClosed(
                new BlockPos(minX, variantYThreshold, minZ),
                new BlockPos(maxX, sectionMaxYPos.getY(), maxZ))) {

            BlockState state = level.getBlockState(pos);

            for(OreOutcropBlockSettings oreOutcropBlockSettings : oreOutcropBlockSettingsList){
                TagKey<Block> baseBlocks = oreOutcropBlockSettings.baseBlocks;
                float grownChance = oreOutcropBlockSettings.grownOutcropChance;
                float growingChance = oreOutcropBlockSettings.growingOutcropChance;
                float randomChance = level.getRandom().nextFloat();
                int maxAge = Math.min(((GrowingOutcropBlock) oreOutcropBlockSettings.outcropBlock.getBlock()).getMaxAge(), 2);
                if (state.is(baseBlocks)) {
                    if (randomChance < grownChance) placed += placeGrownOutcrop(oreOutcropBlockSettings, level, pos, sectionBounds, maxAge);
                    else if (randomChance < growingChance + grownChance && maxAge > 1) placed += placeGrowingOutcrop(oreOutcropBlockSettings, level, pos, sectionBounds);
                }
            }
        }

        return placed > 0;
    }

    public static int placeOutcropVariant(OreOutcropBlockSettings oreOutcropBlockSettings, WorldGenLevel level, BlockPos pos, int[][] sectionBounds, int maxAge){
        int placed = 0;
        List<BlockState> variantsList = oreOutcropBlockSettings.outcropVariants;
        RandomSource random = level.getRandom();

        if (variantsList.isEmpty() || random.nextFloat() > oreOutcropBlockSettings.outcropVariantsChance) return placeGrownOutcrop(oreOutcropBlockSettings, level, pos, sectionBounds, maxAge);

        BlockState outcrop = variantsList.get(random.nextInt(variantsList.size()));

        for (Direction direction : Direction.values()) {
            BlockPos adjacentPos = pos.relative(direction);
            boolean noOtherInRange = !otherInRange(level, pos, oreOutcropBlockSettings.outcropBlock.getBlock(), 4, maxAge);

            if (noOtherInRange) {
                if (adjacentPos.getX() < sectionBounds[0][0] || adjacentPos.getX() > sectionBounds[0][1] ||
                        adjacentPos.getZ() < sectionBounds[1][0] || adjacentPos.getZ() > sectionBounds[1][1]) {
                    continue;
                }

                BlockState adjacentState = level.getBlockState(adjacentPos);

                if (adjacentState.isAir() || adjacentState.is(Blocks.WATER)) {
                    FluidState fluidState = level.getFluidState(adjacentPos);
                    boolean waterlogged = fluidState.getType() == Fluids.WATER;
                    level.setBlock(adjacentPos, outcrop
                            .setValue(BlockStateProperties.FACING, direction)
                            .setValue(ROTATION_FACING, getRandomOrthogonalDirection(direction))
                            .setValue(WATERLOGGED, waterlogged), 2);
                }
            }
        }

        return placed;
    }

    public static int placeGrownOutcrop(OreOutcropBlockSettings oreOutcropBlockSettings, WorldGenLevel level, BlockPos pos, int[][] sectionBounds, int maxAge){
        int placed = 0;

        TagKey<Block> baseBlocks = oreOutcropBlockSettings.baseBlocks;
        BlockState outcrop = oreOutcropBlockSettings.outcropBlock;
        float trailChance = oreOutcropBlockSettings.trailChance;
        int maxTrailLength = oreOutcropBlockSettings.maxTrailLength;

        for (Direction direction : Direction.values()) {
            BlockPos adjacentPos = pos.relative(direction);
            boolean noOtherInRange = !otherInRange(level, adjacentPos, outcrop.getBlock(), 4, maxAge);

            if (noOtherInRange) {
                if (adjacentPos.getX() < sectionBounds[0][0] || adjacentPos.getX() > sectionBounds[0][1] ||
                        adjacentPos.getZ() < sectionBounds[1][0] || adjacentPos.getZ() > sectionBounds[1][1]) {
                    continue;
                }

                BlockState adjacentState = level.getBlockState(adjacentPos);

                if (adjacentState.isAir() || adjacentState.is(Blocks.WATER)) {
                    FluidState fluidState = level.getFluidState(adjacentPos);
                    boolean waterlogged = fluidState.getType() == Fluids.WATER;

                    level.setBlock(adjacentPos, outcrop
                            .setValue(AGE, maxAge)
                            .setValue(AGING, false)
                            .setValue(BlockStateProperties.FACING, direction)
                            .setValue(ROTATION_FACING, getRandomOrthogonalDirection(direction))
                            .setValue(WATERLOGGED, waterlogged), 2);
                    placed++;
                    if (maxAge > 1) {
                        placed += placeTrailingOutcrops(level, adjacentPos, baseBlocks, outcrop,
                                new Random().nextInt(maxTrailLength + 1), trailChance);
                    }
                }
            }
        }
        return placed;
    }

    public static int placeGrowingOutcrop(OreOutcropBlockSettings oreOutcropBlockSettings, WorldGenLevel level, BlockPos pos, int[][] sectionBounds){
        int placed = 0;

        BlockState outcrop = oreOutcropBlockSettings.outcropBlock;

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

                level.setBlock(adjacentPos, outcrop
                        .setValue(AGE, 1)
                        .setValue(AGING, false)
                        .setValue(BlockStateProperties.FACING, direction)
                        .setValue(ROTATION_FACING, getRandomOrthogonalDirection(direction))
                        .setValue(WATERLOGGED, waterlogged), 2);
                placed++;
            }
        }
        return placed;
    }

    public static int placeTrailingOutcrops(WorldGenLevel level, BlockPos pos, TagKey<Block> baseBlocks, BlockState outcrop, int maxTrailLength, float trailChance){
        if (maxTrailLength < 1) return 0;
        maxTrailLength --;

        RandomSource levelRandom = level.getRandom();
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
                            (levelRandom.nextFloat() < trailChance && (adjacentState.is(BlockTags.BASE_STONE_OVERWORLD) || adjacentState.is(BlockTags.BASE_STONE_NETHER)))) {
                        suitablesPos.add(adjacentPos);
                        suitablesDirection.add(direction1.getOpposite());
                    }
                }
            }
        }

        if (suitablesPos.isEmpty()) return placed;

        int random = levelRandom.nextInt(suitablesPos.size());
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

        return placed + placeTrailingOutcrops(level, newPos, baseBlocks, outcrop, maxTrailLength, trailChance);
    }

    public static boolean otherInRange(WorldGenLevel level, BlockPos pos, Block outcrop, int length, int age){
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
