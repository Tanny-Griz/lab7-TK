package week_7.q2_ticket;

import input.InputUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import test_utils.PrintUtils;

import java.util.Date;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

public class TicketManagerTestExisting {

    private TicketStore store = TicketStore.getInstance();
    private ResolvedTicketStore resolvedTicketStore = ResolvedTicketStore.getInstance();

    @Before
    public void resetStaticTicketVar()  {
        TicketCounter.setCounter(1);
    }


    @Before
    public void clearTicketStore() {
        store.removeAll();
        resolvedTicketStore.getAll().clear();
    }


    @Test(timeout = 3000)
    public void e2eAddNewTicket() {

        try (MockedStatic<InputUtils> mockInputUtil = Mockito.mockStatic(InputUtils.class)) {

            mockInputUtil.when(() -> InputUtils.intInput(anyString()))
                    .thenReturn(TicketUI.ADD_TICKET)      // select 1 from menu
                    // Provide valid priority, description, reporter - see next setup
                    .thenReturn(4)  // priority 4
                    .thenReturn(TicketUI.QUIT)  // after reporter and description entered, quit program
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            mockInputUtil.when(() -> InputUtils.stringInput(anyString()))
                    .thenReturn("Server down")
                    .thenReturn("Reporter")
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            TicketManager q3 = new TicketManager();
            q3.manage();

            // Assert ticket exists in list
            Ticket added = store.getTicketById(1);

            assertNotNull(added);

            String msg = "New ticket does not contain the correct data - ensure you use user input and set the ticket ID";
            assertEquals(msg, 1, added.getTicketID());
            assertEquals(msg, "Server down", added.getDescription());
            assertEquals(msg, "Reporter", added.getReporter());
            assertEquals("Ticket reported date should be the current date", new Date().getTime(), added.getDateReported().getTime(), 2000);
        }
    }


    @Test(timeout = 3000)
    public void e2eSearchId() {

        Ticket test = new Ticket("Mouse missing", 3, "Cubicle 123", new Date());
        TicketStore.getInstance().add(test);

        try (MockedStatic<InputUtils> mockInputUtil = Mockito.mockStatic(InputUtils.class)) {

            mockInputUtil.when(() -> InputUtils.intInput(anyString()))
                    .thenReturn(TicketUI.SEARCH_BY_ID)      // select 1 from menu
                    .thenReturn(1)   // search for ID 1
                    .thenReturn(TicketUI.QUIT)   // quit program
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            PrintUtils.catchStandardOut();
            TicketManager q3 = new TicketManager();
            q3.manage();
            String out = PrintUtils.resetStandardOut();

            mockInputUtil.verify( () -> InputUtils.intInput(anyString()), times(3));
            mockInputUtil.verifyNoMoreInteractions();

            assertTrue("Search by ID should show the matching Ticket priority", out.contains("3"));
            assertTrue("Search by ID should show the matching Ticket description", out.contains("Mouse missing"));
            assertTrue("Search by ID should show the matching Ticket reporter", out.contains("Cubicle 123"));
        }
    }


    @Test(timeout = 3000)
    public void e2eDeleteTicketByIdDontConfirm() {

        TicketStore.getInstance().removeAll();

        Ticket test = new Ticket("Mouse missing", 3, "Cubicle 123", new Date());
        store.add(test);

        try (MockedStatic<InputUtils> mockInputUtil = Mockito.mockStatic(InputUtils.class)) {

            mockInputUtil.when(() -> InputUtils.intInput(anyString()))
                    .thenReturn(TicketUI.DELETE_BY_TICKET_ID)
                    .thenReturn(1)   // search for ID = 1
                    .thenReturn(TicketUI.QUIT)
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            mockInputUtil.when(() -> InputUtils.yesNoInput(anyString()))
                    .thenReturn(false)
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));


            TicketManager q3 = new TicketManager();
            q3.manage();


            mockInputUtil.verify( () -> InputUtils.intInput(anyString()), times(3));
            mockInputUtil.verify( () -> InputUtils.yesNoInput(anyString()), times(1));

            mockInputUtil.verifyNoMoreInteractions();

            String msg = "If user cancels delete operation, do not modify the ticket store or the resolved ticket list";
            assertEquals(msg, 1, store.getAllTickets().size());
            assertEquals(msg, test, store.getTicketById(1));
        }
    }


    @Test(timeout = 3000)
    public void e2eShowNextTicket() {

        Ticket test = new Ticket("Mouse missing", 3, "Cubicle 123", new Date());
        store.add(test);

        try (MockedStatic<InputUtils> mockInputUtil = Mockito.mockStatic(InputUtils.class)) {

            mockInputUtil.when(() -> InputUtils.intInput(anyString()))
                    .thenReturn(TicketUI.SHOW_NEXT_TICKET)
                    .thenReturn(TicketUI.QUIT)
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));


            PrintUtils.catchStandardOut();
            TicketManager q3 = new TicketManager();
            q3.manage();
            String out = PrintUtils.resetStandardOut();

            mockInputUtil.verify( () -> InputUtils.intInput(anyString()), times(2));
            mockInputUtil.verifyNoMoreInteractions();

            assertTrue("Display the next ticket information.", out.contains(test.toString()));
        }
    }


    @Test(timeout = 3000)
    public void e2eShowAllTickets() {

        Ticket test1 = new Ticket("Mouse missing", 3, "Cubicle 123", new Date());
        Ticket test2 = new Ticket("Server down", 1, "Admin", new Date());
        Ticket test3 = new Ticket("Word is broken", 5, "User", new Date());
        store.add(test1);
        store.add(test2);
        store.add(test3);

        try (MockedStatic<InputUtils> mockInputUtil = Mockito.mockStatic(InputUtils.class)) {

            mockInputUtil.when(() -> InputUtils.intInput(anyString()))
                    .thenReturn(TicketUI.SHOW_ALL_TICKETS)
                    .thenReturn(TicketUI.QUIT)
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            PrintUtils.catchStandardOut();
            TicketManager q3 = new TicketManager();
            q3.manage();
            String out = PrintUtils.resetStandardOut();


            mockInputUtil.verify( () -> InputUtils.intInput(anyString()), times(2));
            mockInputUtil.verifyNoMoreInteractions();


            // Expect test2, then test1, then test3
            int locTest1 = out.indexOf(test1.toString());
            int locTest2 = out.indexOf(test2.toString());
            int locTest3 = out.indexOf(test3.toString());

            String message = "Show tickets in priority order with priority = 1 first and priority = 5 last. The program printed this output:\n" + out;
            assertTrue(message, locTest1 >= 0 && locTest2 >= 0 && locTest3 >= 0);
            assertTrue(message, locTest2 < locTest1);
            assertTrue(message, locTest1 < locTest3);
        }
    }

    
    @Test(timeout=3000)
    public void e2eQuit() {

        try (MockedStatic<InputUtils> mockInputUtil = Mockito.mockStatic(InputUtils.class)) {

            mockInputUtil.when(() -> InputUtils.intInput(anyString()))
                    .thenReturn(TicketUI.QUIT)
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            TicketManager q3 = new TicketManager();

            // If the program keeps running, this test will time out and fail.
            q3.manage();
        }
    }

}
