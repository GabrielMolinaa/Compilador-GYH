package sintatico;

public class erroSintatico extends RuntimeException {
	
	private static final long serialVersionUID = 4928599035264976611L;

	public erroSintatico(String message) {
	        super(message);
	    }
}
