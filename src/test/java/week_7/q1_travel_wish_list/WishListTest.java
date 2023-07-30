package week_7.q1_travel_wish_list;

import org.junit.Before;
import org.junit.Test;
import test_utils.PrintUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Math.abs;
import static org.junit.Assert.*;


public class WishListTest {
    
    private Class placeClass;
    
    @Before
    public void findPlaceClass() throws ClassNotFoundException {
        placeClass = Class.forName("week_7.q1_travel_wish_list.Place");
    }
    
    public Constructor ensureCorrectConstructor() {
        Constructor[] cons = placeClass.getConstructors();
        assertEquals("Can't create Place objects. You must create one public constructor in the Place class.\n" +
                "Make sure it's access modifier is public.", 1, cons.length);
        Constructor c = cons[0];
        assertArrayEquals("Can't create Place objects, wrong constructor.\n" +
                        "Place constructor should take two String arguments for place name and reason to visit, in that order\n " +
                        "The Place constructor should be public\n" +
                        "Constructor should not have a Date argument.\n" +
                        "The constructor will initialize the Date created to the current date/time.",
                new Class[]{String.class, String.class}, c.getParameterTypes());
        return c;
    }
    
    @Test(timeout=3000)
    public void testPlaceClassFields() throws NoSuchFieldException{
        
        // does it have the right fields? Correct names? Correct types? Are they private?
        
        Field nameField = placeClass.getDeclaredField("name");
        assertTrue(Modifier.isPrivate(nameField.getModifiers()));
        assertEquals("Place class should have a private String name field", String.class, nameField.getType());
    
        Field reasonField = placeClass.getDeclaredField("reason");
        assertTrue(Modifier.isPrivate(reasonField.getModifiers()));
        assertEquals("Place class should have a private String reason field", String.class, reasonField.getType());
        
        Field createdField = placeClass.getDeclaredField("dateCreated");
        assertTrue(Modifier.isPrivate(nameField.getModifiers()));
        assertEquals("Place class should have a private Date dateCreated field", Date.class, createdField.getType());
    }
    
    
    @Test(timeout=3000)
    public void testPlaceClassGetAndSet() throws Exception {
        
        try {
            Method getName = placeClass.getMethod("getName");
            assertEquals("Place class should have a public get and set method for the name field", String.class, getName.getReturnType());
    
            Method setName = placeClass.getMethod("setName", String.class);
            assertEquals("Place class should have a public get and set method for the name field", Void.TYPE, setName.getReturnType());
    
            Method getReason = placeClass.getMethod("getReason");
            assertEquals("Place class should have a public get and set method for the reason field", String.class, getReason.getReturnType());
    
            Method setReason = placeClass.getMethod("setReason", String.class);
            assertEquals("Place class should have a public get and set method for the reason field", Void.TYPE, setReason.getReturnType());
    
            Method getCreated = placeClass.getMethod("getDateCreated");
            assertEquals("Place class should have a public get and set method for the dateCreated field", Date.class, getCreated.getReturnType());
    
            Method setCreated = placeClass.getMethod("setDateCreated", Date.class);
            assertEquals("Place class should have a public get and set method for the dateCreated field", Void.TYPE, setCreated.getReturnType());
    
            Method[] methods = {getName, getReason, getName, setName, setReason, setCreated};
    
            for (Method m : methods) {
                assertTrue(m.getName() + "should be public.", Modifier.isPublic(m.getModifiers()));
            }
    
            
            Constructor c = ensureCorrectConstructor();
            Place p = (Place) c.newInstance("example name", "example reason");
    
            setName.invoke(p, "California");
            String name = (String) getName.invoke(p);
            assertEquals("Set method should set the name variable, get method should return its value", name, "California");
    
            setReason.invoke(p, "See the Hollywood sign");
            String reason = (String) getReason.invoke(p);
            assertEquals("Set method should set the reason variable, get method should return its value", "See the Hollywood sign", reason);
    
            Date now = new Date();
            setCreated.invoke(p, now);
            Date created = (Date) getCreated.invoke(p);
            assertEquals("Set method should set the created variable, get method should return its value", now, created);
        
        } catch (NoSuchMethodException nsme) {
            System.out.println("Missing a method that is expected in the Place class. Make sure you have " +
                    "\ncreated all the get and set methods required. Read the error message below for the missing method name.");
            throw nsme;
        }
    }
    
    @Test(timeout=3000)
    public void testPlaceClassConstructor() throws  Exception{
    
        Constructor c = ensureCorrectConstructor();
        
        Place p = (Place) c.newInstance("Arizona", "To see the Grand Canyon");
        
        Field name = placeClass.getDeclaredField("name");
        name.setAccessible(true);
    
        Field reason = placeClass.getDeclaredField("reason");
        reason.setAccessible(true);
    
        Field created = placeClass.getDeclaredField("dateCreated");
        created.setAccessible(true);
    
        assertEquals("Constructor should set the name field.", "Arizona", name.get(p));
    
        assertEquals("Constructor should set the reason field.", "To see the Grand Canyon", reason.get(p));
    
        assertEquals("Constructor should create the date for the created field.", new Date().toString(), created.get(p).toString());
    }

    
    @Test(timeout=3000)
    public void testPlaceToString() throws Exception{
    
        Constructor c = ensureCorrectConstructor();
    
        Date now = new Date();
        
        Place firstPlace = (Place) c.newInstance("Arizona", "To see the Grand Canyon");
        
        Method toString = placeClass.getMethod("toString");
        String outString = (String) toString.invoke(firstPlace);
        
        String expected = String.format("Place to visit: %s. Reason: %s. Date created: %s", "Arizona", "To see the Grand Canyon", now.toString());
        assertEquals("toString should return a string in the exact format requested.", expected, outString);
    }
    
    
    @Test(timeout=3000)
    public void newPlace() throws Exception{
    
        Place place = WishList.newPlace("Arizona", "To visit the Grand Canyon");

        assertNotNull("Complete the newPlace method in Wishlist.java to create and return a new Place object", place);
    
        Method getName = placeClass.getMethod("getName");
        Method getReason = placeClass.getMethod("getReason");
        Method getCreated = placeClass.getMethod("getDateCreated");
    
        String name = (String) getName.invoke(place);
        assertEquals("WishList new place method should create a new Place object with the data provided", name, "Arizona");
    
        String reason = (String) getReason.invoke(place);
        assertEquals("WishList new place method should create a new Place object with the data provided", "To visit the Grand Canyon", reason);
    
        Date now = new Date();
        Date created = (Date) getCreated.invoke(place);
        
        assertTrue("WishList new place method should create a new Place object. The Place constructor should set the created field",  abs(now.getTime() - created.getTime()) < 1000 );  // within a second
    }
    
    @Test(timeout=3000)
    public void displayPlaces() throws Exception {
    
        Constructor c = ensureCorrectConstructor();
        
        PrintUtils.catchStandardOut();
    
        Date d = new Date();
        Place firstPlace = (Place) c.newInstance("Egypt", "See the Pyramids");
        Place secondPlace = (Place) c.newInstance("Rome", "Eat gelato");
        Place thirdPlace = (Place) c.newInstance("Arizona", "See the Grand Canyon");

        System.out.println("Adding three example places, " + firstPlace + "\n" + secondPlace + "\n" + thirdPlace);
    
        ArrayList<Place> places = new ArrayList<>();
        places.add(firstPlace); places.add(secondPlace); places.add(thirdPlace);
    
        WishList.displayPlaces(places);
        
        String out = PrintUtils.resetStandardOut().replace("\r", "").trim();
        
        String firstExpectedString = String.format("Place to visit: %s. Reason: %s. Date created: %s", "Egypt", "See the Pyramids", d.toString());
        String secondExpectedString = String.format("Place to visit: %s. Reason: %s. Date created: %s", "Rome", "Eat gelato", d.toString());
        String thirdExpectedString = String.format("Place to visit: %s. Reason: %s. Date created: %s", "Arizona", "See the Grand Canyon", d.toString());
        
        assertTrue("Print each of your places in the displayPlaces method.\n" + firstPlace + " is missing, expecting" + firstExpectedString, out.contains(firstExpectedString));
        assertTrue("Print each of your places in the displayPlaces method.\n" + secondPlace + " is missing, expecting" + secondExpectedString, out.contains(secondExpectedString));
        assertTrue("Print each of your places in the displayPlaces method.\n" + thirdPlace + " is missing, expecting" + thirdExpectedString, out.contains(thirdExpectedString));
    }
}