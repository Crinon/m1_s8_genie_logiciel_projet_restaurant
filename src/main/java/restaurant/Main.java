package restaurant;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		// Initialisation du programme
		Restaurant.initialisation();

		LocalTime time = LocalTime.parse("12:00:00");
		int secondOfDay = time.toSecondOfDay();
		System.out.println("heureDejeunerOuverture " + secondOfDay);
		// Save to database
		// Get from database
		LocalTime time1 = LocalTime.ofSecondOfDay(secondOfDay);
	}

}
