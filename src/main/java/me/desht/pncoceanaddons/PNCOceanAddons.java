package me.desht.pncoceanaddons;

import com.mojang.logging.LogUtils;
import me.desht.pncoceanaddons.client.ClientSetup;
import me.desht.pncoceanaddons.depth.DepthUtil;
import me.desht.pncoceanaddons.registry.ModItems;
import me.desht.pncoceanaddons.registry.Upgrades;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.upgrade.IUpgradeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.slf4j.Logger;

@Mod(PNCOceanAddons.MODID)
public class PNCOceanAddons {
    public static final String MODID = "pnc_ocean_addons";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Lazy<Item> LEGGINGS = Lazy.of(() ->
            BuiltInRegistries.ITEM.getOrThrow(ResourceKey.create(Registries.ITEM, PneumaticRegistry.RL("pneumatic_leggings"))));

    public PNCOceanAddons(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.ITEMS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
        NeoForge.EVENT_BUS.addListener(this::playerTick);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        if (FMLEnvironment.dist.isClient()) {
            ClientSetup.onModConstruction(modContainer, modEventBus);
        }
    }

    private void playerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide && !player.isCreative() && !player.isSpectator()) {
            DepthUtil.checkForDepth(player);
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        IUpgradeRegistry upgradeRegistry = PneumaticRegistry.getInstance().getUpgradeRegistry();
        IUpgradeRegistry.Builder builder = IUpgradeRegistry.Builder.of(Upgrades.DEPTH_UPGRADE, 1);
        upgradeRegistry.addApplicableUpgrades(LEGGINGS.get(), builder);

        Upgrades.initUpgradeHandlers();
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().location().equals(ResourceLocation.parse("pneumaticcraft:default"))) {
            event.accept(ModItems.DEPTH_UPGRADE_1);
            event.accept(ModItems.DEPTH_UPGRADE_2);
        }
    }
}