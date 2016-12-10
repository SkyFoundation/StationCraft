package io.github.cvronmin.railwayp.tileentity;

import org.apache.logging.log4j.Level;

import io.github.cvronmin.railwayp.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.FMLLog;

public class TileEntityColorful/* extends TileEntityBanner*/extends TileEntity {
	private int color;
	private String colorEncoded;
	public void setColorEncoded(String colorEncoded) {
		this.colorEncoded = colorEncoded;
	}

	//private short rotation;

	// private String pattern;
	public void decodeColor() {
		if(colorEncoded.isEmpty()) return;
		if (colorEncoded.length() <= 6){
			if (!colorEncoded.startsWith("0x")) colorEncoded= "0x"+colorEncoded;
				try {
					color = Integer.decode(colorEncoded);
				} catch (Exception e) {
					FMLLog.log(Reference.NAME, Level.WARN, e, "unable to encode color code: %s . This shouldn\'t be happened", colorEncoded);
					color = 0;
				}
			}
	}
	//@Override
    public void setItemValues(ItemStack stack)
    {

        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("BlockEntityTag", 10))
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("BlockEntityTag");
            if(nbttagcompound.hasKey("Color", 8)){
            	colorEncoded = nbttagcompound.getString("Color");
            	decodeColor();
            }
            /*if(nbttagcompound.hasKey("Rotation", 2)){
        		rotation = nbttagcompound.getShort("Rotation");
            }*/
        }
    }
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("Color", Integer.toHexString(this.color));
		//compound.setShort("Rotation", (short) (rotation % 360));
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
		return compound;
	}
    @Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		colorEncoded = compound.getString("Color");
		decodeColor();
		//rotation = compound.getShort("Rotation");
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

	/*public short getRotation() {
		return rotation;
	}*/
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 6, getUpdateTag());
	}
	@Override
	public NBTTagCompound getUpdateTag() {
		// TODO Auto-generated method stub
		return this.writeToNBT(new NBTTagCompound());
	}
	public Packet<?> getDescriptionPacket() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBT(nbttagcompound);
		return new SPacketUpdateTileEntity(this.pos, 6, nbttagcompound);
	}
}
