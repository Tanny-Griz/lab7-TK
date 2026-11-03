package week_7.q2_ticket;

import java.util.Date;

public class Ticket implements Comparable<Ticket> {

    private int priority;
    private String reporter;     // Stores person or department who reported problem
    private String description;
    private Date dateReported;
    private final int ticketID;    // The ID for each ticket, which will be unique for each ticket, and can't be changed once it is set.

    // TODO Problem 5: Tickets need to store the resolution Date in a variable called dateResolved
    private Date dateResolved;
    // TODO Problem 5: and a String describing the resolution, in a variable called resolution.
    //  Add two new fields (variables) here.
    //  You do not need to modify the constructor. When a new ticket is created,
    //  the user will not know the data resolved or the resolution.
    private String resolution;
     
    public Ticket(String description, int priority, String reporter, Date dateReported) {
        this.description = description;
        this.priority = priority;
        this.reporter = reporter;
        this.dateReported = dateReported;
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

    public Date getDateResolved() {
        return dateResolved;
    }

    public void setDateResolved(Date dateResolved) {
        this.dateResolved = dateResolved;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
    
    @Override
    public String toString(){
        
        // TODO Problem 5. Modify this method. If the ticket is not resolved, return the String below.
        //  If the ticket is resolved, return a String that includes the same data plus the resolution, and date resolved.
        String ticketInfo = "ID: " + this.ticketID
                + " Description: " + this.description
                + " Priority: " + this.priority
                + " Reported by: " + this.reporter
                + " Reported on: " + this.dateReported;

        if (dateResolved != null) {
            ticketInfo += " Resolution: " + resolution
                    + " Date resolved: " + dateResolved;
        }

        return ticketInfo;
    }

    @Override
    public int compareTo(Ticket otherTicket) {

        // This method is needed to be able to sort a list of Ticket objects
        // Strings, int, double values have an expected sort order,
        // but there's no inherent order to Ticket objects.
        // so, we have to write a method that can tell if one Ticket should be
        // sorted before, or after another ticket.
        // This method is called for a Ticket object, passing in another Ticket object as an argument
        // The method needs to indicate if this Ticket should be before or after the other Ticket object.
        // This method
        //    returns 0 if two tickets are equal in sort order.
        //    a negative number if this ticket should be before otherTicket in a sorted list.
        //    a positive number if this ticket should be after otherTicket in a sorted list.
        // We never call this method directly, but the Collections.sort method calls
        // this method behind the scenes when we ask it to sort a list of Ticket objects.

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

