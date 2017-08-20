package io.github.cvronmin.railwayp.init;

import java.util.Arrays;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
@Mod.EventBusSubscriber
public class RPCraftingManager {
	private static void register() {
		GameRegistry.addShapedRecipe(new ResourceLocation("railwayp:platform_banner"),new ResourceLocation("railwayp"),new ItemStack(RPItems.platform_banner, 2), "AAA",
				"BBB",
				'A', new ItemStack(Blocks.STAINED_HARDENED_CLAY,1,EnumDyeColor.WHITE.getMetadata()),
				'B',Blocks.PLANKS);
		GameRegistry.addShapedRecipe(new ResourceLocation("railwayp:platform_banner_more"),new ResourceLocation("railwayp"),new ItemStack(RPItems.platform_banner, 4), "CCC",
				"AAA",
				"BBB",
				'A', new ItemStack(Blocks.STAINED_HARDENED_CLAY,1,EnumDyeColor.WHITE.getMetadata()),
				'B',Blocks.PLANKS,
				'C',Items.SIGN);
		GameRegistry.addShapedRecipe(new ResourceLocation("railwayp:platform_indicator"),new ResourceLocation("railwayp"),new ItemStack(RPItems.whpf, 2), "AAA",
				"BBB",
				'A', new ItemStack(Blocks.STAINED_HARDENED_CLAY,1,EnumDyeColor.BLACK.getMetadata()),
				'B',Blocks.PLANKS);
		GameRegistry.addShapedRecipe(new ResourceLocation("railwayp:station_name_banner"),new ResourceLocation("railwayp"),new ItemStack(RPItems.name_banner, 4), " A ",
				"BBB",
				'A', Items.SIGN,
				'B',Blocks.STAINED_HARDENED_CLAY);
		GameRegistry.addShapedRecipe(new ResourceLocation("railwayp:editor"),new ResourceLocation("railwayp"),new ItemStack(RPItems.EDITOR, 1), "  A",
				" A ",
				"B  ",
				'A', Items.STICK,
				'B',Items.DYE);
		registerMosaticTile();
		registerPlate();
		net.minecraftforge.oredict.RecipeSorter.register("railwayp:cloning", CloningRecipe.class, Category.SHAPELESS, "after:minecraft:shapeless");
	}
	private static void registerMosaticTile(){
		for(int i = 0;i < 16;i++){
			final EnumDyeColor color = EnumDyeColor.byMetadata(i);
			ItemStack itemstack = new ItemStack(RPBlocks.mosaic_tile, 8);
            NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("Color", Integer.toHexString(color.getColorValue()));
            itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
			GameRegistry.addShapedRecipe(new ResourceLocation(String.format("railwayp:%s_mosatic_tile", color.getName())),new ResourceLocation("railwayp"),itemstack, "ABA",
					"BBB",
					"ABA",
					'A', new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, i),
					'B',Items.QUARTZ);
		}
	}
	private static void registerPlate(){
		for(int i = 0;i < 16;i++){
			final EnumDyeColor color = EnumDyeColor.byMetadata(i);
			ItemStack itemstack = new ItemStack(RPBlocks.plate, 8);
            NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("Color", Integer.toHexString(color.getColorValue()));
            itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
			GameRegistry.addShapedRecipe(new ResourceLocation(String.format("railwayp:%s_plate", color.getName())),new ResourceLocation("railwayp"),itemstack, "AAA",
					"ABA",
					"AAA",
					'B', new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, i),
					'A',Items.IRON_INGOT);
		}
	}
	@SubscribeEvent
	public static void onRegisterRecipe(RegistryEvent.Register<IRecipe> event){
		register();
		IForgeRegistry<IRecipe> registry = event.getRegistry();
		registry.register(new CloningRecipe(RPItems.name_banner).setRegistryName("railwayp:station_name_banner_cloning"));
		registry.register(new CloningRecipe(RPItems.platform_banner).setRegistryName("railwayp:platform_banner_cloning"));
		registry.register(new CloningRecipe(RPItems.whpf).setRegistryName("railwayp:platform_indicator_cloning"));
		registry.register(new CloningRecipe(RPItems.route_sign).setRegistryName("railwayp:railline_sign_cloning"));
		registry.register(new CloningRecipe(Item.getItemFromBlock(RPBlocks.mosaic_tile)).setRegistryName("railwayp:mosaic_tile_cloning"));
		registry.register(new CloningRecipe(Item.getItemFromBlock(RPBlocks.plate)).setRegistryName("railwayp:plate_cloning"));
	}
	private static class CloningRecipe extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe{
		Item item;
		public CloningRecipe(Item item) {
			this.item = item;
		}
		@Override
		public boolean matches(InventoryCrafting inv, World worldIn) {
	        int i = 0;
	        ItemStack itemstack = null;

	        for (int j = 0; j < inv.getSizeInventory(); ++j)
	        {
	            ItemStack itemstack1 = inv.getStackInSlot(j);

	            if (itemstack1 != null)
	            {
	            	if(itemstack1.getItem() == item){
	                if (itemstack1.hasTagCompound())
	                {
	                    if (itemstack != null)
	                    {
	                        return false;
	                    }

	                    itemstack = itemstack1;
	                }
	                else ++i;
	            	}else return false;
	            }
	        }

	        return itemstack != null && i > 0;
		}
		
		@Override
		public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
	        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);

	        for (int i = 0; i < nonnulllist.size(); ++i)
	        {
	            ItemStack itemstack = inv.getStackInSlot(i);
	                nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
	        }

	        return nonnulllist;
		}

		@Override
		public ItemStack getRecipeOutput() {
			return ItemStack.EMPTY;
		}
		
		@Override
		public ItemStack getCraftingResult(InventoryCrafting inv) {
	        int i = 0;
	        ItemStack itemstack = ItemStack.EMPTY;

	        for (int j = 0; j < inv.getSizeInventory(); ++j)
	        {
	            ItemStack itemstack1 = inv.getStackInSlot(j);

	            if (!itemstack1.isEmpty())
	            {
	            	if(itemstack1.getItem() == item){
		                if (itemstack1.hasTagCompound())
		                {
		                    if (!itemstack.isEmpty())
		                    {
		                        return ItemStack.EMPTY;
		                    }

		                    itemstack = itemstack1;
		                }
		                else ++i;
		            	}else return ItemStack.EMPTY;
	            }
	        }

	        if (!itemstack.isEmpty() && i >= 1)
	        {
	            ItemStack itemstack2 = new ItemStack(item, i + 1, itemstack.getMetadata());

	            if (itemstack.hasDisplayName())
	            {
	                itemstack2.setStackDisplayName(itemstack.getDisplayName());
	            }
	            itemstack2.setTagCompound(itemstack.getTagCompound());

	            return itemstack2;
	        }
	        else
	        {
	            return ItemStack.EMPTY;
	        }
		}

		public boolean isHidden()
		{
			return true;
		}
		@Override
		public boolean canFit(int width, int height) {
			return width >= 3 && height >= 3;
		}

	}
}
