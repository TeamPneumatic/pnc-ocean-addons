package dev.ftb.mods.pncoceanaddons.datagen;

import dev.ftb.mods.pncoceanaddons.depth.DepthUpgradeHandler;
import dev.ftb.mods.pncoceanaddons.registry.ModItems;
import dev.ftb.mods.pncoceanaddons.PNCOceanAddons;
import dev.ftb.mods.pncoceanaddons.registry.Upgrades;
import me.desht.pneumaticcraft.api.lib.Names;
import me.desht.pneumaticcraft.api.pneumatic_armor.IArmorUpgradeHandler;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, PNCOceanAddons.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addItem(ModItems.DEPTH_UPGRADE_1, "Depth Upgrade Mk1");
        addItem(ModItems.DEPTH_UPGRADE_2, "Depth Upgrade Mk2");

        addMod("depth_warning", "WARNING: Approaching %sm crush depth!");
        addMod("depth_alarm", "WARNING: Crush depth %sm exceeded!");
        addMod("depth_display", "Depth: %sm");

        addConfig("depth_upgrade_0_limit", "Max Safe Depth (no Depth Upgrade)");
        addConfig("depth_upgrade_1_limit", "Max safe depth (Depth Upgrade Mk1)");
        addConfig("depth_upgrade_2_limit", "Max safe depth (Depth Upgrade Mk2)");
        addConfig("depth_damage", "Depth crush damage per second");

        add(IArmorUpgradeHandler.getStringKey(DepthUpgradeHandler.ID), "Depth Protection");
    }

    private void addMod(String key, String text) {
        add(PNCOceanAddons.MODID + "." + key, text);
    }

    private void addConfig(String key, String text) {
        addMod("configuration." + key, text);
    }
}
