package io.github.cvronmin.railwayp.network;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Level;

import io.github.cvronmin.railwayp.Reference;
import io.github.cvronmin.railwayp.client.ClientProxy;
import io.github.cvronmin.railwayp.tileentity.TileEntityPlatformBanner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessagerFromServer {
	public static class SUpdateRailNoticerGuiMessager implements IMessageHandler<SUpdateRailNoticerGui, IMessage>{
		@Override
		public IMessage onMessage(final SUpdateRailNoticerGui message, MessageContext ctx) {
			if (ctx.side != Side.CLIENT) { 
				       System.err.println("Message received on wrong side:" + ctx.side); 
				       return null; 
				     } 
		 
				     // we know for sure that this handler is only used on the server side, so it is ok to assume 
				     //  that the ctx handler is a serverhandler, and that WorldServer exists. 
				     // Packets received on the client side must be handled differently!  See MessageHandlerOnClient 
				 
				 
				     final EntityPlayerMP sendingPlayer = ctx.getServerHandler().playerEntity; 
				     if (sendingPlayer == null) { 
				       System.err.println("EntityPlayerMP was null when AirstrikeMessageToServer was received"); 
				       return null; 
				     } 
				 
				     final WorldClient worldClient = Minecraft.getMinecraft().world; 

				     // This code creates a new task which will be executed by the server during the next tick, 
				     //  for example see MinecraftServer.updateTimeLightAndEntities(), just under section 
				     //      this.theProfiler.startSection("jobs"); 
				     //  In this case, the task is to call messageHandlerOnServer.processMessage(message, sendingPlayer) 
				     final WorldServer playerWorldServer = sendingPlayer.getServerWorld(); 
				     playerWorldServer.addScheduledTask(new Runnable() { 
				       public void run() { 
				         processMessage(worldClient, message); 
				       } 
				     }); 

				     return null; 

		}

		protected void processMessage(WorldClient client, SUpdateRailNoticerGui message) {
			ClientProxy.statio.setThis(message.getThisStation());
			ClientProxy.statio.setNext(message.getNextStation());
			ClientProxy.statio.setInterchange(message.getInterchange());
		}

	}
	
}
