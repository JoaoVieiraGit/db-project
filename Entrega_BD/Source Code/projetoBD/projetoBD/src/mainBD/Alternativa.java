package mainBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Alternativa {
	public Alternativa(){}

	private static Connection conn = ConnectionManager.getInstance().getConnection();

	public static boolean createAlternativa(String alternativa, int projectID) throws SQLException{
		String sql = "INSERT into alternativa (IDprojeto,nomeAlternativa)" + "VALUES (?,?)";
		ResultSet keys = null;
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		){
			stmt.setInt(1, projectID);
			stmt.setString(2, alternativa);
			int affected = stmt.executeUpdate();
			conn.commit();
			if(affected == 1){
				keys = stmt.getGeneratedKeys();
				keys.next();
			}else{
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

	public static StringBuffer displayAlternativas(int projectID) throws SQLException{
		String sql = "SELECT IDalternativa, nomeAlternativa FROM alternativa WHERE IDprojeto = ?";
		ResultSet rs = null;
		StringBuffer bf = new StringBuffer();
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
		){
			stmt.setInt(1, projectID);
			rs = stmt.executeQuery();
			conn.commit();
			while(rs.next()){
				bf.append("\nID da alternativa: "+rs.getString("IDalternativa"));
				bf.append("\nAlternativa: "+rs.getString("nomeAlternativa"));
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
	
	public static void printVotosProjeto(int projectID) throws SQLException{
		String sql = "SELECT * FROM voto WHERE IDprojeto = ?";
		ResultSet rs = null;
		StringBuffer bf = new StringBuffer();
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			stmt.setInt(1, projectID);
			rs = stmt.executeQuery();
			conn.commit();
			while(rs.next()){
				bf.append("ID do voto: "+rs.getInt("IDprojeto"));
				bf.append("\nID do user: "+rs.getString("nomeProjeto"));
				bf.append("\nID do projeto: "+rs.getString("estado"));
				bf.append("\nAlternativa: "+rs.getString("descricao"));
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
