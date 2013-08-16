package bin.service.impl.mysql;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

import bin.model.Dialog;
import bin.service.DialogService;
import bin.utility.DataSource;

public class DialogServiceImpl implements DialogService {
	private static Logger log = Logger.getLogger(DialogServiceImpl.class);
	private DataSource ds = DataSource.getInstance();

	@Override
	public boolean save(Dialog dialog) throws Throwable {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ds.getConnection();
			stmt = conn.createStatement();
			StringBuffer sql = new StringBuffer();
			sql.append("insert into dialog (txt,type,date,clientIP) values(");
			dialog.setTxt(java.net.URLEncoder.encode(dialog.getTxt(), "UTF-8").replaceAll("\\+", "%20"));
			sql.append("\"" + dialog.getTxt() + "\",");
			sql.append(dialog.getType() + ",");
			sql.append("\"" + new java.sql.Timestamp(dialog.getDate().getTime()) + "\",");
			sql.append("\"" + dialog.getClientIP() + "\"");
			sql.append(")");
			int result = stmt.executeUpdate(sql.toString());
			if (result > 0)
				return true;
			return false;
		} catch (SQLException ex) {
			log.error(ex.getMessage());
			throw ex;
		} catch (PatternSyntaxException ex) {
			log.error(ex.getMessage());
			throw ex;
		} catch (UnsupportedEncodingException ex) {
			log.error(ex.getMessage());
			throw ex;
		} finally {
			DataSource.closeConnResouces(conn, stmt, null);
		}
	}

	@Override
	public List<Dialog> getAll() throws Throwable {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from dialog");
			List<Dialog> list = new ArrayList<Dialog>();
			while (rs.next()) {
				Dialog d = new Dialog();
				d.setClientIP(rs.getString("clientIP"));
				d.setDate(rs.getDate("date"));
				d.setId(rs.getInt("id"));
				d.setTxt(rs.getString("txt"));
				d.setType(rs.getInt("type"));
				list.add(d);
			}
			return list;
		} catch (SQLException ex) {
			log.error(ex.getMessage());
			throw ex;
		} finally {
			DataSource.closeConnResouces(conn, stmt, rs);
		}
	}

	@Override
	public Dialog getSingle(int id) throws Throwable {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from dialog where(id=" + id + ")");
			Dialog d = new Dialog();
			while (rs.next()) {
				d.setClientIP(rs.getString("clientIP"));
				d.setDate(rs.getDate("date"));
				d.setId(rs.getInt("id"));
				d.setTxt(rs.getString("txt"));
				d.setType(rs.getInt("type"));
			}
			return d;
		} catch (SQLException ex) {
			log.error(ex.getMessage());
			throw ex;
		} finally {
			DataSource.closeConnResouces(conn, stmt, rs);
		}
	}

	@Override
	public Dialog getFirst() throws Throwable {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from dialog limit 0,1");
			if (rs.next()) {
				Dialog d = new Dialog();
				d.setClientIP(rs.getString("clientIP"));
				d.setDate(rs.getDate("date"));
				d.setId(rs.getInt("id"));
				d.setTxt(rs.getString("txt"));
				d.setType(rs.getInt("type"));
				return d;
			}
		} catch (SQLException ex) {
			log.error(ex.getMessage());
			throw ex;
		} finally {
			DataSource.closeConnResouces(conn, stmt, rs);
		}
		return null;
	}

	public int getMaxId() throws Throwable {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		int id = -1;
		try {
			conn = ds.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select max(id) from dialog");
			while (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException ex) {
			log.error(ex.getMessage());
			throw ex;
		} finally {
			DataSource.closeConnResouces(conn, stmt, rs);
		}
		return id;
	}

	@Override
	public boolean removeAll() throws Throwable {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ds.getConnection();
			stmt = conn.createStatement();
			int result = stmt.executeUpdate("delete from dialog");
			if (result > 0)
				return true;
			return false;
		} catch (SQLException ex) {
			log.error(ex.getMessage());
			throw ex;
		} finally {
			DataSource.closeConnResouces(conn, stmt, null);
		}
	}
}
