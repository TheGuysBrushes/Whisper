/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author flodavid
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {

    public static void main(String[] args) {

        String arg;
        if (args.length < 1) {
            arg = "2008";
        } else {
            arg = args[0];
        }
        //Serveur
        try {
            int port = Integer.parseInt(arg);
            DatagramSocket socket = new DatagramSocket(port);
            boolean continuer = true;
            while (continuer) {
                //Reception
                byte[] inBuf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(inBuf, inBuf.length);

                socket.receive(packet);
                String s = new String(packet.getData(), 0, packet.getLength());

                if (s.equals("fin")) {
                    continuer = false;
                } else {
                    System.out.println(s);

                    //Envoi
                    String ok = new String(s + "_ok");
                    byte[] buf = ok.getBytes();
                    //DatagramSocket socketSend= new DatagramSocket(Integer.parseInt(args[0]));
                    DatagramPacket packetSend = new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort());

                    socket.send(packetSend);
                }
            }
            socket.close();
        } catch (Exception e) {
            System.err.println("impossible de creer : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
