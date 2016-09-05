package warrocker.ticketsystem;

import warrocker.ticketsystem.hibernate.Hibernate;
import warrocker.ticketsystem.hibernate.Users;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main implements Runnable {

    private static final int PORT = 4444;

    private static ServerSocket serverSocket;
    private Socket socket;

    public Main() throws IOException {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!serverSocket.isClosed()) {
            socket = serverSocket.accept();
            System.out.println(socket.getInetAddress());
            Thread socketThread = new Thread(this);
            socketThread.start();
        }
    }

    public static void main(String[] args) {

//        try {
//            new Main();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Hibernate hibernate = new Hibernate();
        //hibernate.listUsers();
        hibernate.listTickets();

    }

    @Override
    public void run() {
        try(ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())){
            Users user = (Users) objectInputStream.readObject();
            //System.out.println( (objectInputStream.readObject()).toString());
            String login = user.getLogin();
            String hash = Hibernate.md5Custom(user.getHash());
            Hibernate hibernate = new Hibernate();
            Users userResult = hibernate.auth(login, hash);
            if(userResult != null) {
                objectOutputStream.writeObject(userResult);
            }else {
                objectOutputStream.writeBoolean(false);
            }

        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
