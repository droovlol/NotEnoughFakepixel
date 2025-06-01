package org.ginafro.notenoughfakepixel.mixin;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraftforge.common.MinecraftForge;
import org.ginafro.notenoughfakepixel.events.ParticlePacketEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
    @Inject(method = "handleParticles", at = @At("HEAD"))
    private void onHandleParticles(S2APacketParticles packet, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new ParticlePacketEvent(packet));
    }
}