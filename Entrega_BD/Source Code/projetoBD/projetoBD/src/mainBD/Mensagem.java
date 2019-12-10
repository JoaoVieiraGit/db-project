package mainBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Mensagem {

	private static Connection conn = ConnectionManager.getInstance().getConnection();
	
	public static boolean insert(String assunto,String mensagem,int userID,int projectID,int destinatario) throws SQLException{
		String sql = "INSERT into mensagem (destinatario,assunto,mensagem,IDuser,IDprojeto)" +
				"VALUES (?,?,?,?,?)";
		ResultSet keys = null;
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				){
			stmt.setInt(1,destinatario);
			stmt.setString(2,assunto);
			stmt.setString(3,mensagem);
			stmt.setInt(4,userID);
			stmt.setInt(5,projectID);
			int affected = stmt.executeUpdate();
			conn.commit();
			if(affected == 1){
				keys = stmt.getGeneratedKeys();
				keys.next();
				//System.out.println("Mensagem enviada!!");
			}else{
				//System.err.println("No rows affected");
				return false;
			}
		}catch (SQLException e){
			conn.rollback();
			System.err.println(e.getMessage());
			return false;
		}finally{
			if(keys!=null) keys.close(); 
		}
		return true;
	}
	
	public static String getUsername(int messageID) throws SQLException{
		String sql = "select username from user, mensagem where mensagem.IDuser = user.IDuser and IDmensagem = ?";
		ResultSet rs = null;
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			stmt.setInt(1, messageID);
			rs = stmt.executeQuery();
			conn.commit();
			if(rs.next()){
				return rs.getString("username");
			}
		} catch(SQLException e){
			conn.rollback();
			System.err.println(e.getMessage());
			return null;
		}finally{
			if(rs!=null) rs.close();
		}
		return null;
	}

	public static String getIDUser(int messageID) throws SQLException{
		String sql = "select IDuser from mensagem where IDmensagem = ?";
		ResultSet rs = null;
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
		){
			stmt.setInt(1, messageID);
			rs = stmt.executeQuery();
			conn.commit();
			if(rs.next()){
				return rs.getString("IDuser");
			}
		} catch(SQLException e){
			conn.rollback();
			System.err.println(e.getMessage());
			return null;
		}finally{
			if(rs!=null) rs.close();
		}
		return null;
	}

	public static String getIDProjeto(int messageID) throws SQLException{
		String sql = "select IDprojeto from mensagem where IDmensagem = ?";
		ResultSet rs = null;
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
		){
			stmt.setInt(1, messageID);
			rs = stmt.executeQuery();
			conn.commit();
			if(rs.next()){
				return rs.getString("IDprojeto");
			}
		} catch(SQLException e){
			conn.rollback();
			System.err.println(e.getMessage());
			return null;
		}finally{
			if(rs!=null) rs.close();
		}
		return null;
	}
	
	public static String getDestinatario(int messageID) throws SQLException{
		String sql = "select username as destinatario from user, mensagem where mensagem.destinatario = user.IDuser and IDmensagem = ?";
		ResultSet rs = null;
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			stmt.setInt(1, messageID);
			rs = stmt.executeQuery();
			conn.commit();
			if(rs.next()){
				return rs.getString("destinatario");
			}
		} catch(SQLException e){
			conn.rollback();
			System.err.println(e.getMessage());
			return null;
		}finally{
			if(rs!=null) rs.close();
		}
		return null;
	}
	
	public static StringBuffer printMessage(int messageID) throws SQLException{
		
		String sql = "SELECT * FROM mensagem WHERE IDmensagem = ?";
		ResultSet rs = null;
		StringBuffer bf = new StringBuffer();
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			stmt.setInt(1, messageID);
			rs = stmt.executeQuery();
			conn.commit();
			if(rs.next()){
				bf.append("\tAssunto: "+rs.getString("assunto"));
				bf.append("\nMensagem: "+rs.getString("mensagem"));
				bf.append("\n------------------------");
			}
		} catch(SQLException e){
			conn.rollback();
			System.err.println(e.getMessage());
		}finally{
			if(rs!=null) rs.close();
		}
		return bf;
	}
	
	
	
	public static StringBuffer getMessagesUser(int userID) throws SQLException{
		String sql = "SELECT * FROM mensagem WHERE IDuser = ?";
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
				bf.append("\nID: "+rs.getInt("IDmensagem"));
				bf.append("\t|| Username: "+getUsername(rs.getInt("IDmensagem")));
				bf.append("\t|| Assunto: " + rs.getString("assunto"));
				bf.append("\t|| Destinatario: "+ getDestinatario(rs.getInt("IDmensagem")));
			}
		} catch(SQLException e){
			conn.rollback();
			System.err.println(e.getMessage());
		}finally{
			if(rs!=null) rs.close();
		}
		return bf;
	}
	
	public static boolean getUsernameProject(int projectID,int userID) throws SQLException{
		String sql = "SELECT * FROM mensagem WHERE IDuser= ? and IDprojeto = ?";
		ResultSet rs = null;
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
		){
			stmt.setInt(1, userID);
			stmt.setInt(2, projectID);
			rs = stmt.executeQuery();
			conn.commit();
			if(rs.next()){
				return true;
			}
			else{
				return false;
			}
		} catch(SQLException e){
			conn.rollback();
			System.err.println(e.getMessage());
			return false;
		}finally{
			if(rs!=null) rs.close();
		}
	}

	public static void getMessagesProject(int ProjectID) throws SQLException{
		String sql = "SELECT * FROM mensagem WHERE IDproject = ?";
		ResultSet rs = null;
		StringBuffer bf = new StringBuffer();
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
		){
			stmt.setInt(1, ProjectID);
			rs = stmt.executeQuery();
			conn.commit();
			while(rs.next()){
				bf.append("Username: "+getUsername(rs.getInt("IDmensagem")));
				bf.append("\tAssunto: "+rs.getString("assunto"));
				bf.append("\tDestinatario: "+getDestinatario(rs.getInt("destinatario")));
				bf.append("\n------------------------");
				System.out.println(bf.toString());
			}
		} catch(SQLException e){
			conn.rollback();
			System.err.println(e.getMessage());
		}finally{
			if(rs!=null) rs.close();
		}
	}

}
