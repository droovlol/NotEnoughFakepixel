package org.ginafro.notenoughfakepixel.mixin;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.features.skyblock.qol.ItemBackgroundRarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public class RenderItemMixin {
    @Inject(method = "renderItemIntoGUI(Lnet/minecraft/item/ItemStack;II)V", at = @At("HEAD"))
    private void renderRarity(ItemStack itemStack, int xPosition, int yPosition, CallbackInfo info) {
        if (Config.feature.qol.qolItemRarity) {
            ItemBackgroundRarity.renderRarityOverlay(itemStack, xPosition, yPosition);
        }
    }
}