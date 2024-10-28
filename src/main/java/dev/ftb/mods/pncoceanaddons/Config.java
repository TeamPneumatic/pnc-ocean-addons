package dev.ftb.mods.pncoceanaddons;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = PNCOceanAddons.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue DEPTH_UPGRADE_0_LIMIT = BUILDER
            .comment("Maximum depth below sea level with no Pneumatic Armor/Depth Upgrade before taking crush damage")
            .defineInRange("depth_upgrade_0_limit", 64, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue DEPTH_UPGRADE_1_LIMIT = BUILDER
            .comment("Maximum depth below sea level with Depth Upgrade Mk1 before taking crush damage")
            .defineInRange("depth_upgrade_1_limit", 128, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue DEPTH_UPGRADE_2_LIMIT = BUILDER
            .comment("Maximum depth below sea level with Depth Upgrade Mk2 before taking crush damage")
            .defineInRange("depth_upgrade_2_limit", 192, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue DEPTH_DAMAGE = BUILDER
            .comment("Depth per second dealt to players who are in too deep")
            .defineInRange("depth_damage", 2.0, 0.0, Double.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    }
}
