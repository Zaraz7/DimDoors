package org.dimdev.dimdoors.block;

import org.jetbrains.annotations.Nullable;
import net.fabricmc.api.Dist;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.dimdev.dimdoors.block.entity.DetachedRiftBlockEntity;
import org.dimdev.dimdoors.block.entity.ModBlockEntityTypes;
import org.dimdev.dimdoors.particle.client.RiftParticleEffect;
import org.dimdev.dimdoors.world.ModDimensions;

public class DetachedRiftBlock extends WaterLoggableBlockWithEntity implements RiftProvider<DetachedRiftBlockEntity>, SimpleWaterloggedBlock {
	public static final String ID = "rift";
	public DetachedRiftBlock(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public MaterialColor defaultMaterialColor() {
		return MaterialColor.COLOR_BLACK;
	}

	@Override
	public DetachedRiftBlockEntity getRift(Level world, BlockPos pos, BlockState state) {
		return (DetachedRiftBlockEntity) world.getBlockEntity(pos);
	}

	@Override
	@Environment(Dist.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		// randomDisplayTick can be called before the tile entity is created in multiplayer
		if (!(blockEntity instanceof DetachedRiftBlockEntity)) return;
		DetachedRiftBlockEntity rift = (DetachedRiftBlockEntity) blockEntity;

		boolean outsidePocket = !ModDimensions.isPocketDimension(world);
		double speed = 0.1;

		if (rift.closing) {
			world.addParticle(RiftParticleEffect.of(outsidePocket),
					pos.getX() + .5,
					pos.getY() + .5,
					pos.getZ() + .5,
					rand.nextGaussian() * speed,
					rand.nextGaussian() * speed,
					rand.nextGaussian() * speed
			);
		}

		world.addParticle(RiftParticleEffect.of(outsidePocket, rift.stabilized),
				pos.getX() + .5,
				pos.getY() + .5,
				pos.getZ() + .5,
				rand.nextGaussian() * speed,
				rand.nextGaussian() * speed,
				rand.nextGaussian() * speed
		);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
		return Shapes.block();
	}

	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.block();
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DetachedRiftBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, ModBlockEntityTypes.DETACHED_RIFT, DetachedRiftBlockEntity::tick);
	}
}
