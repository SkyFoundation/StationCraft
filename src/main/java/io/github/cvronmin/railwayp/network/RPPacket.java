package io.github.cvronmin.railwayp.network;

import io.github.cvronmin.railwayp.client.ClientProxy;
import io.github.cvronmin.railwayp.client.gui.GuiPen;
import io.github.cvronmin.railwayp.util.NTUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.jcraft.jogg.Packet;

public class RPPacket extends Packet implements IRPPacket{

    public static enum EnumRPPacket {
	C_UPDATETHISSTATION(Side.CLIENT, String.class),
	C_UPDATENEXTSTATION(Side.CLIENT, String.class),
	C_UPDATEINTERCHANGE(Side.CLIENT, String.class);
	private Side targetSide;
	private Class<?>[] decodeAs;

	private EnumRPPacket(Side targetSide, Class<?>... decodeAs)
	{
		this.targetSide = targetSide;
		this.decodeAs = decodeAs;
	}

	public Side getTargetSide()
	{
		return this.targetSide;
	}

	public Class<?>[] getDecodeClasses()
	{
		return this.decodeAs;
	}
    }
	private EnumRPPacket type;
	private List<Object> data;
//	private static String spamCheckString;
    public RPPacket() {}
    public RPPacket(EnumRPPacket packetType, Object[] data)
	{
	this(packetType, Arrays.asList(data));
	}

    public RPPacket(EnumRPPacket packetType, List<Object> data)
    {
	if (packetType.getDecodeClasses().length != data.size())
	{
		new RuntimeException().printStackTrace();
	}
	this.type = packetType;
	this.data = data;
    }

    @Override
    public void encode(ChannelHandlerContext context, ByteBuf buffer) {
	buffer.writeInt(this.type.ordinal());

	try
	{
		NTUtil.encodeData(buffer, this.data);
	}
	catch (IOException e)
	{
		e.printStackTrace();
	}
    }

    @Override
    public void decode(ChannelHandlerContext context, ByteBuf buffer) {
	this.type = EnumRPPacket.values()[buffer.readInt()];

	try
	{
		if (this.type.getDecodeClasses().length > 0)
		{
			this.data = NTUtil.decodeData(this.type.getDecodeClasses(), buffer);
		}
	}
	catch (Exception e)
	{
		System.err.println("[Railway Plus] Error handling simple packet type: " + this.type.toString() + " " + buffer.toString());
		e.printStackTrace();
	}
    }

    @Override
    public void handleClientSide(EntityPlayer player) {
	try{
	switch (this.type) {
	case C_UPDATETHISSTATION:
	    ClientProxy.statio.setThis(String.valueOf(this.data.get(0)));
	    break;
	case C_UPDATENEXTSTATION:
	    ClientProxy.statio.setNext(String.valueOf(this.data.get(0)));
	    break;
	case C_UPDATEINTERCHANGE:
	    ClientProxy.statio.setInterchange(String.valueOf(this.data.get(0)));
	    break;
	default:
	    break;
	}
	}
	catch(Exception e){
	    e.printStackTrace();
	}
    }

    @Override
    public void handleServerSide(EntityPlayer player) {
	// TODO Auto-generated method stub
	
    }

}
