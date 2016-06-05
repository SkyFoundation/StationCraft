package io.github.cvronmin.railwayp.network;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Level;

import io.github.cvronmin.railwayp.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CUpdateBannerByGui implements IMessage {
	private EnumTEType targetTileEntity;
	private BlockPos pos;
	private Map<String, String> data;

	public CUpdateBannerByGui(EnumTEType target, BlockPos pos, Map<String, String> data) {
		targetTileEntity = target;
		this.pos = pos;
		this.data = data;
	}

	public CUpdateBannerByGui(EnumTEType target, int x, int y, int z, Map<String, String> data) {
		this(target, new BlockPos(x, y, z), data);
	}

	public CUpdateBannerByGui(EnumTEType target, double x, double y, double z, Map<String, String> data) {
		this(target, new BlockPos(x, y, z), data);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			targetTileEntity = EnumTEType.fromBytes(buf);
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			Map<String, String> tmp = new HashMap<String, String>();
			String tmpkey = "", tmpValue = "";
			boolean isValueNow = false;
			for (byte b : buf.readBytes(buf.readableBytes()).array()) {
				if(b == ':' & !isValueNow){
					isValueNow = true;
					continue;
					}
				if(b == ';' & isValueNow){
					tmp.put(tmpkey, tmpValue);
					tmpkey = tmpValue = "";
					isValueNow = false;
					continue;
					}
				if (!isValueNow) {
					tmpkey += String.valueOf((char)b);
				}else {
					tmpValue += String.valueOf((char)b);
				}
			}
		} catch (IndexOutOfBoundsException ioe) {
			FMLLog.log(Reference.NAME, Level.ERROR, ioe, "Exception while reading CUpdateBannerByGui: ");
			return;
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		try {
			targetTileEntity.toBytes(buf);
			buf.writeInt(pos.getX());
			buf.writeInt(pos.getY());
			buf.writeInt(pos.getZ());
			for (Entry<String, String> entry : data.entrySet()) {
				buf.writeBytes(entry.getKey().getBytes());
				buf.writeByte(':');
				buf.writeBytes(entry.getValue().getBytes());
				buf.writeByte(';');
			}
		} catch (Exception e) {
			FMLLog.log(Reference.NAME, Level.ERROR, e, "Exception while writing CUpdateBannerByGui: ");
			return;
		}
	}

	public EnumTEType getTargetTileEntity() {
		return targetTileEntity;
	}

	public BlockPos getPos() {
		return pos;
	}

	public Map<String, String> getData() {
		return data;
	}

	public enum EnumTEType {
		PLATFORMBANNER((byte) 1), ROUTESIGNAGE((byte) 2), WHPF((byte) 3), NAMEBANNER((byte) 4);
		private final byte id;

		private EnumTEType(byte id) {
			this.id = id;
		}

		public static EnumTEType fromBytes(ByteBuf buf) {
			byte ID = buf.readByte();
			for (EnumTEType t : EnumTEType.values()) {
				if (ID == t.id)
					return t;
			}
			return null;
		}

		public void toBytes(ByteBuf buf) {
			buf.writeByte(id);
		}
	}
}
