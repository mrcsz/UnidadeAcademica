/*
 * @author Marcos Souza
 */
package UA;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;

public class Processos {

	private static Scanner input = new Scanner(System.in);
	// recursos disponiveis

	public static List<Recursos> carregarRecursos() {
		List<Recursos> l_recursos = new ArrayList<Recursos>();
		l_recursos.add(new Recursos("LAB01", "Laboratorio 01", 0, 0, 106, ""));
		l_recursos.add(new Recursos("LAB02", "Laboratorio 02", 0, 0, 107, ""));
		l_recursos.add(new Recursos("AUD01", "Auditorio 01", 0, 0, 108, ""));
		l_recursos.add(new Recursos("AUD02", "Auditorio 02", 0, 0, 109, ""));
		l_recursos.add(new Recursos("SALA01", "Sala de Aula 01", 0, 0, 110, ""));
		l_recursos.add(new Recursos("SALA02", "Sala de Aula 02", 0, 0, 111, ""));
		l_recursos.add(new Recursos("SALA03", "Sala de Aula 03", 0, 0, 112, ""));
		l_recursos.add(new Recursos("SALA04", "Sala de Aula 04", 0, 0, 113, ""));
		l_recursos.add(new Recursos("SALA05", "Sala de Aula 05", 0, 0, 114, ""));
		l_recursos.add(new Recursos("SALA06", "Sala de Aula 06", 0, 0, 115, ""));
		l_recursos.add(new Recursos("SALA07", "Sala de Aula 07", 0, 0, 116, ""));
		l_recursos.add(new Recursos("SALA08", "Sala de Aula 08", 0, 0, 117, ""));
		l_recursos.add(new Recursos("PROJ01", "Projetor 01", 0, 0, 101, ""));
		l_recursos.add(new Recursos("PROJ02", "Projetor 02", 0, 0, 102, ""));
		l_recursos.add(new Recursos("PROJ03", "Projetor 03", 0, 0, 103, ""));
		l_recursos.add(new Recursos("PROJ04", "Projetor 04", 0, 0, 104, ""));
		l_recursos.add(new Recursos("PROJ05", "Projetor 05", 0, 0, 105, ""));

		return l_recursos;
	}

	public static String status(int status) {
		String situacao = null;

		switch (status) {
		case 0:
			return "** Em processo de alocacao **";
		case 1:
			return "** Alocado **";
		case 2:
			return "** Em andamento **";
		case 3:
			return "** Concluido **";
		}
		return situacao;
	}

	public static String responsavel(int id, List<Usuario> users) {

		for (int i = 0; i < users.size(); i++) {
			Usuario us = (Usuario) users.get(i);
			if (id == us.getId()) {

				return (" Resp: " + us.getId() + " " + us.getNome() + " " + us.getSobrenome());
			}
		}

		return "";
	}

	public static boolean vericacaoDeDisponibilidade(String cod_recurso, List<Recursos> l_recursos) {
		boolean autorizacao = false;
		int verificador = 0;

		for (int i = 0; i < l_recursos.size(); i++) {
			Recursos lr = (Recursos) l_recursos.get(i);

			if (cod_recurso.equals(lr.getId())) {

				if (lr.getStatus() == 0) {
					autorizacao = true;
					verificador = 2;
					break;
				} else {
					verificador = 1;
				}
			}
		}

		if (verificador == 0)
			System.out.println("cod de recurso invalido!");
		if (verificador == 1)
			System.out.println("Recurso nao disponivel");
		return autorizacao;
	}

	public static List<Atividade> cadastrarAtividade(List<Atividade> l_atividades, int atividade, int id_user,
			String titulo, String descricao, String participantes, String materialApoio, String dataHoraInicio,
			String dataHoraFim, String recurso) {

		Atividade ativ = new Atividade(atividade, id_user, titulo, descricao, participantes, materialApoio,
				dataHoraInicio, dataHoraFim, recurso);
		l_atividades.add(ativ);

		return l_atividades;
	}

	public static boolean autorizacaoAdm(List<Usuario> users, String titulo, String descricao, String participantes,
			String materialApoio, String dataHoraInicio, String dataHoraFim) {
		boolean bool = false;
		String login, senha;

		if (validarDatas(dataHoraInicio, dataHoraFim) && !(titulo == null || titulo.equals("") || descricao == null
				|| descricao.equals("") || participantes == null || participantes.equals("") || materialApoio == null
				|| materialApoio.equals(""))) {

			System.out.println("Verificando consistencia de dados...");
			System.out.println("Digite o usuario e a senha do administrador para confirmar alocacao de Recurso");
			System.out.print("user: ");
			login = input.next();
			System.out.print("senha: ");
			senha = input.next();

			for (int i = 0; i < users.size(); i++) {
				Usuario us = (Usuario) users.get(i);

				if (login.equals(us.getLogin()) && senha.equals(us.getSenha()) && us.getTipo() == 1) {

					System.out.println("Autorizado, status modificado para \"Alocado\"");
					bool = true;
					break;
				}
			}
		} else {
			System.out.println("Voce deve preencher todos campos solicitados corretamente...\nalocacao cancelada...");
		}

		if (!bool)
			System.out.println("Alocacao de recurso nao autorizada...");
		return bool;
	}

	private static boolean validarDatas(String dataHoraInicio, String dataHoraFim) {

		boolean bool = false;

		Calendar dataAtual = new GregorianCalendar();
		Calendar dhi = Calendar.getInstance();
		Calendar dhf = Calendar.getInstance();
		SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		dhi.setLenient(false);
		dhf.setLenient(false);

		try {

			dhi.setTime(formatoData.parse(dataHoraInicio));
			dhf.setTime(formatoData.parse(dataHoraFim));
			bool = true;
		} catch (ParseException ex) {
			bool = false;
		}
		if (bool) {

			// verificar se a data de inicio acontece dps da data atual
			// verificar se a data final eh depois da data inicial
			if (!(dhi.after(dataAtual) && dhf.after(dhi))) {
				bool = false;
			}
		}

		if (!bool)
			System.out.println("Datas de inicio/fim invalidas...");
		return bool;
	}

	public static void listarAtividades(List<Atividade> l_atividades, String cod_rec, List<Usuario> users) {
		for (int i = 0; i < l_atividades.size(); i++) {
			Atividade lat = (Atividade) l_atividades.get(i);

			if (cod_rec.equals(lat.getRecurso())) {
				System.out.println("Tipo de atividade: " + Atividade.nomeAtividade(lat.getTipo())
						+ "\nUsuario Alocador: " + Usuario.NomeUser(lat.getUser(), users) + "\nTitulo da Atividade: "
						+ lat.getTitulo() + "\n");
			}
		}

	}

	public static void autorizacaoResp(List<Recursos> l_recursos, List<Usuario> users) {

		String id_rec;
		int id_user = 0;
		boolean autentic = false;

		System.out.println("--- Recursos que aguardam autorizacao do responsavel ---");
		for (int i = 0; i < l_recursos.size(); i++) {
			Recursos lr = (Recursos) l_recursos.get(i);

			if (1 == lr.getStatus()) {
				System.out.print("--> " + lr.getId() + " ");
				System.out.println(Processos.responsavel(lr.getResponsavel(), users));
			}
		}

		System.out.print("\nid do recurso que vc deseja autorizar: ");

		id_rec = input.next().toUpperCase();

		// verificando se o user esta em algum outro processo "Em andamento"
		// 1 coletando o usuario alocador
		for (int i = 0; i < l_recursos.size(); i++) {
			Recursos lr = (Recursos) l_recursos.get(i);

			if (id_rec.equals(lr.getId())) {
				id_user = lr.getUsuario_alocador();

			}
		}

		// agora verificando se ele tem algum processo "Em andamento"
		for (int i = 0; i < l_recursos.size(); i++) {
			Recursos lr = (Recursos) l_recursos.get(i);

			if (id_user == lr.getUsuario_alocador() && lr.getStatus() == 2) {
				System.out
						.println("o usuario " + lr.getUsuario_alocador() + " ja possui um outro recurso em andamento");
				System.out.println("autorizacao nao permitida...");
				return;
			}
		}

		System.out.println("--- Entre com login e senha do responsavel ---\n");

		autentic = Usuario.autenticar(users);

		for (int i = 0; i < l_recursos.size(); i++) {
			Recursos lr = (Recursos) l_recursos.get(i);

			if (1 == lr.getStatus() && id_rec.equals(lr.getId()) && autentic) {
				lr.setStatus(2);
				System.out.println("efetuado, status modificado para \"Em andamento\" ");
				return;
			}

		}

		System.out.println("Dados nao conferem, autorizacao nao efetuada\n");
	}

	public static List<Recursos> confirmarConclusao(List<Recursos> l_recursos, List<Atividade> l_atividades,
			List<Usuario> users) {
		String login, senha, id_rec;
		System.out.println("--- Recursos que aguardam confirmacao de conclusao ---");
		for (int i = 0; i < l_recursos.size(); i++) {
			Recursos lr = (Recursos) l_recursos.get(i);

			if (2 == lr.getStatus()) {
				System.out.print("--> " + lr.getId() + " ");
				System.out.println(Processos.responsavel(lr.getResponsavel(), users));
			}
		}

		System.out.print("\nid do recurso que vc deseja confirmar a conclusao: ");

		id_rec = input.next().toUpperCase();

		for (int i = 0; i < l_recursos.size(); i++) {
			Recursos lr = (Recursos) l_recursos.get(i);

			if (2 == lr.getStatus() && id_rec.equals(lr.getId())) {

				for (int j = 0; j < l_atividades.size(); j++) {
					Atividade lat = (Atividade) l_atividades.get(j);

					if (lat.getDescricao() != null && !("".equals(lat.getDescricao()))) {
						System.out.println("Digite o usuario e a senha do administrador para confirmar a conclusao");
						System.out.print("usuario: ");
						login = input.next();
						System.out.print("senha: ");
						senha = input.next();

						for (int k = 0; k < users.size(); k++) {
							Usuario us = (Usuario) users.get(k);

							if (login.equals(us.getLogin()) && senha.equals(us.getSenha()) && us.getTipo() == 1) {

								System.out.println("Autorizado, status modificado para \"Concluido\"");
								lr.setStatus(3);
								return l_recursos;
							}
						}
						System.out.println("confirmacao nao concluida, usuario nao adm/Login ou senha Errados");
						return l_recursos;
					}
				}
				System.out.println("confirmacao nao concluida, Descricao nao pode ser vazia");
				return l_recursos;
			}
		}
		System.out.println("confirmacao nao concluida, id nao encontrado/Nao existe nenhum recurso a confirmar");
		return l_recursos;
	}

	public static List<Recursos> disponibizarRecursos(List<Recursos> l_recursos, List<Usuario> users) {

		System.out.println("--- Recursos a liberar  ---\n");
		for (int i = 0; i < l_recursos.size(); i++) {
			Recursos lr = (Recursos) l_recursos.get(i);

			if (3 == lr.getStatus()) {
				System.out.print("--> " + lr.getId() + " ");
				System.out.println(Processos.responsavel(lr.getResponsavel(), users));
			}
		}

		System.out.print("\nid do recurso que vc deseja liberar: ");

		String id_rec = input.next().toUpperCase();
		boolean bool = false;

		for (int i = 0; i < l_recursos.size(); i++) {
			Recursos lr = (Recursos) l_recursos.get(i);

			if (id_rec.equals(lr.getId()) && lr.getStatus() == 3) {
				if (bool = Processos.isAdm(users)) {
					lr.setStatus(0);
					System.out.println("\n**  Recurso liberado com sucesso **");
					return l_recursos;
				}
			}
		}

		if (!bool)
			System.out.println("Id nao encontrada e/ou recurso com status diferente de\"Concluido\" ");
		return l_recursos;
	}

	private static boolean isAdm(List<Usuario> users) {
		System.out.println("Digite o usuario e a senha do administrador para confirmar a conclusao");
		System.out.print("usuario: ");
		String login = input.next();
		System.out.print("senha: ");
		String senha = input.next();

		for (int i = 0; i < users.size(); i++) {
			Usuario us = (Usuario) users.get(i);

			if (login.equals(us.getLogin()) && senha.equals(us.getSenha()) && us.getTipo() == 1) {
				return true;
			}
		}

		System.out.println("Usuario nao autorizado..");
		return false;
	}

	public static void consultarUser(List<Usuario> users, List<HistRecursos> histRec) {
		int id = 0;

		System.out.println("id do usuario a ser consultado: ");
		
		try {
			id = input.nextInt();
		
		} catch (Exception e) {
			
			System.out.println("Digite um numero inteiro..");
			input.nextLine();
			return;
		}

		for (int i = 0; i < users.size(); i++) {
			Usuario us = (Usuario) users.get(i);

			if (id == us.getId()) {
				System.out.println(us.getId() + " " + us.getNome() + " " + us.getSobrenome() + " " + us.getEmail());
				System.out.println("--- Recursos alocados ---\n");
				Processos.imprimirRecursosAlocados(us.getId(), histRec);
				return;
			}
		}
		System.out.println("Usuario nao encontrado");
	}

	private static void imprimirRecursosAlocados(int id, List<HistRecursos> histRec) {
		for (int i = 0; i < histRec.size(); i++) {
			HistRecursos hr = (HistRecursos) histRec.get(i);

			if (hr.getId_user() == id) {
				System.out.println(hr.getCod_rec() + " " + Atividade.nomeAtividade(hr.getAtividade()));
			}
		}
	}

	public static void consultarRecurso(List<Recursos> l_recursos, List<Atividade> l_atividades, List<Usuario> users) {
		System.out.print("digite o codigo do recurso desejado: ");
		String cod_rec = input.next().toUpperCase();

		for (int i = 0; i < l_recursos.size(); i++) {

			Recursos lr = (Recursos) l_recursos.get(i);
			if (cod_rec.equals(lr.getId())) {
				System.out.println(
						"\n" + lr.getId() + " \"" + lr.getNome_recurso() + "\"" + "\natividades Realizadas:\n");
				Processos.listarAtividades(l_atividades, cod_rec, users);
				return;
			}
		}
		System.out.println("cod invalido..");
	}

	public static void solicitarRecurso(List<Recursos> l_recursos, List<Usuario> users,
			List<HistRecursos> historicoRecurso, List<Atividade> l_atividades, CodigoAutomatico somador) {
		System.out.print("cod do recurso desejado: ");
		String cod_recurso = input.next().toUpperCase();
		
		boolean permissao = Processos.vericacaoDeDisponibilidade(cod_recurso, l_recursos);

		if (permissao) {
			System.out.println("Recurso disponivel");
			System.out.print("id do usuario alocador: ");
			
			int id_user = 0;
			try { 
				id_user  = input.nextInt();
			} catch (Exception e){
				System.out.println();
				System.out.println("O id eh um numero inteiro.. voltando ao menu");
				input.nextLine();
				return;
			}

			int atividade = 0;
			boolean perm = false;
			if (Usuario.verificacaoDeUsuarioValido(id_user, users)) {
				
				System.out.print(
						"\n1 - Aula tradicional\n2 - Apresentacoes\n3 - laboratorios\n0 - sair\nAtividade a realizar: ");
				atividade = input.nextInt();

				while (atividade < 1 || atividade > 3) {
					System.out.print(
							"\n1 - Aula tradicional\n2 - Apresentacoes\n3 - laboratorios\n0 - sair\nAtividade a realizar: ");
					atividade = input.nextInt();
					
					if (atividade == 0) {
						System.out.println("Voltando ao menu principal..");
						return;
					}
						
				}
				
				perm = false;
				if (atividade == 2) {
					perm = true;
				} else if (atividade == 1 || atividade == 3) {
					perm = Usuario.permissaoParaAlocacao(users, id_user);
				} else {
					System.out.println("Voce nao tem permissao para alocar esse espaco");
				}
			}

			String dataHoraInicio = null;
			String dataHoraFim = null;
			String titulo = null;
			String descricao = null;
			String participantes = null;
			String materialApoio = null;
			if (perm) {

				HistRecursos hr = new HistRecursos(id_user, cod_recurso, atividade);
				historicoRecurso.add(hr);
				System.out.println("\n** Detalhes da atividade **\n");
				System.out.print("Data e hora de inicio\n(formato: \"dd/MM/yyyy HH:mm\" ): ");
				input.nextLine();
				dataHoraInicio = input.nextLine();
				System.out.print("Data e hora de fim do uso\n(formato: \"dd/MM/yyyy HH:mm\" ): ");
				dataHoraFim = input.nextLine();
				System.out.print("Titulo da atividade: ");
				titulo = input.nextLine();
				System.out.print("Descricao breve: ");
				descricao = input.nextLine();
				System.out.print("Participantes: ");
				participantes = input.nextLine();
				System.out.print("Material de apoio: ");
				materialApoio = input.nextLine();

			} else {
				System.out.println("Voce nao pode alocar esse recurso");
			}

			permissao = false;
			if (perm)
				permissao = Processos.autorizacaoAdm(users, titulo, descricao, participantes, materialApoio,
						dataHoraInicio, dataHoraFim);
			if (permissao) {

				l_atividades = Processos.cadastrarAtividade(l_atividades, atividade, id_user, titulo, descricao,
						participantes, materialApoio, dataHoraInicio, dataHoraFim, cod_recurso);
				for (int i = 0; i < l_recursos.size(); i++) {
					Recursos lr = (Recursos) l_recursos.get(i);

					if (cod_recurso.equals(lr.getId())) {

						lr.setStatus(1);
						lr.setUsuario_alocador(id_user);
						somador.somadorDeAlocacoes();
						switch (atividade) {
						case 1:
							somador.somadorAulaTrad();
							break;
						case 2:
							somador.somadorApres();
							break;
							
						case 3:
							somador.somadorlab();
							break;
								
						default:
							break;
						}
						
						return;
					}
				}

			}

		}
	}

	public static void relatorioDeAtividades(List<Usuario> users, List<Recursos> l_recursos,
			List<Atividade> l_atividades, CodigoAutomatico total) {

		System.out.println("Usuarios cadastrados: " + users.size());
		System.out.println("Recursos \"Em processo de alocacao\": " + Recursos.emAlocacao(l_recursos));
		System.out.println("Recursos \"Alocado\": " + Recursos.alocado(l_recursos));
		System.out.println("Recursos \"Em Andamento\": " + Recursos.emAndamento(l_recursos));
		System.out.println("Recursos \"Concluido\": " + Recursos.Concluido(l_recursos));
		System.out.println("Total de alocacoes: " + total.alocacoes());
		System.out.println("Total de aula tradicionais: " + total.aulasTrad());
		System.out.println("Total de apresentacoes: " + total.apresentacoes());
		System.out.println("Total de aulas de laboratorio: " + total.laboratorio());
		
	}
} // fim da classe
