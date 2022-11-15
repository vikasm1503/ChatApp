import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
//import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Client extends JFrame {
    Socket socket;
    BufferedReader bf;
    PrintWriter out;

    //Declare Component
    private JLabel heading=new JLabel("Client");
    private JTextArea messageArea= new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);

    //Constructor
    public Client()
    {
        try{
            System.out.println("Sending Request to Server");
            socket=new Socket("127.0.0.1", 1234);
            System.out.println("Connection Done");

            bf=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            //startWriting();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==10){
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
                
            }
            
        });
        
    }
    private void createGUI() {
        //gui code
        this.setTitle("Client Messanger[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

       // heading.setIcon(new ImageIcon("Ci.png"));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20, 20));
        messageArea.setEditable(false);

        //set frame area
        this.setLayout(new BorderLayout());

        //adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        jScrollPane.getAutoscrolls();
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

    }
    //Writing
    /*private void startWriting() {
        System.out.println("Started Writing");
        Runnable r1=()->{
            try{
            while(!socket.isClosed()){
                    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
                    String content=br.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("bye")){
                        socket.close();
                        break;
                    }
                }
            }
                catch(Exception e){
                    System.out.println("Connection is closed.*BYE*");
                    //e.printStackTrace();
                }
                //System.out.println("Connection is closed.*BYE*");

        };
        new Thread(r1).start();
    }*/
    //Reading
    private void startReading() {
        System.out.println("Started Reading");
        Runnable r2=()->{
            try{
            while(true){
                    String msg=bf.readLine();
                    if(msg.equals("bye")){
                        System.out.println("Server wants to terminate chat");
                        JOptionPane.showMessageDialog(this,"Server Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }

                    else{
                        messageArea.append("Server : "+msg+"\n");
                    }
                }
            }
                catch(Exception e){
                    System.out.println("Connection is closed.*BYE*");
                }
        };
        new Thread(r2).start();

    }
    public static void main(String[] args) {
        new Client();
    }
}
