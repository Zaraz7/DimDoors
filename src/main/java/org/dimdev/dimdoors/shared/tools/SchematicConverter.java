package org.dimdev.dimdoors.shared.tools;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.dimdev.ddutils.schem.Schematic;
import org.dimdev.dimdoors.DimDoors;
import org.dimdev.dimdoors.shared.blocks.BlockFabric;
import org.dimdev.dimdoors.shared.blocks.ModBlocks;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Robijnvogel
 */
public final class SchematicConverter {

    private static final Map<String, IBlockState> stateMap = new HashMap<>();

    static {
        stateMap.put("dimdoors:Dimensional Door", ModBlocks.DIMENSIONAL_DOOR.getDefaultState());
        stateMap.put("dimdoors:Fabric of Reality", ModBlocks.FABRIC.getDefaultState().withProperty(BlockFabric.TYPE, BlockFabric.EnumType.REALITY));
        stateMap.put("dimdoors:Fabric of RealityPerm", ModBlocks.FABRIC.getDefaultState().withProperty(BlockFabric.TYPE, BlockFabric.EnumType.ANCIENT));
        stateMap.put("dimdoors:transientDoor", ModBlocks.TRANSIENT_DIMENSIONAL_DOOR.getDefaultState());
        stateMap.put("dimdoors:Warp Door", ModBlocks.WARP_DIMENSIONAL_DOOR.getDefaultState());
    }

    public static Schematic convertSchematic(NBTTagCompound nbt, String name) { //@todo, maybe make this a separate class, so values can be final so they HAVE TO  be set in a newly designed constructor?
        Schematic schematic = new Schematic();

        schematic.version = 1; //already the default value
        schematic.author = "DimDoors"; // TODO: didn't the old schematics have an author?
        schematic.schematicName = name.equals("") ? "Unknown" : name;
        schematic.creationDate = System.currentTimeMillis();
        schematic.requiredMods = new String[]{DimDoors.MODID};

        schematic.width = nbt.getShort("Width");
        schematic.height = nbt.getShort("Height");
        schematic.length = nbt.getShort("Length");
        schematic.offset = new int[]{0, 0, 0};

        byte[] blockIntArray = nbt.getByteArray("Blocks");
        if (nbt.hasKey("Palette")) {
            NBTTagList paletteNBT = (NBTTagList) nbt.getTag("Palette");
            for (int i = 0; i < paletteNBT.tagCount(); i++) {
                String blockString = paletteNBT.getStringTagAt(i);
                IBlockState blockstate;

                // Get the correct block state
                if (blockString.startsWith("dimdoors")) {
                    blockstate = stateMap.get(blockString);
                } else {
                    blockstate = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockString)).getDefaultState();
                }

                schematic.pallette.add(blockstate);
            }
        } else {
            byte[] addId = nbt.getByteArray("AddBlocks");
            Map<Integer, Byte> palletteMap = new HashMap<>(); // block ID -> pallette index
            byte currentPalletteIndex = 0;
            for (int i = 0; i < blockIntArray.length; i++) {
                int id;
                if (i >> 1 >= addId.length) {
                    id = (short) (blockIntArray[i] & 0xFF);
                } else if ((i & 1) == 0) {
                    id = (short) (((addId[i >> 1] & 0x0F) << 8) + (blockIntArray[i] & 0xFF));
                } else {
                    id = (short) (((addId[i >> 1] & 0xF0) << 4) + (blockIntArray[i] & 0xFF));
                }
                if (palletteMap.containsKey(id)) {
                    blockIntArray[i] = palletteMap.get(id);
                } else {
                    Block block = Block.getBlockById(id);
                    switch (id) {
                        case 1975:
                            block = ModBlocks.WARP_DIMENSIONAL_DOOR;
                            break;
                        case 1970:
                            block = ModBlocks.DIMENSIONAL_DOOR;
                            break;
                        case 1979:
                            block = ModBlocks.TRANSIENT_DIMENSIONAL_DOOR;
                            break;
                    }
                    if (id != 0 && block.getRegistryName().toString().equals("minecraft:air"))
                        throw new RuntimeException("Change conversion code!");
                    schematic.pallette.add(block.getDefaultState());
                    palletteMap.put(id, currentPalletteIndex);
                    blockIntArray[i] = currentPalletteIndex;
                    currentPalletteIndex++;
                }
            }
        }

        byte[] dataIntArray = nbt.getByteArray("Data");
        schematic.blockData = new int[schematic.width][schematic.height][schematic.length];
        for (int x = 0; x < schematic.width; x++) {
            for (int y = 0; y < schematic.height; y++) {
                for (int z = 0; z < schematic.length; z++) {
                    int blockInt = blockIntArray[x + z * schematic.width + y * schematic.width * schematic.length]; //according to the documentation on https://github.com/SpongePowered/Schematic-Specification/blob/master/versions/schematic-1.md
                    int metadata = dataIntArray[x + z * schematic.width + y * schematic.width * schematic.length]; //according to the documentation on https://github.com/SpongePowered/Schematic-Specification/blob/master/versions/schematic-1.md

                    IBlockState baseState = schematic.pallette.get(blockInt); //this is the default blockstate except for ancient fabric
                    if (baseState == baseState.getBlock().getDefaultState()) { //should only be false if {@code baseState} is ancient fabric
                        IBlockState additionalState = baseState.getBlock().getStateFromMeta(metadata);
                        if (schematic.pallette.contains(additionalState)) { //check whether or not this blockstate is already in the list
                            blockInt = schematic.pallette.indexOf(additionalState);
                        } else {
                            schematic.pallette.add(additionalState);
                            //DimDoors.log.info("New blockstate detected. Original blockInt = " + blockInt + " and baseState is " + baseState);
                            blockInt = schematic.pallette.size() - 1;
                        }
                    } else { // if this is ancient fabric
                        blockInt = schematic.pallette.indexOf(baseState);
                    }
                    assert blockInt >= 0;
                    schematic.blockData[x][y][z] = blockInt;
                }
            }
        }
        schematic.paletteMax = schematic.pallette.size() - 1;

        NBTTagList tileEntitiesNBT = (NBTTagList) nbt.getTag("TileEntities");
        for (int i = 0; i < tileEntitiesNBT.tagCount(); i++) {
            NBTTagCompound tileEntityNBT = tileEntitiesNBT.getCompoundTagAt(i);
            int x = tileEntityNBT.getInteger("x");
            int y = tileEntityNBT.getInteger("y");
            int z = tileEntityNBT.getInteger("z");
            switch (tileEntityNBT.getString("id")) {
                case "TileEntityDimDoor":
                    tileEntityNBT = new NBTTagCompound();
                    tileEntityNBT.setString("id", "EntranceRift");
                    tileEntityNBT.setInteger("x", x);
                    tileEntityNBT.setInteger("y", y);
                    tileEntityNBT.setInteger("z", z);
                    // TODO
                    break;
                case "TileEntityRift":
                    tileEntityNBT = new NBTTagCompound();
                    tileEntityNBT.setString("id", "FloatingRift");
                    tileEntityNBT.setInteger("x", x);
                    tileEntityNBT.setInteger("y", y);
                    tileEntityNBT.setInteger("z", z);
                    // TODO
                    break;
                case "Sign":
                    DimDoors.log.info("Sign: "
                                      + tileEntityNBT.getString("Text1") + "|"
                                      + tileEntityNBT.getString("Text2") + "|"
                                      + tileEntityNBT.getString("Text3") + "|"
                                      + tileEntityNBT.getString("Text4"));
                    tileEntityNBT.setString("Text1", "{\"text\":\"" + tileEntityNBT.getString("Text1") + "\"}");
                    tileEntityNBT.setString("Text2", "{\"text\":\"" + tileEntityNBT.getString("Text2") + "\"}");
                    tileEntityNBT.setString("Text3", "{\"text\":\"" + tileEntityNBT.getString("Text3") + "\"}");
                    tileEntityNBT.setString("Text4", "{\"text\":\"" + tileEntityNBT.getString("Text4") + "\"}");
                    break;
                case "Chest":
                    break;
                default:
                    DimDoors.log.info("TileEntity found: " + tileEntityNBT.getString("id"));
                    break;
            }
            schematic.tileEntities.add(tileEntityNBT);
        }

        return schematic;
    }
}
