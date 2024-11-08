package me.desht.pncoceanaddons.depth;

import me.desht.pncoceanaddons.PNCOceanAddons;
import me.desht.pncoceanaddons.registry.Upgrades;
import me.desht.pneumaticcraft.api.pneumatic_armor.BaseArmorUpgradeHandler;
import me.desht.pneumaticcraft.api.pneumatic_armor.IArmorExtensionData;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorHandler;
import me.desht.pneumaticcraft.api.upgrade.PNCUpgrade;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;

public class DepthUpgradeHandler extends BaseArmorUpgradeHandler<IArmorExtensionData> {
    public static final ResourceLocation ID = PNCOceanAddons.id("depth");

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PNCUpgrade[] getRequiredUpgrades() {
        return new PNCUpgrade[] { Upgrades.DEPTH_UPGRADE };
    }

    @Override
    public float getIdleAirUsage(ICommonArmorHandler armorHandler) {
        return 0;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.LEGS;
    }
}
