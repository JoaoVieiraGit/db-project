package mainBD;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Projeto {
	private static  Connection conn = ConnectionManager.getInstance().getConnection();

	public static String getIDuser(int projectID) throws SQLException{
		String sql = "select IDuser from projeto where IDprojeto = ?";
		ResultSet rs = null;
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
		){
			stmt.setInt(1, projectID);
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

	public static String displayUserProjects(int userID) throws SQLException{
		String sql = "SELECT * FROM projeto WHERE IDuser = ?";
		StringBuffer bf = new StringBuffer();
		ResultSet rs = null;
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
		){
			stmt.setInt(1, userID);
			rs = stmt.executeQuery();
			conn.commit();
			bf.append("Projetos do utilizador: ");
			while(rs.next()){
				bf.append("\nID do projeto: "+rs.getInt("IDprojeto"));
				bf.append("\nNome do Projeto: "+rs.getString("nomeProjeto"));
				bf.append("\n------------------------");

			}
		}catch(SQLException e){
			conn.rollback();
			System.err.println(e.getMessage());
		}
		return bf.toString();
	}

	public static String displayCurrentProjects() throws SQLException{
		String sql = "SELECT * FROM projeto WHERE estado = \"em curso\"";
		StringBuffer bf = new StringBuffer();
		conn.setAutoCommit(false);
		try(
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
		){
			conn.commit();
			bf.append("Projetos atuais: ");
			while(rs.next()){
				bf.append("ID do projeto: "+rs.getInt("IDprojeto"));
				bf.append("\nNome do Projeto: "+rs.getString("nomeProjeto"));
				bf.append("\n------------------------");

			}
		}catch(SQLException e){
			conn.rollback();
			System.err.println(e.getMessage());
		}
		return bf.toString();
	}

	public static String displayOldProjects() throws SQLException{
		String sql = "SELECT * FROM projeto WHERE estado = \"nao concluido\"";
		StringBuffer bf = new StringBuffer();
		conn.setAutoCommit(false);
		try(
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
		){
			conn.commit();
			bf.append("Projetos antigos: ");
			while(rs.next()){
				bf.append("ID do projeto: "+rs.getInt("IDprojeto"));
				bf.append("\nNome do Projeto: "+rs.getString("nomeProjeto"));
				bf.append("\n------------------------");
			}
		}catch(SQLException e){
			conn.rollback();
			System.err.println(e.getMessage());
		}
		return bf.toString();
	}


	public static String displayProjectDetails(int IDprojeto) throws SQLException{
		String sql = "SELECT * FROM projeto WHERE IDprojeto = ?";
		ResultSet rs = null;
		StringBuffer bf = new StringBuffer();
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
		){
			stmt.setInt(1, IDprojeto);
			rs = stmt.executeQuery();
			conn.commit();
			if(rs.next()){
				bf.append("ID do projeto: "+rs.getInt("IDprojeto"));
				bf.append("\nNome do Projeto: "+rs.getString("nomeProjeto"));
				bf.append("\nEstado: "+rs.getString("estado"));
				bf.append("\nDescricao: "+rs.getString("descricao"));
				bf.append("\nValor Obtido: "+rs.getFloat("valorObtido"));
				bf.append("\nValor Pedido: "+rs.getFloat("valorPedido"));
				bf.append("\nData Limite: "+rs.getDate("dataLimite"));
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

	public static int insertProject(Date dataLimite,String descricao,String nomeProjeto,float valorObtido,float valorPedido,int id_user) throws SQLException{
		String sql = "INSERT into projeto (dataLimite,descricao,nomeProjeto,valorObtido,valorPedido,IDuser,estado)" +
				"VALUES (?,?,?,?,?,?,?)";
		ResultSet keys = null;
		int newKey = -1;
		conn.setAutoCommit(false);
		try(
				PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		){
			stmt.setDate(1,dataLimite);
			stmt.setString(2,descricao);
			stmt.setString(3,nomeProjeto);
			stmt.setFloat(4,valorObtido);
			stmt.setFloat(5,valorPedido);
			stmt.setInt(6,id_user);
			stmt.setString(7,"em curso");
			int affected = stmt.executeUpdate();
			conn.commit();
			if(affected == 1){
				keys = stmt.getGeneratedKeys();
				keys.next();
				newKey = keys.getInt(1);
				System.out.println("Projeto adicionado com ID = "+newKey);
			}else{
				System.err.println("Projeto nao adicionado");
				return -1;
			}
		}catch(SQLException e){
			conn.rollback();
			System.err.println(e.getMessage());
			return -1;
		}finally{
			if(keys!=null) keys.close();
		}
		return newKey;
	}

	public static boolean delete(int projetoID) throws SQLException{
		String sql = "DELETE FROM projeto WHERE IDmensagem = ?";
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

	public static Date parseDate(String dateTime) throws ParseException{
		java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateTime);
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		return sqlDate;
	}


	public static void cancelProject(int projectID) throws SQLException{
		String sql = "{call cancelProject(?)}";
		conn.setAutoCommit(false);
		ResultSet rs = null;
		try (
				CallableStatement stmt = conn.prepareCall(sql)
		){
			stmt.setInt(1, projectID);
			stmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			System.err.println(e.getMessage());
		}finally {
			if(rs!=null) rs.close();
		}
	}

	public static boolean checkMoney(int projectID) throws SQLException{
		String sql = "SELECT valorObtido,valorPedido FROM projeto";
		try(
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
		){
			conn.commit();
			if(rs.next()){
				int valorObtido = rs.getInt("valorObtido");
				int valorPedido = rs.getInt("valorPedido");
				if(valorObtido<valorPedido){
					return false;
				}else{
					return true;
				}
			}
		}catch (SQLException e) {
			conn.rollback();
			System.err.println(e.getMessage());
			return false;
		}
		return false;
	}

	public static boolean getUsernameProject(int projectID,int userID) throws SQLException{
		String sql = "SELECT * FROM projeto WHERE IDuser= ? and IDprojeto = ?";
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


	public static void checkDate() throws SQLException, ParseException{
		String sql = "SELECT IDprojeto,dataLimite FROM projeto";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Calendar cal = Calendar.getInstance();
		java.util.Date date = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		System.out.println("Data de hoje: "+sqlDate.toString());
		System.out.println("A ver a data dos projetos...");
		conn.setAutoCommit(false);
		try(
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
		){
			conn.commit();
			while(rs.next()){
				int id = rs.getInt("IDprojeto");
				Date temp = rs.getDate("dataLimite");
				if(temp.before(sqlDate)){
					System.out.println("Projeto com o ID: "+id+" chegou ao tempo limite\nA ver se tem o dinheiro pretendido...");
					if(checkMoney(id)== true) System.out.println("Valor pedido alcancado\nA transferir o montante para a conta do utilizador..."
							+ "\nA alterar o estado do projeto...\nVerificacao concluida");
					else{
						System.out.println("Valor pedido nao alcancado\nA cancelar o projeto...");
						cancelProject(id);
						System.out.println("Projeto e recompensas cancelados\nDinheiro devolvido aos utilizadores\nEstado do projeto alterado");

					}
				}
			}
		}catch (SQLException e) {
			conn.rollback();
			System.err.println(e.getMessage());
		}

	}

}
