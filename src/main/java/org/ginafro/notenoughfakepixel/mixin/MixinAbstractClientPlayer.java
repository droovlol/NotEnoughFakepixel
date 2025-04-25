package org.ginafro.notenoughfakepixel.mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;
import org.ginafro.notenoughfakepixel.features.capes.CapeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class MixinAbstractClientPlayer {

    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    public void onGetCape(CallbackInfoReturnable<ResourceLocation> cir) {
        if(CapeManager.hasCape()) {
            cir.setReturnValue(CapeManager.getCapeTexture(CapeManager.getCape()));
        }
    }
}
