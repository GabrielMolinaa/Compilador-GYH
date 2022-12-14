package sintatico;

import lexico.GyhLexico;

public class MainClass {

	public static void main(String[] args) {
		GyhLexico lexico = new GyhLexico(args[0]);
		GyhSintatico sintatico = new GyhSintatico(lexico);
		sintatico.programa();
	}

}//fim MainClass
