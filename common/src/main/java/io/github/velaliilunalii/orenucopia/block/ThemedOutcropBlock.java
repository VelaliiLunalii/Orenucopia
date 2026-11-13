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
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.Blocks.DEEPSLATE;
import static net.minecraft.world.level.block.Blocks.STONE;

public class ThemedOutcropBlock extends OutcropBlock{
    private final Block block;

    public ThemedOutcropBlock(Block block, Properties properties) {
        super(properties);
        this.block = block;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);

        if (!level.isClientSide) {
            RandomSource random = level.getRandom();

            Vec3 randomVectorAngle = Vec3.directionFromRotation((random.nextFloat() * 360) - 180, (random.nextFloat() * 180) - 90);
            randomVectorAngle = randomVectorAngle.scale(random.nextFloat() * 5 + 10);

            BlockPos candidatePos = pos.offset(BlockPos.containing(randomVectorAngle));
            BlockState candidateBlock = level.getBlockState(candidatePos);
            int counter = 0;
            while (counter < 10 && !candidateBlock.is(DEEPSLATE) && !candidateBlock.is(STONE)) {
                randomVectorAngle = Vec3.directionFromRotation((random.nextFloat() * 360) - 180, (random.nextFloat() * 180) - 90);
                randomVectorAngle = randomVectorAngle.scale(random.nextFloat() * 3 + 5);

                candidatePos = pos.offset(BlockPos.containing(randomVectorAngle));
                candidateBlock = level.getBlockState(candidatePos);

                counter++;
            }

            if (candidateBlock.is(DEEPSLATE) || candidateBlock.is(STONE)) {
                level.setBlock(candidatePos, candidateBlock.is(DEEPSLATE)
                        ? CommonClass.DEEPSLATE_ECHO_TREASURE_BLOCK.defaultBlockState()
                        : CommonClass.STONE_ECHO_TREASURE_BLOCK.defaultBlockState(), 2);
                EchoTreasureBlock.setBlock(level, candidatePos, this.getBlock());
                System.out.println("-----------");
                System.out.println(candidatePos);
            }else {
                System.out.println("-----------");
                System.out.println(counter);
            }
        }
    }

    public Block getBlock() {
        return block;
    }
}
