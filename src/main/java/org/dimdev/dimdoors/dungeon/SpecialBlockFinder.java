package org.dimdev.dimdoors.dungeon;

import org.dimdev.dimdoors.Point3D;
import org.dimdev.dimdoors.schematic.Schematic;
import org.dimdev.dimdoors.schematic.SchematicFilter;
import net.minecraft.block.Block;

import java.util.ArrayList;

public class SpecialBlockFinder extends SchematicFilter {

    private final Block warpDoor;
    private final Block dimensionalDoor;
    private final Block monolithSpawnMarker;
    private final Block exitMarker;
    private int entranceOrientation;
    private Schematic schematic;
    private Point3D entranceDoorLocation;
    private final ArrayList<Point3D> exitDoorLocations;
    private final ArrayList<Point3D> dimensionalDoorLocations;
    private final ArrayList<Point3D> monolithSpawnLocations;

    public SpecialBlockFinder(Block warpDoor, Block dimensionalDoor, Block monolithSpawn, Block exitDoor) {
        super("SpecialBlockFinder");
        this.warpDoor = warpDoor;
        this.dimensionalDoor = dimensionalDoor;
        this.monolithSpawnMarker = monolithSpawn;
        this.exitMarker = exitDoor;
        this.entranceDoorLocation = null;
        this.entranceOrientation = 0;
        this.exitDoorLocations = new ArrayList<Point3D>();
        this.dimensionalDoorLocations = new ArrayList<Point3D>();
        this.monolithSpawnLocations = new ArrayList<Point3D>();
        this.schematic = null;
    }

    public int getEntranceOrientation() {
        return entranceOrientation;
    }

    public Point3D getEntranceDoorLocation() {
        return entranceDoorLocation;
    }

    public ArrayList<Point3D> getExitDoorLocations() {
        return exitDoorLocations;
    }

    public ArrayList<Point3D> getDimensionalDoorLocations() {
        return dimensionalDoorLocations;
    }

    public ArrayList<Point3D> getMonolithSpawnLocations() {
        return monolithSpawnLocations;
    }

    @Override
    protected boolean initialize(Schematic schematic, Block[] blocks, byte[] metadata) {
        this.schematic = schematic;
        return true;
    }

    @Override
    protected boolean applyToBlock(int index, Block[] blocks, byte[] metadata) {
        int indexBelow;
        int indexDoubleBelow;

        if (blocks[index] == monolithSpawnMarker) {
            monolithSpawnLocations.add(schematic.calculatePoint(index));
            return true;
        }
        if (blocks[index] == dimensionalDoor) {
            indexBelow = schematic.calculateIndexBelow(index);
            if (indexBelow >= 0 && blocks[indexBelow] == dimensionalDoor) {
                dimensionalDoorLocations.add(schematic.calculatePoint(index));
                return true;
            } else {
                return false;
            }
        }
        if (blocks[index] == warpDoor) {
            indexBelow = schematic.calculateIndexBelow(index);
            if (indexBelow >= 0 && blocks[indexBelow] == warpDoor) {
                indexDoubleBelow = schematic.calculateIndexBelow(indexBelow);
                if (indexDoubleBelow >= 0 && blocks[indexDoubleBelow] == exitMarker) {
                    exitDoorLocations.add(schematic.calculatePoint(index));
                    return true;
                } else if (entranceDoorLocation == null) {
                    entranceDoorLocation = schematic.calculatePoint(index);
                    entranceOrientation = (metadata[indexBelow] & 3);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean terminates() {
        return false;
    }
}
