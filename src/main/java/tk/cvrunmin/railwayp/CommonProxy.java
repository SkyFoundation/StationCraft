package tk.cvrunmin.railwayp;

import tk.cvrunmin.railwayp.client.renderer.TileEntityPlatformBannerRenderer;
import tk.cvrunmin.railwayp.init.RPBlocks;
import tk.cvrunmin.railwayp.init.RPItems;
import tk.cvrunmin.railwayp.item.ItemPlatformBanner;
import tk.cvrunmin.railwayp.tileentity.TileEntityNameBanner;
import tk.cvrunmin.railwayp.tileentity.TileEntityPlatformBanner;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	RPBlocks.register();
    	RPItems.register();
    	registerEntity();
    }
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	
    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	
    }
    private void registerEntity(){
//    	ClientRegistry.registerTileEntity(TileEntityPlatformBanner.class, "PlatformBanner", new TileEntityPlatformBannerRenderer());
    	GameRegistry.registerTileEntity(TileEntityPlatformBanner.class, "PlatformBanner");
    	GameRegistry.registerTileEntity(TileEntityNameBanner.class, "NameBanner");
    }
	protected void blockRend(Block block, String registerName){
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0,
                new ModelResourceLocation("railwayp:" + registerName, "inventory"));
	}
	protected void itemRend(Item item, String registerName){
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0,
                new ModelResourceLocation("railwayp:" + registerName, "inventory"));
	}
	protected void itemRend(Item item, int damage, String ideniter){
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, damage,
                new ModelResourceLocation("railwayp:" + ideniter, "inventory"));
	}
}
