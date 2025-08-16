package org.bnjax3.redstone_addons.block.advanced;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import org.bnjax3.redstone_addons.tileentity.MinerTile;
import org.bnjax3.redstone_addons.tileentity.ModTileEntities;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

public class Miner extends DirectionalBlock {
    public Miner(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, Boolean.FALSE));
    }
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public BlockPos blockPosOfBlockToBreak;

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.MINER_TILE_ENTITY.get().create();
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_149656_1_) {
        return PushReaction.NORMAL;
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
            if (hand == Hand.MAIN_HAND && !world.isClientSide){
                ItemStack itemInHand = player.getItemInHand(hand);
                TileEntity tileEntity = world.getBlockEntity(blockPos);
                if (!(tileEntity instanceof MinerTile)){
                    System.out.println("que carajovich esto no deberia pasar");
                    return ActionResultType.PASS;
                }
                boolean isHandEmpty = itemInHand == ItemStack.EMPTY;
                boolean isBlockEmpty = ((MinerTile)tileEntity).getTool() == ItemStack.EMPTY;
                System.out.println(itemInHand);
                System.out.println(((MinerTile) tileEntity).getTool());
                if (!isHandEmpty && isBlockEmpty) {
                    System.out.println("store item");
                    ((MinerTile) tileEntity).setTool(itemInHand);
                    player.setItemInHand(hand, ItemStack.EMPTY);
                    world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1.0F, 1.0F);
                } else if (isHandEmpty && !isBlockEmpty) {
                    System.out.println("get item");
                    player.setItemInHand(hand, ((MinerTile) tileEntity).getTool());
                    ((MinerTile) tileEntity).setTool(ItemStack.EMPTY);
                    world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
        return super.use(blockState, world, blockPos, player, hand, blockRayTraceResult);
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void onRemove(BlockState blockState, World world, BlockPos pos, BlockState blockState1, boolean b) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof MinerTile){
            if (((MinerTile) tileEntity).getTool() != ItemStack.EMPTY){
                InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), ((MinerTile) tileEntity).getTool());
            }
            super.onRemove(blockState, world, pos, blockState1, b);
        }


    }
    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void tick(BlockState blockState, ServerWorld serverWorld, BlockPos pos, Random random) {

        TileEntity tileEntity = serverWorld.getBlockEntity(pos);
        blockPosOfBlockToBreak = getBlockPosOfFacingBlock(pos, blockState.getValue(FACING));

        if (tileEntity instanceof MinerTile) {
            try {
                if (!serverWorld.getBlockState(blockPosOfBlockToBreak).getMaterial().isReplaceable()) {
                    serverWorld.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.DISPENSER_DISPENSE, SoundCategory.BLOCKS, 0.5F, 0.5F);
                    breakBlockWithItem(blockPosOfBlockToBreak, serverWorld, (MinerTile) tileEntity);
                } else {
                    serverWorld.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.DISPENSER_FAIL, SoundCategory.BLOCKS, 0.5F, 0.5F);
                }
            } catch (Exception e) {
                System.out.println("se rompio todo we, : " + e);
            }
        }
        super.tick(blockState, serverWorld, pos, random);
    }
    @SuppressWarnings("deprecation")
    @Override
    @ParametersAreNonnullByDefault
    public void onPlace(BlockState blockState, World world, BlockPos pos, BlockState blockState1, boolean b) {
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
        return this.defaultBlockState().setValue(FACING, blockItemUseContext.getNearestLookingDirection().getOpposite());
    }
    @ParametersAreNonnullByDefault
    public void setPlacedBy(World p_180633_1_, BlockPos p_180633_2_, BlockState p_180633_3_, @Nullable LivingEntity p_180633_4_, ItemStack p_180633_5_) {

    }
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState blockState, World world, BlockPos blockPos, Block block, BlockPos blockPos1, boolean b) {
        if (!world.isClientSide) {
            boolean isPowered = blockState.getValue(POWERED);
            boolean isNeighborPowered = world.hasNeighborSignal(blockPos) || world.hasNeighborSignal(blockPos.above());

            if (!isPowered && isNeighborPowered) {
                blockState.setValue(POWERED, Boolean.FALSE);
                world.getBlockTicks().scheduleTick(blockPos, this, 4);
            } else if (isPowered && !isNeighborPowered){
                blockState.setValue(POWERED, Boolean.FALSE);
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
        blockStateBuilder.add(FACING, POWERED);
    }

    public BlockPos getBlockPosOfFacingBlock(BlockPos pos, Direction direction)
    {

        Vector3i directionVector = direction.getNormal();
        // I DO NOT CARE ABOUT THIS VARIABLE BEING USELESS. IT PROTECTS MY EYES AND BRAIN.
        @SuppressWarnings("")
        BlockPos facingBlockPosition = new BlockPos(pos.getX() + directionVector.getX(), pos.getY() + directionVector.getY(), pos.getZ() + directionVector.getZ());
        return facingBlockPosition;
    }
    public static void breakBlockWithItem(BlockPos blockPos, ServerWorld world, MinerTile minerTile){
        BlockState blockState = world.getBlockState(blockPos);
        ItemStack storedItemForReading = minerTile.getTool();
        boolean isBlockWaterlogged;

            try {
                isBlockWaterlogged = blockState.getValue(BlockStateProperties.WATERLOGGED);
            } catch (Exception e) {
                isBlockWaterlogged = false;
            }
            if (!shouldBeAbleToBreakBlockAndDrop(storedItemForReading, blockState))
            {
                return;
            }
            Block.dropResources(blockState, world, blockPos);
            if (isBlockWaterlogged) {
                world.setBlockAndUpdate(blockPos, Blocks.WATER.defaultBlockState());
            } else {
                world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
            }
            minerTile.damageTool();

    }

    public static boolean shouldBeAbleToBreakBlockAndDrop(ItemStack itemStack, BlockState blockState){
        ToolType toolTypeNeeded = blockState.getHarvestTool();
        Item item = itemStack.getItem();
        if (item instanceof ToolItem && blockState.requiresCorrectToolForDrops() && !isToolAboutToBreak(itemStack)){
            System.out.println("el item si es una herramienta y no se va a romper");
            System.out.println(blockState.getHarvestLevel());
            System.out.println(((ToolItem) item).getTier().getLevel());
            System.out.println(itemStack.getItem().getToolTypes(itemStack).contains(toolTypeNeeded));
            if (blockState.getHarvestLevel() <= ((ToolItem) item).getTier().getLevel() && itemStack.getItem().getToolTypes(itemStack).contains(toolTypeNeeded))
            {
                return true;
            } else {
                System.out.println("la herramienta no es lo suficientemente fuerte");
            }
        } else if (!blockState.requiresCorrectToolForDrops()) {
            return true;
        }
        return false;
    }

    private static boolean isToolAboutToBreak(ItemStack itemStack){
        int damage = itemStack.getDamageValue();
        int maxDamage = itemStack.getMaxDamage();
        System.out.println(damage);
        System.out.println(maxDamage);
        return !(damage < (maxDamage - 1));
    }
}
