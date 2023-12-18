package org.dimdev.dimdoors.items;

import org.dimdev.dimdoors.mod_pocketDim;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemWorldThread extends Item {
    public ItemWorldThread() {
        super();
        this.setCreativeTab(mod_pocketDim.dimDoorsCreativeTab);
    }

    @Override
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(mod_pocketDim.modid + ":" + this.getUnlocalizedName().replace("item.", ""));
    }
}
