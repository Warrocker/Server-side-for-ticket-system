package warrocker.ticketsystem.hibernate;


import com.sun.istack.internal.Nullable;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.sql.ordering.antlr.Factory;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

public class Hibernate {
    private SessionFactory sessionFactory;
    private String hash5;
    public Hibernate(){
        try{
            sessionFactory = new Configuration().configure("warrocker/ticketsystem/hibernate/hibernate.cfg.xml").buildSessionFactory();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
    @Nullable
    public Users auth(String login, String pass) {
        Transaction tx = null;
        Users selectedUser = null;
        String hash = "";
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List users = session.createQuery("from Users where login = '" + login + "'").list();
            for (Object user : users) {
                selectedUser = (Users) user;
                hash5 = selectedUser.getHash5();

                hash = md5Custom(md5Custom(pass + selectedUser.getHash5()));

            }

        }catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        if(selectedUser != null) {
            if (selectedUser.getHash() != null | hash.equals(selectedUser.getHash())) {
                return selectedUser;
            } else {
                return null;
            }
        }else {
            return null;
        }
    }

    public void listTickets(){
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List tickets = session.createQuery("from Ticket").list();
            for (Object ticket : tickets) {
                Ticket selectedTicket = (Ticket) ticket;
               // System.out.print("id: " + selectedTicket.getId() + " ");
                System.out.println("caption: " + selectedTicket.getTitle());
                System.out.println("status : " + statusById(selectedTicket.getStatus()));
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void listUsers(){
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List users = session.createQuery("from Users").list();
            for (Object user : users) {
                Users selectedUser = (Users) user;
                System.out.print("id: " + selectedUser.getId()+ " ");
                System.out.println("caption: " + selectedUser.getCaption());
                hash5 = selectedUser.getHash5();
                        String s = md5Custom(md5Custom("waldemar"+ selectedUser.getHash5()));
                System.out.println(s.equals(selectedUser.getHash()));
        }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
   private String statusById(int id){
       Transaction tx = null;
       Status selectedStatus;
       String caption = "";
       try (Session session = sessionFactory.openSession()) {
           tx = session.beginTransaction();
           List statuses = session.createQuery("from Status where id = '" + id + "'").list();
           for (Object status : statuses) {
               selectedStatus = (Status) status;
               caption = selectedStatus.getCaption();
           }
           tx.commit();
       } catch (HibernateException e) {
           if (tx != null) tx.rollback();
           e.printStackTrace();
       }
           return caption;

   }
    public static String md5Custom(String st) {
        MessageDigest messageDigest;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(st.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            // тут можно обработать ошибку
            // возникает она если в передаваемый алгоритм в getInstance(,,,) не существует
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while( md5Hex.length() < 32 ){
            md5Hex = "0" + md5Hex;
        }

        return md5Hex;
    }
}
