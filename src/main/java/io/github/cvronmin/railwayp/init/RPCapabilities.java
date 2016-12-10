package io.github.cvronmin.railwayp.init;

import io.github.cvronmin.railwayp.capability.CapabilityHexColor;
import io.github.cvronmin.railwayp.capability.IHexColor;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class RPCapabilities {
	@CapabilityInject(IHexColor.class)
	public static Capability<IHexColor> hexColor;
	
	public static void preInit(FMLPreInitializationEvent event){
		CapabilityManager.INSTANCE.register(IHexColor.class, new CapabilityHexColor.Storage(), CapabilityHexColor.class);
	}
}
