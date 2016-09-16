package warrocker.ticketsystem.hibernate;


import com.sun.istack.internal.Nullable;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public final class Hibernate {
    ArrayList<Ticket> ticketArrayList = new ArrayList<>();
    private static Hibernate instance = null;
    private SessionFactory sessionFactory;
    private String hash5;
    private Hibernate(){
        try{
            sessionFactory = new Configuration().configure("warrocker/ticketsystem/hibernate/hibernate.cfg.xml").buildSessionFactory();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
    public static synchronized Hibernate getInstance() {
        if (instance == null)
            instance = new Hibernate();
        return instance;
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

    public ArrayList listTickets(){
        Transaction tx = null;
        ArrayList<Ticket> ticketArrayList = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List tickets = session.createQuery("from Ticket").list();
            for (Object ticket : tickets) {
                Ticket selectedTicket = (Ticket) ticket;
                ticketArrayList.add(selectedTicket);
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return ticketArrayList;
    }
    public ArrayList listTickets(int status) {
        Transaction tx = null;
        ArrayList<Ticket> ticketArrayList = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List tickets = session.createQuery("from Ticket ticket where status = " + "'" + status + "'" + "order by ticket.st_dtime desc").list();
            for (Object ticket : tickets) {
                Ticket selectedTicket = (Ticket) ticket;
                ticketArrayList.add(selectedTicket);
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return ticketArrayList;
    }

    public ArrayList listUsers(){
        ArrayList<Users> usersArrayList = new ArrayList<>();
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List users = session.createQuery("from Users").list();
            for (Object user : users) {
                Users selectedUser = (Users) user;
                usersArrayList.add(selectedUser);
                System.out.println(selectedUser);
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return usersArrayList;
    }
    public ArrayList listGroup(){
        ArrayList<Group> groupArrayList = new ArrayList<>();
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List groups = session.createQuery("from Group").list();
            for (Object group : groups) {
                Group selectedGroup = (Group) group;
                groupArrayList.add(selectedGroup);
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return groupArrayList;
    }
    public ArrayList listCategory(){
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List categories = session.createQuery("from Category").list();
            for (Object category : categories) {
                Category selectedCategory = (Category) category;
                categoryArrayList.add(selectedCategory);
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return categoryArrayList;
    }
    public ArrayList listPriority(){
        ArrayList<Priority> priorityArrayList = new ArrayList<>();
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List priorities = session.createQuery("from Priority").list();
            for (Object priority : priorities) {
                Priority selectedPriority = (Priority) priority;
                priorityArrayList.add(selectedPriority);
                System.out.println(selectedPriority.getCaption());
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return priorityArrayList;
    }
    public ArrayList listStatus(){
        ArrayList<Status> statusArrayList = new ArrayList<>();
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List statuses = session.createQuery("from Status").list();
            for (Object status : statuses) {
                Status selectedStatus = (Status) status;
                statusArrayList.add(selectedStatus);
                System.out.print("id: " + selectedStatus.getStatus()+ " ");
                System.out.println("caption: " + selectedStatus.getCaption());
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return statusArrayList;
    }
    public Ticket ticketById(int id) {
        Transaction tx = null;
        Ticket selectedTicket = null;
        String caption = "";
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List tickets = session.createQuery("from Ticket where id = '" + id + "'").list();
            for (Object ticket : tickets) {
                selectedTicket = (Ticket) ticket;
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return selectedTicket;
    }
    public String statusById(int id){
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

    }private String categoryById(int id){
        Transaction tx = null;
        Category selectedCategory;
        String caption = "";
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List categories = session.createQuery("from Category where id = '" + id + "'").list();
            for (Object category : categories) {
                selectedCategory = (Category) category;
                caption = selectedCategory.getCaption();
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return caption;

    }
    private String groupById(int id){
        Transaction tx = null;
        Group selectedGroup;
        String caption = "";
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List groups = session.createQuery("from Group where id = '" + id + "'").list();
            for (Object group : groups) {
                selectedGroup = (Group) group;
                caption = selectedGroup.getCaption();
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return caption;

    }
    private String priorityById(int id){
        Transaction tx = null;
        Priority selectedPriority;
        String caption = "";
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List statuses = session.createQuery("from Priority where id = '" + id + "'").list();
            for (Object status : statuses) {
                selectedPriority = (Priority) status;
                caption = selectedPriority.getCaption();
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return caption;

    }
    private String usersById(int id){
        Transaction tx = null;
        Users selectedUser;
        String caption = "";
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List users = session.createQuery("from Users where id = '" + id + "'").list();
            for (Object user : users) {
                selectedUser = (Users) user;
                caption = selectedUser.getCaption();
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
