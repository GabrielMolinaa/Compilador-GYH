package sintatico;

import java.util.ArrayList;
import java.util.List;
import lexico.GyhLexico;
import lexico.TipoToken;
import lexico.Token;

public class GyhSintatico {
	
	private final static int TAMANHO_BUFFER = 10;
	List<Token> bufferTokens;
	GyhLexico lexico;
	boolean FimTokens = false;
	int numeroLinha;
	
	
	public GyhSintatico(GyhLexico lexico) {
		this.lexico = lexico;
		bufferTokens = new ArrayList<Token>();
		lerToken();
	}//GyhSintatico
	
	private void lerToken() {
		if(bufferTokens.size() > 0) {
			bufferTokens.remove(0);
		}
		while(bufferTokens.size() < TAMANHO_BUFFER && !FimTokens) {
			Token proximo = lexico.proximoToken();
			bufferTokens.add(proximo);
			if(proximo.nome == TipoToken.Fim) {
				FimTokens = true;
			}//if
		}//while
		System.out.println("Lido: "+ lookahead(1));
		
	}//lerToken
	
	Token lookahead(int pos) {
		
		if(bufferTokens.isEmpty()) {
			return null;
		}//if
		
		return bufferTokens.get(pos-1);
	}//lookahead
	
	void match(TipoToken tipo) {
		if(lookahead(1).nome == tipo) {
			System.out.println("Match: " + lookahead(1));
			lerToken();
		
		}else {
			 erroSintatico(tipo.toString());
		}//else
	}//match
	
	void erroSintatico(String... tokensEsperados) {

		String mensagem = "Erro sintático (Linha: "+ lookahead(1).numeroLinha +"): É esperado: (";
		for(int i = 0; i < tokensEsperados.length; i++) {
			mensagem += tokensEsperados[i];
			if(i < tokensEsperados.length-1) {
				mensagem +=" ou ";
			}
		}
		mensagem += "), mas foi encontrado: "+ lookahead(1);
		throw new erroSintatico(mensagem);
	}
	
	
	
	//Programa → ':' 'DEC' ListaDeclaracoes ':' 'PROG' ListaComandos;
	public void programa() {
		match(TipoToken.Delim);
		match(TipoToken.PCDec);
		listaDeclaracoes();
		match(TipoToken.Delim);
		match(TipoToken.PCProg);
		listaComandos();
	}//fim Programa
	
	//ListaDeclaracoes → Declaracao ListaDeclaracoes | Declaracao;
	 void listaDeclaracoes() {
		if(lookahead(4).nome == TipoToken.Var) {
			declaracao();
			listaDeclaracoes();
		}else if(lookahead(4).nome == TipoToken.Delim) {
			declaracao();
		}else {
			//erroSintatico(":", "VARIÁVEL");
			 erroSintatico(":","VARIÁVEL");
		}
	}//fim listaDeclaracoes
	
	//Declaracao → VARIAVEL ':' TipoVar;
	 void declaracao() {
		match(TipoToken.Var);
		match(TipoToken.Delim);
		tipoVar();
	}//fim Declaracao
	
	//TipoVar → 'INT' | 'REAL';
	 void tipoVar() {
		 if(lookahead(1).nome == TipoToken.PCInt) {
			 match(TipoToken.PCInt);
		 }else if(lookahead(1).nome == TipoToken.PCReal) {
			 match(TipoToken.PCReal);
		 }else {
			 erroSintatico("INT","REAL");
		 }
	}//tipoVar
	 
	 
	 //ExpressaoAritmetica → TermoAritmetico 'ExpressaoAritmetica’;
	 void expressaoAritmetica() {
		 termoAritmetico();
		 expressaoAritmetica_();
	 }//expressaoAritmetica
	 
	//ExpressaoAritmetica_ → ExpressaoAritmetica__ ExpressaoAritmetica_ | palavra_vazia
	 void expressaoAritmetica_() {
		 if(lookahead(1).nome == TipoToken.OpAritSub || lookahead(1).nome == TipoToken.OpAritSoma) {
			 expressaoAritmetica__();
			 expressaoAritmetica_();
		 }else {
			 //palavra_vazia
		 }
	 }//expressaoAritmetica_
	 
	 //ExpressaoAritmetica__→  ('+' TermoAritmetico | '-' TermoAritmetico)
	 void expressaoAritmetica__() {
		 if(lookahead(1).nome == TipoToken.OpAritSoma) {
			 match(TipoToken.OpAritSoma);
			 termoAritmetico();
		 }else if(lookahead(1).nome == TipoToken.OpAritSub) {
			 match(TipoToken.OpAritSub);
			 termoAritmetico();
		 }else {
			 erroSintatico("+","-");
		 }
	 }//expressaoAritmetica__
	 
	 //TermoAritmetico → FatorAritmetico TermoAritmetico_;
	 void termoAritmetico() {
		 fatorAritmetico();
		 termoAritmetico_();
	 }//termoAritmetico
	 
	 //'TermoAritmetico_ → TermoAritmetico__ TermoAritmetico_ | palavra_vazia
	 void termoAritmetico_() {
		 if(lookahead(1).nome == TipoToken.OPAritMult || lookahead(1).nome == TipoToken.OpAritDiv) {
			 termoAritmetico__();
			 termoAritmetico_();
		 }else {
			 //palavra vazia
		 }
	 }//termoAritmetico_
	 
	//'TermoAritmetico’ → ('*' FatorAritmetico | '/' FatorAritmetico)
	 void termoAritmetico__() {
		 if(lookahead(1).nome == TipoToken.OPAritMult) {
			 match(TipoToken.OPAritMult);
			 fatorAritmetico();
		 }else if(lookahead(1).nome == TipoToken.OpAritDiv) {
			 match(TipoToken.OpAritDiv);
			 fatorAritmetico();
		 }else {
			 erroSintatico("*","/");
		 }
	 }//termoAritmetico__
	 
	 //FatorAritmetico → NUMINT| NUMREAL | VARIAVEL | '(' ExpressaoAritmetica ')'
	 void fatorAritmetico() {
		 if(lookahead(1).nome == TipoToken.NumInt) {
			 match(TipoToken.NumInt);
		 }else if(lookahead(1).nome == TipoToken.NumReal) {
			 match(TipoToken.NumReal);
		 }else if(lookahead(1).nome == TipoToken.Var) {
			 match(TipoToken.Var);
		 }else if(lookahead(1).nome == TipoToken.AbrePar) {
			 match(TipoToken.AbrePar);
			 expressaoAritmetica();
			 match(TipoToken.FechaPar);
		 }else {
			 erroSintatico("NumInt","NumReal","Variável","(");
		 }
	 }//fatorAritmetico
	 
	 //ExpressaoRelacional → TermoRelacional ‘ExpressaoRelacional‘;
	 void expressaoRelacional() {
		 termoRelacional();
		 expressaoRelacional_();
	 }//expressaoRelacional
	 
	 //‘ExpressaoRelacional‘ → OperadorBooleano ‘ExpressaoRelacional‘ | palavra_vazia
	 //‘ExpressaoRelacional‘ → TermoRelacional ‘ExpressaoRelacional’ | palavra_vazia 
	 void expressaoRelacional_() {
		 if(lookahead(1).nome == TipoToken.OpBoolE || lookahead(1).nome == TipoToken.OpBoolOu) {
			 operadorBooleano();
			 termoRelacional();
			 expressaoRelacional_();
		 }else {
			 //vazio
		 }
	 }//expressaoRelacional_()
	 
	 
	 //TermoRelacional → ExpressaoAritmetica OP_REL ExpressaoAritmetica | '(' ExpressaoRelacional ')';
	 void termoRelacional() {
		 if(lookahead(1).nome == TipoToken.NumInt || lookahead(1).nome == TipoToken.NumReal || lookahead(1).nome == TipoToken.Var || lookahead(1).nome == TipoToken.AbrePar) {
			 expressaoAritmetica();
			 opRel();
			 expressaoAritmetica();
//		 }else if(lookahead(1).nome == TipoToken.AbrePar) {
//			 match(TipoToken.AbrePar);
//			 expressaoRelacional();
//			 match(TipoToken.FechaPar);
		 }else {
			 erroSintatico("NumInt","NumReal","Variável","(");
		 }
	 }//termoRelacional
	 
	 
	 void opRel() {
		 if(lookahead(1).nome == TipoToken.OpRelDif) {
			 match(TipoToken.OpRelDif);
		 }else if(lookahead(1).nome == TipoToken.OpRelIgual) {
			 match(TipoToken.OpRelIgual);
		 }else if(lookahead(1).nome == TipoToken.OpRelMaior) {
			 match(TipoToken.OpRelMaior);
		 }else if(lookahead(1).nome == TipoToken.OpRelMaiorIgual) {
			 match(TipoToken.OpRelMaiorIgual);
		 }else if(lookahead(1).nome == TipoToken.OpRelMenor) {
			 match(TipoToken.OpRelMenor);
		 }else if(lookahead(1).nome == TipoToken.OpRelMenorIgual) {
			 match(TipoToken.OpRelMenorIgual);
		 }else {
			 erroSintatico("==","<","<=",">",">=");
		 }
	 }//opRel
	 
	 //OperadorBooleano → 'E' | 'OU';
	 void operadorBooleano() {
		 if(lookahead(1).nome == TipoToken.OpBoolE) {
			 match(TipoToken.OpBoolE);
		 }else if(lookahead(1).nome == TipoToken.OpBoolOu) {
			 match(TipoToken.OpBoolOu);
		 }else {
			 erroSintatico("E","OU");
		 }
	 }
	 
	 //ListaComandos → Comando ListaComandos | Comando;
	 //Fatorado::
	 //ListaComandos → Comando (ListaComandos | palavra_vazia)
	 void listaComandos() {
		 comando();
		 listaComandos_();
	 }//listaComandos
	 
	 
	 void listaComandos_() {
		 if(lookahead(1).nome == TipoToken.Var || lookahead(1).nome == TipoToken.PCLer || lookahead(1).nome == TipoToken.PCImprimir || lookahead(1).nome == TipoToken.PCSe || lookahead(1).nome == TipoToken.PCEnqto || lookahead(1).nome == TipoToken.PCIni) {
			 listaComandos();
		 }else {
			 //palavra_vazia
		 }
	 }//listaComandos_
	 
	 
	 //Comando → ComandoAtribuicao | ComandoEntrada | ComandoSaida | ComandoCondicao | ComandoRepeticao | SubAlgoritmo;
	 void comando() {
		 if(lookahead(1).nome == TipoToken.Var) {
			 if(lookahead(2).nome == TipoToken.Atrib){
			 comandoAtribuicao();
			 }else {
				 match(TipoToken.Var);
				 erroSintatico(":=");
			 }
		 }else if(lookahead(1).nome == TipoToken.PCLer) {
			 comandoEntrada();
		 }else if(lookahead(1).nome == TipoToken.PCImprimir) {
			 comandoSaida();
		 }else if(lookahead(1).nome == TipoToken.PCSe) {
			 comandoCondicao();
		 }else if(lookahead(1).nome == TipoToken.PCEnqto) {
			 comandoRepeticao();
		 }else if(lookahead(1).nome == TipoToken.PCIni) {
			 subAlgoritmo();
		 }else {
			 erroSintatico(":=","LER","IMPRIMIR","SE","ENQTO","INI");
		 }
	 }//comando
	 
	 //ComandoAtribuicao → VARIAVEL ':=' ExpressaoAritmetica;
	 void comandoAtribuicao() {
		 match(TipoToken.Var);
		 match(TipoToken.Atrib);
		 expressaoAritmetica();
	 }//comandoAtribuicao
	 
	 //ComandoEntrada → 'LER' VARIAVEL;
	 void comandoEntrada() {
		 match(TipoToken.PCLer);
		 match(TipoToken.Var);
	 }//comandoEntrada
	 
	 //ComandoSaida → 'IMPRIMIR'  (VARIAVEL | CADEIA)
	 //FATORADO
	 void comandoSaida() {
		 match(TipoToken.PCImprimir);
		 comandoSaida_();
	 }//comandoSaida
	 
	//ComandoSaida_ →  VARIAVEL | CADEIA
	 void comandoSaida_() {
		 if(lookahead(1).nome == TipoToken.Var) {
			 match(TipoToken.Var);
		 }else if(lookahead(1).nome == TipoToken.Cadeia) {
			 match(TipoToken.Cadeia);
		 }else {
			 erroSintatico("VARIÁVEL","CADEIA");
		 }
	 }//comandoSaida_
	 
	 //ComandoCondicao → 'SE' ExpressaoRelacional 'ENTAO' Comando comandoCondicao_
	 void comandoCondicao() {
		 match(TipoToken.PCSe);
		 expressaoRelacional();
		 match(TipoToken.PCEntao);
		 comando();
		 comandoCondicao_();
	 }//comandoCondicao
	 
	 //comandoCondicao_ -> 'SENAO' Comando | palavra_vazia 
	 void comandoCondicao_() {
		 if(lookahead(1).nome == TipoToken.PCSenao) {
			 match(TipoToken.PCSenao);
			 comando();
		 }else {
			 //palavra_vazia
		 }
	 }//comandoCondicao_
	 
	 //ComandoRepeticao → 'ENQTO' ExpressaoRelacional Comando;
	 void comandoRepeticao() {
		 match(TipoToken.PCEnqto);
		 expressaoRelacional();
		 comando();
	 }//comandoRepeticao
	 
	 //SubAlgoritmo → 'INI' ListaComandos 'FIM';
	 void subAlgoritmo() {
		 match(TipoToken.PCIni);
		 listaComandos();
		 match(TipoToken.PCFim);
	 }//subAlgoritmo
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
}
