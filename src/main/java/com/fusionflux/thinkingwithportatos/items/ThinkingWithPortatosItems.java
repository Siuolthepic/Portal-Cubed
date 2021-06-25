package com.fusionflux.thinkingwithportatos.items;

import com.fusionflux.thinkingwithportatos.ThinkingWithPortatos;
import com.fusionflux.thinkingwithportatos.config.ThinkingWithPortatosConfig;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ThinkingWithPortatosItems {
    public static final ArmorMaterial FluxTechArmor = new FluxTechArmor();
    public static final Item LONG_FALL_BOOTS = new ArmorItem(FluxTechArmor, EquipmentSlot.FEET, new Item.Settings().group(ThinkingWithPortatos.ThinkingWithPortatosGroup).fireproof());

    public static void registerItems() {
        if (ThinkingWithPortatosConfig.get().enabled.enableLongFallBoots)
            Registry.register(Registry.ITEM, new Identifier(ThinkingWithPortatos.MODID, "long_fall_boots"), LONG_FALL_BOOTS);
    }
}
