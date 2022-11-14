package week_7.q2_ticket;

import java.util.*;

import static input.InputUtils.*;

/** User interaction - user input and printing - for the ticket manager  */

public class TicketUI {
    
    // Constants used to display menu options, and figure out what user entered.

    // TODO Problem 3: add constants for the two new options given in the instructions.
    //  Make sure you use int values that haven't been used for other menu options.
    
    static final int ADD_TICKET_MENU_OPTION_NUMBER = 1;
    
    static final int SEARCH_BY_ID_MENU_OPTION_NUMBER = 2;
    static final int DELETE_BY_TICKET_ID_MENU_OPTION_NUMBER = 3;
    static final int SHOW_NEXT_TICKET_MENU_OPTION_NUMBER = 4;
    static final int SHOW_ALL_TICKETS_MENU_OPTION_NUMBER = 5;

    static final int QUIT_MENU_OPTION_NUMBER = 99;
    
    private Map<Integer, String> options = new TreeMap<>();


    /** This constructor sets up the map of options for the menu.
     * The keys are numbers and correspond to number the use will enter to make a choice
     * We are using constants that store the numbers 1, 2, 3 ... instead of numbers 1, 2, 3...
     * for two reasons.
     *
     * Reason 1: it's easier to see the meaning of a constant called ADD_TICKET_MENU_OPTION_NUMBER
     * than the number 1, especially in other parts of the code. Note that TicketManager
     * has a block of code that starts like this,
     *
     * if (userChoice == TicketUI.ADD_TICKET_MENU_OPTION_NUMBER) {
     *    menuOptionAddTicket();
     * } else if (userChoice == TicketUI.SEARCH_BY_ID_MENU_OPTION_NUMBER) {
     *    menuOptionSearchById();
     * } else if (userChoice == TicketUI.DELETE_BY_TICKET_ID_MENU_OPTION_NUMBER) {
     *          .... etc...
     *
     * Which is much clearer than
     *
     * if (userChoice == 1) {   // now we have to remember what 1 means
     *     menuOptionAddTicket();
     * } else if (userChoice == 2) {
     *     menuOptionSearchById();
     * } else if (userChoice == 3) {
     *            .... etc...
     *
     * Because we have to remember what 1 means, and what 2 means, and what 3 means...
     * So, using a named constant or variable that contains a value, instead of
     * using the value directly, makes code more readable.
     *
     * Reason 2: If we want to change the numeric values of the options, then we only need to
     * change the value of the constants in code in this file, and we don't need to modify
     * TicketManager, since it uses the value of those constants. Our code is easier to modify.
     *
     * The values are a text description of that menu item. */
    TicketUI() {
        options.put(ADD_TICKET_MENU_OPTION_NUMBER, "Add new ticket");
        options.put(SEARCH_BY_ID_MENU_OPTION_NUMBER, "Search by ticket ID");
        options.put(DELETE_BY_TICKET_ID_MENU_OPTION_NUMBER, "Delete by ticket ID");
        options.put(SHOW_NEXT_TICKET_MENU_OPTION_NUMBER, "Show next ticket in ticket queue");
        options.put(SHOW_ALL_TICKETS_MENU_OPTION_NUMBER, "Show all open tickets");

        // TODO Problem 3: add the new options to the options HashMap.

        options.put(QUIT_MENU_OPTION_NUMBER, "Quit the program");
        
    }

    /** Prints a menu using the options Map and asks the user for their
     * choice. Validates that the choice made is one of the available choices from the Map.
     */
    protected int showMenuGetChoice() {
        
        while (true) {
            
            for (int option : options.keySet()) {
                System.out.printf("%d: %s\n", option, options.get(option));
            }
            
            int userSelection = intInput("Enter your selection");
            
            // If the user's option is in the map's key set, it's a valid choice. Return it.
            if (options.containsKey(userSelection)) {
                return userSelection;
            }
            
            // Otherwise, loop until user enters valid choice from the menu
        }
    }
    
    /**
     * Ask user for the description, reporter, and priority of a new ticket.
     * The date created is assumed to be the time this code runs
     * @return a new Ticket object with the data entered by the user and the current date. */
    protected Ticket getNewTicketInfo() {
        
        Date dateReported = new Date(); // Default Date constructor creates Date with current date & time
        
        String description = stringInput("Enter description of the problem: ");
        String reporter = stringInput("Who reported this problem? ");
        
        // TODO Problem 2: modify this section of this method to ensure the user
        //  enters a priority between 1 and 5.
        //  Loop until user enters a valid priority.
        //  Use the intInput method to ask for input, not positiveIntInput.
        int priority = intInput("Enter priority of " + description);
        
        return new Ticket(description, priority, reporter, dateReported);
    }
    
    /* Methods for getting information from the user */

    /** Ask user for a ticket ID, ensure it is an integer
     *
     * @return the ticket ID entered.
     */
    protected int getTicketID() {
        return intInput("Enter Ticket ID: ");
    }


    /** Print a question or a message, and ask the user to enter a text response
     * @param question the text to print
     * @return the String the user enters in response
     */
    protected String askUserQuestion(String question) {
        return stringInput(question);
    }


    /** Print a question or a message, and ask the user to enter 'yes' or 'no'.
     * @param question the text to print
     * @return a boolean true if the user enters 'yes' or false if the user enters 'no'
     */
    protected boolean areYouSure(String question) {
        return yesNoInput(question);
    }
    
    
    /* Methods for displaying information to the user */

    /** Displays a message to the user. */
    public void userMessage(String s) {
        System.out.println(s);
    }


    /** If there are tickets in the ticket list, print each Ticket, one per line.
     * If the ticket list is empty, print a "no tickets" message.
     *
      * @param tickets a list of Ticket objects. The list may be empty, but may not be null.
     */
    protected void displayTickets(List<Ticket> tickets) {

        if (tickets.isEmpty()) {
            System.out.println(" ******** No tickets ******** ");
        } else {
            System.out.println(" --------- All tickets --------- ");

            for (Ticket t : tickets) {
                displayTicket(t);
            }

            System.out.println(" --------- End of ticket list --------- ");
        }
    }
    

    /** Prints information about one Ticket.
     * @param ticket the ticket to print. */
    public void displayTicket(Ticket ticket) {
        System.out.println(ticket);
    }
    
}
