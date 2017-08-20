package io.github.cvronmin.railwayp;

import io.github.cvronmin.railwayp.init.RPBlocks;
import io.github.cvronmin.railwayp.init.RPCapabilities;
import io.github.cvronmin.railwayp.init.RPCraftingManager;
import io.github.cvronmin.railwayp.init.RPItems;
import io.github.cvronmin.railwayp.network.CUpdateBannerByGui;
import io.github.cvronmin.railwayp.network.MessagerFromClient;
import io.github.cvronmin.railwayp.tileentity.TileEntityColorful;
import io.github.cvronmin.railwayp.tileentity.TileEntityNameBanner;
import io.github.cvronmin.railwayp.tileentity.TileEntityPFDoor;
import io.github.cvronmin.railwayp.tileentity.TileEntityPlatformBanner;
import io.github.cvronmin.railwayp.tileentity.TileEntityRailNoticer;
import io.github.cvronmin.railwayp.tileentity.TileEntityRouteSignage;
import io.github.cvronmin.railwayp.tileentity.TileEntityWHPF;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {
	public static SimpleNetworkWrapper snw; 
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	RPCapabilities.preInit(event);
    	registerEntity();
    	snw = NetworkRegistry.INSTANCE.newSimpleChannel("RPchannel");
    	snw.registerMessage(MessagerFromClient.CUpdateBannerByGuiMessager.class, CUpdateBannerByGui.class, 80, Side.SERVER);
    }
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	//RPCraftingManager.register();
    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	
    }
    private void registerEntity(){
//    	ClientRegistry.registerTileEntity(TileEntityPlatformBanner.class, "PlatformBanner", new TileEntityPlatformBannerRenderer());
    	GameRegistry.registerTileEntity(TileEntityPlatformBanner.class, "PlatformBanner");
    	GameRegistry.registerTileEntity(TileEntityNameBanner.class, "NameBanner");
    	GameRegistry.registerTileEntity(TileEntityRouteSignage.class, "RouteSignage");
    	GameRegistry.registerTileEntity(TileEntityPFDoor.class, "PlatformDoor");
    	GameRegistry.registerTileEntity(TileEntityWHPF.class, "whpf");
    	GameRegistry.registerTileEntity(TileEntityColorful.class, "Colorful");
    	GameRegistry.registerTileEntity(TileEntityRailNoticer.class, "noticer");
    }

	public EntityPlayer getPlayerFromNetHandler(INetHandler handler)
	{
		if (handler instanceof NetHandlerPlayServer)
		{
			return ((NetHandlerPlayServer) handler).player;
		}
		else
		{
			return null;
		}
	}
}
