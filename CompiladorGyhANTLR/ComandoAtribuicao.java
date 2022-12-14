
public class ComandoAtribuicao extends Comando {

	private String id;
	private String expr;
	
	public ComandoAtribuicao(String id, String expr) {
		this.id = id;
		this.expr = expr;
	}
	
	@Override
	public String generateCode() {
		return "\t" + id + " = " + expr + ";\n";
	}
	
	

}
