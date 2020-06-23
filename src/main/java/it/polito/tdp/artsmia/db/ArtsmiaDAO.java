package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Couple;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public static List<ArtObject> listObjects() {

		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"),
						res.getString("continent"), res.getString("country"), res.getInt("curator_approved"),
						res.getString("dated"), res.getString("department"), res.getString("medium"),
						res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"),
						res.getString("rights_type"), res.getString("role"), res.getString("room"),
						res.getString("style"), res.getString("title"));

				result.add(artObj);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Exhibition> listExhibitions() {

		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"),
						res.getString("exhibition_title"), res.getInt("begin"), res.getInt("end"));

				result.add(exObj);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<String> listAllRoles() {
		String sql = "SELECT DISTINCT role " + "FROM authorship " + "ORDER BY role";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("role"));
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void listArtistsByRole(Map<Integer, Artist> idMapAutori, String role) {
		String sql = "SELECT DISTINCT ar.artist_id, name " + "FROM artists AS ar, authorship AS au "
				+ "WHERE ar.artist_id=au.artist_id AND au.role= ?";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				idMapAutori.put(res.getInt("artist_id"), new Artist(res.getInt("artist_id"), res.getString("name")));
			}
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<Couple> getConnectionsByRole(Map<Integer, Artist> idMapAutori, String role) {
		String sql = "SELECT tab1.artist_id AS id1, tab2.artist_id AS id2, COUNT(DISTINCT tab1.exhibition_id) AS peso "
				+ "FROM (SELECT distinct ar.artist_id, NAME, exo.exhibition_id "
				+ "FROM artists AS ar, authorship AS au, exhibition_objects AS exo "
				+ "WHERE ar.artist_id=au.artist_id AND au.role= ? AND exo.object_id=au.object_id) AS tab1, "
				+ "(SELECT distinct ar.artist_id, NAME, exo.exhibition_id "
				+ "FROM artists AS ar, authorship AS au, exhibition_objects AS exo "
				+ "WHERE ar.artist_id=au.artist_id AND au.role= ? AND exo.object_id=au.object_id) AS tab2 "
				+ "WHERE tab1.artist_id<tab2.artist_id AND tab1.exhibition_id=tab2.exhibition_id "
				+ "GROUP BY tab1.name, tab2.name";
		List<Couple> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			st.setString(2, role);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(new Couple(idMapAutori.get(res.getInt("id1")), idMapAutori.get(res.getInt("id2")),
						res.getInt("peso")));
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
