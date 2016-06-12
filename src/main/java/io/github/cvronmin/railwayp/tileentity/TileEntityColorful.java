package io.github.cvronmin.railwayp.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityBanner;

public class TileEntityColorful extends TileEntityBanner {
	private int color;
	private String colorEncoded;
	private short rotation;

	// private String pattern;
	private void decodeColor() {
		if (colorEncoded.length() <= 6)
			if (!colorEncoded.startsWith("0x")) {
				try {
					color = Integer.decode("0x" + colorEncoded);
				} catch (Exception e) {
					color = 0;
				}
			} else {
				try {
					color = Integer.decode(colorEncoded);
				} catch (Exception e) {
					color = 0;
				}
			}
	}
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("Color", Integer.toHexString(this.color));
		compound.setShort("Rotation", (short) (rotation % 360));
		if(compound.hasKey("Patterns")){
			compound.removeTag("Patterns");
		}
		if(compound.hasKey("Base")){
			compound.removeTag("Base");
		}
		/*
		 * if(pattern != null && pattern != ""){ compound.setString("Pattern",
		 * this.pattern); }
		 */
	}
    @Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		colorEncoded = compound.getString("Color");
		decodeColor();
		rotation = compound.getShort("Rotation");
		/*
		 * if(compound.hasKey("Pattern")){ pattern =
		 * compound.getString("Pattern"); }
		 */
	}

	public int getColor() {
		return color;
	}

	public String getEncodedColor() {
		return colorEncoded;
	}

	public short getRotation() {
		return rotation;
	}

	public Packet<?> getDescriptionPacket() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBT(nbttagcompound);
		return new S35PacketUpdateTileEntity(this.pos, 6, nbttagcompound);
	}
}
