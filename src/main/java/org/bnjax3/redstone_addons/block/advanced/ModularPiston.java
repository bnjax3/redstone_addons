package org.bnjax3.redstone_addons.block.advanced;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.PistonType;
import net.minecraft.tileentity.PistonTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.bnjax3.redstone_addons.block.advanced.idkwhattodowithwhis.ModularPistonStructureHelper;
import org.bnjax3.redstone_addons.block.advanced.idkwhattodowithwhis.MovingModularPiston;

import java.util.List;
import java.util.Map;

public class ModularPiston extends DirectionalBlock {

    public static final BooleanProperty EXTENDED = BlockStateProperties.EXTENDED;
    protected static final VoxelShape EAST_AABB = Block.box(0.0D, 0.0D, 0.0D, 12.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_AABB = Block.box(4.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 12.0D);
    protected static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 4.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape UP_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
    protected static final VoxelShape DOWN_AABB = Block.box(0.0D, 4.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private final boolean isSticky;

    public ModularPiston(boolean isSticky, AbstractBlock.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(EXTENDED, Boolean.valueOf(false)));
        this.isSticky = isSticky;
    }

    public VoxelShape getShape(BlockState blockState, IBlockReader iBlockReader, BlockPos blockPos, ISelectionContext iSelectionContext) {
        if (blockState.getValue(EXTENDED)) {
            switch(blockState.getValue(FACING)) {
                case DOWN:
                    return DOWN_AABB;
                case NORTH:
                    return NORTH_AABB;
                case SOUTH:
                    return SOUTH_AABB;
                case WEST:
                    return WEST_AABB;
                case EAST:
                    return EAST_AABB;
                default:
                    return UP_AABB;
            }
        } else {
            return VoxelShapes.block();
        }
    }

    public void setPlacedBy(World world, BlockPos blockPos, BlockState blockState, LivingEntity p_180633_4_, ItemStack p_180633_5_) {
        if (!world.isClientSide) {
            this.checkIfExtend(world, blockPos, blockState);
        }

    }

    public void neighborChanged(BlockState blockState, World world, BlockPos blockPos, Block block, BlockPos blockPos1, boolean b) {
        if (!world.isClientSide) {
            this.checkIfExtend(world, blockPos, blockState);
        }

    }

    public void onPlace(BlockState blockState, World world, BlockPos blockPos, BlockState blockState1, boolean b) {
        if (!blockState1.is(blockState.getBlock())) {
            if (!world.isClientSide && world.getBlockEntity(blockPos) == null) {
                this.checkIfExtend(world, blockPos, blockState);
            }

        }
    }

    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        return this.defaultBlockState().setValue(FACING, p_196258_1_.getNearestLookingDirection().getOpposite()).setValue(EXTENDED, Boolean.valueOf(false));
    }

    private void checkIfExtend(World world, BlockPos blockPos, BlockState blockState) {

    }

    private boolean getNeighborSignal(World world, BlockPos blockPos, Direction direction2) {
        for(Direction direction : Direction.values()) {
            if (direction != direction2 && world.hasSignal(blockPos.relative(direction), direction)) {
                return true;
            }
        }

        if (world.hasSignal(blockPos, Direction.DOWN)) {
            return true;
        } else {
            BlockPos blockpos = blockPos.above();

            for(Direction direction1 : Direction.values()) {
                if (direction1 != Direction.DOWN && world.hasSignal(blockpos.relative(direction1), direction1)) {
                    return true;
                }
            }

            return false;
        }
    }
    @Override
    public boolean triggerEvent(BlockState blockState, World world, BlockPos blockPos, int i, int j) {
        Direction direction = blockState.getValue(FACING);
        if (!world.isClientSide) {
            boolean flag = this.getNeighborSignal(world, blockPos, direction);
            if (flag && (i == 1 || i == 2)) {
                world.setBlock(blockPos, blockState.setValue(EXTENDED, Boolean.TRUE), 2);
                return false;
            }

            if (!flag && i == 0) {
                return false;
            }
        }

        if (i == 0) {
            if (net.minecraftforge.event.ForgeEventFactory.onPistonMovePre(world, blockPos, direction, true)) return false;
            if (!this.moveBlocks(world, blockPos, direction, true)) {
                return false;
            }

            world.setBlock(blockPos, blockState.setValue(EXTENDED, Boolean.TRUE), 67);
            world.playSound(null, blockPos, SoundEvents.PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.25F + 0.6F);
        } else if (i == 1 || i == 2) {
            if (net.minecraftforge.event.ForgeEventFactory.onPistonMovePre(world, blockPos, direction, false)) return false;
            TileEntity tileentity1 = world.getBlockEntity(blockPos.relative(direction));
            if (tileentity1 instanceof PistonTileEntity) {
                ((PistonTileEntity)tileentity1).finalTick();
            }

            BlockState blockstate = Blocks.MOVING_PISTON.defaultBlockState().setValue(MovingModularPiston.FACING, direction).setValue(MovingModularPiston.TYPE, this.isSticky ? PistonType.STICKY : PistonType.DEFAULT);
            world.setBlock(blockPos, blockstate, 20);
            world.setBlockEntity(blockPos, MovingModularPiston.newMovingBlockEntity(this.defaultBlockState().setValue(FACING, Direction.from3DDataValue(j & 7)), direction, false, true));
            world.blockUpdated(blockPos, blockstate.getBlock());
            blockstate.updateNeighbourShapes(world, blockPos, 2);
            if (this.isSticky) {
                BlockPos blockpos = blockPos.offset(direction.getStepX() * 2, direction.getStepY() * 2, direction.getStepZ() * 2);
                BlockState blockstate1 = world.getBlockState(blockpos);
                boolean flag1 = false;
                if (blockstate1.is(Blocks.MOVING_PISTON)) {
                    TileEntity tileentity = world.getBlockEntity(blockpos);
                    if (tileentity instanceof PistonTileEntity) {
                        PistonTileEntity pistontileentity = (PistonTileEntity)tileentity;
                        if (pistontileentity.getDirection() == direction && pistontileentity.isExtending()) {
                            pistontileentity.finalTick();
                            flag1 = true;
                        }
                    }
                }

                if (!flag1) {
                    if (i != 1 || blockstate1.isAir() || !isPushable(blockstate1, world, blockpos, direction.getOpposite(), false, direction) || blockstate1.getPistonPushReaction() != PushReaction.NORMAL && !blockstate1.is(Blocks.PISTON) && !blockstate1.is(Blocks.STICKY_PISTON)) {
                        world.removeBlock(blockPos.relative(direction), false);
                    } else {
                        this.moveBlocks(world, blockPos, direction, false);
                    }
                }
            } else {
                world.removeBlock(blockPos.relative(direction), false);
            }

            world.playSound(null, blockPos, SoundEvents.PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.15F + 0.6F);
        }

        net.minecraftforge.event.ForgeEventFactory.onPistonMovePost(world, blockPos, direction, (i == 0));
        return true;
    }

    public static boolean isPushable(BlockState blockState, World world, BlockPos blockPos, Direction direction, boolean b, Direction direction1) {
        if (blockPos.getY() >= 0 && blockPos.getY() <= world.getMaxBuildHeight() - 1 && world.getWorldBorder().isWithinBounds(blockPos)) {
            if (blockState.isAir()) {
                return true;
            } else if (!blockState.is(Blocks.OBSIDIAN) && !blockState.is(Blocks.CRYING_OBSIDIAN) && !blockState.is(Blocks.RESPAWN_ANCHOR)) {
                if (direction == Direction.DOWN && blockPos.getY() == 0) {
                    return false;
                } else if (direction == Direction.UP && blockPos.getY() == world.getMaxBuildHeight() - 1) {
                    return false;
                } else {
                    if (!blockState.is(Blocks.PISTON) && !blockState.is(Blocks.STICKY_PISTON)) {
                        if (blockState.getDestroySpeed(world, blockPos) == -1.0F) {
                            return false;
                        }

                        switch(blockState.getPistonPushReaction()) {
                            case BLOCK:
                                return false;
                            case DESTROY:
                                return b;
                            case PUSH_ONLY:
                                return direction == direction1;
                        }
                    } else if (blockState.getValue(EXTENDED)) {
                        return false;
                    }

                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean moveBlocks(World world, BlockPos blockPos, Direction direction1, boolean extending) {
        BlockPos blockpos = blockPos.relative(direction1);
        if (!extending && world.getBlockState(blockpos).is(Blocks.PISTON_HEAD)) {
            world.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 20);
        }

        ModularPistonStructureHelper structureHelper = new ModularPistonStructureHelper(world, blockPos, direction1, extending);
        if (!structureHelper.resolve()) {
            return false;
        } else {
            Map<BlockPos, BlockState> mapToPush = Maps.newHashMap();
            List<BlockPos> blockPosToPush = structureHelper.getToPush();
            List<BlockState> blockStatesToPush = Lists.newArrayList();

            // this for fills the blockStatesToPush list
            for(int i = 0; i < blockPosToPush.size(); ++i) {
                BlockPos blockpos1 = blockPosToPush.get(i);
                BlockState blockstate = world.getBlockState(blockpos1);
                blockStatesToPush.add(blockstate);
                mapToPush.put(blockpos1, blockstate);
            }


            List<BlockPos> blockPosesToDestroy = structureHelper.getToDestroy();
            BlockState[] blockStatesToDestroy = new BlockState[blockPosToPush.size() + blockPosesToDestroy.size()];
            Direction absoluteDirection = extending ? direction1 : direction1.getOpposite();
            int j = 0;

            // this for removes the blocks in blockPosesToDestroy (i think)
            for(int k = blockPosesToDestroy.size() - 1; k >= 0; --k) {
                BlockPos blockpos2 = blockPosesToDestroy.get(k);
                BlockState blockstate1 = world.getBlockState(blockpos2);
                TileEntity tileentity = blockstate1.hasTileEntity() ? world.getBlockEntity(blockpos2) : null;
                dropResources(blockstate1, world, blockpos2, tileentity);
                world.setBlock(blockpos2, Blocks.AIR.defaultBlockState(), 18);
                blockStatesToDestroy[j++] = blockstate1;
            }

            // this does the moving
            for(int l = blockPosToPush.size() - 1; l >= 0; --l) {
                BlockPos blockpos3 = blockPosToPush.get(l);
                BlockState blockstate5 = world.getBlockState(blockpos3);
                blockpos3 = blockpos3.relative(absoluteDirection);
                mapToPush.remove(blockpos3);
                world.setBlock(blockpos3, Blocks.MOVING_PISTON.defaultBlockState().setValue(FACING, direction1), 68);
                world.setBlockEntity(blockpos3, MovingModularPiston.newMovingBlockEntity(blockStatesToPush.get(l), direction1, extending, false));
                blockStatesToDestroy[j++] = blockstate5;
            }
            // ?????
            if (extending) {
                PistonType pistonType = this.isSticky ? PistonType.STICKY : PistonType.DEFAULT;
                BlockState pistonHeadBlockstate = Blocks.PISTON_HEAD.defaultBlockState().setValue(PistonHeadBlock.FACING, direction1).setValue(PistonHeadBlock.TYPE, pistonType);
                BlockState MovingPistonBlockstate = Blocks.MOVING_PISTON.defaultBlockState().setValue(MovingModularPiston.FACING, direction1).setValue(MovingModularPiston.TYPE, this.isSticky ? PistonType.STICKY : PistonType.DEFAULT);
                mapToPush.remove(blockpos);
                world.setBlock(blockpos, MovingPistonBlockstate, 68);
                world.setBlockEntity(blockpos, MovingModularPiston.newMovingBlockEntity(pistonHeadBlockstate, direction1, true, true));
            }

            BlockState airBlockstate = Blocks.AIR.defaultBlockState();

            // sets everything in hashmap to air
            for(BlockPos blockpos4 : mapToPush.keySet()) {
                world.setBlock(blockpos4, airBlockstate, 82);
            }

            for(Map.Entry<BlockPos, BlockState> entry : mapToPush.entrySet()) {
                BlockPos blockpos5 = entry.getKey();
                BlockState blockstate2 = entry.getValue();
                blockstate2.updateIndirectNeighbourShapes(world, blockpos5, 2);
                airBlockstate.updateNeighbourShapes(world, blockpos5, 2);
                airBlockstate.updateIndirectNeighbourShapes(world, blockpos5, 2);
            }

            j = 0;

            // updates i guess
            for(int i1 = blockPosesToDestroy.size() - 1; i1 >= 0; --i1) {
                BlockState blockstate7 = blockStatesToDestroy[j++];
                BlockPos blockpos6 = blockPosesToDestroy.get(i1);
                blockstate7.updateIndirectNeighbourShapes(world, blockpos6, 2);
                world.updateNeighborsAt(blockpos6, blockstate7.getBlock());
            }

            for(int j1 = blockPosToPush.size() - 1; j1 >= 0; --j1) {
                world.updateNeighborsAt(blockPosToPush.get(j1), blockStatesToDestroy[j++].getBlock());
            }

            if (extending) {
                world.updateNeighborsAt(blockpos, Blocks.PISTON_HEAD);
            }

            return true;
        }
    }

    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    public BlockState rotate(BlockState state, net.minecraft.world.IWorld world, BlockPos pos, Rotation direction) {
        return state.getValue(EXTENDED) ? state : super.rotate(state, world, pos, direction);
    }

    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, EXTENDED);
    }

    public boolean useShapeForLightOcclusion(BlockState blockState) {
        return blockState.getValue(EXTENDED);
    }

    public boolean isPathfindable(BlockState blockState, IBlockReader iBlockReader, BlockPos blockPos, PathType pathType) {
        return false;
    }
}
