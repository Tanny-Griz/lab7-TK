package week_7.q2_ticket;

import input.InputUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import test_utils.PrintUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TicketManagerTest {
    
    private TicketStore store = TicketStore.getInstance();
    private ResolvedTicketStore resolvedTicketStore = ResolvedTicketStore.getInstance();


    @Before
    public void resetStaticTicketVar() {
        TicketCounter.setCounter(1);
    }
    
    
    @Before
    public void clearTicketStore() {
        store.removeAll();
        resolvedTicketStore.getAll().clear();
    }
    
    
    @Test(timeout=3000)
    public void e2eDeleteTicketByIdAndConfirm() {

        Ticket test = new Ticket("Mouse missing", 3, "Cubicle 123", new Date());
        store.add(test);

        try (MockedStatic<InputUtils> mockInputUtil = mockStatic(InputUtils.class)) {

            String exampleResolution = "Replaced missing mouse with new mouse";
            mockInputUtil.when(() -> InputUtils.intInput(anyString()))
                    .thenReturn(TicketUI.DELETE_BY_TICKET_ID)
                    .thenReturn(1)  // enter ticket ID 1
                    .thenReturn(TicketUI.QUIT)
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            mockInputUtil.when(() -> InputUtils.stringInput(anyString()))
                    .thenReturn(exampleResolution)  // resolution the user will type in
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            mockInputUtil.when(() -> InputUtils.yesNoInput(anyString()))
                    .thenReturn(true)  // confirm delete in the deleteTicketByID method
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));


            TicketManager q3 = new TicketManager();
            q3.manage();

            // Check the exact number of method calls were made
            mockInputUtil.verify( () -> InputUtils.intInput(anyString()), times(3) );
            mockInputUtil.verify( () -> InputUtils.stringInput(anyString()), times(1) );
            mockInputUtil.verify( () -> InputUtils.yesNoInput(anyString()), times(1) );

            mockInputUtil.verifyNoMoreInteractions();

            assertEquals("There was one open ticket that was deleted. After ticket delete, list should be empty", 0, TicketStore.getInstance().getAllTickets().size());
            assertEquals("There was one open ticket that was deleted. After ticket delete, resolved ticket list should have one ticket.", 1, ResolvedTicketStore.getInstance().getAll().size());
            Ticket resolvedTicket = ResolvedTicketStore.getInstance().getAll().getFirst();

            assertTrue("The resolved ticket should be added to the resolved ticket list.", TicketUtil.sameOpenTicket(test, resolvedTicket));

            try {
                Class ticketClass = Class.forName("week_7.q2_ticket.Ticket");
                Method resolutionGetter = ticketClass.getMethod("getResolution");
                Method resolvedDateGetter = ticketClass.getMethod("getResolvedDate");

                String resolution = (String) resolutionGetter.invoke(resolvedTicket);
                Date resolvedDate = (Date) resolvedDateGetter.invoke(resolvedTicket);

                assertEquals("The resolved ticket's resolution should be set to the text entered by the user in the deleteTicketById method",
                        exampleResolution, resolution);

                assertEquals("The resolved ticket's resolution date should be set to the current date in the deleteTicketById method",
                        new Date().getTime(), resolvedDate.getTime(), 1000);    // within a second, anyway

            } catch (Exception e) {  // various things can go wrong here
                System.out.println(e);
                fail("Error reading resolved ticket information, see exception information for details. " +
                        "\nEnsure the Ticket class has a public method getResolution with no arguments, that returns the Ticket's resolution, " +
                        "\nand a public method getResolvedDate with no arguments that returns the resolvedDate.");
            }
        } catch (Exception e) {
            // Errors with code not using the correct input statements, other code errors
            System.out.println(e);
            fail("Ensure your program asks all of the questions needed, and no extra questions." +
                    "\nUse intInput, not positiveIntInput." +
                    "\nOther things may have gone wrong - see exception message above more more details. ");

        }
    }
    

    @Test(timeout=3000)
    public void e2eDeleteTicketByDescriptionNoMatchingTickets() throws Exception {
        Ticket test1 = new Ticket("Mouse missing", 3, "Cubicle 123", new Date());
        Ticket test2 = new Ticket("Server down", 1, "Admin", new Date());
        Ticket test3 = new Ticket("Lost mouse", 5, "User", new Date());
        store.add(test1);
        store.add(test2);
        store.add(test3);

        int option = 0;


        try {
            Field searchByDescription = TicketUI.class.getDeclaredField("DELETE_BY_DESCRIPTION");
            searchByDescription.setAccessible(true);
            option = (int) searchByDescription.get(null);
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            fail("Create a static DELETE_BY_TICKET_DESCRIPTION variable in TicketUI and use it to create and show the menu. " + e);
        }

        try (MockedStatic<InputUtils> mockInputUtil = mockStatic(InputUtils.class)) {

            mockInputUtil.when(() -> InputUtils.intInput(anyString()))
                    .thenReturn(option)
                    .thenReturn(TicketUI.QUIT)  // end program
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            mockInputUtil.when(() -> InputUtils.stringInput(anyString()))
                    .thenReturn("NOT A SEARCH TERM")  // search term
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            TicketManager q3 = new TicketManager();

            PrintUtils.catchStandardOut();

            q3.manage();    // start program - mock input util above will provide user input

            String programOutput = PrintUtils.resetStandardOut().toLowerCase();
            assertTrue("Print a message 'No matching tickets' if the user's search does not return any tickets.",
                    programOutput.contains("no matching tickets"));

            // Check the exact number of method calls were made
            mockInputUtil.verify( () -> InputUtils.intInput(anyString()), times(2) );
            mockInputUtil.verify( () -> InputUtils.stringInput(anyString()), times(1) );

            mockInputUtil.verifyNoMoreInteractions();

            // Check nothing happened to the ticket stores
            int openTickets = TicketStore.getInstance().getAllTickets().size();
            int resolvedTickets = ResolvedTicketStore.getInstance().getAll().size();

            assertEquals("If there are no matches for the user's search, display a 'No tickets found' message. " +
                    "\nDon't do anything else, don't modify the data in the ticket store or resolved ticket store.",
                    3, openTickets);

            assertEquals("If there are no matches for the user's search, display a 'No tickets found' message. " +
                    "\nDon't do anything else, don't modify the data in the ticket store or resolved ticket store.",
                    0, resolvedTickets);

        }

    }

    @Test(timeout=3000)
    public void e2eDeleteTicketByDescriptionMatchingTickets() throws Exception {
        Ticket test1 = new Ticket("Mouse missing", 3, "Cubicle 123", new Date());
        Ticket test2 = new Ticket("Server down", 1, "Admin", new Date());
        Ticket test3 = new Ticket("Lost mouse", 5, "User", new Date());
        store.add(test1);
        store.add(test2);
        store.add(test3);

        int option = 0;

        try {
            Field searchByDescription = TicketUI.class.getDeclaredField("DELETE_BY_DESCRIPTION");
            searchByDescription.setAccessible(true);
            option = (int) searchByDescription.get(null);
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            fail("Create a static DELETE_BY_TICKET_DESCRIPTION variable in TicketUI and use it to create and show the menu. " + e);
        }

        try (MockedStatic<InputUtils> mockInputUtil = mockStatic(InputUtils.class)) {

            mockInputUtil.when(() -> InputUtils.intInput(anyString()))
                    .thenReturn(option)
                    .thenReturn(3)  // Ticket with ID 3.
                    .thenReturn(TicketUI.QUIT)  // end program
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            mockInputUtil.when(() -> InputUtils.stringInput(anyString()))
                    .thenReturn("mouse")  // search term
                    .thenReturn("rebooted") // resolution
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));


            mockInputUtil.when(() -> InputUtils.yesNoInput(anyString()))
                    .thenReturn(true)  // user approves delete
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));


            TicketManager q3 = new TicketManager();
            q3.manage();    // start program - mock input util above will provide user input

            // Check the exact number of method calls were made
            mockInputUtil.verify( () -> InputUtils.intInput(anyString()), times(3) );
            mockInputUtil.verify( () -> InputUtils.stringInput(anyString()), times(2) );
            mockInputUtil.verify( () -> InputUtils.yesNoInput(anyString()), times(1) );

            mockInputUtil.verifyNoMoreInteractions();

            assertNull("After resolving a ticket, remove it from the ticket store.", store.getTicketById(3));  // expect ticket 3 to no longer be there

            Ticket resolved = ResolvedTicketStore.getInstance().getAll().getFirst();

            System.out.println(resolved);
            assertTrue("After resolving a ticket, remove it from the ticket store and add to the resolved ticket list.\n" +
                    "Resolved ticket not found in resolved ticket list", TicketUtil.sameOpenTicket(resolved, test3));
        }
    }
    
    
    @Test(timeout=3000)
    public void e2eDeleteTicketByDescriptionUserCancels() throws Exception {
        
        Ticket test1 = new Ticket("Mouse missing", 3, "Cubicle 123", new Date());
        Ticket test2 = new Ticket("Server down", 1, "Admin", new Date());
        Ticket test3 = new Ticket("Lost mouse", 5, "User", new Date());
        store.add(test1);
        store.add(test2);
        store.add(test3);
        
        int option = 0;
        try {
            Field searchByDescription = TicketUI.class.getDeclaredField("DELETE_BY_DESCRIPTION");
            searchByDescription.setAccessible(true);
            option = (int) searchByDescription.get(null);
        } catch (NoSuchFieldException|IllegalAccessException|ClassCastException e) {
            fail("Create a static DELETE_BY_DESCRIPTION variable in TicketUI and use it to create and show the menu. " + e);
        }

        try (MockedStatic<InputUtils> mockInputUtil = mockStatic(InputUtils.class)) {

            mockInputUtil.when(() -> InputUtils.intInput(anyString()))
                    .thenReturn(option)
                    .thenReturn(-1)  // user enters -1 on list of matching tickets to cancel
                    .thenReturn(TicketUI.QUIT)
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            mockInputUtil.when(() -> InputUtils.stringInput(anyString()))
                    .thenReturn("mouse")
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            TicketManager q3 = new TicketManager();
            q3.manage();

            // Check the exact number of method calls were made
            mockInputUtil.verify( () -> InputUtils.intInput(anyString()), times(3) );
            mockInputUtil.verify( () -> InputUtils.stringInput(anyString()), times(1) );

            mockInputUtil.verifyNoMoreInteractions();

            String message = "If user cancels delete operation by entering -1 for the ticket ID to delete, don't remove any tickets from the store.";
            assertTrue(message, TicketUtil.sameOpenTicket(test1, store.getTicketById(1)));
            assertTrue(message, TicketUtil.sameOpenTicket(test2, store.getTicketById(2)));
            assertTrue(message, TicketUtil.sameOpenTicket(test3, store.getTicketById(3)));
        }
    }
    
    
    @Test(timeout=3000)
    public void e2eSearchByDescription() {
        Ticket test1 = new Ticket("Mouse missing", 3, "Cubicle 123", new Date());
        Ticket test2 = new Ticket("Server down", 1, "Admin", new Date());
        Ticket test3 = new Ticket("Lost mouse", 5, "User", new Date());
        store.add(test1);
        store.add(test2);
        store.add(test3);
        
        int option = 0;
        try {
            Field searchByDescription = TicketUI.class.getDeclaredField("SEARCH_BY_DESCRIPTION");
            searchByDescription.setAccessible(true);
            option = (int) searchByDescription.get(null);
        } catch (NoSuchFieldException|IllegalAccessException|ClassCastException e) {
            fail("Create a static SEARCH_BY_DESCRIPTION variable in TicketUI and use it to create and show the menu. " + e);
        }
        

        try (MockedStatic<InputUtils> mockInputUtil = mockStatic(InputUtils.class)) {

            mockInputUtil.when(() -> InputUtils.intInput(anyString()))
                    .thenReturn(option)
                    .thenReturn(TicketUI.QUIT)
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            mockInputUtil.when(() -> InputUtils.stringInput(anyString()))
                    .thenReturn("mouse")
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            PrintUtils.catchStandardOut();
            TicketManager q3 = new TicketManager();
            q3.manage();
            String out = PrintUtils.resetStandardOut();

            // Check the exact number of method calls were made
            mockInputUtil.verify( () -> InputUtils.intInput(anyString()), times(2) );
            mockInputUtil.verify( () -> InputUtils.stringInput(anyString()), times(1) );

            mockInputUtil.verifyNoMoreInteractions();

            // Expect test1, then test3. No test2
            int locTest1 = out.indexOf(test1.toString());
            int locTest2 = out.indexOf(test2.toString());
            int locTest3 = out.indexOf(test3.toString());

            System.out.println("This test searches for the string \"mouse\" in tickets \n" + test1 + "\n" + test2 + "\n" + test3);

            assertTrue("Ticket ID 1 is shown in the results", locTest1 >= 0);
            assertTrue("Ticket ID 2 is shown in the results", locTest3 >= 0);
            assertEquals("Ticket ID 2 is not shown in the results", -1, locTest2);
            assertTrue("Ticket with ID 1 should be shown before ticket with ID 3", locTest1 < locTest3);
        }
    }
    
    
    @Test(timeout = 3000)
    public void ticketManagerDoesNotUseInputUtilsOrSystemOutPrintln() {
    
        String projectRoot = Paths.get("").toString();
        Path ticketFilePath = Paths.get(projectRoot,
                "src", "main", "java", "week_7", "q2_ticket", "TicketManager.java");
        
        try {
            
            byte[] bytes = Files.readAllBytes(ticketFilePath);
            String allFileText = new String(bytes);
            
            String msg = "TicketManager should not use any InputUtils, System.out.print calls. " +
                    "\nAll user input and output should be delegated to TicketUI. " +
                    "\nCall the ticketUI methods to print data, and to ask the user for input. ";
            
            assertFalse(msg, allFileText.contains("InputUtils"));
            assertFalse(msg, allFileText.contains("intInput"));
            assertFalse(msg, allFileText.contains("stringInput"));
            assertFalse(msg, allFileText.contains("System.out.print"));
            assertFalse(msg, allFileText.contains("Scanner"));
            
        } catch (IOException e) {
            fail("If you have moved or renamed TicketManager.java, or any of the directories in the project, please restore the original location and names. " +
                    "\nIf not, please push your code to GitHub and report this test fail to Clara. ");
        }
        
        
    }
    
}
