package io.github.velaliilunalii.orenucopia;

import io.github.velaliilunalii.orenucopia.item.OrenucopiaItem;
import io.github.velaliilunalii.orenucopia.item.TourmalineItem;
import io.github.velaliilunalii.orenucopia.particle.ColoredTerrainParticle;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static io.github.velaliilunalii.orenucopia.Orenucopia.*;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class OrenucopiaClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(CommonClass.COAL_OUTCROP, RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(CommonClass.COPPER_OUTCROP, RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(CommonClass.DIAMOND_OUTCROP, RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(CommonClass.GOLD_OUTCROP, RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(CommonClass.IRON_OUTCROP, RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(CommonClass.LAPIS_OUTCROP, RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(CommonClass.REDSTONE_OUTCROP, RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(CommonClass.EMERALD_OUTCROP, RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(CommonClass.NETHER_GOLD_OUTCROP, RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(CommonClass.QUARTZ_OUTCROP, RenderType.cutoutMipped());

            ItemBlockRenderTypes.setRenderLayer(CommonClass.INFECTED_IRON_OUTCROP, RenderType.cutoutMipped());


            registerTransgenderTourmalineProperty(CommonClass.TOURMALINE);
            registerLesbianTourmalineProperty(CommonClass.TOURMALINE);
            registerBisexualTourmalineProperty(CommonClass.TOURMALINE);
            registerAsexualTourmalineProperty(CommonClass.TOURMALINE);
            registerNonBinaryTourmalineProperty(CommonClass.TOURMALINE);

            registerOrenucopiaItemProperty(CommonClass.ORENUCOPIA);
        });
    }

    @SubscribeEvent
    public static void onRegisterParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(TRANSGENDER_PARTICLE.get(), ColoredTerrainParticle.Provider::new);
        event.registerSpriteSet(LESBIAN_PARTICLE.get(), ColoredTerrainParticle.Provider::new);
        event.registerSpriteSet(BISEXUAL_PARTICLE.get(), ColoredTerrainParticle.Provider::new);
        event.registerSpriteSet(ASEXUAL_PARTICLE.get(), ColoredTerrainParticle.Provider::new);
        event.registerSpriteSet(NON_BINARY_PARTICLE.get(), ColoredTerrainParticle.Provider::new);
    }

    private static void registerTransgenderTourmalineProperty(Item item) {
        ItemProperties.register(item,
                new ResourceLocation(Constants.MOD_ID, "transgender"),
                (stack, level, entity, seed) -> {
                    return TourmalineItem.getTourmaline(stack).equals("transgender") ? 1 : 0;
                }
        );
    }

    private static void registerLesbianTourmalineProperty(Item item) {
        ItemProperties.register(item,
                new ResourceLocation(Constants.MOD_ID, "lesbian"),
                (stack, level, entity, seed) -> {
                    return TourmalineItem.getTourmaline(stack).equals("lesbian") ? 1 : 0;
                }
        );
    }

    private static void registerBisexualTourmalineProperty(Item item) {
        ItemProperties.register(item,
                new ResourceLocation(Constants.MOD_ID, "bisexual"),
                (stack, level, entity, seed) -> {
                    return TourmalineItem.getTourmaline(stack).equals("bisexual") ? 1 : 0;
                }
        );
    }

    private static void registerAsexualTourmalineProperty(Item item) {
        ItemProperties.register(item,
                new ResourceLocation(Constants.MOD_ID, "asexual"),
                (stack, level, entity, seed) -> {
                    return TourmalineItem.getTourmaline(stack).equals("asexual") ? 1 : 0;
                }
        );
    }

    private static void registerNonBinaryTourmalineProperty(Item item) {
        ItemProperties.register(item,
                new ResourceLocation(Constants.MOD_ID, "non_binary"),
                (stack, level, entity, seed) -> {
                    return TourmalineItem.getTourmaline(stack).equals("non_binary") ? 1 : 0;
                }
        );
    }

    private static void registerOrenucopiaItemProperty(Item item) {
        ItemProperties.register(item,
                new ResourceLocation(Constants.MOD_ID, "orenucopia"),
                (stack, level, entity, seed) -> {
                    return OrenucopiaItem.hasContent(stack) ? 1 : 0;
                }
        );
    }
}
