package me.desht.pncoceanaddons.depth;

import me.desht.pncoceanaddons.Config;
import me.desht.pncoceanaddons.PNCOceanAddons;
import me.desht.pncoceanaddons.client.DepthUpgradeClientHandler;
import me.desht.pncoceanaddons.registry.Upgrades;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorHandler;
import me.desht.pneumaticcraft.common.PNCDamageSource;
import me.desht.pneumaticcraft.common.item.PneumaticArmorItem;
import me.desht.pneumaticcraft.common.registry.ModSounds;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LiquidBlock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DepthUtil {
    private static final Map<UUID, Long> LAST_ALARM_TIME = new HashMap<>();
    private static final Map<UUID, Long> LAST_WARN_TIME = new HashMap<>();

    private static final List<EquipmentSlot> ARMOR_SLOTS = List.of(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD);

    public static void checkForDepth(Player player) {
        if (player.tickCount % 20 == 0) {
            double depth = getDepth(player);
            int maxSafeDepth = getMaxSafeDepth(getDepthUpgradeLevel(player));
            if (depth > maxSafeDepth) {
                dealDepthDamage(player, maxSafeDepth);
            } else if (player.getDeltaMovement().y < -0.01 && maxSafeDepth - depth < 3.0) {
                sendDepthWarning(player, maxSafeDepth);
            }
        }
    }

    private static void sendDepthWarning(Player player, int crushDepth) {
        if (player.getRandom().nextFloat() < 0.2f) {
            player.level().playSound(null, player.blockPosition(), ModSounds.CREAK.get(), SoundSource.NEUTRAL, 1f, 0.8f + player.getRandom().nextFloat() * 0.4f);
        }
        sendCooldownMessage(player, LAST_WARN_TIME, Component.translatable("pnc_ocean_addons.depth_warning", crushDepth), 0x80FF8000);
    }

    private static void dealDepthDamage(Player player, int crushDepth) {
        player.hurt(PNCDamageSource.pressure(player.level()), Config.DEPTH_DAMAGE.get().floatValue());
        if (player.getRandom().nextFloat() < 0.35f) {
            player.level().playSound(null, player.blockPosition(), ModSounds.CREAK.get(), SoundSource.NEUTRAL, 1f, 0.5f + player.getRandom().nextFloat() * 0.4f);
        }
        sendCooldownMessage(player, LAST_ALARM_TIME, Component.translatable("pnc_ocean_addons.depth_alarm", crushDepth), 0x80FF0000);
    }

    private static void sendCooldownMessage(Player player, Map<UUID,Long> map, Component message, int bgColor) {
        long last = map.getOrDefault(player.getUUID(), 0L);
        long now = Util.getMillis();
        if (now - last > 10_000L) {
            if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof PneumaticArmorItem) {
                ICommonArmorHandler handler = PneumaticRegistry.getInstance().getCommonArmorRegistry().getCommonArmorHandler(player);
                handler.addArmorMessage(message, 70, bgColor);
            } else {
                player.displayClientMessage(message.copy().withColor(bgColor), true);
            }
            map.put(player.getUUID(), now);
        }
    }

    private static int getDepthUpgradeLevel(Player player) {
        int nTagged = 0;
        boolean requireAll = Config.REQUIRE_FULL_TAGGED_SET.get();
        for (var slot : ARMOR_SLOTS) {
            if (player.getItemBySlot(slot).is(PNCOceanAddons.CRUSH_DEPTH_PROTECTION)) {
                if (!requireAll) {
                    return Upgrades.DEPTH_UPGRADE.getMaxTier();
                }
                nTagged++;
            } else if (requireAll) {
                break;
            }
        }
        if (nTagged == 4) {
            return Upgrades.DEPTH_UPGRADE.getMaxTier();
        }

        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        return leggings.getItem() == PNCOceanAddons.LEGGINGS.get() ?
                PneumaticRegistry.getInstance().getUpgradeRegistry().getUpgradeCount(leggings, Upgrades.DEPTH_UPGRADE) :
                0;
    }

    public static double getDepth(Player player) {
        boolean headInFluid;
        if (Config.HEAD_MUST_BE_IN_FLUID.get()) {
            BlockPos headPos = BlockPos.containing(player.getEyePosition());
            headInFluid = player.level().getBlockState(headPos).getBlock() instanceof LiquidBlock;
        } else {
            headInFluid = true;
        }

        return headInFluid ? Math.max(0.0, getSeaLevel(player) - player.getEyeY()) : 0.0;
    }

    private static int getSeaLevel(Player player) {
        return player.level() instanceof ServerLevel sl ?
                sl.getChunkSource().getGenerator().getSeaLevel() :
                DepthUpgradeClientHandler.getClientSeaLevel(player);
    }

    private static int getMaxSafeDepth(int nUpgrades) {
        return switch (nUpgrades) {
            case 0 -> Config.DEPTH_UPGRADE_0_LIMIT.get();
            case 1 -> Config.DEPTH_UPGRADE_1_LIMIT.get();
            default -> Config.DEPTH_UPGRADE_2_LIMIT.get();
        };
    }
}
