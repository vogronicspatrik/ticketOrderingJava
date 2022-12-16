import java.io.*;
import java.util.*;
public class Main {
    static int ch;
    static Scanner sc = new Scanner(System.in);

    public static void displayVisitorMenu() throws IOException {
        System.out.println("**************** Welcome To our Ticket Order App ****************");
        System.out.println("======================================================");
        System.out.println("           1.Registration");
        System.out.println("           2.Login");
        System.out.println("           3.Recommended Movies");
        System.out.println("======================================================");
        System.out.println("          What Do you Want to do??");
        System.out.print("Enter Your Choice: ");
        ch = sc.nextInt();
        switch(ch){
            case 1: //Registration
                ActualUser.registration(sc);
                break;
            case 2: //Login
                ActualUser.Login(sc);
                break;
            case 3: //Recommended Movies
                ActualUser.displayRecommendedMovies(sc);
                break;
            default:
                break;
        }

    }

    public static void displayUserMenu() throws IOException {
        System.out.println("**************** Welcome here " + ActualUser.Username + "****************");
        System.out.println("======================================================");
        System.out.println("           1.Order");
        System.out.println("           2.Check your orders");
        System.out.println("           3.Recommended movies");
        System.out.println("           4.Add Movie to wishlist");
        System.out.println("======================================================");
        System.out.println("          What Do you Want to do??");
        System.out.print("Enter Your Choice: ");
        ch = sc.nextInt();
        switch(ch){
            case 1: //Registration
                ActualUser.order(sc);
                break;
            case 2: //Check your orders
                ActualUser.checkMyOrders();
                break;
            case 3: //Recommended movies
                ActualUser.displayRecommendedMovies(sc);
                break;
            case 4:
                ActualUser.addMovieToWishlist(sc);
                break;
            default:
                break;
        }

    }

    public static void displayAdminMenu() throws IOException {
        System.out.println("**************** Welcome here " + ActualUser.Username + "****************");
        System.out.println("======================================================");
        System.out.println("           1.Add/delete Movies");
        System.out.println("           2.Check orders");
        System.out.println("======================================================");
        System.out.println("          What Do you Want to do??");
        System.out.print("Enter Your Choice: ");
        ch = sc.nextInt();
        switch(ch){
            case 1: //Add and delete movies
                ActualUser.AdminAddOrDelete(sc);
                break;
            case 2: //Check orders
                ActualUser.AdminCheckOrders();
                break;
            default:
                break;
        }

    }
    public static Admin ActualUser= new Admin();
    public static void main(String[] args) throws IOException {
        while (true) {
        if (ActualUser.Username == null) {
            displayVisitorMenu();
        } else {
            if (ActualUser.Role.equals("user")) {
                displayUserMenu();
            } else {
                displayAdminMenu();
            }
        }
    }
    }
}

