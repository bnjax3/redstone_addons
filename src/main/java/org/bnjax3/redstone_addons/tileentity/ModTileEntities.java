package org.bnjax3.redstone_addons.tileentity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.bnjax3.redstone_addons.Redstone_addons;
import org.bnjax3.redstone_addons.block.ModBlocks;

public class ModTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Redstone_addons.MOD_ID);

    public static final RegistryObject<TileEntityType<MinerTile>> MINER_TILE_ENTITY = TILE_ENTITIES.register("miner_tile_entity", () -> TileEntityType.Builder.of(MinerTile::new, ModBlocks.MINER_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus){
        TILE_ENTITIES.register(eventBus);
    }
}































