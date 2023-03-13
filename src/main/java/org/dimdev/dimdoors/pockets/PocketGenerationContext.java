package org.dimdev.dimdoors.pockets;

import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import org.dimdev.dimdoors.DimensionalDoors;
import org.dimdev.dimdoors.rift.registry.LinkProperties;
import org.dimdev.dimdoors.rift.targets.VirtualTarget;
import org.dimdev.dimdoors.world.pocket.VirtualLocation;

public record PocketGenerationContext(ServerLevel world, VirtualLocation sourceVirtualLocation, VirtualTarget linkTo, LinkProperties linkProperties) {
	public Map<String, Double> toVariableMap(Map<String, Double> stringDoubleMap) {
		stringDoubleMap.put("depth", (double) this.sourceVirtualLocation.getDepth());
		stringDoubleMap.put("public_size", (double) Constants.CONFIG_MANAGER.get().getPocketsConfig().publicPocketSize);
		stringDoubleMap.put("private_size", (double) Constants.CONFIG_MANAGER.get().getPocketsConfig().privatePocketSize);
		return stringDoubleMap;
	}
}
