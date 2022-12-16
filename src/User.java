import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class User extends Visitor {
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

    public void checkMyOrders() throws IOException {
        List<Log> ResultList = getLogFileForReading().stream().filter(log-> log.UserId.equals(this.Username)).collect(Collectors.toList());
        System.out.println("***************************************************************************************");
        System.out.println("Your purchased tickets: ");
        for(Log log : ResultList){
            System.out.println(log.Location + "-" + log.Theater + "-" + log.Movie + "-" + log.Day + ":" + log.Hour + " Line: " + log.Line + " Seat: " + log.Seat);
        }
        System.out.println("***************************************************************************************");
    }



    public List<Log> getLogFileForReading() throws IOException {
        BufferedReader reader = getBufferedReader("log.txt");

        String line1 = null;
        List<Log> LogList = new ArrayList<>();
        reader.readLine();
        while((line1 = reader.readLine()) != null){
            String[] csvFields = line1.split(",");

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

    public static void removeRecord(String filepath, String removeTerm, int positionOfTerm){
        int position = positionOfTerm-1;
        String tempFile = "temp.txt";
        String currentLine;
        String data[];

        try{
            FileWriter fw = new FileWriter(tempFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            FileReader fr = new FileReader(filepath);
            BufferedReader br = new BufferedReader(fr);

            while ((currentLine = br.readLine()) != null){
                data = currentLine.split(",");
                if(!(data[position].equalsIgnoreCase(removeTerm))){
                    pw.println(currentLine);
                }
            }
            pw.flush();
            pw.close();
            fr.close();
            br.close();
            bw.close();
            fw.close();

            File myObj = new File(filepath);
            if (myObj.delete()) {
                System.out.println("Deleted the file: " + myObj.getName());
            } else {
                System.out.println("Failed to delete the file.");
            }
            File file = new File("temp.txt");
            File rename = new File(filepath);
            boolean flag = file.renameTo(rename);
            if (flag) {
                System.out.println("File Successfully Rename");
            }
            else {
                System.out.println("Operation Failed");
            }

        }
        catch (Exception e){
            System.out.println("Nem jó");
        }
    }


    public void order(Scanner sc) throws IOException {
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
            String[] csvFields = logLine1.split(",");

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

                Log actLog = new Log(this.Username,FinalMovie.Location,FinalMovie.Theater,FinalMovie.Movie,FinalMovie.Day,FinalMovie.Hour,FinalMovie.Price,Integer.parseInt(line),Integer.parseInt(seat));

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
            if(this.Point != 0) {
                System.out.println("Do you want to use your bonus points?(yes/no)");
                String isUseBonus = sc.next();
                if (isUseBonus.equals("yes")) {
                    System.out.println("You have used all your bonus points(" + this.Point + ")");
                    System.out.println("You have paid: " + (numberOfTickets* FinalMovie.Price-this.Point) + "(instead of: " + numberOfTickets* FinalMovie.Price + ")");
                    this.Point = 0;
                }
                else{
                    System.out.println("You have paid: " + numberOfTickets*FinalMovie.Price);
                    System.out.println("Thank you for your purchase! You have earned " + numberOfTickets*100 + "points");
                    this.Point += numberOfTickets*100;
                }

            }
            System.out.println("You have paid: " + numberOfTickets*FinalMovie.Price);
            System.out.println("Thank you for your purchase! You have earned " + numberOfTickets*100 + " points");
            this.Point += numberOfTickets*100;

            for(Log log : ListOfOrderedMoviesWaitingToBuy){
                writeLogToCSV(log);
            }
            savePoints();
        }
        else{
            System.out.println("Thank you, for your visiting, see you later! :)");
        }
    }

    public void savePoints(){
        removeRecord("users.txt", this.FullName, 1);
        writeUserToCSV();
    }

    private void writeLogToCSV(Log log)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true));

            String CSV_SEPARATOR = ",";
            StringBuffer oneLine = new StringBuffer();
            oneLine.append(this.Username);
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

    public void registration(Scanner sc){
        System.out.print("Do you need help? (yes or no) ");
        String NeedHelp = sc.next();
        if(Objects.equals(NeedHelp, "yes")){
           help();
        }

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

        writeUserToCSV();
        System.out.print("Your account created successfully!");
    }

    public void writeUserToCSV()
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true));
            String CSV_SEPARATOR = ",";
            StringBuffer oneLine = new StringBuffer();
            oneLine.append(this.FullName);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(this.Username);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(this.EmailAddress);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(this.DateOfBirth);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(this.Password);
            oneLine.append(CSV_SEPARATOR);
            if(this.Point > 0){
                oneLine.append(this.Point);
            }
            else{
                oneLine.append(0);
            }
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



}
