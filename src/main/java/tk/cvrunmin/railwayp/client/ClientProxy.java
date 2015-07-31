package tk.cvrunmin.railwayp.client;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import tk.cvrunmin.railwayp.CommonProxy;
import tk.cvrunmin.railwayp.client.renderer.TileEntityNameBannerRenderer;
import tk.cvrunmin.railwayp.client.renderer.TileEntityPFDoorRenderer;
import tk.cvrunmin.railwayp.client.renderer.TileEntityPlatformBannerRenderer;
import tk.cvrunmin.railwayp.client.renderer.TileEntityRouteSignageRenderer;
import tk.cvrunmin.railwayp.init.RPBlocks;
import tk.cvrunmin.railwayp.init.RPItems;
import tk.cvrunmin.railwayp.tileentity.TileEntityNameBanner;
import tk.cvrunmin.railwayp.tileentity.TileEntityPFDoor;
import tk.cvrunmin.railwayp.tileentity.TileEntityPlatformBanner;
import tk.cvrunmin.railwayp.tileentity.TileEntityRouteSignage;

public class ClientProxy extends CommonProxy{
	@Override
	public void init(FMLInitializationEvent event) {
//		blockRend(RPBlocks.platform_banner, "wall_platform_banner");
		blockRend(RPBlocks.platform_door_base, "platform_door");
		blockRend(RPBlocks.platform_door_head, "platform_door_head");
		blockRend(RPBlocks.platform_glass, "platform_glass");
		blockRend(RPBlocks.plate, "plate");
		blockRend(RPBlocks.mosaic_tile, "mosaic_tile");
		itemRend(RPItems.platform_banner, "platform_banner");
		itemRend(RPItems.name_banner, "name_banner");
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPlatformBanner.class, new TileEntityPlatformBannerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNameBanner.class, new TileEntityNameBannerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRouteSignage.class, new TileEntityRouteSignageRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPFDoor.class, new TileEntityPFDoorRenderer());
	}
}
