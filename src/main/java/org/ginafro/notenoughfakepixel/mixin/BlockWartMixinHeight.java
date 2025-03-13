package org.ginafro.notenoughfakepixel.mixin;

import net.minecraft.block.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.config.features.QualityOfLife;
import org.ginafro.notenoughfakepixel.mixin.Accesors.BlockAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockNetherWart.class)
public class BlockWartMixinHeight extends BlockMixinHitbox {

    private static final AxisAlignedBB[] NETHER_WART_BOX = {
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.6875D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)
    };

    @Override
    public void getSelectedBoundingBox(World worldIn, BlockPos pos, CallbackInfoReturnable<AxisAlignedBB> cir) {
        if (NotEnoughFakepixel.feature.qol.qolCropsHeight) {
            updateCropsMaxY(worldIn, pos, worldIn.getBlockState(pos).getBlock());
        }
    }

    @Override
    public void collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end, CallbackInfoReturnable<MovingObjectPosition> cir) {
        if (NotEnoughFakepixel.feature.qol.qolCropsHeight) {
            updateCropsMaxY(worldIn, pos, worldIn.getBlockState(pos).getBlock());
        }
    }

    private static void updateCropsMaxY(World world, BlockPos pos, Block block) {
        ((BlockAccessor) block).setMaxY(
                NETHER_WART_BOX[world.getBlockState(pos).getValue(BlockNetherWart.AGE)].maxY
        );

    }

}
