package io.github.cvronmin.railwayp.network;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Level;

import io.github.cvronmin.railwayp.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CUpdateBannerByGui implements IMessage {
	//private EnumTEType targetTileEntity;
	private BlockPos pos;
	private NBTTagCompound data;
	public CUpdateBannerByGui(){}
	public CUpdateBannerByGui(BlockPos pos, NBTTagCompound data) {
		this.pos = pos;
		this.data = data;
	}

	public CUpdateBannerByGui(int x, int y, int z, NBTTagCompound data) {
		this(new BlockPos(x, y, z), data);
	}

	public CUpdateBannerByGui(double x, double y, double z, NBTTagCompound data) {
		this(new BlockPos(x, y, z), data);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			//targetTileEntity = EnumTEType.fromBytes(buf);
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			data = ByteBufUtils.readTag(buf);
		} catch (IndexOutOfBoundsException ioe) {
			FMLLog.log(Reference.NAME, Level.ERROR, ioe, "Exception while reading CUpdateBannerByGui: ");
			return;
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		try {
			//targetTileEntity.toBytes(buf);
			buf.writeInt(pos.getX());
			buf.writeInt(pos.getY());
			buf.writeInt(pos.getZ());
			ByteBufUtils.writeTag(buf, data);
		} catch (Exception e) {
			FMLLog.log(Reference.NAME, Level.ERROR, e, "Exception while writing CUpdateBannerByGui: ");
			return;
		}
	}

	public BlockPos getPos() {
		return pos;
	}

	public NBTTagCompound getData() {
		return data;
	}
}
