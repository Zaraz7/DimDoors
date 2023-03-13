package org.dimdev.dimdoors.world;

import java.util.Objects;

import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.dimdev.dimdoors.DimensionalDoors;
import org.dimdev.dimdoors.world.pocket.BlankChunkGenerator;

public final class ModDimensions {
    public static final ResourceKey<Level> LIMBO = ResourceKey.create(Registries.DIMENSION, DimensionalDoors.resource("limbo"));
    public static final ResourceKey<Level> PERSONAL = ResourceKey.create(Registries.DIMENSION, DimensionalDoors.resource("personal_pockets"));
    public static final ResourceKey<Level> PUBLIC = ResourceKey.create(Registries.DIMENSION, DimensionalDoors.resource("public_pockets"));
    public static final ResourceKey<Level> DUNGEON = ResourceKey.create(Registries.DIMENSION, DimensionalDoors.resource("dungeon_pockets"));

    public static final ResourceKey<DimensionType> LIMBO_TYPE_KEY = ResourceKey.create(Registries.DIMENSION_TYPE, DimensionalDoors.resource("limbo"));
    public static final ResourceKey<DimensionType> POCKET_TYPE_KEY = ResourceKey.create(Registries.DIMENSION_TYPE, DimensionalDoors.resource("personal_pockets"));

    public static DimensionType LIMBO_TYPE;
    public static DimensionType POCKET_TYPE;

    public static ServerLevel LIMBO_DIMENSION;
    public static ServerLevel PERSONAL_POCKET_DIMENSION;
    public static ServerLevel PUBLIC_POCKET_DIMENSION;
    public static ServerLevel DUNGEON_POCKET_DIMENSION;

    public static boolean isPocketDimension(Level world) {
        return isPocketDimension(world.dimension());
    }

    public static boolean isPrivatePocketDimension(Level world) {
		return world != null && world == PERSONAL_POCKET_DIMENSION;
    }

    public static boolean isPocketDimension(ResourceKey<Level> type) {
        return Objects.equals(type, PERSONAL) || Objects.equals(type, PUBLIC) || Objects.equals(type, DUNGEON);
    }

    public static boolean isLimboDimension(Level world) {
        return world != null && world.dimension().equals(LIMBO);
    }

    public static void init() {
		MinecraftServer server = DimensionalDoors.getServer();
		ModDimensions.LIMBO_TYPE = server.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE).get(LIMBO_TYPE_KEY);
		ModDimensions.POCKET_TYPE = server.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE).get(POCKET_TYPE_KEY);
		ModDimensions.LIMBO_DIMENSION = server.getLevel(LIMBO);
		ModDimensions.PERSONAL_POCKET_DIMENSION = server.getLevel(PERSONAL);
		ModDimensions.PUBLIC_POCKET_DIMENSION = server.getLevel(PUBLIC);
		ModDimensions.DUNGEON_POCKET_DIMENSION = server.getLevel(DUNGEON);
		Registry.register(BuiltInRegistries.CHUNK_GENERATOR, DimensionalDoors.resource("blank"), BlankChunkGenerator.CODEC);
	}
}
