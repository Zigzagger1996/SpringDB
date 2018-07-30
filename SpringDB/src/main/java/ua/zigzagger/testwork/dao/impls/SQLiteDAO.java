package ua.zigzagger.testwork.dao.impls;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import ua.zigzagger.testwork.dao.interfaces.MP3Dao;
import ua.zigzagger.testwork.dao.objects.MP3;

@Component("sqliteDAO")
public class SQLiteDAO implements MP3Dao{
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Override
	public void insert(MP3 mp3){
		String sql = "insert into mp3 (name, author) VALUES (:name, :author)";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", mp3.getName());
		params.addValue("author", mp3.getAuthor());

		jdbcTemplate.update(sql, params);
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
		String sql = "delete from mp3 where id=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		jdbcTemplate.update(sql, params);
	}
	
	@Override
	public void delete(MP3 mp3){
		delete(mp3.getId());
	}

	@Override
	public MP3 getMP3ByID(int id) {
		String sql = "select * from mp3 where id=:id";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
				
		return jdbcTemplate.queryForObject(sql, params, new MP3RowMapper());
	}

	@Override
	public List<MP3> getMP3ListByName(String name) {
		String sql = "select * from mp3 where upper(name) like :name";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", "%" + name.toUpperCase() + "%");
		
		return jdbcTemplate.query(sql, params, new MP3RowMapper());
	}

	@Override
	public List<MP3> getMP3ListByAuthor(String author) {
		String sql = "select * from mp3 where upper(author) like :author";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", "%" + author.toUpperCase() + "%");
		
		return jdbcTemplate.query(sql, params, new MP3RowMapper());
	}
	
	public int getMP3Count() {
		String sql = "select count(*) from mp3";
		return jdbcTemplate.getJdbcOperations().queryForObject(sql, Integer.class);
	}
	
	private static final class MP3RowMapper implements RowMapper<MP3>{
		
		@Override
		public MP3 mapRow(ResultSet rs, int rowNum) throws SQLException{
			MP3 mp3 = new MP3();
			mp3.setId(rs.getInt("id"));
			mp3.setName(rs.getString("name"));
			mp3.setAuthor(rs.getString("author"));
			return mp3;
		}
	}
}
