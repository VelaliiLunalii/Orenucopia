package io.github.velaliilunalii.orenucopia.mixin;

import io.github.velaliilunalii.orenucopia.CommonClass;
import io.github.velaliilunalii.orenucopia.item.TourmalineItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(at = @At("HEAD"), method = "playerWillDestroy", cancellable = true)
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player, CallbackInfo ci) {
        Block block = (Block) (Object) this;

        ItemStack handStack = player.getMainHandItem();

        if (!handStack.isEmpty() && TourmalineItem.hasTourmaline(handStack)) {
            if (level instanceof ServerLevel serverLevel){
                String tourmaline = TourmalineItem.getTourmaline(handStack);
                spawnCubeColoredDestroyParticles(blockPos, blockState, serverLevel, CommonClass.BLOCK_PARTICLE_MAP.get(tourmaline));
            }
            if (blockState.is(BlockTags.GUARDED_BY_PIGLINS)) {
                PiglinAi.angerNearbyPiglins(player, false);
            }

            if (!blockState.isAir() && level instanceof ClientLevel clientLevel) {
                SoundType $$39 = blockState.getSoundType();
                clientLevel.playLocalSound(blockPos, $$39.getBreakSound(), SoundSource.BLOCKS, ($$39.getVolume() + 1.0F) / 2.0F, $$39.getPitch() * 0.8F, false);
            }

            level.gameEvent(GameEvent.BLOCK_DESTROY, blockPos, GameEvent.Context.of(player, blockState));
            ci.cancel();
        }
    }

    @Unique
    public void spawnCubeColoredDestroyParticles(BlockPos $$0, BlockState $$1, ServerLevel level, SimpleParticleType particleType) {
        if (!$$1.isAir()) {
            VoxelShape $$2 = $$1.getShape(level, $$0);
            $$2.forAllBoxes(
                    ($$2x, $$3x, $$4, $$5, $$6, $$7) -> {
                        Vec3 vec1 = Vec3.atCenterOf($$0);
                        Vec3 vec2;
                        Vec3 vec3;
                        for (int j = 0; j < 2; j++) {
                            for (int i = 0; i < 4; i++) {
                                double x = (double) i / 4;
                                vec2 = new Vec3((double) $$0.getX() + x,
                                        (double) $$0.getY() + j,
                                        (double) $$0.getZ() + 0.1);
                                vec3 = vec2.vectorTo(vec1).normalize().scale(0.05);
                                level.sendParticles(particleType,
                                        vec2.x,
                                        vec2.y,
                                        vec2.z,
                                        0,
                                        vec3.x,
                                        vec3.y,
                                        vec3.z,
                                        1F);

                                vec2 = new Vec3((double) $$0.getX() + x,
                                        (double) $$0.getY() + j,
                                        (double) $$0.getZ() + 1 - 0.1);
                                vec3 = vec2.vectorTo(vec1).normalize().scale(0.05);
                                level.sendParticles(particleType,
                                        vec2.x,
                                        vec2.y,
                                        vec2.z,
                                        0,
                                        vec3.x,
                                        vec3.y,
                                        vec3.z,
                                        1F);
                            }
                            for (int i = 0; i < 4; i++) {
                                double z = (double) i / 4;
                                vec2 = new Vec3((double) $$0.getX() + 0.1,
                                        (double) $$0.getY() + j,
                                        (double) $$0.getZ() + z);
                                vec3 = vec2.vectorTo(vec1).normalize().scale(0.05);
                                level.sendParticles(particleType,
                                        vec2.x,
                                        vec2.y,
                                        vec2.z,
                                        0,
                                        vec3.x,
                                        vec3.y,
                                        vec3.z,
                                        1F);

                                vec2 = new Vec3((double) $$0.getX() + 1 - 0.1,
                                        (double) $$0.getY() + j,
                                        (double) $$0.getZ() + z);
                                vec3 = vec2.vectorTo(vec1).normalize().scale(0.05);
                                level.sendParticles(particleType,
                                        vec2.x,
                                        vec2.y,
                                        vec2.z,
                                        0,
                                        vec3.x,
                                        vec3.y,
                                        vec3.z,
                                        1F);
                            }
                        }

                        for (int j = 0; j < 2; j++) {
                            for (int i = 0; i < 4; i++) {
                                double y = (double) i / 4;
                                vec2 = new Vec3((double) $$0.getX() + j,
                                        (double) $$0.getY() + y,
                                        (double) $$0.getZ());
                                vec3 = vec2.vectorTo(vec1).normalize().scale(0.05);
                                level.sendParticles(particleType,
                                        vec2.x,
                                        vec2.y,
                                        vec2.z,
                                        0,
                                        vec3.x,
                                        vec3.y,
                                        vec3.z,
                                        1F);

                                vec2 = new Vec3((double) $$0.getX() + j,
                                        (double) $$0.getY() + y,
                                        (double) $$0.getZ() + 1);
                                vec3 = vec2.vectorTo(vec1).normalize().scale(0.05);
                                level.sendParticles(particleType,
                                        vec2.x,
                                        vec2.y,
                                        vec2.z,
                                        0,
                                        vec3.x,
                                        vec3.y,
                                        vec3.z,
                                        1F);
                            }
                        }
            }
            );
        }
    }

    @Unique
    public void spawnColoredDestroyParticles(BlockPos $$0, BlockState $$1, ServerLevel level) {
        if (!$$1.isAir()) {
            VoxelShape $$2 = $$1.getShape(level, $$0);
            $$2.forAllBoxes(
                    ($$2x, $$3x, $$4, $$5, $$6, $$7) -> {
                        for (int i = 0; i < 4; i++) {
                            double x = (double) i /4;
                            level.sendParticles(CommonClass.TRANSGENDER_PARTICLE,
                                    (double)$$0.getX() + x,
                                    (double)$$0.getY() + 1,
                                    (double)$$0.getZ() + 0.1,
                                    0,
                                    0,
                                    - 0.1,
                                    0,
                                    1F);
                            level.sendParticles(CommonClass.TRANSGENDER_PARTICLE,
                                    (double)$$0.getX() + x,
                                    (double)$$0.getY() + 1,
                                    (double)$$0.getZ() + 1 - 0.1,
                                    0,
                                    0,
                                    - 0.1,
                                    0,
                                    1F);
                        }
                        for (int i = 0; i < 4; i++) {
                            double z = (double) i /4;
                            level.sendParticles(CommonClass.TRANSGENDER_PARTICLE,
                                    (double)$$0.getX() + 0.1,
                                    (double)$$0.getY() + 1,
                                    (double)$$0.getZ() + z,
                                    0,
                                    0,
                                    - 0.1,
                                    0,
                                    1F);
                            level.sendParticles(CommonClass.TRANSGENDER_PARTICLE,
                                    (double)$$0.getX() + 1 - 0.1,
                                    (double)$$0.getY() + 1,
                                    (double)$$0.getZ() + z,
                                    0,
                                    0,
                                    - 0.1,
                                    0,
                                    1F);
                        }
                    }
            );
        }
    }

    @Unique
    public void spawnFireDestroyParticles(BlockPos $$0, BlockState $$1, ServerLevel level) {
        if (!$$1.isAir()) {
            VoxelShape $$2 = $$1.getShape(level, $$0);
            double $$3 = 0.25;
            $$2.forAllBoxes(
                    ($$2x, $$3x, $$4, $$5, $$6, $$7) -> {
                        System.out.println($$2x);
                        System.out.println($$3x);
                        System.out.println($$4);
                        System.out.println($$5);
                        System.out.println($$6);
                        System.out.println($$7);
                        double $$8 = Math.min(1.0, $$5 - $$2x);
                        double $$9 = Math.min(1.0, $$6 - $$3x);
                        double $$10 = Math.min(1.0, $$7 - $$4);
                        int $$11 = Math.max(2, Mth.ceil($$8 / 0.25));
                        int $$12 = Math.max(2, Mth.ceil($$9 / 0.25));
                        int $$13 = Math.max(2, Mth.ceil($$10 / 0.25));

                        for (int $$14 = 0; $$14 < $$11; $$14++) {
                            for (int $$15 = 0; $$15 < $$12; $$15++) {
                                for (int $$16 = 0; $$16 < $$13; $$16++) {
                                    double $$17 = ((double)$$14 + 0.5) / (double)$$11;
                                    double $$18 = ((double)$$15 + 0.5) / (double)$$12;
                                    double $$19 = ((double)$$16 + 0.5) / (double)$$13;
                                    double $$20 = $$17 * $$8 + $$2x;
                                    double $$21 = $$18 * $$9 + $$3x;
                                    double $$22 = $$19 * $$10 + $$4;

                                    level.sendParticles(ParticleTypes.FLAME,
                                            (double)$$0.getX() + $$20,
                                            (double)$$0.getY() + $$21,
                                            (double)$$0.getZ() + $$22,
                                            0,
                                            $$17 - 0.5,
                                            $$18 - 0.5,
                                            $$19 - 0.5,
                                            1F);
                                }
                            }
                        }
                    }
            );
        }
    }
}
