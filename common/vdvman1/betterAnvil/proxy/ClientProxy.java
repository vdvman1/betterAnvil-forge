package vdvman1.betterAnvil.proxy;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import vdvman1.betterAnvil.BetterAnvil;


public class ClientProxy extends CommonProxy {
	
	@Override
	public void sendItemNamePacket(String itemName, EntityPlayer player) {
		if(itemName != null && player instanceof EntityClientPlayerMP) {
			Packet250CustomPayload packet = new Packet250CustomPayload(BetterAnvil.channel, itemName.getBytes());
			((EntityClientPlayerMP)player).sendQueue.addToSendQueue(packet);
		}
	}

}
