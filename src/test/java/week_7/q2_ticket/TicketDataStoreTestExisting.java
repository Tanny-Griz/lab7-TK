package week_7.q2_ticket;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;

import static org.junit.Assert.*;


public class TicketDataStoreTestExisting {
    
    @Before()
    public void resetStaticTicketVar() {
        TicketCounter.setCounter(1);
    }
    
    
    @Before()
    public void clearTicketStore()  {
        TicketStore store = TicketStore.getInstance();
        store.removeAll();
    }
    
    
    @Test(timeout = 3000)
    public void testAddTicketSorted()  {
        
        TicketStore store = TicketStore.getInstance();
        
        // Test tickets with all different priorities
        Ticket testPr1 = new Ticket("The server is on fire", 1, "reporter", new Date());
        Ticket testPr5 = new Ticket("Mouse mat stolen", 5, "reporter", new Date());
        Ticket testPr3 = new Ticket("Word needs updating", 3, "reporter", new Date());
        
        //Add these tickets. Assert they are added with lowest priority first
        store.add(testPr1);
        store.add(testPr5);
        store.add(testPr3);
        
        LinkedList<Ticket> allTickets = store.getAllTickets();
        
        String msg = "Tickets should be added in priority order, same priority sort by name";
        assertEquals(msg, allTickets.pop(), testPr1);
        assertEquals(msg, allTickets.pop(), testPr3);
        assertEquals(msg, allTickets.pop(), testPr5);
        
    }
    
    
    @Test(timeout = 3000)
    public void testAddTicketSortedByDate() {
        
        // Tickets with joint priorities should be sorted by date
        TicketStore store = TicketStore.getInstance();
        
        Ticket testPr1_newer = new Ticket("The server is down", 1, "reporter", new Date(1500000000000L));
        Ticket testPr1_older = new Ticket("Another server is down", 1, "reporter", new Date(1400000000000L));
        Ticket testPr4 = new Ticket("Several mouse mats stolen", 4, "reporter", new Date());
        
        //Add these tickets. Assert they are added with lowest priority first,
        //and equal priorities sorted with oldest first
        store.add(testPr1_newer); store.add(testPr1_older); store.add(testPr4);
        
        LinkedList<Ticket> allTickets = store.getAllTickets();
        
        String msg = "Tickets should be added in priority order. Tickets with the same priority should be sorted by date";
        assertEquals(msg, testPr1_older, allTickets.pop());
        assertEquals(msg, testPr1_newer, allTickets.pop());
        assertEquals(msg, testPr4, allTickets.pop());
        
    }
    
    
    @Test(timeout = 3000)
    public void testPeekNextTicket() {
        
        TicketStore store = TicketStore.getInstance();
        
        Ticket testPr4 = new Ticket("The server is dusty", 4, "1", new Date());
        Ticket testPr2 = new Ticket("Server keeps rebooting", 2, "2", new Date());
        Ticket testPr3 = new Ticket("Mouse mat stolen", 3, "3", new Date());
        
        //Add these tickets
        store.add(testPr2); store.add(testPr4); store.add(testPr3);
        
        // Most severe should be returned
        assertEquals("The most severe ticket should be returned on a call to peekNextTicket", testPr2, TicketStore.getInstance().peekNextTicket());
        
    }


    @Test(timeout = 3000)
    public void testCountTicketsInQueue() {
        
        TicketStore store = TicketStore.getInstance();
        
        Ticket testPr4 = new Ticket("The server is dusty", 4, "1", new Date());
        Ticket testPr2 = new Ticket("Server keeps rebooting", 2, "2", new Date());
        Ticket testPr3 = new Ticket("Mouse mat stolen", 3, "3", new Date());
        
        //Add these tickets
        store.add(testPr2); store.add(testPr4); store.add(testPr3);
        
        assertEquals("After adding three tickets, there should be 3 tickets in the queue", 3, store.ticketsInQueue());
        
    }
    
    @Test(timeout = 3000)
    public void testGetTicketByIDTicketFound() {
        
        TicketStore store = TicketStore.getInstance();
        
        Ticket test1 = new Ticket("The server is on fire", 1, "1", new Date());    //1
        Ticket test2 = new Ticket("Server keeps rebooting", 2, "2", new Date());  // 2
        Ticket test3 = new Ticket("Mouse mat stolen", 3, "3", new Date());          // 3
        Ticket test4 = new Ticket("Critical security updates", 1, "3", new Date());    //4
        
        store.add(test1); store.add(test2); store.add(test3); store.add(test4);
        
        String msg = "Fetch ticket for ticket ID returns wrong ticket. ID, expected ticket = ";
        
        assertEquals(msg + 4 + test4, test4, store.getTicketById(4));
        assertEquals(msg + 3 + test3, test3, store.getTicketById(3));
        assertEquals(msg + 2 + test2, test2, store.getTicketById(2));
        assertEquals(msg + 1 + test1, test1, store.getTicketById(1));
        
    }
    
    @Test(timeout = 3000)
    public void testGetTicketByIDNotFound() {
        
        TicketStore store = TicketStore.getInstance();
        
        Ticket test1 = new Ticket("The server is on fire", 1, "1", new Date());    //1
        Ticket test2 = new Ticket("Server keeps rebooting", 2, "2", new Date());  // 2
        Ticket test3 = new Ticket("Mouse mat stolen", 3, "3", new Date());          // 3
        Ticket test4 = new Ticket("Critical security updates", 1, "3", new Date());    //4
        
        store.add(test1); store.add(test2); store.add(test3); store.add(test4);
        
        String msgNull = "Searching for a ticket ID that is not in the list should return null";
        
        assertNull(msgNull, store.getTicketById(0));  // not valid
        assertNull(msgNull, store.getTicketById(-2));  // not valid
        assertNull(msgNull, store.getTicketById(200));  // doesn't exist valid
        
    }
    
    
    @Test(timeout = 3000)
    public void testDeleteTicketByID() {
        
        TicketStore store = TicketStore.getInstance();
        
        Ticket test1 = new Ticket("The server is on fire", 1, "1", new Date());    //1
        Ticket test2 = new Ticket("Server keeps rebooting", 2, "2", new Date());  // 2
        Ticket test3 = new Ticket("Mouse mat stolen", 3, "3", new Date());          // 3
        Ticket test4 = new Ticket("Critical security updates", 1, "3", new Date());    //4
        
        store.add(test1); store.add(test2); store.add(test3); store.add(test4);
        
        store.deleteTicketById(3);
        
        LinkedList<Ticket> allTickets = store.getAllTickets();
        
        String msg = "Delete one ticket from list, list should be one ticket shorter, remaining tickets still present";
        assertEquals(msg, allTickets.size(), 3);
        assertFalse(msg, allTickets.contains(test3));   // 3 has been deleted
        
        assertTrue(msg, allTickets.contains(test1));
        assertTrue(msg, allTickets.contains(test2));
        assertTrue(msg, allTickets.contains(test4));  // Others still present
        
        // Delete non-existent ticket
        store.deleteTicketById(3);
        assertEquals("After deleting non-existent ticket, list does not change", allTickets.size(), 3);   // List not changed
        
        store.deleteTicketById(-1);
        assertEquals("After deleting non-existent ticket, list does not change", allTickets.size(), 3);   // List not changed
        
        // Delete another ticket
        
        store.deleteTicketById(1);
        assertEquals(msg, allTickets.size(), 2);   // one less ticket
        assertFalse(msg, allTickets.contains(test1));   // 1 has been deleted
        
        assertTrue(msg, allTickets.contains(test2));
        assertTrue(msg, allTickets.contains(test4));  // Others still present
        
        
        // Delete last two, should not be errors
        
        store.deleteTicketById(2);
        store.deleteTicketById(4);
        assertEquals(msg, allTickets.size(), 0);   // empty
        
        // Delete non-existent ticket
        
        store.deleteTicketById(1);   // no errors
        
    }
    
    
}