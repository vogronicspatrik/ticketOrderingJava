import java.util.Objects;

public class Log {
    String UserId;
    String Location;
    String Theater;
    String Movie;
    String Day;
    String Hour;
    int PaidPrice;
    int Line;
    int Seat;

    public Log() {
    }

    public Log(String userId, String location, String theater, String movie, String day, String hour, int paidPrice, int line, int seat) {
        UserId = userId;
        Location = location;
        Theater = theater;
        Movie = movie;
        Day = day;
        Hour = hour;
        PaidPrice = paidPrice;
        Line = line;
        Seat = seat;
    }

    @Override
    public boolean equals(Object obj) {
        Log other = (Log) obj;
        return Objects.equals(Location, other.Location)&& Objects.equals(Theater, other.Theater)&& Objects.equals(Movie, other.Movie)&& Objects.equals(Day, other.Day)&& Objects.equals(Hour, other.Hour)&& Objects.equals(PaidPrice, other.PaidPrice)&& Objects.equals(Line, other.Line)&& Objects.equals(Seat, other.Seat);
    }

}
