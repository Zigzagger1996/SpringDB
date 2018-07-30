package ua.zigzagger.testwork.dao.impls;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.zigzagger.testwork.dao.interfaces.MP3Dao;
import ua.zigzagger.testwork.dao.objects.MP3;

@Component("sqliteDAO")
public class SQLiteDAO implements MP3Dao{
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void insert(MP3 mp3){
		String sql = "insert into MP3 (name, author) VALUES (?, ?)";
		jdbcTemplate.update(sql, new Object[] { mp3.getName(), mp3.getAuthor() });
	}
	
	public void insert(List<MP3> mp3List){
		for(MP3 mp3 : mp3List) {
			insert(mp3);
		}
		
	}
	
	public void insertWithJDBC(MP3 mp3) {
		
		Connection conn = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			String url = "jdbc:sqlite:db/SpringDB.db";
			conn = DriverManager.getConnection(url, "", "");
		}catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		String sql = "insert into MP3 (name, author) VALUES (?, ?)";
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, mp3.getName());
			ps.setString(2, mp3.getAuthor());
			ps.executeUpdate();
			ps.close();
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();	
				}catch(SQLException e) {
					
				}
			}
		}
	}

	@Override
	public void delete(int id){
		String sql = "delete from mp3 where id-?";
		int result = jdbcTemplate.update(sql, id);
	}
	
	@Override
	public void delete(MP3 mp3){
		delete(mp3.getId());
	}

	@Override
	public MP3 getMP3ByID(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MP3> getMP3ListByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MP3> getMP3ListByAuthor(String author) {
		// TODO Auto-generated method stub
		return null;
	}

}
