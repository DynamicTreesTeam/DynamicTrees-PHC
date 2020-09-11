package com.ferreusveritas.dynamictreesphc.compat;

import com.ferreusveritas.dynamictreesphc.blocks.BlockMapleSpile;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class WailaCompat implements IWailaPlugin {

	@Override
	public void register(IWailaRegistrar registrar) {
		WailaBranchHandlerPHC branchHandler = new WailaBranchHandlerPHC();
		WailaSpileHandler spileHandler = new WailaSpileHandler();
		
//		registrar.registerBodyProvider(branchHandler, BlockBranchGoldenOak.class);
//		registrar.registerNBTProvider(branchHandler, BlockBranchGoldenOak.class);

		registrar.registerBodyProvider(spileHandler, BlockMapleSpile.class);
	}
	
}
