package org.ginafro.notenoughfakepixel.events.handlers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.events.NEFClientConnectedToServerEvent;
import org.ginafro.notenoughfakepixel.utils.Logger;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@RegisterEvents
public class ConnectionHandler {

    private static final String TARGET_SERVER = "fakepixel.fun";

    @SubscribeEvent
    public void onServerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        NEFClientConnectedToServerEvent nefEvent = NEFClientConnectedToServerEvent.create(event);
        event.manager.channel().pipeline().addBefore("packet_handler", "nef_packet_handler", new PacketHandler());
        Logger.logConsole("Added packet handler to channel pipeline.");
        SocketAddress address = nefEvent.manager.getRemoteAddress();

        if (address instanceof InetSocketAddress) {
            InetSocketAddress inetAddress = (InetSocketAddress) address;
            String hostName = inetAddress.getHostName();
            int port = inetAddress.getPort();

            if (hostName.contains(TARGET_SERVER)) {
                ApiHandler.init();
                Logger.logConsole("Connected to " + TARGET_SERVER + ":" + port);
            } else {
                Logger.logConsole("Not connected to " + TARGET_SERVER + ":" + port);
            }
        }

    }

}