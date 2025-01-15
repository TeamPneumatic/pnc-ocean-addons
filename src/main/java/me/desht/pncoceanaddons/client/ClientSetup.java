package me.desht.pncoceanaddons.client;

import me.desht.pncoceanaddons.Config;
import me.desht.pncoceanaddons.registry.Upgrades;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IClientArmorRegistry;
import me.desht.pneumaticcraft.api.upgrade.IUpgradeItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class ClientSetup {
    public static void onModConstruction(ModContainer modContainer, IEventBus modEventBus) {
        modEventBus.addListener(ClientSetup::onClientSetup);

        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        NeoForge.EVENT_BUS.addListener(ClientSetup::upgradeTooltipListener);
    }

    private static void upgradeTooltipListener(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() instanceof IUpgradeItem upgrade && upgrade.getUpgradeType() == Upgrades.DEPTH_UPGRADE) {
            int depthLimit = switch (upgrade.getUpgradeTier()) {
                case 1 -> Config.DEPTH_UPGRADE_1_LIMIT.get();
                case 2 -> Config.DEPTH_UPGRADE_2_LIMIT.get();
                default -> 0;
            };
            event.getToolTip().add(Component.translatable("pnc_ocean_addons.depth_upgrade.tooltip", depthLimit).withStyle(ChatFormatting.DARK_GREEN));
        }
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        IClientArmorRegistry registry = PneumaticRegistry.getInstance().getClientArmorRegistry();

        registry.registerUpgradeHandler(Upgrades.depthUpgradeHandler, new DepthUpgradeClientHandler());
    }
}
