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
        List<Place> wishList = new ArrayList<>();
        
        do {
            
            String name = stringInput("Enter the name of the place");
            String reason = stringInput("Why would you like to visit " + name + "?");
            
            // TODO use the createNewPlace method to
            //  create a new Place object with the name, and reason
            //   Remember that the Place object's constructor will calculate the date created and store that as part of the Place object.

            Place newPlace = createNewPlace(name, reason);

            // TODO add the new Place object, returned by createNewPlace, to the wishList

            wishList.add(newPlace);

        } while (yesNoInput("More places to add to your wish list?"));
        
        // TODO Call the displayPlaces method to print a list of the places

        displayPlaces(wishList);
        
    }
    
    public static Place createNewPlace(String placeName, String reason) {
        
        // TODO create a new Place object with the given placeName and reason
        //   Remember that the Place object's constructor will calculate the date created and store that.

        return new Place(placeName, reason);  // TODO change this to return the new Place object
    }
    
    
    public static void displayPlaces(List<Place> wishlist) {

        // TODO
        //  Print each Place, one per line.
        //  Don't print anything else
        //  Make sure the Place class has a toString method

        for (Place place : wishlist) {
            System.out.println(place);
        }
    }
    
}
