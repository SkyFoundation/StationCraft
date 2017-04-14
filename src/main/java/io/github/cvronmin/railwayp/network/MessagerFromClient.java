package io.github.cvronmin.railwayp.network;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Level;

import io.github.cvronmin.railwayp.Reference;
import io.github.cvronmin.railwayp.tileentity.TileEntityPlatformBanner;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessagerFromClient {
	public static class CUpdateBannerByGuiMessager implements IMessageHandler<CUpdateBannerByGui, IMessage>{
		@Override
		public IMessage onMessage(final CUpdateBannerByGui message, MessageContext ctx) {
			if (ctx.side != Side.SERVER) { 
				       System.err.println("Message received on wrong side:" + ctx.side); 
				       return null; 
				     } 
		 
				     // we know for sure that this handler is only used on the server side, so it is ok to assume 
				     //  that the ctx handler is a serverhandler, and that WorldServer exists. 
				     // Packets received on the client side must be handled differently!  See MessageHandlerOnClient 
				 
				 
				     final EntityPlayerMP sendingPlayer = ctx.getServerHandler().player;
				     if (sendingPlayer == null) { 
				       System.err.println("EntityPlayerMP was null when AirstrikeMessageToServer was received"); 
				       return null; 
				     } 
				 
				 
				     // This code creates a new task which will be executed by the server during the next tick, 
				     //  for example see MinecraftServer.updateTimeLightAndEntities(), just under section 
				     //      this.theProfiler.startSection("jobs"); 
				     //  In this case, the task is to call messageHandlerOnServer.processMessage(message, sendingPlayer) 
				     final WorldServer playerWorldServer = sendingPlayer.getServerWorld(); 
				     playerWorldServer.addScheduledTask(new Runnable() { 
				       public void run() { 
				         processMessage(message, sendingPlayer); 
				       } 
				     }); 

				     return null; 

		}

		protected void processMessage(CUpdateBannerByGui message, EntityPlayerMP player) {
	        BlockPos blockpos1 = message.getPos();
	        TileEntity tileentity2 = player.world.getTileEntity(blockpos1);
	        tileentity2.readFromNBT(message.getData());
	        tileentity2.markDirty();
	        player.world.notifyBlockUpdate(blockpos1, player.world.getBlockState(blockpos1), player.world.getBlockState(blockpos1), 3);

		}
	}
	
}
