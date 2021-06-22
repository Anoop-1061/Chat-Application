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
public class Server extends JFrame {
    ServerSocket server;    //
    Socket socket;
    JFrame j1=new JFrame();
    BufferedReader br;  //FOR READING INPUT FROM CLIENT
    PrintWriter out;    //FOR WRITING OUTPUT OR SENDING OUTPUT TO CLIENT
    JLabel heading = new JLabel("Server");
    JTextArea messageArea = new JTextArea();
    Font font;
    JTextField messageInput = new JTextField();
    
    Server()    {
        this.font = new Font("SANS_SERIF", Font.PLAIN, 20);
        try {
            
            server = new ServerSocket(7807);    //
            System.out.println("Waiting For Client to get connected ");
            socket = server.accept();    //TO RECEIVE OBJECT FROM CLIENT
            System.out.println("Connection Established");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //TO RETREIVE INFORMATION FROM CLIENT
            out = new PrintWriter(socket.getOutputStream());    //TO PRINT SOCKET INFORMATION
            createGUI();
            handleEvents();
            startReading();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    

    private void createGUI()    {
        j1.setTitle("Gaitonde");
        j1.setLocation(400, 200);
        j1.setSize(550, 700);
        //j1.setLocationRelativeTo(null);
        j1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //CREATING COMPONENTS
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(40, 40,40,40));
        messageArea.setEditable(false);
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
                System.out.println("exit");
                if(e.getKeyCode()==10){
                    String contentToSend=messageInput.getText();
                    messageArea.append("Gaitonde: "+contentToSend+"\n");
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
                    System.out.println("Client Terminated the chat");
                    JOptionPane.showMessageDialog(j1, "Client Terminated the chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                messageArea.append("Bunty: "+msg+"\n");
            }
            }catch(Exception e) {
                System.out.println("Client Connection closed ");
        }
        };
        new Thread(r1).start();
    }

    public static void main(String[] args) {
        System.out.println("This is Server...");
        new Server();
    }
}