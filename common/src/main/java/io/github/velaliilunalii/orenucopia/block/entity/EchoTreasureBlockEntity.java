package io.github.velaliilunalii.orenucopia.block.entity;

import io.github.velaliilunalii.orenucopia.CommonClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

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
        if (level.getGameTime() % 80 == 1) {
            if (level.isClientSide) clientTick(level, pos);
            else serverTick(level, pos);
        }
    }

    public static void clientTick(Level level, BlockPos pos) {
        level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), CommonClass.SCULK_ECHO, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.4F + 0.8F, false);
    }

    public static void serverTick(Level level, BlockPos pos) {
        for (Player player : level.getEntitiesOfClass(Player.class, new AABB(pos.offset(-15, -15, -15), pos.offset(15, 15, 15)))) {
            ((ServerLevel) level).sendParticles
                    (new VibrationParticleOption(new BlockPositionSource(pos), (int)(player.position().distanceTo(pos.getCenter())/3) + 5),
                            player.getX(), player.getY(), player.getZ(),
                            1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }
}
