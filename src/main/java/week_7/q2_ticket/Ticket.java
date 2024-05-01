package week_7.q2_ticket;

import java.util.Date;

public class Ticket implements Comparable<Ticket> {

    private int priority;
    private String reporter;     // Stores person or department who reported problem
    private String description;
    private Date dateReported;
    private final int ticketID;    // The ID for each ticket, which will be unique for each ticket, and can't be changed once it is set.
    
    // TODO Problem 5: Tickets need to store the resolution Date in a variable called dateResolved
    // TODO Problem 5: and a String describing the resolution, in a variable called resolution.
    //  Add two new fields (variables) here.
    //  You do not need to modify the constructor. When a new ticket is created,
    //  the user will not know the data resolved or the resolution.
     
    public Ticket(String desc, int p, String rep, Date date) {
        this.description = desc;
        this.priority = p;
        this.reporter = rep;
        this.dateReported = date;
        this.ticketID = TicketCounter.getNextCounterValue();
    }
    
    
    public int getTicketID() {
        return ticketID;
    }

    // Note there is no setTicketID method to prevent changing an ID once it is set.
    
    protected int getPriority() {
        return priority;
    }
    
    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getReporter() {
        return reporter;
    }
    
    public void setReporter(String reporter) {
        this.reporter = reporter;
    }
    
    public Date getDateReported() {
        return dateReported;
    }
    
    public void setDateReported(Date dateReported) {
        this.dateReported = dateReported;
    }
    
    @Override
    public String toString(){
        
        // TODO Problem 5. Modify this method. If the ticket is not resolved, return the String below.
        //  If the ticket is resolved, return a String that includes the same data plus the resolution, and date resolved.
        return "ID: " + this.ticketID
                + " Description: " + this.description
                + " Priority: " + this.priority
                + " Reported by: " + this.reporter
                + " Reported on: " + this.dateReported;
    }

    @Override
    public int compareTo(Ticket otherTicket) {

        // Joint equal priorities, sort with the oldest first
        if (this.getPriority() == otherTicket.getPriority()){
            return this.getDateReported().compareTo(otherTicket.getDateReported());
        }

        else {
            // Sort with the smallest priority number at the start
            return this.getPriority() - otherTicket.getPriority();
        }
    }
}

