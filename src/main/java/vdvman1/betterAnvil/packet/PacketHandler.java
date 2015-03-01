package vdvman1.betterAnvil.packet;

/*
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ChatAllowedCharacters;
import vdvman1.betterAnvil.BetterAnvil;
import vdvman1.betterAnvil.inventory.ContainerRepairBA;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player _player) {
		if(packet.channel.equals(BetterAnvil.channel) && _player instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)_player;
			if(!(player.openContainer instanceof ContainerRepairBA)) return;
			ContainerRepairBA container = (ContainerRepairBA)player.openContainer;

            if (packet.data != null && packet.data.length >= 1)
            {
                String var15 = ChatAllowedCharacters.filerAllowedCharacters(new String(packet.data));

                if (var15.length() <= 30)
                {
                    container.updateItemName(var15);
                }
            }
            else
            {
                container.updateItemName("");
            }
		}
	} 

}
*/
