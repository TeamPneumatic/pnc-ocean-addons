package me.desht.pncoceanaddons.client;

import me.desht.pncoceanaddons.registry.Upgrades;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IClientArmorRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class ClientSetup {
    public static void onModConstruction(ModContainer modContainer, IEventBus modEventBus) {
        modEventBus.addListener(ClientSetup::onClientSetup);

        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        IClientArmorRegistry registry = PneumaticRegistry.getInstance().getClientArmorRegistry();

        registry.registerUpgradeHandler(Upgrades.depthUpgradeHandler, new DepthUpgradeClientHandler());
    }
}
