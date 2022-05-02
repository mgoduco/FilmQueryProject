package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) throws SQLException {
		FilmQueryApp app = new FilmQueryApp();
//		app.test();

		app.launch();
	}

	private void test() {
		Film film = db.findFilmById(7);
//		Actor actor = db.findActorById(7);
//		List<Actor> actorList = db.findActorsByFilmId(7);
//		List<Film> filmList = db.findFilmByKeyword("gold");
		System.out.println(film);
//		System.out.println(actor);
//		System.out.println(actorList);
//		System.out.println(filmList);
	}

	private void launch() throws SQLException {
		Scanner input = new Scanner(System.in);

		startUserInterface(input);

		input.close();
	}

	private void startUserInterface(Scanner input) throws SQLException {
		boolean keepGoing = true;
		do {
			System.out.println("====================================");
			System.out.println("====  1. Look up a film by id   ====");
			System.out.println("== 2. Look up a film by a keyword ==");
			System.out.println("============= 3. Exit ==============");
			System.out.println("====================================");

			int choice = 0;
			try {
				choice = input.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Invalid input");
				input.nextLine();
				continue;
			}

			switch (choice) {
			case 1:
				lookUpFilmById(input);
				break;
			case 2:
				lookUpFilmBySearch(input);
				break;
			case 3:
				System.out.println("Goodbye");
				exit();
				break;
			default:
				System.out.println("Invalid input");
				break;
			}
			input.nextLine();
		} while (keepGoing);
	}

	private void lookUpFilmById(Scanner input) throws SQLException {
		System.out.println("Enter a film ID: ");
		Film film = db.findFilmById(input.nextInt());
		try {
			printFilm(film);
		} catch (NullPointerException e) {
			System.out.println("No results try again.");
		}
	}

	private void lookUpFilmBySearch(Scanner input) throws SQLException {
		System.out.println("Enter a search word: ");
		String choice = input.nextLine();
		System.out.println(choice);
		List<Film> films = db.findFilmByKeyword(input.next());
		try {
			for (Film f : films) {
				printFilm(f);
			}
		} catch (NullPointerException e) {
			System.out.println("No results, please try again.");
		}
	}

	private void printFilm(Film film) {
		System.out.println("=====  FILM  =====");
		System.out.println(film.getTitle());
		System.out.println("Released on " + film.getReleaseYear());
		System.out.println(film.getRating() + " Rating");
		System.out.println(film.getDesc());
		System.out.println("Language: " + film.getLangName());
		System.out.println("===== Actors =====");
		for (Actor a : film.getActorList()) {
			System.out.println(a.getFirstName() + " " + a.getLastName());
		}
		System.out.println();
	}

	private void exit() {
		System.exit(0);
	}

}

//User Story 1
//The user is presented with a menu in which they can choose to:
//Look up a film by its id.
//Look up a film by a search keyword.
//Exit the application.

//User Story 2
//If the user looks up a film by id, they are prompted to enter the film id. If the film is not found, they see a message saying so. If the film is found, its title, year, rating, and description are displayed.

//User Story 3
//If the user looks up a film by search keyword, they are prompted to enter it. If no matching films are found, they see a message saying so. Otherwise, they see a list of films for which the search term was found anywhere in the title or description, with each film displayed exactly as it is for User Story 2.

//User Story 4
//When a film is displayed, its language (English,Japanese, etc.) is also displayed.

//User Story 5
//When a film is displayed, the list of actors in its cast is displayed along with the title, year, rating, and description.
