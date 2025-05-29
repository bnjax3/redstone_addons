package org.bnjax3.redstone_addons.block.advanced;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockPlacer extends DirectionalBlock {
    public BlockPlacer(Properties properties) {
        super(properties);
        //this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TRIGGERED, Boolean.FALSE));
    }
    /*
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    public static PlayerEntity playerThatPlaced;
    public static BlockPos blockPosOfBlockToBreak;


    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void tick(BlockState blockState, ServerWorld serverWorld, BlockPos pos, Random random) {
        if (blockPosOfBlockToBreak == null)
        {
            blockPosOfBlockToBreak = getBlockPosOfFacingBlock(pos, blockState.getValue(FACING));
        }
        try {
            if (serverWorld.getBlockState(blockPosOfBlockToBreak).getMaterial() != Material.AIR) {
                breakBlock(blockPosOfBlockToBreak, serverWorld);
            }
        } catch (Exception e) {
            System.out.println("se rompio todo we, : " + e);
        }



        super.tick(blockState, serverWorld, pos, random);
    }
    @SuppressWarnings("deprecation")
    @Override
    @ParametersAreNonnullByDefault
    public void onPlace(BlockState blockState, World world, BlockPos pos, BlockState blockState1, boolean b) {
        BlockPos blockPosOfBlockToBreak = getBlockPosOfFacingBlock(pos, blockState.getValue(FACING));
        System.out.println(blockPosOfBlockToBreak.getX());
        System.out.println(blockPosOfBlockToBreak.getY());
        System.out.println(blockPosOfBlockToBreak.getZ());
        super.onPlace(blockState, world, pos, blockState1, b);
    }
    @SuppressWarnings("deprecation")
    @MethodsReturnNonnullByDefault
    @ParametersAreNonnullByDefault
    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }
    @SuppressWarnings("deprecation")
    @MethodsReturnNonnullByDefault
    @ParametersAreNonnullByDefault
    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    public BlockState getStateForPlacement(BlockItemUseContext blockItemUseContext) {
        playerThatPlaced = blockItemUseContext.getPlayer();
        return this.defaultBlockState().setValue(FACING, blockItemUseContext.getNearestLookingDirection().getOpposite());
    }
    @ParametersAreNonnullByDefault
    public void setPlacedBy(World p_180633_1_, BlockPos p_180633_2_, BlockState p_180633_3_, @Nullable LivingEntity p_180633_4_, ItemStack p_180633_5_) {

    }
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState blockState, World world, BlockPos blockPos, Block block, BlockPos blockPos1, boolean b) {
        if (!world.isClientSide) {
            boolean isPowered = blockState.getValue(TRIGGERED);
            boolean isNeighborPowered = world.hasNeighborSignal(blockPos) || world.hasNeighborSignal(blockPos.above());
            
            if (!isPowered && isNeighborPowered) {
                world.getBlockTicks().scheduleTick(blockPos, this, 4);
                System.out.println("me crasheo bien a la fakin verga");
                world.setBlock(blockPos, blockState.setValue(TRIGGERED, Boolean.TRUE), 4);
            } else if (isPowered && !isNeighborPowered){
                world.setBlock(blockPos, blockState.setValue(TRIGGERED, Boolean.FALSE), 4);
            }

        }
    }
    @SuppressWarnings("deprecation")
    @MethodsReturnNonnullByDefault
    @ParametersAreNonnullByDefault
    public BlockRenderType getRenderShape(BlockState blockState) {
        return BlockRenderType.MODEL;
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(FACING, TRIGGERED);
    }

    public BlockPos getBlockPosOfFacingBlock(BlockPos pos, Direction direction)
    {

        Vector3i directionVector = direction.getNormal();
        // I DO NOT CARE ABOUT THIS VARIABLE BEING USELESS. IT PROTECTS MY EYES AND BRAIN.
        BlockPos facingBlockPosition = new BlockPos(pos.getX() + directionVector.getX(), pos.getY() + directionVector.getY(), pos.getZ() + directionVector.getZ());
        return facingBlockPosition;
    }
    public static void breakBlock(BlockPos blockPos, World world){
        BlockState blockState = world.getBlockState(blockPos);
        System.out.println("TENGO QUE MINAR CABRON");
        Block.dropResources(blockState, world, blockPos);
        blockState.

    }

     */
}
