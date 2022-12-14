grammar LinguagemGyh;

//Simbolos úteis ANTLR
// '?' - predicado opcional ou semântico
// '~' - não combina/match not
// '*' - 0 ou mais
// '+' - 1 ou mais
// '$' - atributo
// '@' - ação
// '+=' - atribuição lista/list label assignment



@header{
 import java.util.ArrayList;
 import java.util.Stack;
}

//Metodos e Atributos para ser utilizados
@members {
	private String varName;
	private String varValue;
	private String varType; 
	private GyhSymbol varSymbol;
	private SymbolTableGyh symbolTable = new SymbolTableGyh();
	
	
    private String leVar=""; //Grava Leitura
    private String escreveVar="";//Grava Escrita
    private String decisao = "";// Grava Decisão
    private String varAtrib = "";  // Grava Atribuição
    private String varExp = "";    // Grava Expressão
    
    private ProgramaGyh programa = new ProgramaGyh();
    private ArrayList<Comando> listaComandos;
	private Stack<ArrayList<Comando>> stack = new Stack<ArrayList<Comando>>();
	private ArrayList<Comando> listaTrue;
	private ArrayList<Comando> listaFalse;
	
	// Metodo para verificar se uma variável foi declarada e marcar que essa variável foi utilizada
    public void verificarVar(String varName){
        if(!symbolTable.verificaSimbolo(varName)){
            throw new SemanticError("Erro Semântico: Variável "+"'" + varName + "'" + " não declarada");
        }//if
        if (!symbolTable.getSimbolo(varName).foiUtilizada()) {
            symbolTable.getSimbolo(varName).setUtilizada();
        }//if
    }//verificaVar
    

    //Metodo que gera código C (UHUL!)
    public void generateCode(String filename){
    	programa.generateTarget(filename);
    
    }
	
}

programa:':' 'DEC' listaDeclaracoes ':' 'PROG' listaComandos EOF
{ 
	programa.setTabelaVariaveis(symbolTable);
	programa.setComandos(stack.pop());
};

	
listaDeclaracoes: (declaracao)+;			
				  
declaracao: Var  Delim ('REAL' | 'INT') {
	varName = _input.LT(-3).getText();
    varType = _input.LT(-1).getText();
    varSymbol = new GyhSymbol(varName, varType);

    // Adiciona Variável a tabela de símbolos e verifica se ela já existe
    if(!symbolTable.verificaSimbolo(varSymbol.getName())) {
        symbolTable.addSimbolo(varSymbol);
        //System.out.println("Simbolo Adicionado! "+varSymbol);
    } else {
        throw new SemanticError("Erro Semântico: Variável " + varSymbol.toString() + " já existe");
    }
};

expressaoAritmetica: termoAritmetico (('+' {varExp+=_input.LT(-1).getText();} | '-') termoAritmetico)*;


termoAritmetico: fatorAritmetico (('*'{varExp+=_input.LT(-1).getText();} | '/'
 {
 varExp+=_input.LT(-1).getText();
	if(varType.equals("INT")){
		System.out.println("Erro Semântico: INT não recebe REAL"); //nao colocar exception
	}
})termoAritmetico)*;



fatorAritmetico: '-'? {if (_input.LT(-1).getText().equals("-")) varExp+=_input.LT(-1).getText();} //Tratar numero negativo
NumInt {varExp+=_input.LT(-1).getText();} 
| '-'? {if (_input.LT(-1).getText().equals("-")) varExp+=_input.LT(-1).getText();} //Tratar numero negativo
 
NumReal 
{
 	if(varType.equals("INT")){ 
 		throw new SemanticError("Erro Semântico: INT não recebe REAL");
}
 	String[] digitos = _input.LT(-1).getText().split("\\.");
    String real = _input.LT(-1).getText();
	varExp+=real;
 }  
 		
|'-'? {if (_input.LT(-1).getText().equals("-")) varExp+=_input.LT(-1).getText();} //Tratar numero negativo
Var {
 	verificarVar(_input.LT(-1).getText());
	varExp+=_input.LT(-1).getText();
	if(varType.equals("INT") && symbolTable.getSimbolo(_input.LT(-1).getText()).getType().equals("REAL")){
		throw new RuntimeException("Erro Semântico: INT não recebe REAL");
	}
}

| AbrePar {varExp+=_input.LT(-1).getText();} expressaoAritmetica FechaPar {varExp+=_input.LT(-1).getText();};


expressaoRelacional: termoRelacional (operadorBooleano termoRelacional)*;


termoRelacional: {varExp="";}
expressaoAritmetica OpRel 
{varExp+=" " + _input.LT(-1).getText() + " ";} 
expressaoAritmetica | AbrePar {varExp=" " + _input.LT(-1).getText() + " ";} expressaoRelacional FechaPar {varExp+=" " + _input.LT(-1).getText() + " ";};


listaComandos: 
{ 
	listaComandos = new ArrayList<Comando>();
	stack.push(listaComandos);
} (comando)+;
				


comando: comandoAtribuicao | comandoEntrada | comandoSaida | comandoCondicao | comandoRepeticao | subAlgoritmo;

comandoAtribuicao: Var Atrib 
{ 
  verificarVar(_input.LT(-2).getText());
  varAtrib = _input.LT(-2).getText();	
  varType =  symbolTable.getSimbolo(varAtrib).getType();
  varExp = "";
} 

expressaoAritmetica
{
	varType = "";
	ComandoAtribuicao comando = new ComandoAtribuicao(varAtrib, varExp);
	stack.peek().add(comando);
};

operadorBooleano: 'E' {varExp+=" && ";} | 'OU' {varExp+=" || ";};

comandoEntrada: 'LER' Var
{ 
	verificarVar(_input.LT(-1).getText());
	leVar = _input.LT(-1).getText();
	GyhSymbol var = (GyhSymbol)symbolTable.getSimbolo(leVar);
	ComandoLeitura comando = new ComandoLeitura(leVar,var);
 	stack.peek().add(comando);

};

comandoSaida: 'IMPRIMIR' (	Var 
							{
 								verificarVar(_input.LT(-1).getText());
 								escreveVar = _input.LT(-1).getText();
 								GyhSymbol var = (GyhSymbol)symbolTable.getSimbolo(escreveVar);
								ComandoEscrita comando = new ComandoEscrita(escreveVar,var,1);
								stack.peek().add(comando);
							}

						| Cadeia
							{
							
							 	ComandoEscrita comando = new ComandoEscrita(_input.LT(-1).getText(),0);
							 	stack.peek().add(comando);
							});


//Isso aqui foi o capiroto para conseguir resolver, só ele vale 100 pontos!!!!!!!! (quase chorei de emoção quando consegui)
comandoCondicao: 'SE' expressaoRelacional {decisao = varExp; varExp = "";} 
				 
				 'ENTAO' {listaComandos = new ArrayList<Comando>(); stack.push(listaComandos);} comando {listaTrue = stack.pop();}
				 
				 ('SENAO'{listaComandos = new ArrayList<Comando>(); stack.push(listaComandos);} comando 
				 			{listaFalse = stack.pop(); 
				    		ComandoDecisao comando = new ComandoDecisao(decisao,listaTrue,listaFalse);
				    		stack.peek().add(comando);
				 })?; 


comandoRepeticao: 'ENQTO' expressaoRelacional { 
											  	decisao = varExp; varExp = "";
				                              	listaComandos = new ArrayList<Comando>(); stack.push(listaComandos);
				                              } 
					comando {
								ComandoRepeticao comando = new ComandoRepeticao(decisao, listaComandos);
                        		listaComandos = stack.pop();
                        		stack.peek().add(comando);
                        	};

subAlgoritmo: 'INI'{varExp = "";} comando* 'FIM';

//LEXICO
PC: 'DEC'| 'PROG' | 'INT' | 'REAL' | 'LER' | 'IMPRIMIR' | 'SE' | 'SENAO' | 'ENTAO' | 'ENQTO' | 'INI' | 'FIM';
Atrib: ':=';
Delim: ':';
OpRel: '>='|'>'|'<='|'<'|'=='|'!=';
OpArit: '+'|'-'|'*'|'/';
AbrePar: '(';
FechaPar: ')';
OpBoolE: 'E';
OpBoolOu: 'OU';
Cadeia: '"'(' '|'\t'|':'| [a-z] | [A-Z] | [0-9] |'á'|'à'|'Á'|'À'|'ã'|'Ã'|'â'|'Â'|'é'|'É'|'ê'|'Ê'|'ç'|'ó'|'Ó'|'í'|'Í')* '"';
Var: [a-z] ([a-z] | [A-Z] | [0-9])*;
NumInt: [0-9]+;
NumReal: [0-9]+'.'[0-9]*;
Coment: '#' ~('\n')* '\n' -> skip;
Ws: (' ' | '\t' | '\r' | '\n') ->skip;