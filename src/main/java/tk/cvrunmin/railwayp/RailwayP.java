package tk.cvrunmin.railwayp;

import org.apache.logging.log4j.Level;

import tk.cvrunmin.railwayp.network.RPChannelHandler;
import tk.cvrunmin.railwayp.tileentity.TileEntityPlatformBanner;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.SidedProxy;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class RailwayP {
	@SidedProxy(clientSide = Reference.CPROXY,serverSide = Reference.SPROXY)
	public static CommonProxy proxy;
	@Instance(Reference.MODID)
	public static RailwayP instance;
	public static RPChannelHandler channelHandle;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	proxy.preInit(event);
    }
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	proxy.init(event);
    	channelHandle = RPChannelHandler.init();
    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
}
