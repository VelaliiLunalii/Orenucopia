package io.github.velaliilunalii.orenucopia;

import io.github.velaliilunalii.orenucopia.particle.ColoredTerrainParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.renderer.RenderType;

import static io.github.velaliilunalii.orenucopia.CommonClass.*;

public class OrenucopiaClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(COAL_OUTCROP, RenderType.cutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(COPPER_OUTCROP, RenderType.cutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(DIAMOND_OUTCROP, RenderType.cutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(GOLD_OUTCROP, RenderType.cutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(IRON_OUTCROP, RenderType.cutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(LAPIS_OUTCROP, RenderType.cutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(REDSTONE_OUTCROP, RenderType.cutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(EMERALD_OUTCROP, RenderType.cutoutMipped());

        ParticleFactoryRegistry.getInstance().register(
                CommonClass.TRANSGENDER_PARTICLE,
                ColoredTerrainParticle.Provider::new
        );
        ParticleFactoryRegistry.getInstance().register(
                LESBIAN_PARTICLE,
                ColoredTerrainParticle.Provider::new
        );
        ParticleFactoryRegistry.getInstance().register(
                BISEXUAL_PARTICLE,
                ColoredTerrainParticle.Provider::new
        );
        ParticleFactoryRegistry.getInstance().register(
                ASEXUAL_PARTICLE,
                ColoredTerrainParticle.Provider::new
        );
        ParticleFactoryRegistry.getInstance().register(
                NON_BINARY_PARTICLE,
                ColoredTerrainParticle.Provider::new
        );
    }
}
