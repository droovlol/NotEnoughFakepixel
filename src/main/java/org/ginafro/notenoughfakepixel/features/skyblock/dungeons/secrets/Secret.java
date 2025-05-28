package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.secrets;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class Secret {
    public final BlockPos pos;
    public final String closestBlockSolid;
    public final SecretType type;
    public boolean enabled ;
    public Secret(BlockPos p, SecretType type, Block blockBelow) {
        this.pos = p;
        this.type = type;
        this.closestBlockSolid = blockBelow != null && blockBelow.getRegistryName() != null
                ? blockBelow.getRegistryName()
                : "unknown";
        this.enabled = true;
    }

    public Block getBlock() {
        return Block.getBlockFromName(closestBlockSolid);
    }

    public BlockPos getPos() {
        return RoomDetection.rotateRelativeCoordToWorld(pos);
    }
}
