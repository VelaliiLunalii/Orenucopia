package io.github.velaliilunalii.orenucopia.block;

import io.github.velaliilunalii.orenucopia.CommonClass;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.world.level.block.Blocks.DEEPSLATE;

public class ThemedOutcropBlock extends OutcropBlock{
    private final Block block;

    public ThemedOutcropBlock(Block block, Properties properties) {
        super(properties);
        this.block = block;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);

        if (!level.isClientSide) {
            RandomSource random = level.getRandom();

            Vec3 randomVectorAngle = Vec3.directionFromRotation(0, (random.nextFloat() * 180) - 90);
            randomVectorAngle = randomVectorAngle.scale(random.nextFloat() * 5 + 10);

            BlockPos candidatePos = pos.offset(BlockPos.containing(randomVectorAngle));
            BlockState candidateBlock = level.getBlockState(candidatePos);
            int counter = 0;
            while (counter < 10 && !candidateBlock.is(DEEPSLATE)) {
                randomVectorAngle = Vec3.directionFromRotation(0, (random.nextFloat() * 180) - 90);
                randomVectorAngle = randomVectorAngle.scale(random.nextFloat() * 3 + 5);

                candidatePos = pos.offset(BlockPos.containing(randomVectorAngle));
                candidateBlock = level.getBlockState(candidatePos);

                counter++;
            }

            if (candidateBlock.is(DEEPSLATE)) {
                level.setBlock(candidatePos,CommonClass.ECHO_TREASURE_BLOCK.defaultBlockState(), 2);
                EchoTreasureBlock.setBlock(level, candidatePos, this.getBlock());
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        System.out.println(this.getBlock());
        return super.use(state, level, pos, player, hand, hit);
    }

    public Block getBlock() {
        return block;
    }
}
