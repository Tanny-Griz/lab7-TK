package week_7.q2_ticket;

import input.InputUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import test_utils.PrintUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import static org.easymock.EasyMock.anyString;
import static org.junit.Assert.*;

public class TicketManagerTest {
    
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
    public void e2eDeleteTicketByIdAndConfirm() throws Exception{
        
        
        Ticket test = new Ticket("Mouse missing", 3, "Cubicle 123", new Date());
        store.add(test);

        //  mockStatic(InputUtils.class);
//        expect(InputUtils.intInput(anyString())).andReturn(TicketUI.DELETE_BY_TICKET_ID);
//        expect(InputUtils.intInput(anyString())).andReturn(1);  // search for ID 1
//        expect(InputUtils.yesNoInput(anyString())).andReturn(true);
//        expect(InputUtils.stringInput(anyString())).andReturn("Fixed ticket");
//        expect(InputUtils.intInput(anyString())).andReturn(TicketUI.QUIT);
//        PowerMock.replay(InputUtils.class);
//

        try (MockedStatic<InputUtils> mockInputUtil = Mockito.mockStatic(InputUtils.class)) {

            mockInputUtil.when(() -> InputUtils.intInput(anyString()))
                    .thenReturn(TicketUI.DELETE_BY_TICKET_ID)
                    .thenReturn(1)  // search for ticket ID 1
                    .thenReturn(TicketUI.QUIT)
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            mockInputUtil.when(() -> InputUtils.stringInput(anyString()))
                    .thenReturn("Fixed ticket")  // resolution
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            mockInputUtil.when(() -> InputUtils.yesNoInput(anyString()))
                    .thenReturn(true)  // confirm delete
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));


            TicketManager q3 = new TicketManager();
            q3.manage();

            assertEquals("There was one open ticket. After ticket delete, list should be empty", 0, TicketStore.getInstance().getAllTickets().size());
            assertEquals("There was one open ticket. After ticket delete, resolved ticket list should have one ticket.", 1, ResolvedTicketStore.getInstance().getAll().size());
            Ticket resolvedTicket = ResolvedTicketStore.getInstance().getAll().getFirst();

            assertTrue(TicketUtil.sameOpenTicket(test, resolvedTicket));

           // assertEquals("The ticket needs to be updated with the resolution information", "Fixed ticket",t);
        }
    }
    
    
    @Test(timeout=3000)
    public void e2eDeleteTicketByDescription() throws Exception {
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

//        mockStatic(InputUtils.class);
//        expect(InputUtils.intInput(anyString())).andReturn(option);
//        expect(InputUtils.stringInput(anyString())).andReturn("mouse");   // Search term
//        expect(InputUtils.intInput(anyString())).andReturn(3);    // ticket 3
//        expect(InputUtils.yesNoInput(anyString())).andReturn(true);
//        expect(InputUtils.stringInput(anyString())).andReturn("rebooted");  // resolution
//        expect(InputUtils.intInput(anyString())).andReturn(TicketUI.QUIT);
//        PowerMock.replay(InputUtils.class);

        try (MockedStatic<InputUtils> mockInputUtil = Mockito.mockStatic(InputUtils.class)) {

            mockInputUtil.when(() -> InputUtils.intInput(anyString()))
                    .thenReturn(option)
                    .thenReturn(3)  // user enters -1 on list of matching tickets to cancel
                    .thenReturn(TicketUI.QUIT)
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            mockInputUtil.when(() -> InputUtils.stringInput(anyString()))
                    .thenReturn("mouse")  // search term
                    .thenReturn("rebooted") // resolution
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            mockInputUtil.when(() -> InputUtils.yesNoInput(anyString()))
                    .thenReturn(true)
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));


            TicketManager q3 = new TicketManager();
            q3.manage();

            assertNull(store.getTicketById(3));

            Ticket resolved = ResolvedTicketStore.getInstance().getAll().getFirst();

            System.out.println(test3);
            System.out.println(resolved);
            assertTrue("Resolved ticket not found in resolved ticket list", TicketUtil.sameOpenTicket(resolved, test3));
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

        try (MockedStatic<InputUtils> mockInputUtil = Mockito.mockStatic(InputUtils.class)) {

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
        

        try (MockedStatic<InputUtils> mockInputUtil = Mockito.mockStatic(InputUtils.class)) {

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
