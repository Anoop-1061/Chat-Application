/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattingappusingsockets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class Client extends JFrame  {
    Socket socket;
    BufferedReader br;  //FOR READING INPUT FROM SERVER
    PrintWriter out;    //FOR WRITING OUTPUT OR SENDING OUTPUT TO SERVER
    JFrame j1=new JFrame();
    JLabel heading = new JLabel("Client");
    JTextArea messageArea = new JTextArea();
    JTextField messageInput = new JTextField();
    Font font;
    
    Client()    {
        this.font = new Font("SANS_SERIF", Font.PLAIN, 20);
        try {
            System.out.println("Sending request to Server");
            socket = new Socket("127.0.0.1", 7807); //IP ADDRESS(I.E SERVER RUNNING IN OUR SYSTEM) AND PORT NUMBER TO WHICH IT COMMUNICATES
            System.out.println("Connection done");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //TO RETRIEVE INFORMATION FROM SERVER
            out = new PrintWriter(socket.getOutputStream());    //TO PRINT SOCKET INFORMATION
            createGUI();
            handleEvents();
            startReading();
        }catch(Exception e) {
            e.printStackTrace();
        }
   
}
    
    private void createGUI()    {
        //SETTING UP THE FRAME
        j1.setTitle("Bunty");
        j1.setLocation(1100, 200);
        j1.setSize(550, 700);
        //j1.setLocationRelativeTo(null);
        j1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //CREATING COMPONENTS
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(40, 40,40,40));
        messageArea.setEditable(false);         //WE CANNOT EDIT THE MESSAGES AFTER IT IS SENT
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        //SETTING FRAME LAYOUT
        j1.setLayout(new BorderLayout());
        //ADDING COMPONENTS TO THE FRAME
        j1.add(heading, BorderLayout.NORTH);
        JScrollPane jsp =new JScrollPane(messageArea);
        j1.add(messageArea, BorderLayout.CENTER);
        j1.add(messageInput, BorderLayout.SOUTH);
        
        j1.setVisible(true);
    }
    
    private void handleEvents()  {
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("Key Released: "+e.getKeyCode());
                if(e.getKeyCode()==10){
                    String contentToSend=messageInput.getText();
                    messageArea.append("Bunty: "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                    System.out.println("");
                }
            }
        });
    }
    
    
    public void startReading()  {
        //USE THREADS TO READ DATA
        Runnable r1=()->{
            System.out.println("Reading Started ");
            try {
            while(true) {
                          
                String msg=br.readLine();
                if(msg.equals("exit")) {
                    System.out.println("Server Terminated the chat");
                    JOptionPane.showMessageDialog(j1, "Server Terminated the chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                messageArea.append("Gaitonde: "+msg+"\n");
            }
            }catch(Exception e) {
                System.out.println("Server Connection closed ");
        }
        };
        new Thread(r1).start();
    }
    
    public static void main(String[] args)  {
        System.out.println("This is Client...");
        new Client();
    }
}