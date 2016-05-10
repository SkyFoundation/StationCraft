package io.github.cvronmin.railwayp.network;

import io.github.cvronmin.railwayp.Reference;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.EnumMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class RPChannelHandler extends FMLIndexedMessageToMessageCodec<IRPPacket> {
	private EnumMap<Side, FMLEmbeddedChannel> channels;
	private RPChannelHandler() {
	    this.addDiscriminator(0, RPPacket.class);
	}
	@Override
	public void encodeInto(ChannelHandlerContext ctx, IRPPacket packet, ByteBuf data) throws Exception {
		packet.encode(ctx, data);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data, IRPPacket packet) {
	    packet.decode(ctx, data);
/*	    switch (FMLCommonHandler.instance().getEffectiveSide()) {
	        case CLIENT:
	            packet.handleClientSide(FMLClientHandler.instance().getClientPlayerEntity());
	            break;
	        case SERVER:
	            INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
	            packet.handleServerSide(((NetHandlerPlayServer) netHandler).playerEntity);
	            break;
	    }*/
	}

	public static RPChannelHandler init() {
	    RPChannelHandler channelh = new RPChannelHandler();
	    channelh.channels = NetworkRegistry.INSTANCE.newChannel(Reference.MODID, channelh, new RPPacketHandler());
	    return channelh;
	}
	/**
	 * Send this message to everyone.
	 * <p/>
	 * Adapted from CPW's code in
	 * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
	 * 
	 * @param message
	 *            The message to send
	 */
	public void sendToAll(IRPPacket message)
	{
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		this.channels.get(Side.SERVER).writeOutbound(message);
	}

	/**
	 * Send this message to the specified player.
	 * <p/>
	 * Adapted from CPW's code in
	 * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
	 * 
	 * @param message
	 *            The message to send
	 * @param player
	 *            The player to send it to
	 */
	public void sendTo(IRPPacket message, EntityPlayerMP player)
	{
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		this.channels.get(Side.SERVER).writeOutbound(message);
	}
	/**
	 * Send this message to everyone within a certain range of a point.
	 * <p/>
	 * Adapted from CPW's code in
	 * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
	 * 
	 * @param message
	 *            The message to send
	 * @param point
	 *            The
	 *            {@link cpw.mods.fml.common.network.NetworkRegistry.TargetPoint}
	 *            around which to send
	 */
	public void sendToAllAround(IRPPacket message, NetworkRegistry.TargetPoint point)
	{
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
		this.channels.get(Side.SERVER).writeOutbound(message);
	}

	/**
	 * Send this message to everyone within the supplied dimension.
	 * <p/>
	 * Adapted from CPW's code in
	 * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
	 * 
	 * @param message
	 *            The message to send
	 * @param dimensionId
	 *            The dimension id to target
	 */
	public void sendToDimension(IRPPacket message, int dimensionId)
	{
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
		this.channels.get(Side.SERVER).writeOutbound(message);
	}

	/**
	 * Send this message to the server.
	 * <p/>
	 * Adapted from CPW's code in
	 * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
	 * 
	 * @param message
	 *            The message to send
	 */
	public void sendToServer(IRPPacket message)
	{
		this.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		this.channels.get(Side.CLIENT).writeOutbound(message);
	}
}
