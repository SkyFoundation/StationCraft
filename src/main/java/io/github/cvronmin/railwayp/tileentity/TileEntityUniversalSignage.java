package io.github.cvronmin.railwayp.tileentity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;

import io.github.cvronmin.railwayp.json.CSSDReader;
import io.github.cvronmin.railwayp.json.CustomStationSignageDefinition;
import io.github.cvronmin.railwayp.util.ClassUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityUniversalSignage extends TileEntity {
	private Map<String, Object> parameters;
	private String definition;
	public TileEntityUniversalSignage() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("definition", definition);
		NBTTagList list = new NBTTagList();
		for (Entry<String, Object> entry : parameters.entrySet()) {
			NBTTagCompound c = new NBTTagCompound();
			c.setString("name", entry.getKey());
			c.setString("type", entry.getValue().getClass().getName());
			c.setString("value", entry.getValue().toString());
		}
		compound.setTag("par", list);
		return compound;
	}
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		definition = compound.getString(definition);
		NBTTagList list = compound.getTagList("par", 10);
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound c = list.getCompoundTagAt(i);
			String name = c.getString("name");
			String type = c.getString("type");
			Class<?> clazz;
			try {
				clazz = Class.forName(type);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				continue;
			}
			String value = c.getString("value");
			Constructor<?> constructor = null;
			try {
				if(clazz.isAssignableFrom(char.class) | clazz.isAssignableFrom(Character.class)){
					constructor = clazz.getConstructor(char.class);
				}else {
					constructor = clazz.getConstructor(String.class);
				}
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				continue;
			}
			Object obj = null;
			try {
				if(clazz.isAssignableFrom(char.class) | clazz.isAssignableFrom(Character.class)){
					obj = constructor.newInstance(value.charAt(0));
				}
				else{
				obj = constructor.newInstance(value);
				}
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
				continue;
			}
			parameters.put(name, obj);
		}
	}
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 6, getUpdateTag());
	}
	public Map<String, Object> getParameters() {
		return parameters;
	}
	public String getDefinitionName() {
		return definition;
	}
	public CustomStationSignageDefinition getDefinition() {
		return CSSDReader.INSTANCE.getDefinition(definition);
	}
	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
}
