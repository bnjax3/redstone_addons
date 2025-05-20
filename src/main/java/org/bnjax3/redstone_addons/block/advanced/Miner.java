package org.bnjax3.redstone_addons.block.advanced;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class Miner extends DirectionalBlock {
    public Miner(Properties properties) {
        super(properties);
    }
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    @SuppressWarnings("deprecated")
    @Override
    public void tick(BlockState blockState, ServerWorld serverWorld, BlockPos pos, Random random) {
        if (blockState.getValue(POWERED))
        {

        }
        
        super.tick(blockState, serverWorld, pos, random);
    }
}
