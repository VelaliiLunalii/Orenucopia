package io.github.velaliilunalii.orenucopia.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;

public class ColoredTerrainParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final BlockPos pos;
    private final float uo;
    private final float vo;

    public ColoredTerrainParticle(ClientLevel $$0, double $$1, double $$2, double $$3, double $$4, double $$5, double $$6, SpriteSet sprites) {
        this($$0, $$1, $$2, $$3, $$4, $$5, $$6, sprites, BlockPos.containing($$1, $$2, $$3));
    }

    public ColoredTerrainParticle(ClientLevel $$0, double $$1, double $$2, double $$3, double $$4, double $$5, double $$6, SpriteSet sprites, BlockPos $$8) {
        super($$0, $$1, $$2, $$3, $$4, $$5, $$6);
        this.sprites = sprites;
        this.setSpriteFromAge(sprites);
        this.pos = $$8;
        this.gravity = 0.0F;

        this.xd = $$4;
        this.yd = $$5;
        this.zd = $$6;

        this.lifetime = 16;

        this.quadSize /= 4.0F;
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(sprites);
        this.quadSize *= 0.90F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }


    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprites) {
            this.sprite = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ColoredTerrainParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite);
        }
    }
}
