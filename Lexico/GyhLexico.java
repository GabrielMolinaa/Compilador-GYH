package lexico;

public class GyhLexico {
	Leitor leitor;
	
	public GyhLexico(String arquivo) {
		leitor = new Leitor(arquivo);
	}

	// Verifica qual categoria o lexema pertence
	public Token proximoToken() {
		Token proximo = null;
		
		espacosEComents();
		leitor.confirmar();
		
		proximo = fim();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = palavrasChave();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = operadorAritmetico();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = operadorRelacional();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = booleanos();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = delimitador();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = atribuicao();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = parenteses();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = variaveis();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = numeros();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = cadeia();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		// Caso o lexema não pertença a nenhuma categoria, há erro léxico
		System.err.println("Erro Léxico!" ); 
		System.err.println("Linha: "+ leitor.numeroLinha + "\n" + leitor.toString());
		return null;
	}//proximo token
	
	public int numLinha() {
		return leitor.numeroLinha;
	}
	
	// ======================================================================
	//                   Automatos para cada categoria
	// ======================================================================
	
	private Token operadorAritmetico() {
		int caractereLido = leitor.lerProximoCaractere();
		char c = (char) caractereLido;
		
		switch(c) {
		
		case '*': return new Token(TipoToken.OPAritMult,leitor.getLexema(),leitor.numeroLinha+1);
		case '/': return new Token(TipoToken.OpAritDiv,leitor.getLexema(),leitor.numeroLinha+1);
		case '+': return new Token(TipoToken.OpAritSoma,leitor.getLexema(),leitor.numeroLinha+1);
		case '-': return new Token(TipoToken.OpAritSub,leitor.getLexema(),leitor.numeroLinha+1);
		default: return null;
		}//switch
	}//operadorAritmetico
	
	private Token delimitador() {
		int caractereLido = leitor.lerProximoCaractere();
		char c = (char) caractereLido;
		
		switch(c) {
		case ':': 
			c = (char) leitor.lerProximoCaractere();
			if(c =='=') {
				return new Token(TipoToken.Atrib,leitor.getLexema(),leitor.numeroLinha+1);
			}else {
				leitor.retroceder();
				return new Token(TipoToken.Delim,leitor.getLexema(),leitor.numeroLinha+1);
			}
		default: return null;
		}//switch
	}//delimitador
	
	private Token operadorRelacional() {
		int caractereLido = leitor.lerProximoCaractere();
		char c = (char) caractereLido;
		
		switch(c) {
		
		case '<':
			c = (char) leitor.lerProximoCaractere();
			if(c=='=') {
				return new Token(TipoToken.OpRelMenorIgual,leitor.getLexema(),leitor.numeroLinha+1);
			}else {
				leitor.retroceder();
				return new Token(TipoToken.OpRelMenor,leitor.getLexema(),leitor.numeroLinha+1);
			}
		
		case '>':
			c = (char) leitor.lerProximoCaractere();
			if(c=='=') {
				return new Token(TipoToken.OpRelMaiorIgual,leitor.getLexema(),leitor.numeroLinha+1);
			}else {
				leitor.retroceder();
				return new Token(TipoToken.OpRelMaior,leitor.getLexema(),leitor.numeroLinha+1);
			}
		
		case '=':
			c = (char) leitor.lerProximoCaractere();
			if(c == '=') {
				return new Token(TipoToken.OpRelIgual,leitor.getLexema(),leitor.numeroLinha+1);
			}else {
				return null;
			}
		
		case '!':
			c = (char) leitor.lerProximoCaractere();
			if(c=='=') {
				return new Token(TipoToken.OpRelDif,leitor.getLexema(),leitor.numeroLinha+1);
			}else {
				return null;
			}

		default: return null;

		}//switch
	}//operadorRelacional
	
	
	private Token parenteses() {
		int caractereLido = leitor.lerProximoCaractere();
		char c = (char) caractereLido;
		
		switch(c) {
		case '(': return new Token(TipoToken.AbrePar,leitor.getLexema(),leitor.numeroLinha+1);
		case ')': return new Token(TipoToken.FechaPar,leitor.getLexema(),leitor.numeroLinha+1);
		default: return null;
		}//switch
	}//parentesis
	
	private Token atribuicao() {
		int caractereLido = leitor.lerProximoCaractere();
		char c = (char) caractereLido;
		
		switch(c) {
		case ':':
			c = (char) leitor.lerProximoCaractere();
			if(c == '=') {
				return new Token(TipoToken.Atrib,leitor.getLexema(),leitor.numeroLinha+1);
			}else {
				return null;
			}
		default: return null;
		}//switch
	}//atribuicao
	
	
	private Token numeros() {
		int estado = 1;
		
			while(true) {
				char c = (char) leitor.lerProximoCaractere();
				if(estado == 1) {
					if(Character.isDigit(c)) {
						estado = 2;
					}else {
						return null;
					}
				}else if(estado == 2) {
					if(c=='.') {
						c = (char) leitor.lerProximoCaractere();
						if(Character.isDigit(c)) {
						estado = 3;
					}else {
						return null;
					}
				}else if(!Character.isDigit(c)) {
					leitor.retroceder();
					return new Token(TipoToken.NumInt,leitor.getLexema(),leitor.numeroLinha+1);
					}
				}else if(estado == 3) {
					if(!Character.isDigit(c)) {
					leitor.retroceder();
					return new Token(TipoToken.NumReal,leitor.getLexema(),leitor.numeroLinha+1);
					}//if interno
				}//if externo
			}//while
	}//numeros
	
	private Token variaveis() {
		int estado = 1;
		int aux = 0;
		
		while(true) {
			char c = (char) leitor.lerProximoCaractere();
			aux++;
			
			if(estado == 1) {
				if(Character.isLetter(c) && aux == 1 && Character.isUpperCase(c) ) { 
					return null;
				}else if(Character.isLetter(c)){
					estado = 2;					
				}else {
					return null;
				}
			}else if(estado == 2) {
				if(!Character.isLetterOrDigit(c)) {
					leitor.retroceder();
					return new Token(TipoToken.Var,leitor.getLexema(),leitor.numeroLinha+1);
				}
			}
		}//while
	}//variaveis
	
	
	private Token cadeia() {
		int estado = 1;
		while(true) {
			char c = (char) leitor.lerProximoCaractere();
			if(estado == 1) {
				if(c == '"') {
					estado = 2;
				}else {
					return null;
				}
			}else if(estado == 2) {
				if(c=='\n') {
					return null;
				}
				if(c=='"') {
					return new Token(TipoToken.Cadeia,leitor.getLexema(),leitor.numeroLinha+1);
				}
			}
		}//while
	}//cadeia
	
	private void espacosEComents() {
		int estado = 1;
		while(true) {
			char c = (char) leitor.lerProximoCaractere();
			
			
			if(estado == 1) {
				if(Character.isWhitespace(c)||c== ' ') {
					estado = 2;
				}else if(c == '#') {
					estado = 3;
				}else {
					leitor.retroceder();
					return;
				}
			}else if(estado == 2) {
				if(c == '#') {
					estado = 3;
				}else if(!(Character.isWhitespace(c)||c==' ')) {
					leitor.retroceder();
					return;
				}
			}else if(estado == 3) {
				if(c == '\n') {
					estado = 2;
				}
			}
		}//while
	}//espacosEComents
	
	private Token palavrasChave() {
		while(true) {
			char c = (char) leitor.lerProximoCaractere();
			if(!Character.isLetter(c)) {
				leitor.retroceder();
				String lexema = leitor.getLexema();
				switch(lexema) {
				case "DEC": 
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCDec,lexema,leitor.numeroLinha+1);						
					}
				case "PROG": 
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCProg,lexema,leitor.numeroLinha+1);
					}
				case "INT": 
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCInt,lexema,leitor.numeroLinha+1);
					}
				case "REAL": 
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCReal,lexema,leitor.numeroLinha+1);
					}
				case "LER": 
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCLer,lexema,leitor.numeroLinha+1);
					}
				case "IMPRIMIR":
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCImprimir,lexema,leitor.numeroLinha+1);
					}
				case "SE":
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCSe,lexema,leitor.numeroLinha+1);
					}
				case "SENAO": 
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCSenao,lexema,leitor.numeroLinha+1);
					}
				case "ENTAO": 
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCEntao,lexema,leitor.numeroLinha+1);
					}
				case "ENQTO": 
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCEnqto,lexema,leitor.numeroLinha+1);
					}
				case "INI": 
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCIni,lexema,leitor.numeroLinha+1);
					}
				case "FIM":
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCFim,lexema,leitor.numeroLinha+1);
					}
				default: return null;
				}
			}
		}//while
	}//palavrasChave
	
	private Token booleanos() {
		while(true) {
			char c = (char) leitor.lerProximoCaractere();
			if(!Character.isLetter(c)) {
				leitor.retroceder();
				String lexema = leitor.getLexema();
				
				switch(lexema) {
				case "E": return new Token(TipoToken.OpBoolE,lexema,leitor.numeroLinha+1);
				case "OU": return new Token(TipoToken.OpBoolOu,lexema,leitor.numeroLinha+1);
				default: return null;
				}
			}
		}//while
	}//booleanos
	

	// Token para verificar o fim do arquivo
	// A condição de parada do programa depende do retorno deste Token
	private Token fim() {
		int caractereLido = leitor.lerProximoCaractere();
		if(caractereLido == -1) {
			return new Token(TipoToken.Fim,"Fim",leitor.numeroLinha+1);
		}
		return null;
	}

	// Método para verificar se todas as letras das palavras chave são maiúsculas
	private boolean maiusculas(String teste) {
		for(int i = 0; i < teste.length(); i++) {
			char c = teste.charAt(i);
			
			if(Character.isLowerCase(c)) {
				return false;
			}
			
		}//for
		
		return true;
	}

}