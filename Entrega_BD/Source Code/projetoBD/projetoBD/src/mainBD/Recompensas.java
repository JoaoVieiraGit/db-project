package mainBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Recompensas {
	
	private static Connection conn = ConnectionManager.getInstance().getConnection();

	public static boolean insert(String descricao, int valor, int projectID,int userID) throws SQLException{
		String sql = "INSERT into recompensas (descricao,valor,IDprojeto,IDuser)" +
				"VALUES (?,?,?,?)";
		ResultSet keys = null;
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				){
			stmt.setString(1,descricao);
			stmt.setInt(2,valor);
			stmt.setInt(3,projectID);
			stmt.setInt(4,userID);
			
			int affected = stmt.executeUpdate();
			conn.commit();
			if(affected == 1){
				keys = stmt.getGeneratedKeys();
				keys.next();
				//System.out.println("Recompensa adicionada com sucesso!!!");
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
	
	public static boolean delete(int recompensasID) throws SQLException{
		 String sql = "DELETE FROM recompensas WHERE IDrecompensas = ?";
		 conn.setAutoCommit(false);
		 try (
				 PreparedStatement stmt = conn.prepareStatement(sql); 
				 ){
			stmt.setInt(1, recompensasID);
			int affected = stmt.executeUpdate();
			conn.commit();
			if(affected == 1){
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
	
	public static String consultaRecompensas(int projectID) throws SQLException{
		String sql = "SELECT * FROM recompensas WHERE IDprojeto = ?";
		StringBuffer bf = new StringBuffer();
		ResultSet rs = null;
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			stmt.setInt(1, projectID);
			rs = stmt.executeQuery();
			conn.commit();
			while(rs.next()){
				bf.append("ID da recompensa: "+rs.getInt("IDrecompensas"));
				bf.append("\nDescricao: "+rs.getString("descricao"));
				bf.append("\nValor: "+rs.getFloat("valor"));
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
	
	public static boolean deleteProjeto(int projetoID) throws SQLException{
		 String sql = "DELETE FROM recompensas WHERE IDprojeto = ?";
		 conn.setAutoCommit(false);
		 try (
				 PreparedStatement stmt = conn.prepareStatement(sql); 
				 ){
			stmt.setInt(1, projetoID);
			int affected = stmt.executeUpdate();
			conn.commit();
			if(affected == 1){
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
	
	public static int moneyRecompensas(int recompensaID) throws SQLException{
		String sql = " SELECT valor FROM recompensas WHERE IDrecompensas = ?";
		ResultSet rs = null;
		conn.setAutoCommit(false);
		 try (
				 PreparedStatement stmt = conn.prepareStatement(sql); 
				 ){
			stmt.setInt(1, recompensaID);
			rs = stmt.executeQuery();
			conn.commit();
			if(rs.next()){
				return rs.getInt("valor");
			}
		 }catch(SQLException e){
			 conn.rollback();
			System.err.println(e.getMessage());
			return 0;
		 }
		 return 0;
	}
			
	
}
