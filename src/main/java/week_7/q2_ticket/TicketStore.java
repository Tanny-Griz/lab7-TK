package week_7.q2_ticket;

import java.util.*;


/**
 * A data structure to store Tickets in memory as the program runs
 * Stores tickets in priority order, so tickets with priority 5 are at the start
 * If more than one ticket with same priority, oldest tickets are before newer tickets
 * Supports add, delete, search operations on the list of Tickets.
 *
 * Implemented as a Singleton - there can only ever be one TicketStore.
 * If you want to work with the TicketStore, call TicketStore.getInstance()
 *
 */


public class TicketStore {
    
    private LinkedList<Ticket> ticketList;
    
    private static TicketStore instance;
    
    private TicketStore() {
        ticketList = new LinkedList<>();
    }
    
    public static TicketStore getInstance() {
        if (instance == null) {
            instance = new TicketStore();
        }
        return instance;
    }
    
    /** Add all the tickets in a list to the Ticket Queue, in priority order.
     * @param tickets a list of tickets. */
    public void addAll(LinkedList<Ticket> tickets) {
        ticketList.addAll(tickets);
        Collections.sort(ticketList);
    }
    
    
    /** Add ticket, and then sort list to keep the highest-priority at the top of the queue.
     * @param newTicket the new Ticket to add. */
    public void add(Ticket newTicket) {
        ticketList.add(newTicket);
        Collections.sort(ticketList);
    }
    
    
    /** Returns all tickets in the queue.
     * @return All the tickets.  */
    public LinkedList<Ticket> getAllTickets() {
        return ticketList;
    }
    
    
    /** Returns, but does not remove, the tickets from the top of the TicketQueue
     * @return the ticket at the top of the queue */
    public Ticket peekNextTicket() {
        return ticketList.peek();
    }
    
    
    /** How many currently open tickets in the list?
     * @return the number of open tickets */
    public int ticketsInQueue() {
        return ticketList.size();
    }

    
    /** Searches store for ticket with given ID.
     * @param id The ticket ID
     * @return The ticket with this ID, if found; null otherwise */
    public Ticket getTicketById(int id) {
        for (Ticket ticket: ticketList) {
            if (ticket.getTicketID() == id) {
                return ticket;
            }
        }
        
        return null; // If ticket with this ID is not found
    }
    
    
    /** Delete a single ticket by ticket ID.
     *  @return true if a ticket was found and deleted, false if a ticket with this ID is not in the queue */
    public boolean deleteTicketById(int deleteID) {
        
        // Loop over all tickets. Delete the first one found with this ticket ID
        for (Ticket ticket : ticketList) {
            if (ticket.getTicketID() == deleteID) {
                ticketList.remove(ticket);
                return true;
            }
        }
        
        // Not found? Return false
        return false;
    }

    /** Create a list of tickets, with a description containing
     * the given String. The search is not case-sensitive.
     * @param searchDescription Text to search for in Ticket descriptions
     * @return a list of matching Tickets. If no matches, return an empty list.
     */
    public List<Ticket> searchByDescription(String searchDescription) {
        //TODO Problem 4: Search by description. implement this method.

        // This method should search the ticketList and return a new list of Tickets whose
        // descriptions contain a certain String.
        // For example, you might want to search for all tickets with the word “server” in the description.
        //
        // The search should NOT be case-sensitive.
        //
        // You should not modify the description when you save tickets.
        // So, the approach of saving all descriptions in lowercase or uppercase is not an acceptable solution for this problem.
        //
        // If this method does not find any matching tickets, return an empty list.
        // If this method is called with an empty string, or a null string, it should return an empty list.

        return null;
    }
    
    
    /** Remove all tickets from the Ticket queue */
    public void removeAll() {
        ticketList.clear();
    }

}
