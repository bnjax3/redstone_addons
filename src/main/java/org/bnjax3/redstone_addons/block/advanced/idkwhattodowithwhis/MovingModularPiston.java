package org.bnjax3.redstone_addons.block.advanced.idkwhattodowithwhis;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.PistonType;
import net.minecraft.tileentity.PistonTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class MovingModularPiston extends ContainerBlock {
    public static final DirectionProperty FACING = PistonHeadBlock.FACING;
    public static final EnumProperty<PistonType> TYPE = PistonHeadBlock.TYPE;

    public MovingModularPiston(AbstractBlock.Properties p_i48282_1_) {
        super(p_i48282_1_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TYPE, PistonType.DEFAULT));
    }

    @Nullable
    public TileEntity newBlockEntity(IBlockReader iBlockReader) {
        return null;
    }

    public static TileEntity newMovingBlockEntity(BlockState blockState, Direction direction, boolean extending, boolean isSourcePiston) {
        return new PistonTileEntity(blockState, direction, extending, isSourcePiston);
    }

    public void onRemove(BlockState blockState, World world, BlockPos blockPos, BlockState blockState1, boolean b) {
        if (!blockState.is(blockState1.getBlock())) {
            TileEntity tileentity = world.getBlockEntity(blockPos);
            if (tileentity instanceof PistonTileEntity) {
                ((PistonTileEntity)tileentity).finalTick();
            }

        }
    }

    public void destroy(IWorld iWorld, BlockPos blockPos, BlockState blockState) {
        BlockPos blockpos = blockPos.relative(blockState.getValue(FACING).getOpposite());
        BlockState blockstate = iWorld.getBlockState(blockpos);
        if (blockstate.getBlock() instanceof PistonBlock && blockstate.getValue(PistonBlock.EXTENDED)) {
            iWorld.removeBlock(blockpos, false);
        }

    }

    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        if (!world.isClientSide && world.getBlockEntity(blockPos) == null) {
            world.removeBlock(blockPos, false);
            return ActionResultType.CONSUME;
        } else {
            return ActionResultType.PASS;
        }
    }

    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder builder) {
        PistonTileEntity pistontileentity = this.getBlockEntity(builder.getLevel(), new BlockPos(builder.getParameter(LootParameters.ORIGIN)));
        return pistontileentity == null ? Collections.emptyList() : pistontileentity.getMovedState().getDrops(builder);
    }

    public VoxelShape getShape(BlockState blockState, IBlockReader iBlockReader, BlockPos blockPos, ISelectionContext iSelectionContext) {
        return VoxelShapes.empty();
    }

    public VoxelShape getCollisionShape(BlockState blockState, IBlockReader iBlockReader, BlockPos blockPos, ISelectionContext iSelectionContext) {
        PistonTileEntity pistontileentity = this.getBlockEntity(iBlockReader, blockPos);
        return pistontileentity != null ? pistontileentity.getCollisionShape(iBlockReader, blockPos) : VoxelShapes.empty();
    }

    @Nullable
    private PistonTileEntity getBlockEntity(IBlockReader iBlockReader, BlockPos blockPos) {
        TileEntity tileentity = iBlockReader.getBlockEntity(blockPos);
        return tileentity instanceof PistonTileEntity ? (PistonTileEntity)tileentity : null;
    }

    public ItemStack getCloneItemStack(IBlockReader iBlockReader, BlockPos blockPos, BlockState blockState) {
        return ItemStack.EMPTY;
    }

    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE);
    }

    public boolean isPathfindable(BlockState blockState, IBlockReader iBlockReader, BlockPos blockPos, PathType pathType) {
        return false;
    }
}
