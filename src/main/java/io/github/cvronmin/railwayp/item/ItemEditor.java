package io.github.cvronmin.railwayp.item;

import io.github.cvronmin.railwayp.client.gui.GuiPlatformBannerEditor;
import io.github.cvronmin.railwayp.tileentity.TileEntityPlatformBanner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ItemEditor extends Item {
	public ItemEditor(){
		
	}
    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
    	TileEntity te = worldIn.getTileEntity(pos);
    	if (te instanceof TileEntityPlatformBanner) {
			FMLClientHandler.instance().getClient().displayGuiScreen(new GuiPlatformBannerEditor((TileEntityPlatformBanner) te));
			return EnumActionResult.SUCCESS;
		}
        return EnumActionResult.PASS;
    }
}
