package week_7.q2_ticket;

/**
 * This class keeps track of the Ticket ID numbers
 * Each Ticket gets its own unique ID, starting from 1 and counting up
 *
 * You don't need to modify this class
 */

public class TicketCounter {

    // Counter is static, so all TicketCounter objects share the same counter value.
    // If any TicketCounter object calls getNextCounterValue, the current value of the
    // counter is returned, and the counter is increased by 1.
    // The first time getNextCounterValue is called, it returns 1. Using ++ after a variable
    // is known as 'postfix incrementing' which means that the value is read, and then it's increased.
    // You can also  use 'prefix incrementing' and write ++counter which means
    // increase the value, then read the value. If we did that here then the first
    // counter value would be 2.

    private static int counter = 1;
    public static int getNextCounterValue() {
        return counter++;
    }
    public static void setCounter(int value) {
        counter = value;
    }
}
