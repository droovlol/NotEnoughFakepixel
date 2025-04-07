package org.ginafro.notenoughfakepixel.mixin;


import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.ginafro.notenoughfakepixel.events.GuiContainerBackgroundDrawnEvent;
import org.ginafro.notenoughfakepixel.events.SlotClickEvent;
import org.ginafro.notenoughfakepixel.features.skyblock.slotlocking.SlotLocking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static org.ginafro.notenoughfakepixel.config.gui.core.util.render.RenderUtils.drawGradientRect;

@Mixin(value = GuiContainer.class, priority = 500)
public class MixinGuiContainer {
    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V", ordinal = 1))
    private void drawBackground(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        new GuiContainerBackgroundDrawnEvent(((GuiContainer) (Object) this), partialTicks).post();
    }

    @Inject(method = "drawSlot", at = @At("RETURN"))
    public void drawSlotRet(Slot slotIn, CallbackInfo ci) {
        SlotLocking.getInstance().drawSlot(slotIn);
    }


    @Shadow
    private Slot theSlot;

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawGradientRect(IIIIII)V"))
    public void drawScreen_drawGradientRect(
            GuiContainer container,
            int left,
            int top,
            int right,
            int bottom,
            int startColor,
            int endColor
    ) {
        if (startColor == 0x80ffffff && endColor == 0x80ffffff &&
                theSlot != null && SlotLocking.getInstance().isSlotLocked(theSlot)) {
            int col = 0x80ff8080;
            drawGradientRect(100,left, top, right, bottom, col, col);
        } else {
            drawGradientRect(100,left, top, right, bottom, startColor, endColor);
        }
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    public void drawScreen(CallbackInfo ci) {
        if (theSlot != null && SlotLocking.getInstance().isSlotLocked(theSlot)) {
            SlotLocking.getInstance().setRealSlot(theSlot);
            theSlot = null;
        } else if (theSlot == null) {
            SlotLocking.getInstance().setRealSlot(null);
        }
    }

    @Inject(method = "checkHotbarKeys", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/inventory/GuiContainer;handleMouseClick(Lnet/minecraft/inventory/Slot;III)V"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void checkHotbarKeys_Slotlock(int keyCode, CallbackInfoReturnable<Boolean> cir, int i) {
        if (SlotLocking.getInstance().isSlotIndexLocked(i)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "handleMouseClick", at = @At(value = "HEAD"), cancellable = true)
    public void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType, CallbackInfo ci) {
        if (slotIn == null) return;
        GuiContainer $this = (GuiContainer) (Object) this;
        SlotClickEvent event = new SlotClickEvent($this, slotIn, slotId, clickedButton, clickType);
        event.post();
        if (event.isCanceled()) {
            ci.cancel();
            return;
        }
        if (event.usePickblockInstead) {
            $this.mc.playerController.windowClick(
                    $this.inventorySlots.windowId,
                    slotId, 2, 3, $this.mc.thePlayer
            );
            ci.cancel();
        }
    }
}
