package org.bnjax3.redstone_addons.block.advanced;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class Miner extends DirectionalBlock {
    public Miner(Properties properties) {
        super(properties);
    }
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    @Override
    public void tick(BlockState blockState, ServerWorld serverWorld, BlockPos pos, Random random) {
        if (!serverWorld.isClientSide() && blockState.getValue(POWERED))
        {
            breakBlock(getBlockFacing(pos, blockState.getValue(FACING), serverWorld), serverWorld);
        }
        super.tick(blockState, serverWorld, pos, random);
    }

    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        return this.defaultBlockState().setValue(FACING, p_196258_1_.getNearestLookingDirection().getOpposite());
    }

    public static BlockPos getBlockFacing(BlockPos pos, Direction direction, World world)
    {
        
        BlockPos facingBlockPosition =
        return facingBlockPosition;
    }
    public void breakBlock(BlockPos blockPos, World world){

    }
}
