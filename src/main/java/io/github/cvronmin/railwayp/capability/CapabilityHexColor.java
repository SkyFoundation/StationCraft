package io.github.cvronmin.railwayp.capability;

import org.apache.logging.log4j.Level;

import io.github.cvronmin.railwayp.Reference;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLLog;

public class CapabilityHexColor implements IHexColor{
	public static class Storage implements Capability.IStorage<IHexColor>{

		@Override
		public NBTBase writeNBT(Capability<IHexColor> capability, IHexColor instance, EnumFacing side) {
			
			return new NBTTagString(Integer.toHexString(instance.getColor()));
		}

		@Override
		public void readNBT(Capability<IHexColor> capability, IHexColor instance, EnumFacing side, NBTBase nbt) {
			if(nbt!= null)
				instance.setColor(((NBTTagString)nbt).getString());
		}
	}
	private int color;
	@Override
	public int getColor() {
		// TODO Auto-generated method stub
		return color;
	}

	@Override
	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public void setColor(String colorEncoded) {
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
	
}
