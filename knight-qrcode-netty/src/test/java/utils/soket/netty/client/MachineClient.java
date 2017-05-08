package utils.soket.netty.client;

import socket.netty.machine.TcpMachineClient;

/**
 * Hello world!
 */
public class MachineClient {
    public static void main(String[] args) {
        TcpMachineClient.getInstance().run();
    }
}
