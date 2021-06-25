package com.fusionflux.thinkingwithportatos.client;

import com.fusionflux.thinkingwithportatos.blocks.ThinkingWithPortatosBlocks;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;

@Environment(EnvType.CLIENT)
public class ThinkingWithPortatosClient implements ClientModInitializer {

    public static void registerBlockRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(ThinkingWithPortatosBlocks.HLB_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ThinkingWithPortatosBlocks.HLB_EMITTER_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ThinkingWithPortatosBlocks.NEUROTOXIN_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ThinkingWithPortatosBlocks.EXCURSION_FUNNEL, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ThinkingWithPortatosBlocks.EXCURSION_FUNNEL_EMITTER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ThinkingWithPortatosBlocks.NEUROTOXIN_EMITTER, RenderLayer.getTranslucent());
    }



    @Override
    public void onInitializeClient() {

        registerBlockRenderLayers();

        //setupFluidRendering(ThinkingWithPortatosBlocks.STILL_TOXIC_GOO, ThinkingWithPortatosBlocks.FLOWING_TOXIC_GOO, new Identifier("thinkingwithportatos", "acid"), 0x2D1B00);
        //BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), ThinkingWithPortatosBlocks.STILL_TOXIC_GOO, ThinkingWithPortatosBlocks.FLOWING_TOXIC_GOO);
    }



    public static void setupFluidRendering(final Fluid still, final Fluid flowing, final Identifier textureFluidId, final int color) {
        final Identifier stillSpriteId = new Identifier(textureFluidId.getNamespace(), "block/" + textureFluidId.getPath() + "_still");
        final Identifier flowingSpriteId = new Identifier(textureFluidId.getNamespace(), "block/" + textureFluidId.getPath() + "_flow");

        // If they're not already present, add the sprites to the block atlas
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
            registry.register(stillSpriteId);
            registry.register(flowingSpriteId);
        });

        final Identifier fluidId = Registry.FLUID.getId(still);


        final Sprite[] fluidSprites = { null, null };



        // The FluidRenderer gets the sprites and color from a FluidRenderHandler during rendering
        final FluidRenderHandler renderHandler = new FluidRenderHandler()
        {
            @Override
            public Sprite[] getFluidSprites(BlockRenderView view, BlockPos pos, FluidState state) {
                return fluidSprites;
            }

            @Override
            public int getFluidColor(BlockRenderView view, BlockPos pos, FluidState state) {
                return color;
            }
        };

        FluidRenderHandlerRegistry.INSTANCE.register(still, renderHandler);
        FluidRenderHandlerRegistry.INSTANCE.register(flowing, renderHandler);
    }

}
