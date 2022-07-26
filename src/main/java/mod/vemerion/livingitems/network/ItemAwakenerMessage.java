package mod.vemerion.livingitems.network;

import java.util.function.Supplier;

import mod.vemerion.livingitems.blockentity.ItemAwakenerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ItemAwakenerMessage {

	private ItemAwakenerBlockEntity.Data data;
	private BlockPos pos;

	public ItemAwakenerMessage(ItemAwakenerBlockEntity.Data data, BlockPos pos) {
		this.data = data;
		this.pos = pos;
	}

	public void encode(final FriendlyByteBuf buffer) {
		data.writeToBuffer(buffer);
		buffer.writeBlockPos(pos);
	}

	public static ItemAwakenerMessage decode(final FriendlyByteBuf buffer) {
		return new ItemAwakenerMessage(new ItemAwakenerBlockEntity.Data(buffer), buffer.readBlockPos());
	}

	public void handle(final Supplier<NetworkEvent.Context> supplier) {
		final NetworkEvent.Context context = supplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(() -> {
			var player = context.getSender();
			var level = player.level;
			if (pos.distSqr(player.blockPosition()) < 64
					&& level.getBlockEntity(pos) instanceof ItemAwakenerBlockEntity itemAwakener) {
				itemAwakener.receiveMessage(data);
			}
		});
	}
}
