import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Main {

    static List<String> listOfMovies = Arrays.asList(new String[]{"Avatar", "Hobbit", "Taken"});
    static int ch;
    static Scanner sc = new Scanner(System.in);

    public static void displayMenu() {
        System.out.println("**************** Welcome To our Ticket Order App ****************");
        System.out.println("=====================================================");
        System.out.println("           1.Registration");
        System.out.println("           2.Movie list");
        System.out.println("           3.Ask Movie?!");

        System.out.println("           4.Exit                       ");
        System.out.println("======================================================");
        System.out.println("          What Do you Want to Order Today??");
    }// Our Food Menu is ready

    public static void displayMovies(){
        System.out.println(Arrays.toString(listOfMovies.toArray()));
        System.out.println("           0.Back");
    }



    public static void main(String[] args) {
        displayMenu();
        while(true){
            System.out.print("Enter Your Choice: ");
            ch = sc.nextInt();
            switch(ch){
                case 0: //back to main menu
                    displayMenu();
                    break;
                case 1: //Registration
                    System.out.print("Registration process will be here");
                    break;
                case 2: //Movie list
                    displayMovies();
                    break;
                case 3: //ask movie?
                    System.out.print("ezt nem tudom micsoda");
                    break;
                case 4: //Exit
                    System.exit(1);
                    break;
                default:
                    break;
            }
        }
    }
}

