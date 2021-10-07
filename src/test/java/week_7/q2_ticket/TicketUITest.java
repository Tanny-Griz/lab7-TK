package week_7.q2_ticket;

import input.InputUtils;
import org.junit.Before;
import org.junit.Test;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import test_utils.PrintUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

public class TicketUITest {
    
    
    @Before()
    public void resetStaticTicketVar() {
        TicketCounter.setCounter(0);
    }
    
    
    @Before()
    public void clearTicketStore() {
        TicketStore store = TicketStore.getInstance();
        store.removeAll();
    }
    
    
    /* *************** Test TicketUI Methods **********************/
    
    
    @Test(timeout=3000)
    public void testPriorityInRange() {

        System.out.println("If this test times out, make sure you don't accept priorities outside the range 1-5. " +
        "\nIf you get 'Unexpected method call' errors, make sure you only ask 3 questions: description, reporter, priority, in that order");
        
        TicketUI ui = new TicketUI();

        try (MockedStatic<InputUtils> mockInputUtil = Mockito.mockStatic(InputUtils.class)) {

            int expectedValidPriorityValue = 4;

            mockInputUtil.when(() -> InputUtils.intInput(anyString()))
                    .thenReturn(6)   // user entered value for priority
                    .thenReturn(1000)
                    .thenReturn(0)
                    .thenReturn(-1)
                    .thenReturn(-5000)
                    .thenReturn(expectedValidPriorityValue)   // end with a valid value
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            mockInputUtil.when(() -> InputUtils.stringInput(anyString()))
                    .thenReturn("Whatever problem")  // specific problem not important
                    .thenReturn("Whatever reporter") // specific reporter not important
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            mockInputUtil.when(() -> InputUtils.yesNoInput(anyString()))
                    .thenReturn(true)
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            // TODO if you modify the signature of getNewTicketInfo, for example,
            //  to pass in a max and min priority, you'll need to modify this test too.
            Ticket t = ui.getNewTicketInfo();

            assertEquals("Do not accept ticket priorities outside the range 1-5. Accept the first valid input in the range 1-5",
                    expectedValidPriorityValue, t.getPriority());
        }
    }
    
    
    @Test(timeout=3000)
    public void addedNewMenuItemsSearchByDescriptionDeleteByDescription() {
        
        TicketManager q3 = new TicketManager();

        // Check for presence of new constants in TicketUI

        Class ui = TicketUI.class;
        try {
            Field dbd = ui.getDeclaredField("DELETE_BY_DESCRIPTION");
            Field sbd = ui.getDeclaredField("SEARCH_BY_DESCRIPTION");

            assertTrue("Constants added should be static", Modifier.isStatic(dbd.getModifiers()));
            assertTrue("Constants added should be static", Modifier.isStatic(sbd.getModifiers()));

            assertTrue("Constants added should be constants (final)", Modifier.isFinal(sbd.getModifiers()));
            assertTrue("Constants added should be constants (final)", Modifier.isFinal(sbd.getModifiers()));

        } catch (NoSuchFieldException e) {
            fail("Add two new constants with the same names given in the instructions. ");
        }

        // Run program and ensure both new menu options are displayed

        try (MockedStatic<InputUtils> mockInputUtil = Mockito.mockStatic(InputUtils.class)) {

            // Run program, which should display initial menu, then quit.
            mockInputUtil.when(() -> InputUtils.intInput(anyString()))
                    .thenReturn(TicketUI.QUIT)   // user entered value for priority
                    .thenThrow(new RuntimeException("Only ask for the input requested in the requirements."));

            PrintUtils.catchStandardOut();
            q3.manage();
            String out = PrintUtils.resetStandardOut();

            mockInputUtil.verify( () -> InputUtils.intInput(anyString()), times(1));
            mockInputUtil.verifyNoMoreInteractions();

            assertTrue("Ensure your menu has an option 'Search by description'. Use that exact text.", out.toLowerCase().contains("search by description"));
            assertTrue("Ensure your menu has an option 'Delete by description'. Use that exact text.", out.toLowerCase().contains("delete by description"));

        }
    }
}