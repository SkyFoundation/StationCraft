package tk.cvrunmin.railwayp.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public interface IRPPacket {
	public void encode(ChannelHandlerContext context, ByteBuf buffer);

	public void decode(ChannelHandlerContext context, ByteBuf buffer);

	public void handleClientSide(EntityPlayer player);

	public void handleServerSide(EntityPlayer player);
}
