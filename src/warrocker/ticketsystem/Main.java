package warrocker.ticketsystem;

import warrocker.ticketsystem.hibernate.Hibernate;
import warrocker.ticketsystem.hibernate.Users;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import warrocker.ticketsystem.hibernate.Category;
import warrocker.ticketsystem.hibernate.Group;
import warrocker.ticketsystem.hibernate.Priority;
import warrocker.ticketsystem.hibernate.Status;
import warrocker.ticketsystem.hibernate.Ticket;
import warrocker.ticketsystem.hibernate.Users;
public class Main implements Runnable {

    private static final int PORT = 4445;

    private static ServerSocket serverSocket;
    private Socket socket;

    private Main() throws IOException {
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

        try {
            new Main();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        hibernate.listUsers();
        Hibernate hibernate = Hibernate.getInstance();
        hibernate.listTickets(1);

//        hibernate.listCategory();
//        hibernate.listPriority();
//        hibernate.listStatus();
//        hibernate.listGroup();
    }

    @Override
    public void run() {
        try(ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())){
            Object response = objectInputStream.readObject();
            if(response instanceof RequestCode) {
                RequestCode rc = (RequestCode) response;
                Hibernate hibernate = Hibernate.getInstance();
                switch (rc.getCode()) {
                    case RequestCode.CATEGORY:
                        objectOutputStream.writeObject(hibernate.listCategory());
                        break;
                    case RequestCode.PRIORITY:
                        objectOutputStream.writeObject(hibernate.listPriority());
                        break;
                    case RequestCode.USERS:
                        objectOutputStream.writeObject(hibernate.listUsers());
                        break;
                    case RequestCode.STATUS:
                        objectOutputStream.writeObject(hibernate.listStatus());
                        break;
                    case RequestCode.GROUP:
                        objectOutputStream.writeObject(hibernate.listGroup());
                        break;
                    case RequestCode.ACTIVE: {
                        objectOutputStream.writeObject(hibernate.listTickets(1));
                        break;
                    }
                    case RequestCode.ALL: {
                        objectOutputStream.writeObject(hibernate.listTickets());
                        break;
                    }
                }
            }else {
                    Users user = (Users) response;
                    //System.out.println( (objectInputStream.readObject()).toString());
                    String login = user.getLogin();
                    String hash = Hibernate.md5Custom(user.getHash());
                    Hibernate hibernate = Hibernate.getInstance();
                    Users userResult = hibernate.auth(login, hash);
                    if (userResult != null) {
                        objectOutputStream.writeObject(userResult);
                    } else {
                        objectOutputStream.writeBoolean(false);
                    }
                }
            }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
         }
        }
    }

