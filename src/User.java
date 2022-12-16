import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class User {
    String Username;
    String Password;
    String FullName;
    String DateOfBirth;
    String EmailAddress;
    int Point;
    String Role;

    public void Login(Scanner sc) throws IOException {
        BufferedReader reader = getBufferedReader("users.txt");

        String line1 = null;
        List<User> UserList = new ArrayList<>();
        reader.readLine();
        while((line1 = reader.readLine()) != null){
            String[] csvFields = line1.split(",");

            User actualUser = new User();
            actualUser.FullName = csvFields[0];
            actualUser.Username = csvFields[1];
            actualUser.EmailAddress = csvFields[2];
            actualUser.DateOfBirth = csvFields[3];
            actualUser.Password = csvFields[4];
            actualUser.Point = Integer.parseInt(csvFields[5]);
            actualUser.Role = csvFields[6];
            UserList.add(actualUser);
        }

        while (this.Username == null) {
            User user = new User();
            System.out.print("UserName: ");
            String username = sc.next();
            user.Username = username;

            System.out.print("Password: ");
            String Password = sc.next();
            user.Password = Password;

            for (User u : UserList) {
                if (u.Username.equals(user.Username) && u.Password.equals(user.Password)) {
                    this.Username = u.Username;
                    this.Role = u.Role;
                    this.Point = u.Point;
                    this.Password = u.Password;
                    this.DateOfBirth = u.DateOfBirth;
                    this.EmailAddress = u.EmailAddress;
                    this.FullName = u.FullName;
                    break;
                }
            }
            if(this.Username != null){
                System.out.println("Successfully logged in! :)");
                break;
            }
            else{
                System.out.println("Bad Username or Password, try again!");
            }
        }
        reader.close();


    }

    private static BufferedReader getBufferedReader(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        return bufferedReader;
    }



}
