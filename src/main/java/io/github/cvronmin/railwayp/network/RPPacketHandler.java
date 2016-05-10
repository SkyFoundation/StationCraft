package io.github.cvronmin.railwayp.network;

import io.netty.channel.ChannelHandler.Sharable;
import io.github.cvronmin.railwayp.RailwayP;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Sharable
public class RPPacketHandler extends SimpleChannelInboundHandler<IRPPacket>{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IRPPacket msg)
	    throws Exception {
	INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
	EntityPlayer player = RailwayP.proxy.getPlayerFromNetHandler(netHandler);

	switch (FMLCommonHandler.instance().getEffectiveSide())
	{
	case CLIENT:
		msg.handleClientSide(player);
		break;
	case SERVER:
		msg.handleServerSide(player);
		break;
	default:
		break;
	}
    }

}
