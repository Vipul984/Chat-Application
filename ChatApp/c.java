import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class c {

    Socket socket ;
    BufferedReader br;
    PrintWriter out;
    private volatile boolean isRunning = true;
    public c(){

        try {
            System.out.println("Sending request to server...");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connection done....");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out =new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        }catch (Exception e){

            e.printStackTrace();

        }
    }

    private void startWriting() {
        Runnable r2 = ()->{
            System.out.println("Writer Started");
            try{
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                while(isRunning){

                    String text =br1.readLine();
                    out.println(text);
                    out.flush();
                    if(text.equalsIgnoreCase("exit")){
                        isRunning = false;
                        socket.close();
                        break;
                    }
                }
            }catch(Exception e){

                System.out.println("Connection closed in 4");

            }
        };

        new Thread(r2).start();
    }

    private void startReading() {
        Runnable r1 = ()->{
            System.out.println("Reader started");
          try{
              while(isRunning){
                  String text = br.readLine();
                  if(text == null || text.equalsIgnoreCase("exit")){
                      System.out.println("Sever exited the program..");
                      isRunning = false;
                      socket.close();
                      break ;
                  }
                  System.out.println("Server : " +text);
              }
          }catch(Exception e){
              System.out.println("Connection closed in 3");
          }
        };
        new Thread(r1).start();
    }

    public static void main(String[] args){
        System.out.println("client is starting .....");
        new c();

    }
}
