package io.github.cvronmin.railwayp.network;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.jcraft.jogg.Packet;

import io.github.cvronmin.railwayp.RailwayP;
import io.github.cvronmin.railwayp.Reference;
import io.github.cvronmin.railwayp.capability.IHexColor;
import io.github.cvronmin.railwayp.client.ClientProxy;
import io.github.cvronmin.railwayp.init.RPCapabilities;
import io.github.cvronmin.railwayp.tileentity.TileEntityColorful;
import io.github.cvronmin.railwayp.tileentity.TileEntityNameBanner;
import io.github.cvronmin.railwayp.tileentity.TileEntityPlatformBanner;
import io.github.cvronmin.railwayp.tileentity.TileEntityRouteSignage;
import io.github.cvronmin.railwayp.tileentity.TileEntityWHPF;
import io.github.cvronmin.railwayp.tileentity.TileEntityRouteSignage.Station;
import io.github.cvronmin.railwayp.util.NTUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import scala.annotation.bridge;

public class RPPacket extends Packet implements IRPPacket {

	public static enum EnumRPPacket {
		C_UPDATETHISSTATION(Side.CLIENT, String.class),
		C_UPDATENEXTSTATION(Side.CLIENT, String.class),
		C_UPDATEINTERCHANGE(Side.CLIENT, String.class),
		/**Packet to update platform banner
		 * <br>Required data (in order):
		 * <br>TileEntity posX
		 * <br>TileEntity posY
		 * <br>TileEntity posZ
		 * <br>Platform Number
		 * <br>Direction
		 * <br>Color
		 * <br>Text1
		 * <br>Text2
		 * **/
		C_UPDATEPLATFORMBANNER(Side.CLIENT, Integer.class, Integer.class, Integer.class,String.class, String.class,String.class,String.class,String.class),
		/**Packet to update platform banner
		 * <br>Required data (in order):
		 * <br>TileEntity posX
		 * <br>TileEntity posY
		 * <br>TileEntity posZ
		 * <br>Platform Number
		 * <br>Direction
		 * <br>Color
		 * <br>Text1
		 * <br>Text2
		 * **/
		C_UPDATENAME_BANNER(Side.CLIENT, Integer.class, Integer.class, Integer.class,String.class, String.class,String.class,String.class,String.class),
		/**Packet to update platform banner
		 * <br>Required data (in order):
		 * <br>TileEntity posX
		 * <br>TileEntity posY
		 * <br>TileEntity posZ
		 * <br>Platform Number
		 * <br>Direction
		 * <br>Rotation
		 * <br>Color
		 * <br>Text1
		 * <br>Text2
		 * **/
		C_UPDATE_WHPF(Side.CLIENT, Integer.class, Integer.class, Integer.class,String.class,String.class, String.class,String.class,String.class,String.class),
		C_UPDATE_ROUTE_SIGN(Side.CLIENT,Integer.class,Integer.class,Integer.class,String.class,String.class, NBTTagCompound.class),
		C_UPDATE_COLORFUL(Side.CLIENT,Integer.class,Integer.class,Integer.class,String.class);
		private Side targetSide;
		private Class<?>[] decodeAs;

		private EnumRPPacket(Side targetSide, Class<?>... decodeAs) {
			this.targetSide = targetSide;
			this.decodeAs = decodeAs;
		}

		public Side getTargetSide() {
			return this.targetSide;
		}

		public Class<?>[] getDecodeClasses() {
			return this.decodeAs;
		}
	}

	private EnumRPPacket type;
	private List<Object> data;

	// private static String spamCheckString;
	public RPPacket() {
	}

	public RPPacket(EnumRPPacket packetType, Object[] data) {
		this(packetType, Arrays.asList(data));
	}

	public RPPacket(EnumRPPacket packetType, List<Object> data) {
		if (packetType.getDecodeClasses().length != data.size()) {
			new RuntimeException().printStackTrace();
		}
		this.type = packetType;
		this.data = data;
	}

	@Override
	public void encode(ChannelHandlerContext context, ByteBuf buffer) {
		buffer.writeInt(this.type.ordinal());

		try {
			NTUtil.encodeData(buffer, this.data);
		} catch (Exception e) {
			FMLLog.log(Reference.NAME, Level.ERROR, e, "Error handling simple packet type: " + this.type.toString());
		}
	}

	@Override
	public void decode(ChannelHandlerContext context, ByteBuf buffer) {
		this.type = EnumRPPacket.values()[buffer.readInt()];
		try {
			if (this.type.getDecodeClasses().length > 0) {
				this.data = NTUtil.decodeData(this.type.getDecodeClasses(), buffer);
			}
		} catch (Exception e) {
			System.err.println("["+ Reference.NAME + "] Error handling simple packet type: " + this.type.toString() + " "
					+ buffer.toString());
			e.printStackTrace();
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		try {
			switch (this.type) {
			case C_UPDATETHISSTATION:
				ClientProxy.statio.setThis(String.valueOf(this.data.get(0)));
				break;
			case C_UPDATENEXTSTATION:
				ClientProxy.statio.setNext(String.valueOf(this.data.get(0)));
				break;
			case C_UPDATEINTERCHANGE:
				ClientProxy.statio.setInterchange(String.valueOf(this.data.get(0)));
				break;
			case C_UPDATEPLATFORMBANNER:
				TileEntityPlatformBanner te = null;
                BlockPos blockpos1 = new BlockPos(Integer.valueOf(String.valueOf(data.get(0))), Integer.valueOf(String.valueOf(data.get(1))), Integer.valueOf(String.valueOf(data.get(2))));
        		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        		World world = server.worlds[0];
                TileEntity tileentity2 = world.getTileEntity(blockpos1);
                IBlockState state = world.getBlockState(blockpos1);
                if(tileentity2 instanceof TileEntityPlatformBanner) te = (TileEntityPlatformBanner)tileentity2;
                if(te == null) break;
                try {
                	String pf = String.valueOf(data.get(3));
                	if(pf.isEmpty())pf = "1";
                	String dir = String.valueOf(data.get(4));
                	if(dir.isEmpty())dir = "0";
                    te.setData(Integer.valueOf(pf), Byte.valueOf(dir), String.valueOf(data.get(5)), String.valueOf(data.get(6)), String.valueOf(data.get(7)));
                    te.markDirty();
                    world.notifyBlockUpdate(blockpos1, state, state, 3);
                } catch (Exception e) {
                	FMLLog.log(Reference.MODID, Level.ERROR, e, "");
				}
				break;
			case C_UPDATENAME_BANNER:
				TileEntityNameBanner te1 = null;
                BlockPos blockpos11 = new BlockPos(Integer.valueOf(String.valueOf(data.get(0))), Integer.valueOf(String.valueOf(data.get(1))), Integer.valueOf(String.valueOf(data.get(2))));
        		MinecraftServer server1 = FMLCommonHandler.instance().getMinecraftServerInstance();
        		World world1 = server1.worlds[0];
                TileEntity tileentity21 = world1.getTileEntity(blockpos11);
                IBlockState state1 = world1.getBlockState(blockpos11);
                if(tileentity21 instanceof TileEntityNameBanner) te1 = (TileEntityNameBanner)tileentity21;
                if(te1 == null) break;
                try {
                	String signtype = String.valueOf(data.get(3));
                	if(signtype.isEmpty())signtype = "0";
                    te1.setData(Integer.valueOf(signtype), String.valueOf(data.get(4)), String.valueOf(data.get(5)), String.valueOf(data.get(6)), String.valueOf(data.get(7)));
                    te1.markDirty();
                    world1.notifyBlockUpdate(blockpos11, state1, state1, 3);
                } catch (Exception e) {
                	FMLLog.log(Reference.MODID, Level.ERROR, e, "");
				}
				break;
			case C_UPDATE_WHPF:
				TileEntityWHPF te11 = null;
                BlockPos blockpos111 = new BlockPos(Integer.valueOf(String.valueOf(data.get(0))), Integer.valueOf(String.valueOf(data.get(1))), Integer.valueOf(String.valueOf(data.get(2))));
        		MinecraftServer server11 = FMLCommonHandler.instance().getMinecraftServerInstance();
        		World world11 = server11.worlds[0];
                TileEntity tileentity211 = world11.getTileEntity(blockpos111);
                IBlockState state11 = world11.getBlockState(blockpos111);
                if(tileentity211 instanceof TileEntityWHPF) te11 = (TileEntityWHPF)tileentity211;
                if(te11 == null) break;
                try {
                	String pf1 = String.valueOf(data.get(3));
                	if(pf1.isEmpty())pf1 = "0";
                	String dir = String.valueOf(data.get(4));
                	if(dir.isEmpty())dir = "0";
                	String r = String.valueOf(data.get(5));
                	if(r.isEmpty())r = "0";
                    te11.setData(Integer.valueOf(pf1), Byte.parseByte(dir), Short.parseShort(r), String.valueOf(data.get(6)), String.valueOf(data.get(7)), String.valueOf(data.get(8)));
                    te11.markDirty();
                    world11.notifyBlockUpdate(blockpos111, state11, state11, 3);
                } catch (Exception e) {
                	FMLLog.log(Reference.MODID, Level.ERROR, e, "");
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleServerSide(EntityPlayer player) {
		try {
			switch (this.type) {
			case C_UPDATETHISSTATION:
				ClientProxy.statio.setThis(String.valueOf(this.data.get(0)));
				break;
			case C_UPDATENEXTSTATION:
				ClientProxy.statio.setNext(String.valueOf(this.data.get(0)));
				break;
			case C_UPDATEINTERCHANGE:
				ClientProxy.statio.setInterchange(String.valueOf(this.data.get(0)));
				break;
			case C_UPDATEPLATFORMBANNER:
				TileEntityPlatformBanner te = null;
                BlockPos blockpos1 = new BlockPos(Integer.valueOf(String.valueOf(data.get(0))), Integer.valueOf(String.valueOf(data.get(1))), Integer.valueOf(String.valueOf(data.get(2))));
        		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        		World world = server.worlds[0];
                TileEntity tileentity2 = world.getTileEntity(blockpos1);
                IBlockState state = world.getBlockState(blockpos1);
                if(tileentity2 instanceof TileEntityPlatformBanner) te = (TileEntityPlatformBanner)tileentity2;
                if(te == null) break;
                try {
                	String pf = String.valueOf(data.get(3));
                	if(pf.isEmpty())pf = "1";
                	String dir = String.valueOf(data.get(4));
                	if(dir.isEmpty())dir = "0";
                    te.setData(Integer.valueOf(pf), Byte.valueOf(dir), String.valueOf(data.get(5)), String.valueOf(data.get(6)), String.valueOf(data.get(7)));
                    te.markDirty();
                    world.notifyBlockUpdate(blockpos1, state, state, 3);
                } catch (Exception e) {
                	FMLLog.log(Reference.MODID, Level.ERROR, e, "");
				}
				break;
			case C_UPDATENAME_BANNER:
				TileEntityNameBanner te1 = null;
                BlockPos blockpos11 = new BlockPos(Integer.valueOf(String.valueOf(data.get(0))), Integer.valueOf(String.valueOf(data.get(1))), Integer.valueOf(String.valueOf(data.get(2))));
        		MinecraftServer server1 = FMLCommonHandler.instance().getMinecraftServerInstance();
        		World world1 = server1.worlds[0];
                TileEntity tileentity21 = world1.getTileEntity(blockpos11);
                IBlockState state1 = world1.getBlockState(blockpos11);
                if(tileentity21 instanceof TileEntityNameBanner) te1 = (TileEntityNameBanner)tileentity21;
                if(te1 == null) break;
                try {
                	String signtype = String.valueOf(data.get(3));
                	if(signtype.isEmpty())signtype = "0";
                    te1.setData(Integer.valueOf(signtype), String.valueOf(data.get(4)), String.valueOf(data.get(5)), String.valueOf(data.get(6)), String.valueOf(data.get(7)));
                    te1.markDirty();
                    world1.notifyBlockUpdate(blockpos11, state1, state1, 3);
                } catch (Exception e) {
                	FMLLog.log(Reference.MODID, Level.ERROR, e, "");
				}
				break;
			case C_UPDATE_WHPF:
				TileEntityWHPF te11 = null;
                BlockPos blockpos111 = new BlockPos(Integer.valueOf(String.valueOf(data.get(0))), Integer.valueOf(String.valueOf(data.get(1))), Integer.valueOf(String.valueOf(data.get(2))));
        		MinecraftServer server11 = FMLCommonHandler.instance().getMinecraftServerInstance();
        		World world11 = server11.worlds[0];
                TileEntity tileentity211 = world11.getTileEntity(blockpos111);
                IBlockState state11 = world11.getBlockState(blockpos111);
                if(tileentity211 instanceof TileEntityWHPF) te11 = (TileEntityWHPF)tileentity211;
                if(te11 == null) break;
                try {
                	String pf1 = String.valueOf(data.get(3));
                	if(pf1.isEmpty())pf1 = "0";
                	String dir = String.valueOf(data.get(4));
                	if(dir.isEmpty())dir = "0";
                	String r = String.valueOf(data.get(5));
                	if(r.isEmpty())r = "0";
                    te11.setData(Integer.valueOf(pf1), Byte.parseByte(dir), Short.parseShort(r), String.valueOf(data.get(6)), String.valueOf(data.get(7)), String.valueOf(data.get(8)));
                    te11.markDirty();
                    world11.notifyBlockUpdate(blockpos111, state11, state11, 3);
                } catch (Exception e) {
                	FMLLog.log(Reference.MODID, Level.ERROR, e, "");
				}
				break;
			case C_UPDATE_ROUTE_SIGN:
				TileEntityRouteSignage ters = null;
                BlockPos bp = new BlockPos(Integer.valueOf(String.valueOf(data.get(0))), Integer.valueOf(String.valueOf(data.get(1))), Integer.valueOf(String.valueOf(data.get(2))));
        		MinecraftServer server2 = FMLCommonHandler.instance().getMinecraftServerInstance();
        		World world2 = server2.worlds[0];
                TileEntity tileEntity = world2.getTileEntity(bp);
                IBlockState state2 = world2.getBlockState(bp);
                if(tileEntity instanceof TileEntityRouteSignage) ters = (TileEntityRouteSignage)tileEntity;
                if(ters == null) break;
                try {
                    ters.setData(Byte.parseByte((String) data.get(3)), (String) data.get(4), Station.readStationsFromTagList(((NBTTagCompound) data.get(5)).getTagList("Stations", 10)));
                    ters.markDirty();
                    world2.notifyBlockUpdate(bp, state2, state2, 3);
                } catch (Exception e) {
                	FMLLog.log(Reference.MODID, Level.ERROR, e, "");
				}
				break;
			case C_UPDATE_COLORFUL:
				TileEntityColorful tec = null;
                BlockPos bpc = new BlockPos(Integer.valueOf(String.valueOf(data.get(0))), Integer.valueOf(String.valueOf(data.get(1))), Integer.valueOf(String.valueOf(data.get(2))));
        		MinecraftServer serverc = FMLCommonHandler.instance().getMinecraftServerInstance();
        		World worldc = serverc.worlds[0];
                TileEntity tet = worldc.getTileEntity(bpc);
                IBlockState statec = worldc.getBlockState(bpc);
                if(tet instanceof TileEntityColorful) tec = (TileEntityColorful)tet;
                if(tec == null) break;
                try {
                	/*if(tec.hasCapability(RPCapabilities.hexColor, null)){
                		IStorage<IHexColor> storage = RPCapabilities.hexColor.getStorage();
                		IHexColor color = tec.getCapability(RPCapabilities.hexColor, null);
                		storage.readNBT(RPCapabilities.hexColor, color, null, new NBTTagString(String.valueOf(data.get(3))));
                	}*/
                	tec.setColorEncoded(String.valueOf(data.get(3)));
                	tec.decodeColor();
                    tec.markDirty();
                    worldc.notifyBlockUpdate(bpc, statec, statec, 3);
                    FMLClientHandler.instance().getClient().renderGlobal.markBlockRangeForRenderUpdate(bpc.getX(), bpc.getY(), bpc.getZ(), bpc.getX(), bpc.getY(), bpc.getZ());
                } catch (Exception e) {
                	FMLLog.log(Reference.MODID, Level.ERROR, e, "");
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
