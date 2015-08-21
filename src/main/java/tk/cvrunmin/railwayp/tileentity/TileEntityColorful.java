package tk.cvrunmin.railwayp.tileentity;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityColorful extends TileEntity{
    private int color;
    private String colorEncoded;
	private boolean useCustomColor;
	private int existColor;
//    private String pattern;
    private void decodeColor(){
    	if(colorEncoded.length() <= 6)
    	if(!colorEncoded.startsWith("0x")){
    		try{
    			color = Integer.decode("0x" + colorEncoded);
    		}
    		catch(Exception e){
    			color = 0;
    		}
    	}
    	else{
    		try{
    			color = Integer.decode(colorEncoded);
    		}
    		catch(Exception e){
    			color = 0;
    		}
    	}
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        compound.setBoolean("UseCustomColor", useCustomColor);
        if(color >= 0x0 && color < 0x1000000 && useCustomColor){
        	compound.setInteger("Color", this.color);
        	compound.setString("EColor", Integer.toHexString(this.color));
        }
        else if ((existColor >= 0 && existColor < 15) && !useCustomColor) {
			compound.setInteger("ExistColor", existColor);
			compound.setString("EColor", Integer.toHexString(EnumDyeColor.byDyeDamage(existColor).getMapColor().colorValue));
		}
/*        if(pattern != null && pattern != ""){
        compound.setString("Pattern", this.pattern);
        }*/
    }
	@Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        useCustomColor = compound.getBoolean("UseCustomColor");
        if(useCustomColor){
        	colorEncoded = compound.getString("EColor");
        	decodeColor();
        }
        else {
			existColor = compound.getInteger("ExistColor");
		}
/*        if(compound.hasKey("Pattern")){
    	pattern = compound.getString("Pattern");
        }*/
    }
	public int getColor(){
		return color;
	}
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.pos, 6, nbttagcompound);
    }
}
