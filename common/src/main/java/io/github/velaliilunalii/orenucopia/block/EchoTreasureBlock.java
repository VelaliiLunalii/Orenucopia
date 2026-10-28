package io.github.velaliilunalii.orenucopia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static io.github.velaliilunalii.orenucopia.util.ItemStackUtil.getTreasurePos;
import static net.minecraft.world.item.Items.ECHO_SHARD;

public class EchoTreasureBlock extends RotatedPillarBlock {
    public EchoTreasureBlock(Properties $$0) {
        super($$0);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, BlockEntity $$4, ItemStack $$5) {
        super.playerDestroy(level, player, blockPos, blockState, $$4, $$5);

        for (ItemStack itemStack : player.getInventory().items){
            if (itemStack.is(ECHO_SHARD)){
                BlockPos treasurePos = getTreasurePos(itemStack);
                if (treasurePos != null && treasurePos.equals(blockPos)){
                    CompoundTag tag = itemStack.getOrCreateTag();
                    tag.remove("TreasurePos");
                    player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                    if (!player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }

                    return;
                }
            }
        }
    }
}
