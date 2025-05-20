package org.bnjax3.redstone_addons.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;


public class ModItemGroup {
     public static final ItemGroup REDSTONE_ADDONS_GROUP = new ItemGroup("Redstone Addons"){
         @Override
         public ItemStack makeIcon() {
             return new ItemStack(ModItems.TEST_ITEM.get());
         }
     };
}
