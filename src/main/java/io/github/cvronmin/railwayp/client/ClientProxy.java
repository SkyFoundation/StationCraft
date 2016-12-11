package io.github.cvronmin.railwayp.client;

import io.github.cvronmin.railwayp.CommonProxy;
import io.github.cvronmin.railwayp.block.BlockPlatformDoor;
import io.github.cvronmin.railwayp.client.gui.GuiPen;
import io.github.cvronmin.railwayp.client.renderer.TileEntityColorfulRenderer;
import io.github.cvronmin.railwayp.client.renderer.TileEntityNameBannerRenderer;
import io.github.cvronmin.railwayp.client.renderer.TileEntityPFDoorRenderer;
import io.github.cvronmin.railwayp.client.renderer.TileEntityPlatformBannerRenderer;
import io.github.cvronmin.railwayp.client.renderer.TileEntityRouteSignageRenderer;
import io.github.cvronmin.railwayp.client.renderer.TileEntityWHPFRenderer;
import io.github.cvronmin.railwayp.init.RPBlocks;
import io.github.cvronmin.railwayp.init.RPItems;
import io.github.cvronmin.railwayp.network.MessagerFromServer;
import io.github.cvronmin.railwayp.network.SUpdateRailNoticerGui;
import io.github.cvronmin.railwayp.tileentity.TileEntityColorful;
import io.github.cvronmin.railwayp.tileentity.TileEntityNameBanner;
import io.github.cvronmin.railwayp.tileentity.TileEntityPFDoor;
import io.github.cvronmin.railwayp.tileentity.TileEntityPlatformBanner;
import io.github.cvronmin.railwayp.tileentity.TileEntityRouteSignage;
import io.github.cvronmin.railwayp.tileentity.TileEntityWHPF;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy extends CommonProxy{
    public static GuiPen statio;
    @Override
    public void preInit(FMLPreInitializationEvent event) {
    	super.preInit(event);
    	snw.registerMessage(MessagerFromServer.SUpdateRailNoticerGuiMessager.class, SUpdateRailNoticerGui.class, 81, Side.CLIENT);
		ModelLoader.setCustomStateMapper(RPBlocks.platform_door_base, new StateMap.Builder().ignore(BlockPlatformDoor.Base.POWERED).build());
    }
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		blockRend(RPBlocks.platform_door_base, "platform_door");
		//blockRend(RPBlocks.platform_door_head, "platform_door_head");
		//blockRend(RPBlocks.platform_door_extension, "platform_door_extension");
		blockRend(RPBlocks.platform_glass, "platform_glass");
		blockRend(RPBlocks.plate, "plate");
		blockRend(RPBlocks.mosaic_tile, "mosaic_tile");
		blockRend(RPBlocks.noticer, "rail_noticer");
		itemRend(RPItems.platform_banner, "platform_banner");
		itemRend(RPItems.name_banner, "name_banner");
		itemRend(RPItems.route_sign, "route_sign");
		itemRend(RPItems.whpf, "whpf");
		itemRend(RPItems.EDITOR, "editor");
		FMLClientHandler.instance().getClient().getBlockColors().registerBlockColorHandler(new IBlockColor() {
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
                if (worldIn != null && pos != null)
                {
                    TileEntity tileentity = worldIn.getTileEntity(pos);

                    if (tileentity instanceof TileEntityColorful)
                    {
                    	return ((TileEntityColorful)tileentity).getColor();
                    }

                    return -1;
                }
                else
                {
                    return -1;
                }
			}
		}, RPBlocks.plate, RPBlocks.mosaic_tile);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPlatformBanner.class, new TileEntityPlatformBannerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNameBanner.class, new TileEntityNameBannerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRouteSignage.class, new TileEntityRouteSignageRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPFDoor.class, new TileEntityPFDoorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWHPF.class, new TileEntityWHPFRenderer());
		//ClientRegistry.bindTileEntitySpecialRenderer(TileEntityColorful.class, new TileEntityColorfulRenderer());
		statio = new GuiPen(FMLClientHandler.instance().getClient());
		//MinecraftForge.EVENT_BUS.register(statio);
		MinecraftForge.EVENT_BUS.register(this);
	}
	@SubscribeEvent
	public void onModelBake(ModelBakeEvent event){
		event.getModelManager().getBlockModelShapes().registerBuiltInBlocks(RPBlocks.wall_where_pf, RPBlocks.roof_where_pf,RPBlocks.wall_name_banner,RPBlocks.wall_platform_banner,RPBlocks.wall_route_sign,RPBlocks.platform_door_extension);
	}
}
