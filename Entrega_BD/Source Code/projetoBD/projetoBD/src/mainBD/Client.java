package mainBD;

import java.io.*;
import java.net.Socket;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;

public class Client {

	public static void LoggedIn(int user, Socket ServerSocket, DataInputStream in, DataOutputStream out) throws NumberFormatException, SQLException, IOException {
        String texto = null;
        String data = null;
        InputStreamReader writerinput = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(writerinput);
        int i = 0;
        String extra = null;
        Date limit = null;
        try {
            limit = Projeto.parseDate("2010-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String auxiliar = null;
        while (true) {
            System.out.println("Escolha que opcao quer realizar:\n 1. Listar projetos atuais\n" +
                    "2. Listar projetos antigos\n" +
                    "3. Consultar saldo\n" +
                    "4. Consultar recompensas\n" +
                    "5. Criar um projeto\n" +
                    "6. Adicionar e remover recompensas a um projeto\n" +
                    "7. Cancelar projeto\n" +
                    "8. Ver e responder a mensagens de apoiantes\n" +
                    "0. Logout");

            try {
                texto = reader.readLine();
                out.writeUTF(texto);
            } catch (Exception e) {
            	System.err.println(e);
            }

            switch (Integer.parseInt(texto)) {
                case 0:
                    System.exit(0);
                    break;
                case 1:

                    //Listar projetos atuais:
                    try {
                        System.out.println(in.readUTF());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Escolha o numero do projeto que quer consultar. Pressione 0" +
                            "para voltar para tras");

                    //Utilizador escolhe o projeto que quer ver:
                    try {
                        auxiliar = reader.readLine();
                        out.writeUTF(auxiliar);
                    } catch (Exception e) {
                    	System.err.println(e);
                    }

                    //Se escolher sair:
                    if (auxiliar.equals("0")){
                        continue;
                    }

                    //Se escolher um projeto:
                    else{
                        try {
                            System.out.println(in.readUTF());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Opcoes: \n" +
                                "1. Doar dinheiro correspondente a uma recompensa\n" +
                                "2. Enviar mensagem\n" +
                                "0. Sair");

                        //Utilizador escolhe a opcao
                        try {
                            texto = reader.readLine();
                            out.writeUTF(texto);
                        } catch (Exception e) {
                        	System.err.println(e);
                        }

                        if (texto.equals("0")) continue;

                        if (texto.equals("1")){
                            //Doar dinheiro:
                            System.out.println("Recompensas:");
                            try {
                                System.out.println(in.readUTF());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            System.out.println("Insira o numero da recompensa correspondente a doacao" +
                                    "que quer fazer:");
                            try {
                                texto = reader.readLine();
                                out.writeUTF(texto);
                            } catch (Exception e) {
                            	System.err.println(e);
                            }
                            try {
                                System.out.println(in.readUTF());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Escolha a alternativa em que quer votar:");
                            try {
                                texto = reader.readLine();
                                out.writeUTF(texto);
                            } catch (Exception e) {
                                System.err.println(e);
                            }
                        }

                        if (texto.equals("2")){
                            //Enviar mensagem
                            System.out.println("Escreva o titulo da mensagem que quer enviar a este projeto: ");

                            //Utilizador escreve titulo da mensagem e enviar para o servidor:
                            try {
                                texto = reader.readLine();
                                out.writeUTF(texto);
                            } catch (Exception e) {
                            }

                            System.out.println("Escreva o conteudo da mensagem: ");

                            try {
                                data = reader.readLine();
                                out.writeUTF(data);
                            } catch (Exception e) {
                            	System.err.println(e);
                            }

                        }
                    }

                    break;
                case 2:
                    System.out.println("Escolha o numero do projeto que quer consultar. Pressione 0" +
                            " para voltar para tras");
                    try {
                        System.out.println(in.readUTF());
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    texto = reader.readLine();
                    out.writeUTF(texto);
                    if (texto.equals("0")){
                        continue;
                    }
                    else{
                        System.out.println(in.readUTF());
                    }
                    break;
                case 3:
                    //Mostrar Saldo
                    System.out.println(in.readUTF());
                    break;
                case 4:
                    //Mostrar recompensas
                    System.out.println("Recompensas associadas ao utilizador: ");
                    System.out.println(in.readUTF());
                	break;
                case 5:
                    //Criar um projeto
                    System.out.println("Insira o nome do projeto que deseja adicionar: ");

                    try {
                        texto = reader.readLine();
                        out.writeUTF(texto);
                    } catch (Exception e) {
                    	System.err.println(e);
                    }


                    System.out.println("Insira a descricao do projeto: ");
                    try {
                        data = reader.readLine();
                        out.writeUTF(data);
                    } catch (Exception e) {
                    	System.err.println(e);
                    }


                    System.out.println("Insira a data limite do projeto (Formato: YYYY-MM-DD): ");
                    try {
                        auxiliar = reader.readLine();
                        out.writeUTF(auxiliar);
                    } catch (Exception e) {
                    	System.err.println(e);
                    }

                    System.out.println("Insira o valor pedido do projeto: ");
                    try {
                        extra = reader.readLine();
                        out.writeUTF(extra);
                    } catch (Exception e) {
                    	System.err.println(e);
                    }

                    System.out.println("Quantas recompensas deseja ter?");
                    try {
                        texto = reader.readLine();
                        out.writeUTF(texto);
                    } catch (Exception e) {
                    	System.err.println(e);
                    }


                    for(i=0;i<Integer.parseInt(texto);i++){
                        System.out.println("Insira recompensa: ");
                        try {
                            auxiliar = reader.readLine();
                            out.writeUTF(auxiliar);
                        } catch (Exception e) {
                        	System.err.println(e);
                        }

                        System.out.println("Insira valor da recompensa: ");

                        try {
                            data = reader.readLine();
                            out.writeUTF(data);
                        } catch (Exception e) {
                        	System.err.println(e);
                        }
                    }

                    System.out.println("Quantas alternativas deseja ter? Os utilizadores poderem votar na que preferem");
                    try {
                        texto = reader.readLine();
                        out.writeUTF(texto);
                    } catch (Exception e) {
                        System.err.println(e);
                    }

                    for(i=0;i<Integer.parseInt(texto);i++){
                        System.out.println("Insira alternativa: ");
                        try {
                            auxiliar = reader.readLine();
                            out.writeUTF(auxiliar);
                        } catch (Exception e) {
                            System.err.println(e);
                        }
                    }
                    break;
                case 6:
                    //Adicionar e remover recompensas a um projeto
                    System.out.println("Selecione que projeto deseja editar: ");
                    System.out.println("Projetos associados: "+in.readUTF());

                    try {
                        texto = reader.readLine();
                        out.writeUTF(texto);
                    } catch (Exception e) {
                    	System.err.println(e);
                    }

                    //Enviar projeto escolhido

                    if (texto.equals("0")) continue;

                    else{
                        System.out.println("Opcoes: \n0. Sair\n1. Adicionar recompensas\n2.Remover recompensa");
                        try {
                            auxiliar = reader.readLine();
                            out.writeUTF(auxiliar);
                        } catch (Exception e) {
                        	System.err.println(e);
                        }
                        if (auxiliar.equals("0")) continue;

                        else{
                            if (auxiliar.equals("1")){
                                System.out.println("Recompensa a adicionar:\nNome da Recompensa:");

                                try {
                                    data = reader.readLine();
                                    out.writeUTF(data);
                                } catch (Exception e) {
                                	System.err.println(e);
                                }

                                System.out.println("Valor da Recompensa:");

                                try {
                                    extra = reader.readLine();
                                    out.writeUTF(extra);
                                } catch (Exception e) {
                                	System.err.println(e);
                                }

                            }

                            if (auxiliar.equals("2")){
                                System.out.println(in.readUTF());
                                System.out.println("Selecione que recompensa deseja remover: ");

                                try {
                                    texto = reader.readLine();
                                    out.writeUTF(texto);
                                } catch (Exception e) {
                                	System.err.println(e);
                                }
                                System.out.println("Recompensa removida!");
                            }

                        }

                    }
                    break;
                case 7:
                    //Cancelar projeto

                    System.out.println("Selecione que projeto deseja cancelar: ");
                    System.out.println(in.readUTF());


                    try {
                        texto = reader.readLine();
                        out.writeUTF(texto);
                    } catch (Exception e) {
                    	System.err.println(e);
                    }

                    System.out.println("Projeto cancelado");
                    break;
                case 8:
                    //Ver e responder a mensagens de apoiantes
                    System.out.println("Selecione a mensagem que quer ler. Pressione 0 para sair ");
                    System.out.println(in.readUTF());

                    try {
                        texto = reader.readLine();
                        out.writeUTF(texto);
                    } catch (Exception e) {
                    	System.err.println(e);
                    }

                    if (texto.equals("0")) continue;

                    else{

                        System.out.println(in.readUTF());

                        System.out.println("Deseja responder a esta mensagem? S/N");

                        try {
                            texto = reader.readLine();
                            out.writeUTF(texto);
                        } catch (Exception e) {
                        	System.err.println(e);
                        }



                        if (texto.equals("N")) continue;

                        else if (texto.equals("S")){
                            System.out.println("Escreva a sua resposta a mensagem. Assunto:");

                            try {
                                texto = reader.readLine();
                                out.writeUTF(texto);
                            } catch (Exception e) {
                            	System.err.println(e);
                            }

                            System.out.println("Conteudo:");

                            try {
                                texto = reader.readLine();
                                out.writeUTF(texto);
                            } catch (Exception e) {
                                System.err.println(e);
                            }
                        }
                    }
                    break;
            	}
        	}
        
	}
	
	
    public static void main(String args[]) throws SQLException, IOException {
    	

        String host = "";


    	String texto = "0";
    	String data = "0";
        String auxiliar = "0";
    	int user = -1;
    	int counter;
    	InputStreamReader writerinput = new InputStreamReader(System.in);
    	BufferedReader reader = new BufferedReader(writerinput);

        System.out.println("Insira hostname: ");

        try {
            host = reader.readLine();
        } catch (Exception e) {
        }

        Socket s = null;
        int serversocket = 7000;
        try {
            s = new Socket(host, serversocket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SOCKET=" + s);


        DataInputStream in = new DataInputStream(s.getInputStream());


        DataOutputStream out = new DataOutputStream(s.getOutputStream());


        while (true) {


            System.out.println("Escolha que opcao quer realizar:\n" +
                    "1. Listar projetos atuais\n" +
                    "2. Listar projetos antigos\n" +
                    "3. Registar conta\n" +
                    "4. Login\n" +
                    "0. Sair");

            try {
                texto = reader.readLine();
                out.writeUTF(texto);
            } catch (Exception e) {
            	System.err.println(e);
            }


            switch (Integer.parseInt(texto)){
                case 0:
                    System.exit(0);
                case 1:
                    System.out.println("Escolha o numero do projeto que quer consultar. Pressione 0" +
                            " para voltar para tras");
                    System.out.println(in.readUTF());
                    try {
                        texto = reader.readLine();
                        out.writeUTF(texto);
                    } catch (Exception e) {
                    	System.err.println(e);
                    }
                    if (texto.equals("0")){
                        continue;
                    }
                    else{
                        System.out.println(in.readUTF());
                    }
                    break;
                case 2:
                    System.out.println("Escolha o numero do projeto que quer consultar. Pressione 0" +
                            " para voltar para tras");
                    System.out.println(in.readUTF());
                    try {
                        texto = reader.readLine();
                        out.writeUTF(texto);
                    } catch (Exception e) {
                    	System.err.println(e);
                    }
                    if (texto.equals("0")){
                        continue;
                    }
                    else{
                        System.out.println(in.readUTF());
                    }
                    break;
                case 3:
                    data = "0";
                    String res = "0";
                    while (res.equals("0")) {
                        System.out.println("Insira o username que deseja: ");
                        try {
                            texto = reader.readLine();
                            out.writeUTF(texto);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Insira a password que deseja: ");
                        try {
                            texto = reader.readLine();
                            out.writeUTF(texto);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Confirme a password: ");
                        try {
                            texto = reader.readLine();
                            out.writeUTF(texto);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (in.readUTF().equals("0")) continue; else res = "1";
                    }
                    break;
                case 4:
                    counter = 0;
                    data = "0";
                    while (data.equals("0")){
                        if (counter > 3){
                            System.out.println("Demasiadas tentativas falhadas. A desligar aplicacao.");
                            System.exit(0);
                        }
                        System.out.println("Username:");
                        try {
							texto = reader.readLine();
                            out.writeUTF(texto);
						} catch (IOException e) {
							e.printStackTrace();
						}

                        System.out.println("Password:");
                        try {
							auxiliar = reader.readLine();
                            out.writeUTF(auxiliar);
						} catch (IOException e) {
							e.printStackTrace();
						}
                        if ((in.readUTF().equals("1"))) data = "1";
                        else counter++;

                    }
                    user = Integer.parseInt(in.readUTF());
                    LoggedIn(user,s,in,out);
                    break;
            }
        }
    }
}
