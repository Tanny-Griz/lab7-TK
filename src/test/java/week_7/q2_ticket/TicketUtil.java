package week_7.q2_ticket;

/**
 * Created by clara on 1/27/18.
 */
public class TicketUtil {
    
    static boolean sameOpenTicket(Ticket t1, Ticket t2) {
        // Could override .equals in the Ticket class, but not guaranteed that student will implement extra fields
        // Overriding .equals requires hashcode to be overridden too, and that's out of scope for this problem
        
        // Dates have to be within one second of each other.
        
        if (t1.getTicketID() != t2.getTicketID()) { return false; }
        if (  Math.abs(t1.getDateReported().getTime() - t2.getDateReported().getTime()) > 1001 )  { return false; }
        if (!(t1.getDescription().equals(t2.getDescription())))  { return false; }
        if (!(t1.getReporter().equals(t2.getReporter())))  { return false; }
        
        return true;
    }
    
}
