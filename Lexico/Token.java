 package lexico;

public class Token {
	public TipoToken nome;
	public String lexema;
	public int numeroLinha;
	
	public Token(TipoToken nome, String lexema,int numeroLinha) {
		this.nome = nome;
		this.lexema = lexema;
		this.numeroLinha = numeroLinha;
	}
	
	// Método para retornar o lexema lido com o Token atribuido
	@Override
	public String toString() {
		return "<"+nome+","+lexema+","+numeroLinha+">";
	}
	
	public int NumLinha() {
		return this.numeroLinha;
	}
}
