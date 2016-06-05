package io.github.cvronmin.railwayp.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SUpdateRailNoticerGui implements IMessage {
	private String thisStation, nextStation, interchange;
	public SUpdateRailNoticerGui(String t, String n, String i) {
		thisStation = t;nextStation = n;interchange = i;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		String string = new String(buf.array());
		String[] sa = string.split(";");
		thisStation = sa[0];
		nextStation = sa[1];
		interchange = sa[2];
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBytes(thisStation.getBytes());
		buf.writeByte(';');
		buf.writeBytes(nextStation.getBytes());
		buf.writeByte(';');
		buf.writeBytes(interchange.getBytes());
		buf.writeByte(';');
	}

	public String getThisStation() {
		return thisStation;
	}

	public String getNextStation() {
		return nextStation;
	}

	public String getInterchange() {
		return interchange;
	}

}
