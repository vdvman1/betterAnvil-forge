package vdvman1.betterAnvil.packet;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ChatAllowedCharacters;
import vdvman1.betterAnvil.inventory.ContainerRepairBA;

/**
 * Created by Master801 on 2/25/2015 at 4:09 PM.
 * @author Master801
 */
public final class MessageCharacters implements IMessage {

    private String text = null;

    public MessageCharacters(String text) {
        this.text = text;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        text = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, text);
    }

    public static final class MessageCharactersHandler implements IMessageHandler<MessageCharacters, IMessage> {

        @Override
        public IMessage onMessage(MessageCharacters message, MessageContext ctx) {
            FMLLog.info("Received message");
            if (!(ctx.getServerHandler().playerEntity.openContainer instanceof ContainerRepairBA)) {
                return null;
            }
            ContainerRepairBA container = (ContainerRepairBA)ctx.getServerHandler().playerEntity.openContainer;
            if (message.text != null) {
                String characters = ChatAllowedCharacters.filerAllowedCharacters(message.text);
                if (characters.length() < 31) {
                    container.updateItemName(characters);
                }
            } else {
                container.updateItemName("");
            }
            return null;
        }

    }

}
