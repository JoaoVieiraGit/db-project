package mainBD;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.*;
import java.text.ParseException;


public class Server {
    public static void main(String args[]){
        int serverPort = 7000;
        int numero = 0;
        System.out.println("A Escuta no Porto 7000");
        ServerSocket listenSocket = null;
        try {
            listenSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("LISTEN SOCKET="+listenSocket);
        while(true) {
            Socket clientSocket = null;
            try {
                clientSocket = listenSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("CLIENT_SOCKET (created at accept())="+clientSocket);
            numero ++;
            new TCP_Connection(clientSocket, numero);
        }
    }
}


class TCP_Connection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int thread_number;

    public TCP_Connection (Socket aClientSocket, int numero) {
        thread_number = numero;
        try{
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.start();
        }catch(IOException e){System.out.println("Connection:" + e.getMessage());}
    }
    //=============================
    public void LoggedIn(int user) throws Exception{
        String texto = "";
        String auxiliar = "";
        String nomeProjeto = "";
        String recompensa = "";
        String nome = "";
        String descricao = "";
        float valorPedido;
        String valor = "";
        Date dataLimite = null;
        try {
            dataLimite = Projeto.parseDate("2010-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String mensagem = "";

        int i;

        try{
            while(true){

                String data = in.readUTF();

                switch (Integer.parseInt(data)){

                    case 0:
                        this.join();

                    case 1:
                        //Consulta base de dados, lista projetos atuais com numeros para cada.
                        texto = Projeto.displayCurrentProjects();
                        out.writeUTF(texto);
                        //Ve que projeto e que o cliente quis ver:
                        data = in.readUTF();
                        nomeProjeto = data;
                        //Display informacoes sobre o projeto: Data limite, rewards, descricao e niveis extra
                        texto = Projeto.displayProjectDetails(Integer.parseInt(data));
                        out.writeUTF(texto);

                        valor = in.readUTF();
                        if (valor.equals("0")) continue;

                        if (valor.equals("1")){

                            //Doar dinheiro
                            texto = Recompensas.consultaRecompensas(Integer.parseInt(data));
                            //Lista de recompensas e dinheiro a pagar por elas
                            out.writeUTF(texto);

                            texto = in.readUTF();
                            recompensa = texto;
                            Doacao.insert(Integer.parseInt(texto), user, Recompensas.moneyRecompensas(Integer.parseInt(recompensa)), Integer.parseInt(recompensa));

                            out.writeUTF(Alternativa.displayAlternativas(Integer.parseInt(nomeProjeto)).toString());

                            texto = in.readUTF();

                            Voto.insertVoto(user,Integer.parseInt(texto),Recompensas.moneyRecompensas(Integer.parseInt(recompensa)));
                        }

                        if (valor.equals("2")){
                            //Enviar mensagem

                            //Receber titulo
                            texto = in.readUTF();
                            nome = texto;

                            //Receber mensagem
                            texto = in.readUTF();
                            mensagem = texto;

                            Mensagem.insert(nome,mensagem,user,Integer.parseInt(data),Integer.parseInt(Projeto.getIDuser(Integer.parseInt(data))));

                        }
                        break;



                    case 2:
                        //Consulta base de dados, lista projetos antigos com numeros para cada
                        texto = Projeto.displayOldProjects();
                        out.writeUTF(texto);
                        data = in.readUTF();
                        if (data.equals("0")) continue;
                        else{
                            texto = Projeto.displayProjectDetails(Integer.parseInt(data));
                            out.writeUTF(texto);
                        }
                        //Display informaces sobre o projeto: Se foi financiado e quando acabou
                        break;


                    case 3:
                        //Saldo deste utilizador
                        texto = User.showCash(user);
                        out.writeUTF(texto);
                        break;
                    case 4:
                        texto = User.displayRecompensas(user);
                        out.writeUTF(texto);
                        break;
                        //Recompensas deste utilizador
                    case 5:
                        //Criar um projeto
                        data = in.readUTF();
                        nomeProjeto = data;

                        data = in.readUTF();
                        descricao = data;
                        //Adicionar descricao a base de dados

                        data = in.readUTF();
                        dataLimite = Projeto.parseDate(data);
                        //Adicionar data limite a base de dados

                        data = in.readUTF();
                        valorPedido = Float.parseFloat(data);
                        //Adicionar valor pedido a base de dados
                        int idproj = Projeto.insertProject(dataLimite, descricao, nomeProjeto, 0, valorPedido, user);

                        data = in.readUTF();
                        int temp1 = Integer.parseInt(data);
                        for(i = 0; i < temp1;i++){
                            data = in.readUTF();
                            //Adicionar recompensa a base de dados
                            recompensa = data;

                            data = in.readUTF();
                            valor = data;
                            int temp = Integer.parseInt(valor);
                            //Adicionar valor dessa recompensa a base de dados
                            Recompensas.insert(recompensa,temp,idproj,user);
                        }

                        data = in.readUTF();
                        auxiliar = data;
                        for(i = 0; i < Integer.parseInt(auxiliar);i++){
                            data = in.readUTF();
                            recompensa = data;
                            //Adicionar essa alternativa a base de dados
                            Alternativa.createAlternativa(recompensa,idproj);
                        }
                        break;


                    case 6:
                        //Adicionar e remover recompensas a um projeto

                        //Projetos deste utilizador
                        texto = Projeto.displayUserProjects(user);
                        out.writeUTF(texto);
                        data = in.readUTF();
                        nomeProjeto = data;
                        data = in.readUTF();
                        if (data.equals("0")) continue;

                        else{
                            if (data.equals("0")){
                                continue;
                            }
                            if (data.equals("1")){
                                //Receber nome da recompensa
                                data = in.readUTF();
                                recompensa = data;

                                //Receber valor da recompensa
                                data = in.readUTF();
                                valor = data;
                                Recompensas.insert(recompensa,Integer.parseInt(valor),Integer.parseInt(nomeProjeto),user);
                            }
                            if (data.equals("2")) {
                                texto = Recompensas.consultaRecompensas(Integer.parseInt(nomeProjeto));
                                out.writeUTF(texto);

                                //Receber a recompensa a apagar
                                data = in.readUTF();

                                if (data.equals("0")) continue;

                                else{
                                    Recompensas.delete(Integer.parseInt(data));
                                }
                            }
                        }
                        break;

                    case 7:
                        //Cancelar projeto

                        //Enviar lista de projetos:
                        texto = Projeto.displayUserProjects(user).toString();
                        out.writeUTF(texto);

                        data = in.readUTF();
                        Projeto.cancelProject(Integer.parseInt(data));
                        break;

                    case 8:
                        //Ver e responder a mensagens de apoiantes
                        texto = Mensagem.getMessagesUser(user).toString();
                        out.writeUTF(texto);

                        //Ver a mensagem que o utilizador escolheu:
                        auxiliar = in.readUTF();
                        texto = Mensagem.printMessage(Integer.parseInt(auxiliar)).toString();
                        //Mandar conteudo da mensagem para o cliente
                        out.writeUTF(texto);
                        //Saber se o cliente quer responder a mensagems (S/N)
                        data = in.readUTF();

                        if(data.equals("N")) continue;

                        else if (data.equals("S")){
                            valor = in.readUTF();
                            texto = in.readUTF();
                            //Adicionar a mensagem presente em "data" a base de dados, como resposta a mensagem escolhida
                            Mensagem.insert(valor,texto,user,Integer.parseInt(Mensagem.getIDProjeto(Integer.parseInt(auxiliar))),Integer.parseInt(Mensagem.getIDUser(Integer.parseInt(auxiliar))));
                        }
                    break;
                }
            }
        }catch(EOFException e){System.out.println("EOF:" + e);
        }catch(IOException e){System.out.println("IO:" + e);}

    }
    public void run(){
        String texto = "";
        String res = "";
        String user = "";
        String nomeProjeto = "";
        int id_user;
        try {
            Projeto.checkDate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try{
            while(true){
                String data = in.readUTF();


                switch (Integer.parseInt(data)){
                    case 0:
                        this.join();
                    case 1:
                        //Consulta base de dados, lista projetos atuais com numeros para cada. Mostrar se estao perto do valor objetivo


                        texto = Projeto.displayCurrentProjects().toString();
                        out.writeUTF(texto);
                        data = in.readUTF();
                        nomeProjeto = data;
                        //Display informacoes sobre o projeto: Data limite, rewards, descricao e niveis extra
                        if (!data.equals("0")){
                            texto = Projeto.displayProjectDetails(Integer.parseInt(nomeProjeto)).toString();
                            out.writeUTF(texto);
                        }
                        break;

                    case 2:
                        texto = Projeto.displayOldProjects().toString();
                        out.writeUTF(texto);
                        data = in.readUTF();
                        if (!data.equals("0")){
                            texto = Projeto.displayProjectDetails(Integer.parseInt(data)).toString();
                            out.writeUTF(texto);
                        }
                        break;

                    case 3:
                        res = "0";
                        while (res == "0") {
                            data = in.readUTF();
                            String username = data;

                            texto = in.readUTF();

                            if (!(in.readUTF()).equals(texto)) res = "0"; else res = "1";
                            out.writeUTF(res);
                            if (res == "0") continue;

                            //Adicionar utilizador com password a base de dados
                            User.signup(username,texto);
                        }
                        break;

                    case 4:
                        res = "0";
                        int counter = 0;
                        id_user = 0;
                        while (res == "0") {
                            if (counter > 3){
                                System.out.println("Demasiadas tentativas falhadas. A desligar aplicacao.");
                                System.exit(0);
                            }
                            data = in.readUTF();
                            user = data;
                            data = in.readUTF();
                            id_user = User.loggin(user, data);
                            if (id_user == -1){
                                out.writeUTF("0");
                                continue;
                            }
                            else{
                                out.writeUTF("1");
                                out.writeUTF(Integer.toString(User.loggin(user,data)));
                                break;
                            }
                        }
                        LoggedIn(id_user);
                }
            }
        }catch(EOFException e){System.out.println("EOF:" + e);
        }catch(IOException e){System.out.println("IO:" + e);} catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}