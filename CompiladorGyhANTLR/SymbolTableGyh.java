import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTableGyh {

	private HashMap<String,GyhSymbol> tabela;
	
	public SymbolTableGyh() {
		tabela = new HashMap<String,GyhSymbol>();
	}
	
	public void addSimbolo(GyhSymbol symbol) {
		tabela.put(symbol.getName(), symbol);
	}
	
	public boolean verificaSimbolo(String symbolName) {
		return tabela.get(symbolName) != null;
	}
	
	public GyhSymbol getSimbolo(String symbolName) {
		return tabela.get(symbolName);
	}
	
    public ArrayList<GyhSymbol> getAll() {
        return new ArrayList<GyhSymbol>(tabela.values());
    }   // Retorna todos os simbolos como uma lista
	
}
