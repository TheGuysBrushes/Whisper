/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import MessageExchange.MessageReceiver;
import MessageExchange.MessageDisplayer;
import static MessageExchange.MessageDisplayer.DEFAULT_TEXT;
import MessageExchange.MessageSender;
import MessageExchange.MessageWriter;
import MessageExchange.Whisper;
import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import org.apache.log4j.Logger;

/**
 *
 * @author flodavid
 */
public class ClientGUI extends javax.swing.JFrame implements ActionListener, MessageDisplayer, MessageWriter {
    private final static Logger LOGGER = Logger.getLogger(ClientGUI.class);
    
    private MessageSender sender;
    private String SENDER_NAME = "SENDER_NAME";
    private String[] WELCOME_MESSAGES;
    private boolean exchangeStarted;
        
    /**
     * Creates a new chat window
     * @param canStartChat : Defines if the Client is already connected to an
     * host (and can send messages right now) or if it's waiting for connection
     * (must wait for a client connection )
     */
    public ClientGUI(boolean canStartChat) {
        exchangeStarted= canStartChat;

        if (canStartChat) {
            startConversation();
        } else {
            try {
                InetAddress ip= InetAddress.getLocalHost();

                WELCOME_MESSAGES = new String[]{
                    "<html><i>Attente de la connexion d'un contact</i></html>",
                    "<html>Adresse de connexion : <u>" + ip.getHostAddress() + "</u> ("+ ip.getHostName() + ")</html>",
                    "======================"};
                
                InetAddress[] ips = InetAddress.getAllByName(ip.getCanonicalHostName());
                if (ips  != null ) {
                    for (InetAddress ip1 : ips) {
                        if (ip1.isSiteLocalAddress()) {
                            String[] tmpWELCOME_MESSAGES = WELCOME_MESSAGES;
                            WELCOME_MESSAGES = new String[WELCOME_MESSAGES.length +1];
                            System.arraycopy(tmpWELCOME_MESSAGES, 0, WELCOME_MESSAGES, 0, tmpWELCOME_MESSAGES.length);
                            WELCOME_MESSAGES[WELCOME_MESSAGES.length-1]= "<html>Adresse : <u>" + ip1.getHostAddress() + "</u></html>";
                        }
                    }
                }
                
            } catch (UnknownHostException e) {
                e.printStackTrace();
                String[] tmpWELCOME_MESSAGES = WELCOME_MESSAGES;
                WELCOME_MESSAGES = new String[WELCOME_MESSAGES.length +1];
                System.arraycopy(tmpWELCOME_MESSAGES, 0, WELCOME_MESSAGES, 0, tmpWELCOME_MESSAGES.length);
                WELCOME_MESSAGES[WELCOME_MESSAGES.length-1]= "<html><b style=\"color:#FF0000\";>Impossible de trouver l'hôte canonique<b><html>";
            }
            
            try {
                Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
                for (; n.hasMoreElements();)
                {
                    NetworkInterface e = n.nextElement();

                    Enumeration<InetAddress> a = e.getInetAddresses();
                    for (; a.hasMoreElements();)
                    {
                        InetAddress addr = a.nextElement();
                        
                        if (addr.isSiteLocalAddress()) {
                            String[] tmpWELCOME_MESSAGES = WELCOME_MESSAGES;
                            WELCOME_MESSAGES = new String[WELCOME_MESSAGES.length +1];
                            System.arraycopy(tmpWELCOME_MESSAGES, 0, WELCOME_MESSAGES, 0, tmpWELCOME_MESSAGES.length);
                            WELCOME_MESSAGES[WELCOME_MESSAGES.length-1]= "<html>Add : <u>"+addr.getHostAddress()+"</u></html>";
                        }
                    }
                }
            } catch (SocketException se) {
                se.printStackTrace();
                
                String[] tmpWELCOME_MESSAGES = WELCOME_MESSAGES;
                WELCOME_MESSAGES = new String[WELCOME_MESSAGES.length +1];
                System.arraycopy(tmpWELCOME_MESSAGES, 0, WELCOME_MESSAGES, 0, tmpWELCOME_MESSAGES.length);
                WELCOME_MESSAGES[WELCOME_MESSAGES.length-1]= "<html><b style=\"color:#FF0000\";>Réseau inconnu</b></html>";
            }
        }
        
        initComponents();
//        messagesList.setDropMode(DropMode.INSERT);
        initPlaceHolder();
        
        if (canStartChat) {
            setLocation(getWidth(), 0);
        }
    }
    
    private void initPlaceHolder() {
        messageField.setForeground(Color.GRAY);
        messageField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (messageField.getText().equals(DEFAULT_TEXT)) {
                    messageField.setText("");
                    messageField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (messageField.getText().isEmpty()) {
                    resetMessageField();
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        tabPanel = new javax.swing.JPanel();
        messagesScrollPane = new javax.swing.JScrollPane();
        messagesList = new javax.swing.JList<>();
        messagePanel = new javax.swing.JPanel();
        messageField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Whisper");

        tabPanel.setLayout(new java.awt.BorderLayout());

        resetMessages();
        messagesScrollPane.setViewportView(messagesList);

        tabPanel.add(messagesScrollPane, java.awt.BorderLayout.CENTER);

        messagePanel.setLayout(new java.awt.BorderLayout());

        messageField.setText(DEFAULT_TEXT);
        messageField.setToolTipText("");
        messageField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messageFieldActionPerformed(evt);
            }
        });
        messageField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                messageFieldKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                messageFieldKeyTyped(evt);
            }
        });
        messagePanel.add(messageField, java.awt.BorderLayout.CENTER);

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });
        messagePanel.add(sendButton, java.awt.BorderLayout.LINE_END);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        messagePanel.add(cancelButton, java.awt.BorderLayout.LINE_START);

        tabPanel.add(messagePanel, java.awt.BorderLayout.PAGE_END);

        jTabbedPane1.addTab("<html><i>SENDER_NAME</i></html>", tabPanel);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jMenu1.setText("Connect");

        jMenuItem1.setText("Connexion à l'aut'");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Attendre la connexion");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("About");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        resetMessageField();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        if ( !(messageField.getText().isEmpty() || messageField.getText().equals(DEFAULT_TEXT)) ) sendMessage();
    }//GEN-LAST:event_sendButtonActionPerformed

    private void messageFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_messageFieldKeyTyped
        if (messageField.getText().equals(DEFAULT_TEXT)) messageField.setText("");
    }//GEN-LAST:event_messageFieldKeyTyped

    private void messageFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_messageFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) sendMessage();
    }//GEN-LAST:event_messageFieldKeyPressed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void messageFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_messageFieldActionPerformed
        if (messageField.getText().equals(DEFAULT_TEXT)) messageField.setText("");
    }//GEN-LAST:event_messageFieldActionPerformed

    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source= e.getSource();

        if(source.getClass() == MessageReceiver.class) {
           messagesList.add(e.getActionCommand(), this);
        }
    }
    
    @Override
    public void showMessage(String message) { 
        addMessage(SENDER_NAME+ " : " + message);
    }

    @Override
    public void showMessage(Whisper message)     {
        if (message.hasBeenSendByMe()) {
            addMessage("ME : " + message.getContent());
        } else {
            updateSenderName(message.getSenderName());
            addMessage(message.toString());
        }
    }

    @Override
    public void setMessageSender(MessageSender msgSender) {
        sender= msgSender;
    }

    @Override
    public void startSending() {
        setVisible(true);
    }
    

    @Override
    public void stopSending() {
        setVisible(false);
    }
    
    @Override
    public void chatStarted() {
        exchangeStarted= true;
        startConversation();
        resetMessages();
    }
    
    private void resetMessageField() {
        if (messageField.hasFocus()) {
            messageField.setForeground(Color.BLACK);
            messageField.setText("");
        } else {
            messageField.setForeground(Color.GRAY);
            messageField.setText(DEFAULT_TEXT);
        }
    }
    
    private void resetMessages() {
        setMessages(WELCOME_MESSAGES);
    }
    
    private void startConversation() {
        WELCOME_MESSAGES = new String[]{"Début de la conversation",
            "======================"};
    }
    
   private void setMessages(String[] messages) {
        messagesList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = messages;
            @Override
            public int getSize() { return strings.length; }
            @Override
            public String getElementAt(int i) { return strings[i]; }
        });
   }
        
    private void addMessage(String message) {
        javax.swing.AbstractListModel<String> listModel = (javax.swing.AbstractListModel<String>)messagesList.getModel();
        String[] messages= new String[listModel.getSize() + 1];
        
        int i= 0;
        for (; i < listModel.getSize(); ++i) {
            messages[i]= listModel.getElementAt(i);
        }
        messages[i]= message;
        
        setMessages(messages);
    }
    
    public void sendMessage() {
        try {
            if (exchangeStarted) {
                sender.sendMessage(messageField.getText());
                addMessage("ME : " + messageField.getText());
                resetMessageField();
            } else {
                addMessage("<html><i style=\"color:#FF0000\";>Vous devez attendre la connexion d'un contact</i></html>");
            }
        } catch (IOException e) {
            System.err.println("Impossible d'envoyer le message" + e.getMessage());
        }
    }
    
    public void updateSenderName(String senderName) {
        
        SENDER_NAME= senderName;
        jTabbedPane1.setTitleAt(jTabbedPane1.getSelectedIndex(), SENDER_NAME);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //address = "192.168.99.107";
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, "Class non trouvée", ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, "Erreur d'instanciation", ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, "Accès illégal !", ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, "Les éléments d'interface ne sont pas supportés", ex);
        }
        //</editor-fold>

        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ClientGUI(false).setVisible(true);
            
//                ClientGUI.client.stopChat();
//                client.closeConnection();
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField messageField;
    private javax.swing.JPanel messagePanel;
    private javax.swing.JList<String> messagesList;
    private javax.swing.JScrollPane messagesScrollPane;
    private javax.swing.JButton sendButton;
    private javax.swing.JPanel tabPanel;
    // End of variables declaration//GEN-END:variables

}
