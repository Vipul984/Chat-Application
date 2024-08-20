import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Server {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    private volatile boolean isRunning = true;
    public Server() {

        try{
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection .......");
            socket = server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out =new PrintWriter(socket.getOutputStream());
            
            startReading();
            startWriting();

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void startWriting() {

        Runnable r2 = ()->{
            System.out.println("Writer Started...");
            try {
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));


                while (isRunning) {

                        String text = br1.readLine();
                        out.println(text);
                        out.flush();

                        if(text.equalsIgnoreCase("exit")){
                            isRunning = false;
                            socket.close();
                            break;
                        }
                }
                System.out.println("Connection closed in 2");

            }catch(Exception e){
                System.out.println("Connection closed in 2");
            }
        };

        new Thread(r2).start();
    }

    private void startReading()  {

        Runnable r1 = ()->{
            System.out.println("Reader started");
            try{
                while(isRunning){
                    String text = br.readLine();
                    if(text==null || text.equalsIgnoreCase("exit") )
                    {
                        System.out.println("Client terminated the chat......");
                        isRunning = false;
                        socket.close();
                        break;
                    }
                    System.out.println("Client : "+text);
                }
            }catch (Exception e){

                System.out.println("Connection closed in 1");

            }

        };
        new Thread(r1).start();
    }

    public static void main(String[] args) {

        System.out.println("hello server is running...");
        new Server();
    }
}
