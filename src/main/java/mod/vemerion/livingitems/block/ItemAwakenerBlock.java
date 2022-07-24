package mod.vemerion.livingitems.block;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import mod.vemerion.livingitems.menu.ItemAwakenerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.network.NetworkHooks;

public class ItemAwakenerBlock extends Block {

	private static final Map<Direction, VoxelShape> SHAPES = ImmutableMap.of(Direction.NORTH,
			Block.box(1, 1, 12, 15, 15, 16), Direction.SOUTH, Block.box(1, 1, 0, 15, 15, 4), Direction.WEST,
			Block.box(12, 1, 1, 16, 15, 15), Direction.EAST, Block.box(0, 1, 1, 4, 15, 15), Direction.UP,
			Block.box(1, 0, 1, 15, 4, 15), Direction.DOWN, Block.box(1, 12, 1, 15, 16, 15));

	public ItemAwakenerBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(BlockStateProperties.FACING, Direction.NORTH));
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPES.get(pState.getValue(BlockStateProperties.FACING));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		pBuilder.add(BlockStateProperties.FACING);
	}

	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		var base = pPos.relative(pState.getValue(BlockStateProperties.FACING).getOpposite());
		var blockEntity = pLevel.getBlockEntity(base);
		return blockEntity != null
				&& blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent();
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		for (var direction : pContext.getNearestLookingDirections()) {
			var state = defaultBlockState().setValue(BlockStateProperties.FACING, direction.getOpposite());
			if (state.canSurvive(pContext.getLevel(), pContext.getClickedPos())) {
				return state;
			}
		}
		return null;
	}

	@Override
	public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState,
			LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
		return pDirection.getOpposite() == pState.getValue(BlockStateProperties.FACING)
				&& !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : pState;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult hit) {
		if (!level.isClientSide) {
			NetworkHooks.openGui((ServerPlayer) player, new MenuProvider() {

				@Override
				public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
					return new ItemAwakenerMenu(pContainerId, pInventory, ContainerLevelAccess.create(level, pos));
				}

				@Override
				public Component getDisplayName() {
					return ItemAwakenerBlock.this.getName();
				}

			}, pos);
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}

}
