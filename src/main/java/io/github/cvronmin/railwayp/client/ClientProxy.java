package io.github.cvronmin.railwayp.client;

import io.github.cvronmin.railwayp.CommonProxy;
import io.github.cvronmin.railwayp.client.gui.GuiPen;
import io.github.cvronmin.railwayp.client.renderer.TileEntityNameBannerRenderer;
import io.github.cvronmin.railwayp.client.renderer.TileEntityPFDoorRenderer;
import io.github.cvronmin.railwayp.client.renderer.TileEntityPlatformBannerRenderer;
import io.github.cvronmin.railwayp.client.renderer.TileEntityRouteSignageRenderer;
import io.github.cvronmin.railwayp.client.renderer.TileEntityWHPFRenderer;
import io.github.cvronmin.railwayp.init.RPBlocks;
import io.github.cvronmin.railwayp.init.RPItems;
import io.github.cvronmin.railwayp.tileentity.TileEntityNameBanner;
import io.github.cvronmin.railwayp.tileentity.TileEntityPFDoor;
import io.github.cvronmin.railwayp.tileentity.TileEntityPlatformBanner;
import io.github.cvronmin.railwayp.tileentity.TileEntityRouteSignage;
import io.github.cvronmin.railwayp.tileentity.TileEntityWHPF;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy{
    public static GuiPen statio;
	@Override
	public void init(FMLInitializationEvent event) {
//		blockRend(RPBlocks.platform_banner, "wall_platform_banner");
		blockRend(RPBlocks.platform_door_base, "platform_door");
		blockRend(RPBlocks.platform_door_head, "platform_door_head");
		blockRend(RPBlocks.platform_door_extension, "platform_door_extension");
		blockRend(RPBlocks.platform_glass, "platform_glass");
		blockRend(RPBlocks.plate, "plate");
		blockRend(RPBlocks.mosaic_tile, "mosaic_tile");
		blockRend(RPBlocks.wall_platform_banner, "wall_platform_banner");
		blockRend(RPBlocks.wall_name_banner, "wall_name_banner");
		blockRend(RPBlocks.wall_route_sign, "wall_route_sign");
		blockRend(RPBlocks.roof_where_pf, "roof_whpf");
		blockRend(RPBlocks.wall_where_pf, "wall_whpf");
		itemRend(RPItems.platform_banner, "platform_banner");
		itemRend(RPItems.name_banner, "name_banner");
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPlatformBanner.class, new TileEntityPlatformBannerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNameBanner.class, new TileEntityNameBannerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRouteSignage.class, new TileEntityRouteSignageRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPFDoor.class, new TileEntityPFDoorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWHPF.class, new TileEntityWHPFRenderer());
		statio = new GuiPen(FMLClientHandler.instance().getClient());
		MinecraftForge.EVENT_BUS.register(statio);
	}
}
