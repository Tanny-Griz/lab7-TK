package week_7.q1_travel_wish_list;

import java.util.ArrayList;
import java.util.List;

import static input.InputUtils.stringInput;
import static input.InputUtils.yesNoInput;

/**
Program to create and display a list of places a user wishes to travel to.
 */


public class WishList {
    
    public static void main(String[] args) {

        // TODO modify this line so the wishList List stores Place objects
        List wishList = new ArrayList<>();
        
        do {
            
            String name = stringInput("Enter the name of the place");
            String reason = stringInput("Why would you like to visit " + name + "?");
            
            // TODO use the newPlace method to
            //  create a new Place object with the name, and reason
            //   Remember that the Place object's constructor will calculate the date created and store that as part of the Place object.

            // TODO add the new Place object, returned by newPlace, to the wishList
            
        } while (yesNoInput("More places to add to your wish list?"));
        
        // TODO Call the displayPlaces method to print a list of the places
        
    }
    
    public static Place newPlace(String placeName, String reason) {
        
        // TODO create a new Place object with the given placeName and reason
        //   Remember that the Place object's constructor will calculate the date created and store that.


        return null;  // TODO change this to return the new Place
    }
    
    
    public static void displayPlaces(List<Place> places) {

        // TODO
        //  Print each Place, one per line.
        //  Don't print anything else
        //  Make sure the Place class has a toString method
        
    }
    
}
