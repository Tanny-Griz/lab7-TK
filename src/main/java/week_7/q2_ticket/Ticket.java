package week_7.q2_ticket;

import java.util.Date;

public class Ticket implements Comparable<Ticket> {

    private int priority;
    private String reporter; //Stores person or department who reported problem
    private String description;
    private Date dateReported;
    private int ticketID;     //The ID for each ticket - an instance variable. Each Ticket will have it's own ticketID variable
    
    // TODO Problem 5: tickets need to store the resolution Date in a variable called dateResolved
    // TODO Problem 5: and a String describing the resolution
     
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
    
    protected int getPriority() {
        return priority;
    }
    
    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public String getDescription() { return description; }
    
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
    
    public String toString(){
        
        // TODO modify this method. If the ticket is not resolved, return the String below.
        //  If the ticket is resolved, return a String that includes the same data plus the resolution, and date resolved.
        return("ID: " + this.ticketID
                + " Description: " + this.description
                + " Priority: " + this.priority
                + " Reported by: " + this.reporter
                + " Reported on: " + this.dateReported);
    }

    @Override
    public int compareTo(Ticket otherTicket) {

        //Joint equal priorities, sort with oldest first
        if (this.getPriority() == otherTicket.getPriority()){
            return this.getDateReported().compareTo(otherTicket.getDateReported());
        }

        else {
            // Sort with smallest priority number at the start
            return this.getPriority() - otherTicket.getPriority();
        }
    }
}

