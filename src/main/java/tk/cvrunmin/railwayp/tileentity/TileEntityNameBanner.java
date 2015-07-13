package tk.cvrunmin.railwayp.tileentity;

import java.util.List;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;

public class TileEntityNameBanner extends TileEntityBanner
{
	public final IChatComponent[] signText = new IChatComponent[] {new ChatComponentText(""), new ChatComponentText("")};
    private int color;
    private String colorEncoded;
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
	@Override
    public void setItemValues(ItemStack stack)
    {

        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("BlockEntityTag", 10))
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("BlockEntityTag");
            if(nbttagcompound.hasKey("Color", 8)){
            	colorEncoded = nbttagcompound.getString("Color");
            	decodeColor();
            }
            for (int i = 0; i < 2; ++i)
            {
            	if(nbttagcompound.hasKey("Text" + (i + 1))){
                String s = nbttagcompound.getString("Text" + (i + 1));

                try
                {
                    IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
                    this.signText[i] = ichatcomponent;
                }
                catch (JsonParseException jsonparseexception)
                {
                    this.signText[i] = new ChatComponentText(s);
                }
            	}
            }
        }

    }
	@Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if(color >= 0x0 && color < 0x1000000){
        	compound.setString("Color", Integer.toHexString(this.color));
        }
        for (int i = 0; i < 2; ++i)
        {
            String s = IChatComponent.Serializer.componentToJson(this.signText[i]);
            compound.setString("Text" + (i + 1), s);
        }
    }
	@Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
    	colorEncoded = compound.getString("Color");
    	decodeColor();
        for (int i = 0; i < 2; ++i)
        {
            String s = compound.getString("Text" + (i + 1));

            try
            {
                IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
                this.signText[i] = ichatcomponent;
            }
            catch (JsonParseException jsonparseexception)
            {
                this.signText[i] = new ChatComponentText(s);
            }
        }
    }

	public int getColor(){
		return color != 0 ? color : 0;
	}
    /**
     * Allows for a specialized description packet to be created. This is often used to sync tile entity data from the
     * server to the client easily. For example this is used by signs to synchronise the text to be displayed.
     */
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.pos, 6, nbttagcompound);
    }

}