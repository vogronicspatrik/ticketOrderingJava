import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Main {
    static int ch;
    static Scanner sc = new Scanner(System.in);

    public static void displayVisitorMenu() throws IOException {
        System.out.println("**************** Welcome To our Ticket Order App ****************");
        System.out.println("======================================================");
        System.out.println("           1.Registration");
        System.out.println("           2.Login");
        System.out.println("           3.Recommended Movies");
        System.out.println("           4.Help");
        System.out.println("======================================================");
        System.out.println("          What Do you Want to do??");
        System.out.print("Enter Your Choice: ");
        ch = sc.nextInt();
        switch(ch){
            case 1: //Registration
                registration();
                break;
            case 2: //Login
                Login();
                break;
            case 3: //Recommended Movies
                displayRecommendedMovies();
                break;
            case 4:
                help();
                break;
            default:
                break;
        }

    }

    public static void AdminAddOrDelete() throws IOException {
        System.out.println("Delete or Add?(delete/add)");
        String isDeleteOrAdd = sc.next();
        if(isDeleteOrAdd.equals("delete")){
            //TODO
        }
        else if(isDeleteOrAdd.equals("add")){
            BufferedReader reader = getBufferedReader("movies.txt");
            List<Movie> listOfMovies= getMoviesToList(reader);
            for(Movie m : listOfMovies){
                System.out.println(m.Location + "-" + m.Theater + "-" + m.Movie + "-" + m.Day + ":" + m.Hour);
            }
            Movie movie = new Movie();

            System.out.println("Id:");
            String id = sc.next();
            movie.Id = id;

            System.out.println("Location:");
            String location = sc.next();
            movie.Location = location;

            System.out.println("Theater:");
            String theater = sc.next();
            movie.Theater = theater;

            System.out.println("Movie:");
            String movieFilm = sc.next();
            movie.Movie = movieFilm;

            System.out.println("Day:");
            String day = sc.next();
            movie.Day = day;

            System.out.println("Hour:");
            String hour = sc.next();
            movie.Hour = hour;

            System.out.println("Price:");
            String price = sc.next();
            movie.Price = Integer.parseInt(price);

            writeMovieToCSV(movie);
        }
        else{
            System.out.println("Wrong command");
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
                order();
                break;
            case 2: //Check your orders
                checkMyOrders();
                break;
            case 3: //Recommended movies
                displayRecommendedMovies();
                break;
            case 4:
                addMovieToWishlist();
                break;
            default:
                break;
        }

    }
    public static void help(){
        System.out.println("FullName: - Write your full name together");
        System.out.println("Username: - Write a username what you want to use");
        System.out.println("EmailAddress: - Write your email address");
        System.out.println("DateOfBirth: - Write your birthdate like this: 1990/01/28");
        System.out.println("Password: - Write your password and note it down");
    }

    public static void addMovieToWishlist() throws IOException {
        System.out.println("Movie name: ");
        String name = sc.next();
        BufferedWriter writer = new BufferedWriter(new FileWriter("wishlist.txt", true));
        StringBuffer oneLine = new StringBuffer();
        oneLine.append(name);
        oneLine.append(",");
        writer.append(oneLine);
        writer.close();
    }

    public static void displayRecommendedMovies() throws IOException {
        BufferedReader reader = getBufferedReader("movies.txt");
        List<Movie> MovieList = getMoviesToList(reader);
        MovieList = MovieList.stream().filter(distinctByKey(m->m.getMovie())).collect(Collectors.toList());
        System.out.println("****************Recommended Movies****************");
        for(Movie m : MovieList){
            System.out.println("- " + m.Movie);
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
                AdminAddOrDelete();
                break;
            case 2: //Check orders
                adminCheckOrders();
                break;
            default:
                break;
        }

    }

    public static List<Movie> getMoviesToList(BufferedReader reader) throws IOException {
        String line1 = null;
        List<Movie> MovieList = new ArrayList<>();
        reader.readLine();
        while((line1 = reader.readLine()) != null){
            String[] csvFields = line1.split(CSV_SEPARATOR);

            Movie actualMovie = new Movie();
            actualMovie.Id = csvFields[0];
            actualMovie.Location = csvFields[1];
            actualMovie.Theater = csvFields[2];
            actualMovie.Movie = csvFields[3];
            actualMovie.Day = csvFields[4];
            actualMovie.Hour = csvFields[5];
            actualMovie.Price = Integer.parseInt(csvFields[6]);
            MovieList.add(actualMovie);
        }
        return MovieList;
    }

    public static void order() throws IOException {
        BufferedReader reader = getBufferedReader("movies.txt");

        List<Movie> MovieList = getMoviesToList(reader);

        //List<Movie> distinctElement = MovieList.stream().distinct().collect(Collectors.toList());

        List<Movie> locationList = MovieList.stream().filter(distinctByKey(m->m.getLocation())).collect(Collectors.toList());
        List<String> actualIds = new ArrayList<>();
        System.out.println("Which city?");
        for(Movie movie : locationList){
            actualIds.add(movie.Id);
            System.out.println(movie.Id + "." + movie.Location);
        }


        System.out.print("Id: ");
        String correctIdForLocation;
        while(true){
            String id = sc.next();
            if(actualIds.contains(id)){
                correctIdForLocation = id;
                break;
            }
            System.out.print("This Id is not exist, try again: ");
        }
        //----------A megfelelő város kiválasztása----------------------------------------------------------------------
        Optional<Movie> MovieForLocation = MovieList.stream().filter(movie -> movie.Id.equals(correctIdForLocation)).findFirst();
        List<Movie> TheatersForLocation = MovieList.stream().filter(movie -> movie.Location.equals(MovieForLocation.get().Location)).collect(Collectors.toList());
        TheatersForLocation = TheatersForLocation.stream().filter(distinctByKey(m-> m.getTheater())).collect(Collectors.toList());

        //--------------------------------------------------------------------------------------------------------------
        //------------A megfelelő mozi kiválasztása a válaszott városban------------------------------------------------
        System.out.println("Which Theater?");
        actualIds.clear();//ugyanazt a listát használom a lépéseknél, ezért kiürítem előtte
        for(Movie movie : TheatersForLocation){
            actualIds.add(movie.Id);
            System.out.println(movie.Id + "." + movie.Theater);
        }

        System.out.print("Id: ");
        String correctIdForTheater;
        while(true){
            String id = sc.next();
            if(actualIds.contains(id)){
                correctIdForTheater = id;
                break;
            }
            System.out.print("This Id is not exist, try again: ");
        }
        //--------------------------------------------------------------------------------------------------------------
        //------------------Az adott film kiválasztása a megfelelő moziban----------------------------------------------
        Optional<Movie> MovieForTheaterAndLocation = MovieList.stream().filter(movie -> movie.Id.equals(correctIdForTheater)).findFirst();
        List<Movie> MoviesForTheatersAndLocation = MovieList.stream().filter(movie -> movie.Theater.equals(MovieForTheaterAndLocation.get().Theater)).collect(Collectors.toList());
        List<Movie> DistinctMoviesForTheatersAndLocation = MoviesForTheatersAndLocation.stream().filter(distinctByKey(m-> m.getMovie())).collect(Collectors.toList());
        System.out.println("Which Movie?");
        actualIds.clear();//ugyanazt a listát használom a lépéseknél, ezért kiürítem előtte
        for(Movie movie : DistinctMoviesForTheatersAndLocation){
            actualIds.add(movie.Id);
            System.out.println(movie.Id + "." + movie.Movie);
        }
        System.out.print("Id: ");
        String correctIdForMovie;
        while(true){
            String id = sc.next();
            if(actualIds.contains(id)){
                correctIdForMovie = id;
                break;
            }
            System.out.print("This Id is not exist, try again: ");
        }
        //--------------------------------------------------------------------------------------------------------------
        //--------------Az időpont kiválasztása a filmhez---------------------------------------------------------------
        Optional<Movie> TimeForTheaterAndLocation = MovieList.stream().filter(movie -> movie.Id.equals(correctIdForMovie)).findFirst();
        List<Movie> FinalList = MoviesForTheatersAndLocation.stream().filter(movie -> movie.Movie.equals(TimeForTheaterAndLocation.get().Movie)).collect(Collectors.toList());
        System.out.println("Which Time?");
        actualIds.clear();//ugyanazt a listát használom a lépéseknél, ezért kiürítem előtte
        for(Movie movie : FinalList){
            actualIds.add(movie.Id);
            System.out.println(movie.Id + "." + movie.Day + ": " + movie.Hour);
        }

        String finalId;
        while(true){
            String id = sc.next();
            if(actualIds.contains(id)){
                finalId = id;
                break;
            }
            System.out.print("This Id is not exist, try again: ");
        }
        //--------------------------------------------------------------------------------------------------------------
        //-------------------Megvan a film, jöhet a darabszám-----------------------------------------------------------
        Optional<Movie> FinalMovieOpt = MovieList.stream().filter(movie -> movie.Id.equals(finalId)).findFirst();
        Movie FinalMovie = FinalMovieOpt.get();
        System.out.print("How many ticket do you want?: ");
        int numberOfTickets = 0;
        while (true) {
            String input = sc.next();

            try {
                numberOfTickets = Integer.parseInt(input);
                break;
            } catch (NumberFormatException ne) {
                System.out.println("Input is not a number, try again:");
            }
        }

        BufferedReader logReader = getBufferedReader("log.txt");
        String logLine1 = null;
        List<Log> LogList = new ArrayList<>();
        reader.readLine();
        while((logLine1 = reader.readLine()) != null){
            String[] csvFields = logLine1.split(CSV_SEPARATOR);

            Log actualLog = new Log();

            actualLog.UserId = csvFields[0];
            actualLog.Location = csvFields[1];
            actualLog.Theater = csvFields[2];
            actualLog.Movie = csvFields[3];
            actualLog.Day = csvFields[4];
            actualLog.Hour = csvFields[5];
            actualLog.PaidPrice = Integer.parseInt(csvFields[6]);
            actualLog.Line = Integer.parseInt(csvFields[7]);
            actualLog.Seat = Integer.parseInt(csvFields[8]);
            LogList.add(actualLog);
        }


        List<Log> ListOfOrderedMoviesWaitingToBuy = new ArrayList<>();
        for(int i = 0;i<numberOfTickets;i++){
            while(true) {
                System.out.print(i + 1 + ". ticket line?: ");
                String line = sc.next();
                System.out.print(i + 1 + ". ticket seat?: ");
                String seat = sc.next();

                Log actLog = new Log(ActualUser.Username,FinalMovie.Location,FinalMovie.Theater,FinalMovie.Movie,FinalMovie.Day,FinalMovie.Hour,FinalMovie.Price,Integer.parseInt(line),Integer.parseInt(seat));

                if (LogList.size() != 0) {
                    if ( LogList.contains(actLog) || ListOfOrderedMoviesWaitingToBuy.contains(actLog)) {
                        System.out.println("This place is occupied, please choose another one!");
                    }
                    else{
                        ListOfOrderedMoviesWaitingToBuy.add(actLog);
                        break;
                    }
                }
                else if(ListOfOrderedMoviesWaitingToBuy.size() != 0 && ListOfOrderedMoviesWaitingToBuy.contains(actLog)){
                    System.out.println("This place is occupied, please choose another one!");
                }
                else{
                    ListOfOrderedMoviesWaitingToBuy.add(actLog);
                    break;
                }
            }
        }


        System.out.println("Do you want to pay?(yes/no)");
        String isPaying = sc.next();
        if(isPaying.equals("yes")){
            if(ActualUser.Point != 0) {
                System.out.println("Do you want to use your bonus points?(yes/no)");
                String isUseBonus = sc.next();
                if (isUseBonus.equals("yes")) {
                    System.out.println("You have used all your bonus points(" + ActualUser.Point + ")");
                    System.out.println("You have paid: " + (numberOfTickets* FinalMovie.Price-ActualUser.Point) + "(instead of: " + numberOfTickets* FinalMovie.Price + ")");
                    ActualUser.Point = 0;
                }
                else{
                    System.out.println("You have paid: " + numberOfTickets*FinalMovie.Price);
                }
                System.out.println("Thank you for your purchase! You have earned " + numberOfTickets*100 + "points");
                ActualUser.Point += numberOfTickets*100;
            }
            System.out.println("You have paid: " + numberOfTickets*FinalMovie.Price);
            System.out.println("Thank you for your purchase! You have earned " + numberOfTickets*100 + " points");
            ActualUser.Point += numberOfTickets*100;

            for(Log log : ListOfOrderedMoviesWaitingToBuy){
                writeLogToCSV(log);
            }

        }
        else{
            System.out.println("Thank you, for your visiting, see you later! :)");
        }




    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static void checkMyOrders() throws IOException {
        List<Log> ResultList = getLogFileForReading().stream().filter(log-> log.UserId.equals(ActualUser.Username)).collect(Collectors.toList());
        System.out.println("***************************************************************************************");
        System.out.println("Your purchased tickets: ");
        for(Log log : ResultList){
            System.out.println(log.Location + "-" + log.Theater + "-" + log.Movie + "-" + log.Day + ":" + log.Hour + " Line: " + log.Line + " Seat: " + log.Seat);
        }
        System.out.println("***************************************************************************************");
    }

    public static void adminCheckOrders() throws IOException {
        for(Log log : getLogFileForReading()){
            System.out.println(log.Location + "-" + log.Theater + "-" + log.Movie + "-" + log.Day + ":" + log.Hour + " Line: " + log.Line + " Seat: " + log.Seat);
        }
    }

    public static List<Log> getLogFileForReading() throws IOException {
        BufferedReader reader = getBufferedReader("log.txt");

        String line1 = null;
        List<Log> LogList = new ArrayList<>();
        reader.readLine();
        while((line1 = reader.readLine()) != null){
            String[] csvFields = line1.split(CSV_SEPARATOR);

            Log actualLog = new Log();
            actualLog.UserId = csvFields[0];
            actualLog.Location = csvFields[1];
            actualLog.Theater = csvFields[2];
            actualLog.Movie = csvFields[3];
            actualLog.Day = csvFields[4];
            actualLog.Hour = csvFields[5];
            actualLog.PaidPrice = Integer.parseInt(csvFields[6]);
            actualLog.Line = Integer.parseInt(csvFields[7]);
            actualLog.Seat = Integer.parseInt(csvFields[8]);
            LogList.add(actualLog);
        }
        return LogList;
    }

    public static void registration(){
        User user = new User();
        System.out.print("UserName: ");
        String username = sc.next();
        user.Username = username;

        System.out.print("Password: ");
        String Password = sc.next();
        user.Password = Password;

        System.out.print("FullName: ");
        String FullName = sc.next();
        user.FullName = FullName;

        System.out.print("EmailAddress: ");
        String EmailAddress = sc.next();
        user.EmailAddress = EmailAddress;

        System.out.print("DateOfBirth: ");
        String DateOfBirth = sc.next();
        user.DateOfBirth = DateOfBirth;

        user.Point = 0;

        writeUserToCSV(user);
        System.out.print("Your account created successfully!");
    }

    public static void Login() throws IOException {
        BufferedReader reader = getBufferedReader("users.txt");

        String line1 = null;
        List<User> UserList = new ArrayList<>();
        reader.readLine();
        while((line1 = reader.readLine()) != null){
            String[] csvFields = line1.split(CSV_SEPARATOR);

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

        while (ActualUser.Username == null) {
            User user = new User();
            System.out.print("UserName: ");
            String username = sc.next();
            user.Username = username;

            System.out.print("Password: ");
            String Password = sc.next();
            user.Password = Password;

            for (User u : UserList) {
                if (u.Username.equals(user.Username) && u.Password.equals(user.Password)) {
                    ActualUser = u;
                    break;
                }
            }
            if(ActualUser.Username != null){
                System.out.println("Successfully logged in! :)");
                break;
            }
            else{
                System.out.println("Bad Username or Password, try again!");
            }
        }


    }

    private static BufferedReader getBufferedReader(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        return bufferedReader;
    }

    private static final String CSV_SEPARATOR = ",";
    private static void writeUserToCSV(User user)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true));

            StringBuffer oneLine = new StringBuffer();
            oneLine.append(user.FullName);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(user.Username);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(user.EmailAddress);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(user.DateOfBirth);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(user.Password);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(0);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("user");
            oneLine.append("\n");
            writer.append(oneLine.toString());

            writer.close();
        }
        catch (UnsupportedEncodingException e) {
            System.out.print(e);
        }
        catch (FileNotFoundException e){
            System.out.print(e);
        }
        catch (IOException e){
            System.out.print(e);
        }
    }

    private static void writeMovieToCSV(Movie movie)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("movies.txt", true));

            StringBuffer oneLine = new StringBuffer();
            oneLine.append(movie.Id);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(movie.Location);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(movie.Theater);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(movie.Movie);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(movie.Day);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(movie.Hour);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(movie.Price);
            oneLine.append("\n");
            writer.append(oneLine.toString());

            writer.close();
        }
        catch (UnsupportedEncodingException e) {
            System.out.print(e);
        }
        catch (FileNotFoundException e){
            System.out.print(e);
        }
        catch (IOException e){
            System.out.print(e);
        }
    }

    private static void writeLogToCSV(Log log)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true));

            StringBuffer oneLine = new StringBuffer();
            oneLine.append(ActualUser.Username);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(log.Location);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(log.Theater);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(log.Movie);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(log.Day);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(log.Hour);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(log.PaidPrice);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(log.Line);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(log.Seat);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("\n");
            writer.append(oneLine.toString());

            writer.close();
        }
        catch (UnsupportedEncodingException e) {
            System.out.print(e);
        }
        catch (FileNotFoundException e){
            System.out.print(e);
        }
        catch (IOException e){
            System.out.print(e);
        }
    }

    public static User ActualUser= new User();
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

