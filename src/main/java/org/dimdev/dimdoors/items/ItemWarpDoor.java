package org.dimdev.dimdoors.items;

import org.dimdev.dimdoors.blocks.BaseDimDoor;
import org.dimdev.dimdoors.mod_pocketDim;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemWarpDoor extends BaseItemDoor {
    public ItemWarpDoor(Material material, ItemDoor door) {
        super(material, door);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        mod_pocketDim.translateAndAdd("info.warpDoor", par3List);
    }

    @Override
    protected BaseDimDoor getDoorBlock() {
        return (BaseDimDoor) mod_pocketDim.warpDoor;
    }
}