package week_7.q2_ticket;

import java.util.LinkedList;

/**
 * This code controls the program. It shows the menu, asks user for their choice,
 * and carries out that action.
 * It delegates tasks to methods in TicketUI, TicketStore and ResolvedTicketStore.
 *
 * The instruction are in the Lab 7 Questions.md file  */

public class TicketManager {

    public static void main(String[] args) {
        new TicketManager().manage();
    }

    // Global objects - the data stores, and the user interface object
    // you can access these from any method in this class
    private TicketUI ticketUI = new TicketUI();  // delegate all user interaction to the ticketUI object
    private TicketStore ticketStore = TicketStore.getInstance(); // stores all the open tickets
    
    // TODO Problem 6: create a ResolvedTicketStore object here


    void manage() {
        
        boolean wantToQuit = false;

        while (!wantToQuit) {

            int userChoice = ticketUI.showMenuGetChoice();

            // TODO Problem 3: add the two new options here statement

            if (userChoice == TicketUI.ADD_TICKET) {
                menuOptionAddTicket();
            } else if (userChoice == TicketUI.SEARCH_BY_ID) {
                menuOptionSearchById();
            } else if (userChoice == TicketUI.DELETE_BY_TICKET_ID) {
                menuOptionDeleteById();
            } else if (userChoice == TicketUI.SHOW_NEXT_TICKET) {
                menuOptionShowNextTicket();
            } else if (userChoice == TicketUI.SHOW_ALL_TICKETS) {
                menuOptionDisplayAllTickets();
            } else if (userChoice == TicketUI.QUIT) {
                wantToQuit = true;
            }
        }
    }
    
    
    private void menuOptionAddTicket() {
        
        // Get ticket data. Use ticketUI method to ask for data.
        Ticket newTicket = ticketUI.getNewTicketInfo();
        
        // Add to the ticket store
        ticketStore.add(newTicket);
        ticketUI.userMessage("Ticket added to the ticket queue");
    }


    /* Ask for a ticket ID, request store searches for a ticket with that ID, displays the
    * ticket if one is found, or a message if the ticket with that ID is not found. */
    protected void menuOptionSearchById() {

        int ticketID = ticketUI.getTicketID();
        Ticket ticket = ticketStore.getTicketById(ticketID);
        if (ticket == null) {
            ticketUI.userMessage("No ticket found with that ID");
        } else {
            ticketUI.displayTicket(ticket);
        }
    }


    /* Ask for ticket ID, call deleteTicketByID to find ticket, confirm delete operation, and delete */
    protected void menuOptionDeleteById() {
        int ticketID = ticketUI.getTicketID();
        deleteTicketById(ticketID);
    }


    /* Display next ticket in the queue - the highest priority */
    protected void menuOptionShowNextTicket() {
        Ticket next = ticketStore.peekNextTicket();
        ticketUI.displayTicket(next);
    }


    /* Show all tickets, in priority order */
    protected void menuOptionDisplayAllTickets() {
        LinkedList<Ticket> allTickets = ticketStore.getAllTickets();
        ticketUI.displayTickets(allTickets);
    }

    
    /* Ask user for a search term, display all tickets matching that term */
    protected void menuOptionSearchByDescription() {

        // TODO Problem 3: implement this method.

        // Use the TicketUI askUserQuestion method to ask user for search term e.g. "server" or "powerpoint"
        // Finish the searchByDescription method in TicketStore to get list of matching Tickets for a search term;
        //      this method should return a list of all tickets which contain the user's
        //      search term in their description
        // Use TicketUI displayTickets method to print the list of matching tickets

    }


    /* Ask user for a search term, display all matching tickets, ask for ID of ticket to delete */
    protected void menuOptionDeleteTicketByDescription() {

        // TODO Problem 7: implement this method.
        // Ask user for search term e.g. "server". Use TicketUI's askUserQuestion method to ask the question.

        // If there are matching tickets, display a list of these tickets.

        // Ask the user TicketUI to ask user which ticket ID to delete, or to enter -1 to cancel.

        // If the user enters a ticket ID, then call the deleteTicketById(ticketID) method
        // in this class to delete the ticket.

        // If the user doesn't wish to delete any tickets, they can enter -1 to cancel.
        // If the user enters -1 then don't delete any tickets.

        // else, use TicketUI to show user 'not found' message

    }

    
    /* This method will be called by menuOptionDeleteTicketByDescription and menuOptionDeleteById */
    protected void deleteTicketById(int ticketID) {

        // Fetch the ticket
        Ticket toDelete = ticketStore.getTicketById(ticketID);

        // Check if a ticket with this ID is in the store
        if (toDelete == null) {
            ticketUI.userMessage("Ticket not found in the ticket store.");
        } else {
            // Display ticket info, and verify delete operation
            ticketUI.displayTicket(toDelete);
            if (ticketUI.areYouSure("Delete the above ticket, are you sure?")) {

                /// TODO Problem 6: use TicketUI to get the resolution for this Ticket
                // Save the resolution and the current date in this Ticket
                // add it to the ResolvedTicketStore object.

                // Delete ticket from store.
                ticketStore.deleteTicketById(ticketID);

                // Use ticketUI to display a delete confirmation message.
            } else {
                ticketUI.userMessage("Cancelled - ticket not deleted.");
            }
        }
    }

}
