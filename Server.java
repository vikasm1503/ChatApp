import java.io.BufferedReader;
//import java.io.Exception;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
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

public class Server extends JFrame
{
    ServerSocket server;
    Socket socket;
    BufferedReader bf;
    PrintWriter out;

    private JLabel heading=new JLabel("Server");
    private JTextArea messageArea= new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);
//constructor
public Server(){
    try {
        server=new ServerSocket(1234);
        System.out.println("Server is ready for Connection");
        System.out.println("Waiting ");
        socket=server.accept();

        bf=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out=new PrintWriter(socket.getOutputStream());
        createGUI();
        handleEvents();
        startReading();
        //startWriting();
    } catch (Exception e) {
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
    this.setTitle("Server Messanger[END]");
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
/*private void startWriting()
{
    Runnable r1=()->{
        System.out.println("Writing started");
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
private void startReading () 
{
    Runnable r2=()->{
        System.out.println("Reader Started");
        try{
        while(true){
                String msg= bf.readLine();
                if(msg.equals("bye")){
                    System.out.println("Client wants to terminate chat");
                    JOptionPane.showMessageDialog(this,"Client Terminated the chart");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                else messageArea.append("Client : "+msg+"\n");;
            } 
        }
            catch (Exception e) {
                System.out.println("Connection is closed.*BYE*");
            }

        
    };
    new Thread(r2).start();
}
public static void main(String[] args) {
   new Server(); 
}

}