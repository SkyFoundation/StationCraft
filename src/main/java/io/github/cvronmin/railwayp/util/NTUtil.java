package io.github.cvronmin.railwayp.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

import org.apache.logging.log4j.Level;

import io.github.cvronmin.railwayp.Reference;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class NTUtil {
	public static void encodeData(ByteBuf buffer, Collection<Object> sendData) throws IOException
	{
		for (Object dataValue : sendData)
		{
			if (dataValue instanceof Integer)
			{
				buffer.writeInt((Integer) dataValue);
			}
			else if (dataValue instanceof Float)
			{
				buffer.writeFloat((Float) dataValue);
			}
			else if (dataValue instanceof Double)
			{
				buffer.writeDouble((Double) dataValue);
			}
			else if (dataValue instanceof Byte)
			{
				buffer.writeByte((Byte) dataValue);
			}
			else if (dataValue instanceof Boolean)
			{
				buffer.writeBoolean((Boolean) dataValue);
			}
			else if (dataValue instanceof String)
			{
				ByteBufUtils.writeUTF8String(buffer, (String) dataValue);
			}
			else if (dataValue instanceof Short)
			{
				buffer.writeShort((Short) dataValue);
			}
			else if (dataValue instanceof Long)
			{
				buffer.writeLong((Long) dataValue);
			}
			else if (dataValue instanceof NBTTagCompound)
			{
				NTUtil.writeNBTTagCompound((NBTTagCompound) dataValue, buffer);
			}
			else if (dataValue instanceof Entity)
			{
				buffer.writeInt(((Entity) dataValue).getEntityId());
			}
			else if (dataValue instanceof BlockPos)
			{
				buffer.writeInt(((BlockPos) dataValue).getX());
				buffer.writeInt(((BlockPos) dataValue).getX());
				buffer.writeInt(((BlockPos) dataValue).getX());
			}
        else if (dataValue instanceof UUID)
        {
            buffer.writeLong(((UUID) dataValue).getLeastSignificantBits());
            buffer.writeLong(((UUID) dataValue).getMostSignificantBits());
        }
			else if (dataValue instanceof Collection)
			{
				NTUtil.encodeData(buffer, (Collection<Object>) dataValue);
			}
			else if (dataValue instanceof Integer[])
			{
				Integer[] array = (Integer[]) dataValue;
				buffer.writeInt(array.length);

				for (int i = 0; i < array.length; i++)
				{
					buffer.writeInt(array[i]);
				}
			}
			else if (dataValue instanceof String[])
			{
				String[] array = (String[]) dataValue;
				buffer.writeInt(array.length);

				for (int i = 0; i < array.length; i++)
				{
					ByteBufUtils.writeUTF8String(buffer, array[i]);
				}
			}
			else
			{
				FMLLog.log(Level.INFO, Reference.NAME, "Could not find data type to encode!: " + dataValue);
			}
		}
	}

	public static ArrayList<Object> decodeData(Class<?>[] types, ByteBuf buffer)
	{
		ArrayList<Object> objList = new ArrayList<Object>();

		for (Class clazz : types)
		{
			if (clazz.equals(Integer.class))
			{
				objList.add(buffer.readInt());
			}
			else if (clazz.equals(Float.class))
			{
				objList.add(buffer.readFloat());
			}
			else if (clazz.equals(Double.class))
			{
				objList.add(buffer.readDouble());
			}
			else if (clazz.equals(Byte.class))
			{
				objList.add(buffer.readByte());
			}
			else if (clazz.equals(Boolean.class))
			{
				objList.add(buffer.readBoolean());
			}
			else if (clazz.equals(String.class))
			{
				objList.add(ByteBufUtils.readUTF8String(buffer));
			}
			else if (clazz.equals(Short.class))
			{
				objList.add(buffer.readShort());
			}
			else if (clazz.equals(Long.class))
			{
				objList.add(buffer.readLong());
			}
			else if (clazz.equals(NBTTagCompound.class))
			{
				try
				{
					objList.add(NTUtil.readNBTTagCompound(buffer));
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			else if (clazz.equals(BlockPos.class))
			{
				objList.add(new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()));
			}
        else if (clazz.equals(UUID.class))
        {
            objList.add(new UUID(buffer.readLong(), buffer.readLong()));
        }
			else if (clazz.equals(Integer[].class))
			{
				int size = buffer.readInt();

				for (int i = 0; i < size; i++)
				{
					objList.add(buffer.readInt());
				}
			}
			else if (clazz.equals(String[].class))
			{
				int size = buffer.readInt();

				for (int i = 0; i < size; i++)
				{
					objList.add(ByteBufUtils.readUTF8String(buffer));
				}
			}
		}

		return objList;
	}

	public static Object getFieldValueFromStream(Field field, ByteBuf buffer, World world) throws IOException
	{
		Class<?> dataValue = field.getType();

		if (dataValue.equals(int.class))
		{
			return buffer.readInt();
		}
		else if (dataValue.equals(float.class))
		{
			return buffer.readFloat();
		}
		else if (dataValue.equals(double.class))
		{
			return buffer.readDouble();
		}
		else if (dataValue.equals(byte.class))
		{
			return buffer.readByte();
		}
		else if (dataValue.equals(boolean.class))
		{
			return buffer.readBoolean();
		}
		else if (dataValue.equals(String.class))
		{
			return ByteBufUtils.readUTF8String(buffer);
		}
		else if (dataValue.equals(short.class))
		{
			return buffer.readShort();
		}
		else if (dataValue.equals(Long.class))
		{
			return buffer.readLong();
		}
		else if (dataValue.equals(NBTTagCompound.class))
		{
			return NTUtil.readNBTTagCompound(buffer);
		}
		else if (dataValue.equals(BlockPos.class))
		{
			return new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
		}
    else if (dataValue.equals(UUID.class))
    {
        return new UUID(buffer.readLong(), buffer.readLong());
    }
		else
		{
			Class<?> c = dataValue;

			while (c != null)
			{
				if (c.equals(Entity.class))
				{
					return world.getEntityByID(buffer.readInt());
				}

				c = c.getSuperclass();
			}
		}

		throw new NullPointerException("Field type not found: " + field.getType().getSimpleName());
	}

	public static ItemStack readItemStack(ByteBuf buffer) throws IOException
	{
		ItemStack itemstack = null;
		short itemID = buffer.readShort();

		if (itemID >= 0)
		{
			byte stackSize = buffer.readByte();
			short meta = buffer.readShort();
			itemstack = new ItemStack(Item.getItemById(itemID), stackSize, meta);
			itemstack.setTagCompound(NTUtil.readNBTTagCompound(buffer));
		}

		return itemstack;
	}

	public static void writeItemStack(ItemStack itemStack, ByteBuf buffer) throws IOException
	{
		if (itemStack == null)
		{
			buffer.writeShort(-1);
		}
		else
		{
			buffer.writeShort(Item.getIdFromItem(itemStack.getItem()));
			buffer.writeByte(itemStack.getCount());
			buffer.writeShort(itemStack.getItemDamage());
			NBTTagCompound nbttagcompound = null;

			if (itemStack.getItem().isDamageable() || itemStack.getItem().getShareTag())
			{
				nbttagcompound = itemStack.getTagCompound();
			}

			NTUtil.writeNBTTagCompound(nbttagcompound, buffer);
		}
	}

	public static NBTTagCompound readNBTTagCompound(ByteBuf buffer) throws IOException
	{
		/*short dataLength = buffer.readShort();

		if (dataLength < 0)
		{
			return null;
		}
		else
		{
			byte[] compressedNBT = new byte[dataLength];
			buffer.readBytes(compressedNBT);
			return CompressedStreamTools.readCompressed(new ByteBufInputStream(buffer));*/
			return ByteBufUtils.readTag(buffer);
		//}
	}

	public static void writeNBTTagCompound(NBTTagCompound nbt, ByteBuf buffer) throws IOException
	{
		/*if (nbt == null)
		{
			buffer.writeShort(-1);
		}
		else
		{
			byte[] compressedNBT = compress(nbt);
			buffer.writeShort((short) compressedNBT.length);
			buffer.writeBytes(compressedNBT);
		}*/
		ByteBufUtils.writeTag(buffer, nbt);
	}
    public static byte[] compress(NBTTagCompound compound) throws IOException
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        DataOutputStream dataoutputstream = new DataOutputStream(new GZIPOutputStream(bytearrayoutputstream));

        try
        {
            CompressedStreamTools.write(compound, dataoutputstream);
        }
        finally
        {
            dataoutputstream.close();
        }

        return bytearrayoutputstream.toByteArray();
    }
}
