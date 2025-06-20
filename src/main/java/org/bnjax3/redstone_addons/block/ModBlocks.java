package org.bnjax3.redstone_addons.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import org.bnjax3.redstone_addons.Redstone_addons;
import org.bnjax3.redstone_addons.block.advanced.Miner;
import org.bnjax3.redstone_addons.block.advanced.ModularPiston;
import org.bnjax3.redstone_addons.item.ModItemGroup;
import org.bnjax3.redstone_addons.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Redstone_addons.MOD_ID);
    public static final RegistryObject<Block> MINER_BLOCK = registerBlock("miner", () -> new Miner(AbstractBlock.Properties.of(Material.PISTON).harvestTool(ToolType.PICKAXE).strength(2f).harvestLevel(1)));
    public static final RegistryObject<Block> MODULAR_PISTON_BLOCK = registerBlock("modular_piston", () -> new ModularPiston(false, AbstractBlock.Properties.of(Material.PISTON)));
    public static final RegistryObject<Block> MODULAR_STICKY_PISTON_BLOCK = registerBlock("modular_sticky_piston", () -> new ModularPiston(true, AbstractBlock.Properties.of(Material.PISTON)));

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block)
    {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block)
    {
        ModItems.ITEMS.register( name, () -> new BlockItem(block.get(), new Item.Properties().tab(ModItemGroup.REDSTONE_ADDONS_GROUP)));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
