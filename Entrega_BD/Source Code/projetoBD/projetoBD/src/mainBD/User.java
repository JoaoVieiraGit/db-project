package mainBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {

	private static Connection conn = ConnectionManager.getInstance().getConnection();
	private static StringBuffer bf = new StringBuffer();

	public static String showCash(int userID) throws SQLException{
		String sql = "SELECT saldo FROM user WHERE IDuser = ?";
		ResultSet rs = null;
		conn.setAutoCommit(false);
		String string = "";
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
		) {
			stmt.setInt(1, userID);
			rs = stmt.executeQuery();
			conn.commit();
			if (rs.next()) string = "Saldo: " + rs.getString("saldo");
		} catch (SQLException e) {
			conn.rollback();
			System.err.println(e.getMessage());
		}finally{
			if(rs!=null) rs.close();
		}
		return string;
	}

	public static String displayRecompensas(int userID) throws SQLException{
		String sql = "SELECT IDrecompensas, descricao FROM recompensas WHERE IDuser = ?";
		ResultSet rs = null;
		StringBuffer bf = new StringBuffer();
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
		){
			stmt.setInt(1, userID);
			rs = stmt.executeQuery();
			conn.commit();
			while(rs.next()){
				bf.append("\nID da recompensa: "+rs.getString("IDrecompensas"));
				bf.append("\nRecompensa: "+rs.getString("descricao"));
				bf.append("\n------------------------");
			}
		} catch(SQLException e){
			conn.rollback();
			System.err.println(e.getMessage());
		}finally{
			if(rs!=null) rs.close();
		}
		return bf.toString();
	}

	public static void displayRow(int userID) throws SQLException{
		String sql = "SELECT * FROM user WHERE IDuser = ?";
		ResultSet rs = null;
		conn.setAutoCommit(false);
		try(
			PreparedStatement stmt = conn.prepareStatement(sql);
			) {
			stmt.setInt(1, userID);	
			rs = stmt.executeQuery();
			conn.commit();
			bf.append("Dados do user: ");
			if(rs.next()){
				bf.append("ID do user: "+rs.getInt("IDuser"));
				bf.append("\nUsername: "+rs.getString("username"));
				bf.append("\nPassword: "+rs.getString("password"));
				bf.append("\nSaldo: "+rs.getString("saldo"));
			}
		} catch (SQLException e) {
			conn.rollback();
			System.err.println(e.getMessage());
		}finally{
			if(rs!=null) rs.close();
		}
	}
	
	public static int loggin(String username,String password) throws SQLException{
		String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
		ResultSet rs = null;
		int login = -1;
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			stmt.setString(1,username);
			stmt.setString(2, password);
			rs = stmt.executeQuery();
			conn.commit();
			if(rs.next()){
				login = rs.getInt("IDuser");
			}
		}catch(SQLException e){
			conn.rollback();
			System.err.println(e.getMessage());
		}
		return login;
	}
	
	public static boolean signup(String username,String password) throws SQLException{
		String sql = "INSERT into user (username,password)" +"VALUES (?,?)";
		ResultSet keys = null;
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				){
			stmt.setString(1, username);
			stmt.setString(2,password);
			int affected = stmt.executeUpdate();
			conn.commit();
			if(affected == 1){
				keys = stmt.getGeneratedKeys();
				keys.next();
				int newKey = keys.getInt(1);
				//System.out.println("Novo user adicionado com o ID: "+newKey);
			}else{
				//System.err.println("User n foi adicionado");
				return false;
			}
		}catch(SQLException e){
			conn.rollback();
			System.err.println(e.getMessage());
			return false;
		}finally{
			if(keys!=null) keys.close();
		}
		return true;
	}
	
}
