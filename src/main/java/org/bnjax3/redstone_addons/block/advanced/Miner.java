package org.bnjax3.redstone_addons.block.advanced;
import net.minecraftforge.common.Tags;
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
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    @SuppressWarnings("deprecated")
    @Override
    public void tick(BlockState blockState, ServerWorld serverWorld, BlockPos pos, Random random) {
        if (blockState.getValue(POWERED))
        {
            damageBlockFacing();
        }
        
        super.tick(blockState, serverWorld, pos, random);
    }
    public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
        return p_185499_1_.setValue(FACING, p_185499_2_.rotate(p_185499_1_.getValue(FACING)));
    }

    public BlockState mirror(BlockState p_185471_1_, Mirror p_185471_2_) {
        return p_185471_1_.rotate(p_185471_2_.getRotation(p_185471_1_.getValue(FACING)));
    }
    public void damageBlockFacing(){

    }
}
