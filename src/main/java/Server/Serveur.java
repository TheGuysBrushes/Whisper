package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Serveur {

    public static void main(String[] args) {

        ServerSocket socketserver;
        Socket socketduserveur;
        BufferedReader in;
        PrintWriter out;

        try {

            socketserver = new ServerSocket(2008);
            System.out.println("Le serveur est à l'écoute du port " + socketserver.getLocalPort());
            socketduserveur = socketserver.accept();
            System.out.println("Un client s'est connecté !");
            out = new PrintWriter(socketduserveur.getOutputStream());
            out.println("Bonjour, Pour vous deconnecté faite logout.");
            out.flush();

            String chaine;
            chaine = "";
            while ((chaine.equals("logout")) == false) {
                in = new BufferedReader(new InputStreamReader(socketduserveur.getInputStream()));
                chaine = in.readLine();
                System.out.println(chaine);
                out = new PrintWriter(socketduserveur.getOutputStream());
                out.println("Ok ! " + chaine);
                out.flush();
                if (chaine == "logout") {
                    socketserver.close();
                    socketduserveur.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
