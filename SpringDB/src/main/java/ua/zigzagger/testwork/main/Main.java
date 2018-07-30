package ua.zigzagger.testwork.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.zigzagger.testwork.dao.impls.SQLiteDAO;
import ua.zigzagger.testwork.dao.interfaces.MP3Dao;
import ua.zigzagger.testwork.dao.objects.MP3;

public class Main {

	public static void main(String[] args) {
		MP3 mp3 = new MP3();
		mp3.setName("Song name");
		mp3.setAuthor("Song author");

		// new SQLiteDAO().insertWithJDBC(mp3);

		ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
		MP3Dao sqLiteDAO = (MP3Dao) context.getBean("sqliteDAO");

		System.out.println(sqLiteDAO.getMP3ListByAuthor("Justin").size());

	}
}
