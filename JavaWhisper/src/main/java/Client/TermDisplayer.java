/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import MessageExchange.Whisper;

/**
 *
 * @author flodavid
 */
public class TermDisplayer implements MessageExchange.MessageDisplayer{

    @Override
    public void showMessage(String message) {
        System.out.flush();
        for (int i= 0; i < DEFAULT_TEXT.length(); ++i) System.out.print("\b");

        System.out.println("SENDER_NAME : " + message);
        System.out.print(DEFAULT_TEXT);
    }
    
    @Override
    public void showMessage(Whisper message) {
        System.out.flush();
        for (int i= 0; i < DEFAULT_TEXT.length(); ++i) System.out.print("\b");

        System.out.println(message);
        System.out.print(DEFAULT_TEXT);
    }
}
