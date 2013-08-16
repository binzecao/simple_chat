package bin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import bin.model.Dialog;
import bin.service.impl.mysql.DialogServiceImpl;
import bin.utility.DataSource;

public class Test5 {
	@Test
	@Ignore
	public void t1() {
		String database = "test";
		String admin = "root";
		String pwd = "12345";
		String characterEncoding = "UTF-8";
		String port = "3306";
		String url = "jdbc:mysql://localhost:" + port + "/" + database + "?useUnicode=true&characterEncoding=" + characterEncoding;
		try {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			Connection conn = DriverManager.getConnection(url, admin, pwd);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from t1");
			while (rs.next()) {
				System.out.println(rs.getInt(1));
				System.out.println(rs.getString(2));
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public void t2(){
		DataSource.initialize("WebContent/WEB-INF/datastore.xml");
		DataSource ds = DataSource.getInstance();
		Connection conn = null;
		try {
			conn = ds.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from dialog");
			while (rs.next()) {
				System.out.println(rs.getInt(1));
				System.out.println(rs.getString(2));
			}
			ds.closeConnResouces(conn, stmt, rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public void t3(){
		String path = null;
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (java.lang.NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void t4() throws UnsupportedEncodingException{
		DataSource.initialize("WebContent/WEB-INF/datastore.xml");
		DialogServiceImpl s = new DialogServiceImpl();
		Dialog dialog = new Dialog();
		dialog.setTxt("ركة سحر الشرق ");
		dialog.setType(1);
		dialog.setDate(new Date());
		dialog.setClientIP("");
		
		System.out.println(dialog.getTxt());
		String str = dialog.getTxt().replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"");
		str = java.net.URLEncoder.encode(dialog.getTxt(), "utf-8").replaceAll("\\+", "%20");
		System.out.println(str);
		dialog.setTxt(str);
		//s.save(dialog);
	}
}
