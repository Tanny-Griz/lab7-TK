package week_7.q2_ticket;

import input.InputUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import test_utils.PrintUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;
import static org.powermock.api.easymock.PowerMock.mockStatic;

@RunWith(PowerMockRunner.class)  // Needed for PowerMock to record method calls
@PrepareForTest({InputUtils.class, TicketManager.class})   // To enable InputUtils to be mocked
public class TicketManagerTestExisting {
    
    private TicketStore store = TicketStore.getInstance();
    private ResolvedTicketStore resolvedTicketStore = ResolvedTicketStore.getInstance();
    
    private String testDirectory = "TestTicketData";
    
    @Before
    public void resetStaticTicketVar() throws Exception {
        TicketCounter.setCounter(1);
    }
    
    
    @Before
    public void clearTicketStore() throws Exception {
        store.removeAll();
        resolvedTicketStore.getAll().clear();
    }
    
    
    
    @Test(timeout=3000)
    public void e2eAddNewTicket() {
        
        mockStatic(InputUtils.class);
        
        // Select 1 from menu
        expect(InputUtils.intInput(anyString())).andReturn(TicketUI.ADD_TICKET);
        // Provide valid priority, description, reporter
        expect(InputUtils.intInput(anyString())).andReturn(4);
        expect(InputUtils.positiveIntInput(anyString())).andReturn(4);
        expect(InputUtils.stringInput(anyString())).andReturn("Server down");
        expect(InputUtils.stringInput(anyString())).andReturn("Reporter");
        
        expect(InputUtils.intInput(anyString())).andReturn(TicketUI.QUIT);
        
        PowerMock.replay(InputUtils.class);
        
        TicketManager q3 = new TicketManager();
        q3.manage();
        
        System.out.println(store.getAllTickets());
        
        // Assert ticket exists in list
        
        Ticket added = store.getTicketById(1);
        
        assertNotNull(added);

        String msg = "New ticket does not contain the correct data - ensure you use user input and set the ticket ID";
        assertEquals(msg, 1, added.getTicketID());
        assertEquals(msg, "Server down", added.getDescription());
        assertEquals(msg, "Reporter", added.getReporter());
        assertEquals("Ticket reported date should be the current date", new Date().getTime(), added.getDateReported().getTime(), 2000);
    }
    
    
    @Test(timeout=3000)
    public void e2eSearchId() {
        
        
        Ticket test = new Ticket("Mouse missing", 3, "Cubicle 123", new Date());
        TicketStore.getInstance().add(test);
        
        mockStatic(InputUtils.class);
        
        // Select 1 from menu
        expect(InputUtils.intInput(anyString())).andReturn(TicketUI.SEARCH_BY_ID);
        // Provide valid priority, description, reporter
        expect(InputUtils.intInput(anyString())).andReturn(1);  // search for ID 1
        expect(InputUtils.positiveIntInput(anyString())).andReturn(1);
        
        expect(InputUtils.intInput(anyString())).andReturn(TicketUI.QUIT);
        
        PowerMock.replay(InputUtils.class);
        
        PrintUtils.catchStandardOut();
        TicketManager q3 = new TicketManager();
        q3.manage();
        String out = PrintUtils.resetStandardOut();
        
        assertTrue(out.contains("3"));
        assertTrue(out.contains("Mouse missing"));
        assertTrue(out.contains("Cubicle 123"));
        
    }
    
    
    @Test(timeout=3000)
    public void e2eDeleteTicketByIdDontConfirm() {
        
        TicketStore.getInstance().removeAll();
        
        Ticket test = new Ticket("Mouse missing", 3, "Cubicle 123", new Date());
        store.add(test);
        
        mockStatic(InputUtils.class);
        
        // Select 1 from menu
        expect(InputUtils.intInput(anyString())).andReturn(TicketUI.DELETE_BY_TICKET_ID);
        // Provide valid priority, description, reporter
        expect(InputUtils.intInput(anyString())).andReturn(1);  // search for ID 1
        expect(InputUtils.positiveIntInput(anyString())).andReturn(1);
        
        expect(InputUtils.yesNoInput(anyString())).andReturn(false);
        
        expect(InputUtils.intInput(anyString())).andReturn(TicketUI.QUIT);
        
        PowerMock.replay(InputUtils.class);
        
        TicketManager q3 = new TicketManager();
        q3.manage();

        String msg = "If user cancels delete operation, do not modify the ticket store or the resolved ticket list";
        assertEquals(msg, 1, store.getAllTickets().size());
        assertEquals(msg, test, store.getTicketById(1));
        
    }
    
    
    @Test(timeout=3000)
    public void e2eShowNextTicket() {
        
        Ticket test = new Ticket("Mouse missing", 3, "Cubicle 123", new Date());
        store.add(test);
        
        mockStatic(InputUtils.class);
        expect(InputUtils.intInput(anyString())).andReturn(TicketUI.SHOW_NEXT_TICKET);
        expect(InputUtils.intInput(anyString())).andReturn(TicketUI.QUIT);
        PowerMock.replay(InputUtils.class);
        
        PrintUtils.catchStandardOut();
        TicketManager q3 = new TicketManager();
        q3.manage();
        String out = PrintUtils.resetStandardOut();
        
        assertTrue(out.contains(test.toString()));
        
    }
    
    
    @Test(timeout=3000)
    public void e2eShowAllTickets() {
        
        Ticket test1 = new Ticket("Mouse missing", 3, "Cubicle 123", new Date());
        Ticket test2 = new Ticket("Server down", 1, "Admin", new Date());
        Ticket test3 = new Ticket("Word is broken", 5, "User", new Date());
        store.add(test1);
        store.add(test2);
        store.add(test3);
        
        mockStatic(InputUtils.class);
        expect(InputUtils.intInput(anyString())).andReturn(TicketUI.SHOW_ALL_TICKETS);
        expect(InputUtils.intInput(anyString())).andReturn(TicketUI.QUIT);
        PowerMock.replay(InputUtils.class);
        
        PrintUtils.catchStandardOut();
        TicketManager q3 = new TicketManager();
        q3.manage();
        String out = PrintUtils.resetStandardOut();
        
        // Expect test2, then test1, then test3
        int locTest1 = out.indexOf(test1.toString());
        int locTest2 = out.indexOf(test2.toString());
        int locTest3 = out.indexOf(test3.toString());
        
        assertTrue(locTest1 >= 0 && locTest2 >= 0 && locTest3 >= 0);
        assertTrue(locTest2 < locTest1);
        assertTrue(locTest1 < locTest3);
        
    }
    
    
    
    @Test(timeout=3000)
    public void e2eQuit() {
        
        mockStatic(InputUtils.class);
        expect(InputUtils.intInput(anyString())).andReturn(TicketUI.QUIT);
        PowerMock.replay(InputUtils.class);
        
        TicketManager q3 = new TicketManager();
        q3.manage();
    }
    
}
