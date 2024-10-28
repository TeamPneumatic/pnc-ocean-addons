package dev.ftb.mods.pncoceanaddons.registry;

import dev.ftb.mods.pncoceanaddons.PNCOceanAddons;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PNCOceanAddons.MODID);

    public static final DeferredItem<Item> DEPTH_UPGRADE_1
            = ITEMS.register("depth_upgrade_1", () -> makeDepthUpgrade(1));
    public static final DeferredItem<Item> DEPTH_UPGRADE_2
            = ITEMS.register("depth_upgrade_2", () -> makeDepthUpgrade(2));

    private static Item makeDepthUpgrade(int tier) {
        return PneumaticRegistry.getInstance().getUpgradeRegistry().makeUpgradeItem(Upgrades.DEPTH_UPGRADE, tier, Rarity.COMMON);
    }
}
