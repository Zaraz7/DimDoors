package org.dimdev.dimdoors.enchantment;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.dimdev.dimdoors.DimensionalDoors;

public class ModEnchants {
	public static DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(DimensionalDoors.MOD_ID, Registry.ENCHANTMENT_REGISTRY);
	public static RegistrySupplier<Enchantment> STRING_THEORY_ENCHANTMENT = ENCHANTMENTS.register("string_theory", () -> new StringTheoryEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.WEARABLE, new EquipmentSlot[] {EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD}));

	public static void init() {
		ENCHANTMENTS.register();
	}
}
