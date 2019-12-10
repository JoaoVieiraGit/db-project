package mainBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Doacao {
	
	private static Connection conn = ConnectionManager.getInstance().getConnection();
	
	public static void displayAllDonations(int projectID) throws SQLException{
		String sql = "SELECT IDuser FROM doacao WHERE IDprojeto = ?";
		StringBuffer bf = new StringBuffer();
		ResultSet rs = null;
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			stmt.setInt(1, projectID);
			rs = stmt.executeQuery();
			conn.commit();
			while (rs.next()) {					
					bf.append("User ID = "+rs.getInt("IDuser"));
					System.out.println(bf.toString());
			}
		} catch(SQLException e){
			conn.rollback();
			System.err.println(e.getMessage());
		} finally{
			if(rs!=null) rs.close();
		}
	}
	
	public static boolean delete(int projetoID) throws SQLException{
		 String sql = "DELETE FROM doacao WHERE IDprojeto = ?";
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
	
	public static boolean returnMoney(int projetoID) throws SQLException{
		String sql = " ";
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			stmt.setInt(1, projetoID);
			int affected = stmt.executeUpdate();
			conn.commit();
			if(affected==1){
				return true;
			}else{
				return false;
			}
		} catch (SQLException e){
			conn.rollback();
			System.err.println(e.getMessage());
			return false;
		}
	}
	
	public static boolean insert(int projectID,int userID,int montante,int recompensaID) throws SQLException{
		String sql = "INSERT into doacao(IDuser,IDprojeto,montante,IDrecompensas)" + "VALUES(?,?,?,?)";
		String sql2 = "UPDATE user SET saldo = saldo - ?";
		ResultSet keys = null;
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				PreparedStatement stmt2 = conn.prepareStatement(sql2);
				){
			stmt.setInt(1, userID);
			stmt.setInt(2, projectID);
			stmt.setInt(3,montante);
			stmt.setInt(4,recompensaID);
			stmt2.setInt(1,montante);
			int affected = stmt.executeUpdate();
			stmt.executeUpdate();
			conn.commit();
			if(affected==1){
				keys = stmt.getGeneratedKeys();
				keys.next();
				//System.out.println("Doaçao adicionada com sucesso!!!!");
			}else{
				//System.err.println("Doacao nao foi adicionada");
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
