package week_7.q2_ticket;

import java.util.LinkedList;


/**
 *
 * Storage for Resolved Ticket objects.
 * Implemented as a Singleton - there can only ever be one ResolvedTicketStore.
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
    
    public LinkedList<Ticket> getAll() {
        return resolvedTickets;
    }
    
}
