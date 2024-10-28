package dev.ftb.mods.pncoceanaddons.client;

import dev.ftb.mods.pncoceanaddons.depth.DepthUpgradeHandler;
import dev.ftb.mods.pncoceanaddons.depth.DepthUtil;
import dev.ftb.mods.pncoceanaddons.registry.ModItems;
import dev.ftb.mods.pncoceanaddons.registry.Upgrades;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;

public class DepthUpgradeClientHandler extends IArmorUpgradeClientHandler.SimpleToggleableHandler<DepthUpgradeHandler> {
    private static final StatPanelLayout DEFAULT_LAYOUT = StatPanelLayout.expandsLeft(0.995f, 0.7f);

    private final Lazy<IGuiAnimatedStat> depthStat = Lazy.of(this::createStat);

    public DepthUpgradeClientHandler() {
        super(Upgrades.depthUpgradeHandler);
    }

    @Override
    public void tickClient(ICommonArmorHandler armorHandler, boolean isEnabled) {
        double depth = DepthUtil.getDepth(armorHandler.getPlayer());
        if (depth > 0.0) {
            getStat().setTitle(Component.translatable("pnc_ocean_addons.depth_display", String.format("%3.1f", depth)));
            int tier = armorHandler.getUpgradeCount(EquipmentSlot.LEGS, Upgrades.DEPTH_UPGRADE);
            getStat().setTexture(Upgrades.DEPTH_UPGRADE.getItem(tier).getDefaultInstance());
        } else {
            getStat().setTitle(Component.empty());
        }
    }

    private @NotNull IGuiAnimatedStat getStat() {
        return depthStat.get();
    }

    @Override
    public boolean isToggleable() {
        return false;
    }

    @Override
    public IGuiAnimatedStat getAnimatedStat() {
        return getStat();
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
