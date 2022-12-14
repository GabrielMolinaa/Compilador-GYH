
public class ComandoLeitura extends Comando {

	private String variavel;
	private GyhSymbol var;
	
	public ComandoLeitura(String variavel, GyhSymbol var) {
		this.variavel = variavel;
		this.var = var;
	}
	
	@Override
	public String generateCode() {
        //Monta o scan de acordo com o tipo da variável
        if (var.type == 0)
            return "\tscanf(\"%d\", &" + variavel + ");\n";

        
        return "\tscanf(\"%lf\", &" + variavel + ");\n\n";
    }
}


