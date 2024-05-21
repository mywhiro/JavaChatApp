/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
  private Socket socket = null;
  private BufferedReader inputConsole = null;
  private PrintWriter out = null;
  private BufferedReader in = null;

  public ChatClient(String address, int port) {
      try {
          socket = new Socket(address, port);
          System.out.println("Connected to the chat server");

          inputConsole = new BufferedReader(new InputStreamReader(System.in));
          out = new PrintWriter(socket.getOutputStream(), true);
          in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

          String line = "";
          while (!line.equals("exit")) {
              line = inputConsole.readLine();
              out.println(line);
              System.out.println(in.readLine());
          }

          socket.close();
          inputConsole.close();
          out.close();
      } catch (UnknownHostException u) {
          System.out.println("Host unknown: " + u.getMessage());
      } catch (IOException i) {
          System.out.println("Unexpected exception: " + i.getMessage());
      }
  }

  public static void main(String args[]) {
      ChatClient client = new ChatClient("127.0.0.1", 5000);
  }
}
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

public class ChatClient {
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private Consumer<String> onMessageReceived;

  public ChatClient(String serverAddress, int serverPort, Consumer<String> onMessageReceived) throws IOException {
      this.socket = new Socket(serverAddress, serverPort);
      this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.out = new PrintWriter(socket.getOutputStream(), true);
      this.onMessageReceived = onMessageReceived;
  }

  public void sendMessage(String msg) {
      out.println(msg);
  }

  public void startClient() {
      new Thread(() -> {
          try {
              String line;
              while ((line = in.readLine()) != null) {
                  onMessageReceived.accept(line);
              }
          } catch (IOException e) {
              e.printStackTrace();
          }
      }).start();
  }
}