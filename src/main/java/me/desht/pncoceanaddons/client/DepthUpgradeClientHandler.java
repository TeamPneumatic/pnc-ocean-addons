package me.desht.pncoceanaddons.client;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.desht.pncoceanaddons.depth.DepthUpgradeHandler;
import me.desht.pncoceanaddons.depth.DepthUtil;
import me.desht.pncoceanaddons.registry.ModItems;
import me.desht.pncoceanaddons.registry.Upgrades;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.client.IGuiAnimatedStat;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IArmorUpgradeClientHandler;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IGuiScreen;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IOptionPage;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.StatPanelLayout;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorHandler;
import me.desht.pneumaticcraft.client.pneumatic_armor.ClientArmorRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.Lazy;

public class DepthUpgradeClientHandler extends IArmorUpgradeClientHandler.SimpleToggleableHandler<DepthUpgradeHandler> {
    private static final StatPanelLayout DEFAULT_LAYOUT = StatPanelLayout.expandsLeft(0.995f, 0.7f);

    private static final Object2IntMap<ResourceLocation> DEPTH_MAP = new Object2IntOpenHashMap<>();

    private double prevDepth = -1.0;
    private final Lazy<IGuiAnimatedStat> depthStat = Lazy.of(this::createStat);

    public DepthUpgradeClientHandler() {
        super(Upgrades.depthUpgradeHandler);
    }

    public static void recordSeaLevel(Level level, int seaLevel) {
        DEPTH_MAP.put(level.dimension().location(), seaLevel);
    }

    @Override
    public void tickClient(ICommonArmorHandler armorHandler, boolean isEnabled) {
        double depth = DepthUtil.getDepth(armorHandler.getPlayer());
        if (Math.abs(depth - prevDepth) > Mth.EPSILON) {
            IGuiAnimatedStat stat = getAnimatedStat();
            if (depth > 0.0) {
                stat.setTitle(Component.translatable("pnc_ocean_addons.depth_display", String.format("%3.1f", depth)));
                int tier = armorHandler.getUpgradeCount(EquipmentSlot.LEGS, Upgrades.DEPTH_UPGRADE);
                stat.setTexture(Upgrades.DEPTH_UPGRADE.getItem(tier).getDefaultInstance());
            } else {
                stat.setTitle(Component.empty());
            }
        }
        prevDepth = depth;
    }

    @Override
    public boolean isToggleable() {
        return false;
    }

    @Override
    public IGuiAnimatedStat getAnimatedStat() {
        return depthStat.get();
    }

    @Override
    public void onResolutionChanged() {
        depthStat.invalidate();
    }

    @Override
    public StatPanelLayout getDefaultStatLayout() {
        return DEFAULT_LAYOUT;
    }

    @Override
    public IOptionPage getGuiOptionsPage(IGuiScreen screen) {
        return new OptionsPage(screen, this);
    }

    public static int getClientSeaLevel(Player player) {
        return DEPTH_MAP.getOrDefault(player.level().dimension().location(), 64);
    }

    private IGuiAnimatedStat createStat() {
        IGuiAnimatedStat stat = PneumaticRegistry.getInstance().getClientArmorRegistry()
                .makeHUDStatPanel(Component.empty(), ModItems.DEPTH_UPGRADE_1.toStack(), this);

        Font font = Minecraft.getInstance().font;
        stat.setMinimumContractedDimensions(0, 0);
        stat.setMinimumExpandedDimensions(font.width(Component.translatable("pnc_ocean_addons.depth_display", 100)), Math.max(font.lineHeight, 16));
        stat.setOpeningPredicate(() -> Minecraft.getInstance().player != null && DepthUtil.getDepth(Minecraft.getInstance().player) > 0.0);
        return stat;
    }

    private static class OptionsPage extends IOptionPage.SimpleOptionPage<DepthUpgradeClientHandler> {
        public OptionsPage(IGuiScreen screen, DepthUpgradeClientHandler clientUpgradeHandler) {
            super(screen, clientUpgradeHandler);
        }

        @Override
        public void populateGui(IGuiScreen gui) {
            super.populateGui(gui);

            gui.addWidget(ClientArmorRegistry.getInstance().makeStatMoveButton(30, 128, getClientUpgradeHandler()));
        }
    }
}
