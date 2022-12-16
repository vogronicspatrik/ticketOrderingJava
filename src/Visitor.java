import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Visitor{

    public static void displayRecommendedMovies(Scanner sc) throws IOException {
        BufferedReader reader = getBufferedReader("movies.txt");
        List<Movie> MovieList = getMoviesToList(reader);
        MovieList = MovieList.stream().filter(distinctByKey(m->m.getMovie())).collect(Collectors.toList());
        System.out.println("****************Recommended Movies****************");
        for(Movie m : MovieList){
            System.out.println("- " + m.Movie);
        }
        Random rand = new Random();
        System.out.println("Do you need a recommendation? (yes or no) ");
        String Needrecommendation  = sc.next();
        if(Objects.equals(Needrecommendation, "yes")){
            System.out.println(MovieList.get(rand.nextInt(MovieList.size())).Movie);
        }

    }



    protected static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    protected static BufferedReader getBufferedReader(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        return bufferedReader;
    }

    protected static List<Movie> getMoviesToList(BufferedReader reader) throws IOException {
        String line1 = null;
        List<Movie> MovieList = new ArrayList<>();
        reader.readLine();
        while((line1 = reader.readLine()) != null){
            String[] csvFields = line1.split(",");

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

    public static void help(){
        System.out.println("FullName: - Write your full name together");
        System.out.println("Username: - Write a username what you want to use");
        System.out.println("EmailAddress: - Write your email address");
        System.out.println("DateOfBirth: - Write your birthdate like this: 1990/01/28");
        System.out.println("Password: - Write your password and note it down");
    }

    public static void addMovieToWishlist(Scanner sc) throws IOException {
        System.out.println("Movie name: ");
        String name = sc.next();
        BufferedWriter writer = new BufferedWriter(new FileWriter("wishlist.txt", true));
        StringBuffer oneLine = new StringBuffer();
        oneLine.append(name);
        oneLine.append(",");
        writer.append(oneLine);
        writer.close();
    }

}
