package com.fusionflux.thinkingwithportatos;

import com.fusionflux.thinkingwithportatos.accessor.QuaternionHandler;
import com.fusionflux.thinkingwithportatos.blocks.ThinkingWithPortatosBlocks;
import com.fusionflux.thinkingwithportatos.config.ThinkingWithPortatosConfig;
import com.fusionflux.thinkingwithportatos.items.ThinkingWithPortatosItems;
import com.fusionflux.thinkingwithportatos.sound.ThinkingWithPortatosSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ThinkingWithPortatos implements ModInitializer {
    public static final ThinkingWithPortatosConfig CONFIG = new ThinkingWithPortatosConfig();

    public static final String MODID = "thinkingwithportatos";

    public static final ItemGroup ThinkingWithPortatosGroup = FabricItemGroupBuilder.build(
            id("general"),
            () -> new ItemStack(ThinkingWithPortatosItems.LONG_FALL_BOOTS));

    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }



    @Override
    public void onInitialize() {
        QuaternionHandler.QUATERNION_HANDLER.getClass();
        ThinkingWithPortatosConfig.register();
        ThinkingWithPortatosBlocks.registerBlocks();
        ThinkingWithPortatosItems.registerItems();
        ThinkingWithPortatosSounds.registerSounds();

        FlammableBlockRegistry.getDefaultInstance().add(ThinkingWithPortatosBlocks.NEUROTOXIN_BLOCK,10000,10000);
    }
}
