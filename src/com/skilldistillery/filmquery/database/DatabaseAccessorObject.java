package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private String user = "student";
	private String pass = "student";

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmId) {
		Film film = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT *, language.name 'language' FROM film JOIN language ON film.language_id = language.id WHERE film.id = ?";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("film.id");
				String title = rs.getString("film.title");
				String desc = rs.getString("film.description");
				short releaseYear = rs.getShort("film.release_year");
				int langId = rs.getInt("film.language_id");
				int rentDur = rs.getInt("film.rental_duration");
				double rate = rs.getDouble("film.rental_rate");
				int length = rs.getInt("film.length");
				double repCost = rs.getDouble("film.replacement_cost");
				String rating = rs.getString("film.rating");
				String features = rs.getString("film.special_features");

				film = new Film(id, title, desc, releaseYear, langId, rentDur, rate, length, repCost, rating, features);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}

	@Override
	public Actor findActorById(int actorId) {
		Actor actor = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = " select * from actor where id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int actorIds = rs.getInt("actor.id");
				String firstName = rs.getString("actor.first_name");
				String lastName = rs.getString("actor.last_name");
				actor = new Actor(actorIds, firstName, lastName);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actorList = new ArrayList<>();
		Actor actor = null;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT actor.id, actor.first_name, actor.last_name FROM actor "
					+ "JOIN film_actor ON actor.id = film_actor.actor_id JOIN film ON "
					+ "film_actor.film_id = film.id where film.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int actorIds = rs.getInt("actor.id");
				String firstName = rs.getString("actor.first_name");
				String lastName = rs.getString("actor.last_name");
				actor = new Actor(actorIds, firstName, lastName);
				actorList.add(actor);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actorList;
	}

	@Override
	public List<Film> findFilmByKeyword(String searchWord) throws SQLException {
		Connection conn = DriverManager.getConnection(URL, user, pass);
		String sql = "SELECT *,language.name 'language_name' FROM film JOIN language ON film.language_id = language.id WHERE film.title LIKE ? OR film.description LIKE ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%" + searchWord + "%");
		stmt.setString(2, "%" + searchWord + "%");
		ResultSet rs = stmt.executeQuery();
		List<Film> films = new ArrayList<Film>();
		if (rs.next() == false) {
			return null;
		} else {
			do {
				Film film = new Film();
				film.setFilmId(rs.getInt("id"));
				film.setTitle(rs.getString("title"));
				film.setDesc(rs.getString("description"));
				film.setReleaseYear(rs.getShort("release_year"));
				film.setLangId(rs.getInt("language_id"));
				film.setRentDur(rs.getInt("rental_duration"));
				film.setRate(rs.getDouble("rental_rate"));
				film.setLength(rs.getInt("length"));
				film.setRepCost(rs.getDouble("replacement_cost"));
				film.setRating(rs.getString("rating"));
				film.setFeatures(rs.getString("special_features"));
				film.setActorList(findActorsByFilmId(rs.getInt("id")));
				film.setLangName(rs.getString("language_name"));
				films.add(film);
			} while (rs.next());
		}
		return films;
	}

}

// WHERE title LIKE ? OR description LIKE ?;
// stmt.setString(1 "%" + searchWord + "%")
// stmt.setString(2 "%" + searchWord + "%")