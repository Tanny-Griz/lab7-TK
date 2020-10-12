package week_7.q2_ticket;

/**
 * This class keeps track of the Ticket ID numbers
 * Each Ticket gets its own unique ID, starting from 1 and counting up
 *
 * You don't need to modify this class
 */

public class TicketCounter {

    private static int counter = 1;
    
    public static int getNextCounterValue() {
        return counter++;
    }
    
    public static void setCounter(int value) {
        counter = value;
    }
}
