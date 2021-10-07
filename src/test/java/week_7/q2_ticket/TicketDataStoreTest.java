package week_7.q2_ticket;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;


public class TicketDataStoreTest {
    
    @Before()
    public void resetStaticTicketVar() throws Exception {
        TicketCounter.setCounter(1);
    }
    
    
    @Before()
    public void clearTicketStore() throws Exception {
        TicketStore store = TicketStore.getInstance();
        store.removeAll();
    }
    

    @Test(timeout=3000)
    public void testSearchDescriptionEmptyStore() throws Exception {
        TicketStore store = TicketStore.getInstance();
        
        // Any searches on an empty list should return an empty list, not a null list
        assertEquals(0, store.searchByDescription("office").size());
        assertEquals(0, store.searchByDescription("").size());
        assertEquals(0, store.searchByDescription(null).size());

    }
        
        
    @Test(timeout=3000)
    public void testSearchDescriptionExpectedFound() throws Exception {
    
        TicketStore store = TicketStore.getInstance();
        
        Ticket test1 = new Ticket("The server is down", 1, "1", new Date());
        Ticket test2 = new Ticket("Server keeps rebooting", 2, "2", new Date());
        Ticket test3 = new Ticket("Mouse mat stolen", 3, "3", new Date());
        Ticket test4 = new Ticket("Critical security updates", 1, "3", new Date());
    
        // Add these tickets
        
        store.add(test1); store.add(test2); store.add(test3); store.add(test4);
    
        System.out.println("This test uses the following example tickets\n" + "\n" + test1 + "\n" + test2 + "\n" + test3 + "\n" + test4);
        
        // Search for 'server'. Should not be case sensitive, return test1 and test2
        
        List<Ticket> results = store.searchByDescription("Server");
        String msg = "Return a LinkedList of the 2 Tickets whose description contains the search text 'Server'. If no matches, return an empty list. Your search should not be case sensitive";
        assertNotNull(msg, results);
        assertEquals(msg, results.size(), 2);   // 2 results
        assertTrue(msg, results.contains(test1));
        assertTrue(msg, results.contains(test2));
        assertFalse(msg, results.contains(test3));
        assertFalse(msg, results.contains(test4));
        
        results = store.searchByDescription("SeCuRiTy UpDaTeS");
        
        msg = "Return a LinkedList of 1 Ticket whose description contains the search text 'security updates'. Your search should not be case sensitive";
        assertNotNull(msg, results);
        assertEquals(msg, results.size(), 1);   // just one ticket
        assertTrue(msg, results.contains(test4));
    
    
        results = store.searchByDescription("at stol");   // matches "Mouse mat stolen"
    
        msg = "Return a LinkedList of 1 Ticket whose description contains the search text 'at stol'. Your search should not be case sensitive";
        assertNotNull(msg, results);
        assertEquals(msg, results.size(), 1);   // just one ticket
        assertTrue(msg, results.contains(test3));
        
    }
    
    
    @Test(timeout=3000)
    public void testSearchDescriptionNotFound() throws Exception {
        
        TicketStore store = TicketStore.getInstance();
        
        Ticket test1 = new Ticket("The server is down", 1, "1", new Date());
        Ticket test2 = new Ticket("Server keeps rebooting", 2, "2", new Date());
        Ticket test3 = new Ticket("Mouse mat stolen", 3, "3", new Date());
        Ticket test4 = new Ticket("Critical security updates", 1, "3", new Date());
        
        //Add these tickets
        
        store.add(test1); store.add(test2); store.add(test3); store.add(test4);
        
        System.out.println("This test uses the following example tickets\n" + "\n" + test1 + "\n" + test2 + "\n" + test3 + "\n" + test4);
        
        // Search for something not in the list
        List<Ticket> results = store.searchByDescription("Powerpoint");
        String msg = "Search for 'Powerpoint' should return a LinkedList of results. If no matches, return an empty list.";
        assertNotNull(msg, results);
        assertEquals(msg, 0, results.size());   // No results
        
        // Empty string - should return empty list
        results = store.searchByDescription("");
        assertNotNull("A search for an empty string should return an empty list", results);
        assertEquals("A search for an empty string should return an empty list", 0, results.size());   // No results
    
        
        // Null string - should return empty list
        results = store.searchByDescription(null);
        assertNotNull("A search for null string should return an empty list", results);
        assertEquals("A search for null string should return an empty list", 0, results.size());   // No results
        
    }
    
    

    
}