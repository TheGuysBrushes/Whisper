/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageExchange;

/**
 *
 * @author flodavid
 */
public interface MessageDisplayer{
    public final static String //<editor-fold defaultstate="collapsed" desc="comment">
            DEFAULT_TEXT//</editor-fold>
            = "Chuchoter...";
    
    public void showMessage(String message);
    public void showMessage(Whisper message);

}
