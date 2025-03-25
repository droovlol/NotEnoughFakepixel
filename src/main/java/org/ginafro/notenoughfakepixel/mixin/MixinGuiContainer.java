package org.ginafro.notenoughfakepixel.mixin;


import net.minecraft.client.gui.inventory.GuiContainer;
import org.ginafro.notenoughfakepixel.events.GuiContainerBackgroundDrawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiContainer.class, priority = 500)
public class MixinGuiContainer {
    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V", ordinal = 1))
    private void drawBackground(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        new GuiContainerBackgroundDrawnEvent(((GuiContainer) (Object) this), partialTicks).post();
    }
}
