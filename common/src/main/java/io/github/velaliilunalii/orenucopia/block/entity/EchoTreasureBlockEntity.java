package io.github.velaliilunalii.orenucopia.block.entity;

import io.github.velaliilunalii.orenucopia.CommonClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EchoTreasureBlockEntity extends BlockEntity {
    private Block block;

    public EchoTreasureBlockEntity(BlockPos pos, BlockState blockState) {
        super(CommonClass.ECHO_TREASURE_BLOCK_ENTITY, pos, blockState);
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.block = BuiltInRegistries.BLOCK.get(new ResourceLocation(tag.getString("block")));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("block", BuiltInRegistries.BLOCK.getKey(this.block).toString());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EchoTreasureBlockEntity blockEntity) {
        if (level.getGameTime() % 100 == 0) {
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS, 1.0F, 1.0F, false);

        }
    }
}
