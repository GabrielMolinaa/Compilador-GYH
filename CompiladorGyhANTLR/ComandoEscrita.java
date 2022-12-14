
public class ComandoEscrita extends Comando {
	
	private String variavel;
	private GyhSymbol var;
	private int flag;
	
	public ComandoEscrita(String variavel, GyhSymbol var,int flag) {
		this.variavel = variavel;
		this.var = var;
		this.flag = flag; //flag para verificar se é uma cadeia
	}
	
	//Construtor para cadeia
	public ComandoEscrita(String variavel,int flag) {
		this.variavel = variavel;
	}
	
	@Override
	public String generateCode() {
		
		//Printa de acordo com a variável (INT ou REAL)
		//Flag Verifica se é cadeia para nao ter erro de var.type NULL
		
		if(flag == 1) {
        if (var.type == 0)
            return "\tprintf(\"%d\\n\", " + variavel + ");\n";

            
        else if (var.type == 1)
            return "\tprintf(\"%lf\\n\", " + variavel + ");\n";
		}else return "\tprintf(\"" + variavel.replace("\"", "") + "\\n\");\n";
		
		return variavel;
		
		 
	}

}
