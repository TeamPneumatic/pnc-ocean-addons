package dev.ftb.mods.pncoceanaddons.datagen;

import dev.ftb.mods.pncoceanaddons.registry.ModItems;
import dev.ftb.mods.pncoceanaddons.PNCOceanAddons;
import me.desht.pneumaticcraft.api.lib.Names;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModItemModelProvider extends ItemModelProvider {
    private static final ResourceLocation GENERATED = ResourceLocation.parse("item/generated");

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PNCOceanAddons.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.DEPTH_UPGRADE_1,
                upgradeTex(Names.MOD_ID, "layer0"),
                upgradeTex(Names.MOD_ID, "frame_1"),
                upgradeTex(PNCOceanAddons.MODID, "depth")
        );
        simpleItem(ModItems.DEPTH_UPGRADE_2,
                upgradeTex(Names.MOD_ID, "layer0"),
                upgradeTex(Names.MOD_ID, "frame_2"),
                upgradeTex(PNCOceanAddons.MODID, "depth")
        );
    }

    private ItemModelBuilder simpleItem(DeferredItem<? extends Item> item, String... textures) {
        return simpleItem(item.getId(), textures);
    }

    private ItemModelBuilder simpleItem(ResourceLocation itemKey, String... textures) {
        ItemModelBuilder builder = withExistingParent(itemKey.getPath(), GENERATED);
        for (int i = 0; i < textures.length; i++) {
            builder.texture("layer" + i, textures[i]);
        }
        return builder;
    }

    private static String upgradeTex(String modid, String base) {
        return modid + ":item/upgrades/upgrade_" + base;
    }
}
