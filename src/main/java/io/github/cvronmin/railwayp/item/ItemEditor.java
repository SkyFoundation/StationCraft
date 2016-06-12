package io.github.cvronmin.railwayp.item;

import io.github.cvronmin.railwayp.client.gui.GuiNameBannerEditor;
import io.github.cvronmin.railwayp.client.gui.GuiPlatformBannerEditor;
import io.github.cvronmin.railwayp.client.gui.GuiWHPFEditor;
import io.github.cvronmin.railwayp.tileentity.TileEntityNameBanner;
import io.github.cvronmin.railwayp.tileentity.TileEntityPlatformBanner;
import io.github.cvronmin.railwayp.tileentity.TileEntityWHPF;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEditor extends Item {
	public ItemEditor(){
		
	}
    /**
     * Called when a Block is right-clicked with this Item
     */
	@SideOnly(Side.CLIENT)
	@Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if(!worldIn.isRemote){
			return true;
		}
    	TileEntity te = worldIn.getTileEntity(pos);
    	if (te instanceof TileEntityPlatformBanner) {
			FMLClientHandler.instance().getClient().displayGuiScreen(new GuiPlatformBannerEditor((TileEntityPlatformBanner) te));
			return true;
		}
    	if (te instanceof TileEntityNameBanner) {
			FMLClientHandler.instance().getClient().displayGuiScreen(new GuiNameBannerEditor((TileEntityNameBanner) te));
			return true;
		}
    	if (te instanceof TileEntityWHPF) {
			FMLClientHandler.instance().getClient().displayGuiScreen(new GuiWHPFEditor((TileEntityWHPF) te));
			return true;
		}
        return true;
    }
}
