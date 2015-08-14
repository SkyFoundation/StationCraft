package tk.cvrunmin.railwayp.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRailNoticer extends TileEntity {
    public String thisStat;
    public String nextStat;
    public String interchange;
	@Override
	    public void writeToNBT(NBTTagCompound compound)
	    {
	        super.writeToNBT(compound);
	        if(thisStat != "" && thisStat != null){
	            compound.setString("ThisStation", thisStat);
	        }
	        if(nextStat != "" && nextStat != null){
	            compound.setString("NextStation", nextStat);
	        }
	        if(interchange != "" && interchange != null){
	            compound.setString("InterChange", interchange);
	        }
	    }
		@Override
	    public void readFromNBT(NBTTagCompound compound)
	    {
	        super.readFromNBT(compound);
	        thisStat = compound.getString("ThisStation");
	        nextStat = compound.getString("NextStation");
	        interchange = compound.getString("Interchange");
	    }
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.pos, 6, nbttagcompound);
    }
}
