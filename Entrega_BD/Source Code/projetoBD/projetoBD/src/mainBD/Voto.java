package mainBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Voto {

	private static Connection conn = ConnectionManager.getInstance().getConnection();
	
	public static boolean insertVoto(int IDuser,int IDalternativa,int valorVoto) throws SQLException{
		String sql = "INSERT into voto(IDuser,IDalternativa,valorVoto)" + "VALUES (?,?,?)";
		ResultSet keys = null;
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				){
			stmt.setInt(1,IDuser);
			stmt.setInt(2,IDalternativa);
			stmt.setInt(3,valorVoto);
			int affected = stmt.executeUpdate();
			conn.commit();
			if(affected == 1){
				keys = stmt.getGeneratedKeys();
				keys.next();
				//System.out.println("Voto com sucesso!");
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
	
	public static boolean updateVoto(int IDuser,int IDalternativa) throws SQLException{
		String sql = "UPDATE voto SET "+ "valorVoto = valorVoto+1" + " WHERE voto.IDuser = ? AND IDalternativa = ?";
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			stmt.setInt(1,IDuser);
			stmt.setInt(2,IDalternativa);
			int affected = stmt.executeUpdate();
			conn.commit();
			if(affected==1){
				return true;
			}else{
				return false;
			}
			
		} catch (SQLException e) {
			conn.rollback();
			System.err.println(e.getMessage());
			return false;
		}
	}
	
}
