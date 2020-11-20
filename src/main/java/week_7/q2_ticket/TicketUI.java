package week_7.q2_ticket;

import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import static input.InputUtils.*;

/** User interaction - user input and printing - for the ticket manager  */

public class TicketUI {
    
    // Constants used to display menu options, and figure out what user entered.
    // TODO Problem 3: add constants for the two new options. Make sure you use new
    //  numeric values for your new constants.
    
    static final int ADD_TICKET = 1;
    
    static final int SEARCH_BY_ID = 2;
    static final int DELETE_BY_TICKET_ID = 3;
    static final int SHOW_NEXT_TICKET = 4;
    static final int SHOW_ALL_TICKETS = 5;

    static final int QUIT = 99;
    
    private Map<Integer, String> options = new TreeMap<>();
    
    TicketUI() {
        
        options.put(ADD_TICKET, "Add new ticket");
        options.put(SEARCH_BY_ID, "Search by ticket ID");
        options.put(DELETE_BY_TICKET_ID, "Delete by ticket ID");
        options.put(SHOW_NEXT_TICKET, "Show next ticket in ticket queue");
        options.put(SHOW_ALL_TICKETS, "Show all open tickets");
    
        options.put(QUIT, "Save and quit");
        
    }
    
    
    protected int showMenuGetChoice() {
        
        while (true) {
            
            for (int option : options.keySet()) {
                System.out.printf("%d: %s\n", option, options.get(option));
            }
            
            int task = intInput("Enter your selection");
            
            // If the user's option is in the map's key set, it's a valid choice. Return it.
            if (options.containsKey(task)) {
                return task;
            }
            
            // Otherwise, loop until user enters valid choice from the menu
        }
    }
    
    
    protected Ticket getNewTicketInfo() {
        
        Date dateReported = new Date(); // Default Date constructor creates Date with current date/time
        
        String description = stringInput("Enter description of the problem: ");
        String reporter = stringInput("Who reported this problem? ");
        
        // TODO Problem 2: ensure the priority is between 1 and 5. Loop until user enters a valid priority.
        int priority = intInput("Enter priority of " + description);
        
        return new Ticket(description, priority, reporter, dateReported);
    }
    
    /* Methods for getting information from the user */
    
    protected int getTicketID() {
        return intInput("Enter Ticket ID: ");
    }
    
    
    protected String askUserQuestion(String question) {
        return stringInput(question);
    }
    
    
    protected boolean areYouSure(String question) {
        return yesNoInput(question);
    }
    
    
    /* Methods for displaying information to the user */
    
    public void userMessage(String s) {
        System.out.println(s);
    }
    
    
    protected void displayTickets(LinkedList<Ticket> tickets) {

        if (tickets.isEmpty()) {
            System.out.println(" ******** No tickets ******** ");
        } else {
            System.out.println(" --------- All tickets --------- ");

            for (Ticket t : tickets) {
                System.out.println(t); // This calls the toString method for the Ticket object.
            }

            System.out.println(" --------- End of ticket list --------- ");
        }
    }
    
    
    public void displayTicket(Ticket ticket) {
        System.out.println(ticket);
    }
    
}
