import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Admin extends User {
    public void AdminCheckOrders() throws IOException {
        for(Log log : getLogFileForReading()){
            System.out.println(log.Location + "-" + log.Theater + "-" + log.Movie + "-" + log.Day + ":" + log.Hour + " Line: " + log.Line + " Seat: " + log.Seat);
        }
    }


    public void AdminAddOrDelete(Scanner sc) throws IOException {
        System.out.println("Delete or Add?(delete/add)");
        String isDeleteOrAdd = sc.next();
        if(isDeleteOrAdd.equals("delete")){

            BufferedReader reader = getBufferedReader("movies.txt");
            List<Movie> listOfMovies= getMoviesToList(reader);
            List<String> Ids = new ArrayList<>();
            for(Movie m : listOfMovies){
                System.out.println(m.Id + "-" + m.Location + "-" + m.Theater + "-" + m.Movie + "-" + m.Day + ":" + m.Hour);
                Ids.add(m.Id);
            }
            System.out.print("Id: ");
            String idForDelete;
            while(true){
                String id = sc.next();
                if(Ids.contains(id)){
                    idForDelete = id;
                    break;
                }
                System.out.print("This Id is not exist, try again: ");
            }
            reader.close();
            removeRecord("movies.txt",idForDelete,1);

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


    private static void writeMovieToCSV(Movie movie)
    {
        String CSV_SEPARATOR = ",";
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


}
