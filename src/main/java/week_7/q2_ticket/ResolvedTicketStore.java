package week_7.q2_ticket;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * Storage for Resolved Ticket objects.
 * Implemented as a "Singleton" - there can only ever be one ResolvedTicketStore.
 * This avoids the possibility of creating multiple stores, and then
 * If there is only one ResolvedTicketStore, then all the the tickets must be in this
 * single store.  There's no way to have multiple stores and multiple places where tickets might be.
 *
 * Singleton is a design pattern, a way of doing things in code. More info:
 * https://www.geeksforgeeks.org/singleton-class-java/
 *
 * If you want to work with the ResolvedTicketStore, call ResolvedTicketStore.getInstance()
 *
 * You don't need to modify this class.
 *
 * You will need to call the addTicket method.
* */


public class ResolvedTicketStore {
    
    private static LinkedList<Ticket> resolvedTickets;
    
    private static ResolvedTicketStore instance;
    
    private ResolvedTicketStore() {
        resolvedTickets = new LinkedList<>();
    }
    
    public static ResolvedTicketStore getInstance(){
        if (instance == null) {
            instance = new ResolvedTicketStore();
        }
        return instance;
    }
    
    public void addTicket(Ticket t) {
        resolvedTickets.add(t);
    }
    
    public List<Ticket> getAll() {
        return resolvedTickets;
    }
    
}
