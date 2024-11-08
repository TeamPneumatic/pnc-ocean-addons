package me.desht.pncoceanaddons.registry;

import me.desht.pncoceanaddons.PNCOceanAddons;
import me.desht.pncoceanaddons.depth.DepthUpgradeHandler;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorRegistry;
import me.desht.pneumaticcraft.api.upgrade.PNCUpgrade;

public class Upgrades {
    public static final PNCUpgrade DEPTH_UPGRADE = PneumaticRegistry.getInstance().getUpgradeRegistry()
            .registerUpgrade(PNCOceanAddons.id("depth"), 2);

    public static DepthUpgradeHandler depthUpgradeHandler;

    public static void initUpgradeHandlers() {
        ICommonArmorRegistry registry = PneumaticRegistry.getInstance().getCommonArmorRegistry();

        depthUpgradeHandler = registry.registerUpgradeHandler(new DepthUpgradeHandler());
    }
}
