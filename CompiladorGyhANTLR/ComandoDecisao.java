import java.util.ArrayList;

public class ComandoDecisao extends Comando {

	private String condicao;
	private ArrayList<Comando> listaTrue;
	private ArrayList<Comando> listaFalse;
	
	
	public ComandoDecisao(String condicao,ArrayList<Comando> lt,ArrayList<Comando>lf){
		
		this.condicao = condicao;
		this.listaTrue = lt;
		this.listaFalse = lf;
	}
	
	@Override
	public String generateCode(){
		StringBuilder str = new StringBuilder("\tif(" + condicao + "){\n");
		
        for (Comando comando : listaTrue){
            str.append("\t");
            str.append(comando.generateCode());
        }

        str.append("\t}");
		
        //Adiciona os comandos presente na lista de falsos, caso tenha.
        if (listaFalse.size() > 0){
            str.append("else{\n");

            for (Comando comando : listaFalse){
                str.append("\t");
                str.append(comando.generateCode());
            }
            str.append("\t}\n\n");
        }
        
        
        return str.toString();
	}

	@Override
	public String toString() {
		return "ComandoDecisao [condicao=" + condicao + ", listaTrue=" + listaTrue + ", listaFalse=" + listaFalse + "]";
	}
	
	

}
