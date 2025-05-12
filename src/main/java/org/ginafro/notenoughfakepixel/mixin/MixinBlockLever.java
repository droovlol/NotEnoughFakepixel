package org.ginafro.notenoughfakepixel.mixin;

import net.minecraft.block.BlockLever;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockLever.class)
public abstract class MixinBlockLever {

    @Inject(method = "setBlockBoundsBasedOnState", at = @At("HEAD"), cancellable = true)
    private void modifyLeverBoundingBox(IBlockAccess worldIn, BlockPos pos, CallbackInfo ci) {
        if (Config.feature.qol.qolFullBlockLever) {
            ((BlockLever) (Object) this).setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            ci.cancel();
        }
    }
}
