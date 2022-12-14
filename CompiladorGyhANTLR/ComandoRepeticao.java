import java.util.ArrayList;

public class ComandoRepeticao extends Comando {

	private String condicao;
	 private ArrayList<Comando> listaRep; // Lista de comandos do while
	
	    public ComandoRepeticao(String condicao, ArrayList<Comando> listaComando) {
	        this.condicao = condicao;
	        this.listaRep = new ArrayList<>(listaComando);
	    }
	 
	 
	@Override
	public String generateCode() {
        // Inicia a string da repetição
        StringBuilder str = new StringBuilder("\twhile(" + condicao + "){\n");

        // Adiciona cada comando dentro da repetição
        for (Comando comando : listaRep) {
            str.append("\t");
            str.append(comando.generateCode());
        }

        str.append("\t}\n\n");

        return str.toString();
    }


	@Override
	public String toString() {
		return "ComandoRepeticao [condicao=" + condicao + ", listaRep=" + listaRep + "]";
	}
	
	

}
