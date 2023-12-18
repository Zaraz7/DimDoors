package org.dimdev.dimdoors.blocks;

import org.dimdev.dimdoors.config.DDProperties;
import org.dimdev.dimdoors.core.DimLink;
import org.dimdev.dimdoors.core.LinkType;
import org.dimdev.dimdoors.core.NewDimData;
import org.dimdev.dimdoors.core.PocketManager;
import org.dimdev.dimdoors.mod_pocketDim;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class DimensionalDoor extends BaseDimDoor {
    public DimensionalDoor(Material material, DDProperties properties) {
        super(material, properties);
    }

    @Override
    public void placeLink(World world, int x, int y, int z) {
        if (!world.isRemote && world.getBlock(x, y - 1, z) == this) {
            NewDimData dimension = PocketManager.createDimensionData(world);
            DimLink link = dimension.getLink(x, y, z);
            if (link == null) {
                dimension.createLink(x, y, z, LinkType.POCKET, world.getBlockMetadata(x, y - 1, z));
            }
        }
    }

    @Override
    public Item getDoorItem() {
        return mod_pocketDim.itemDimensionalDoor;
    }
}