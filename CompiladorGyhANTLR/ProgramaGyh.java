import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

//Representação da estrutura do programa

public class ProgramaGyh {

	private SymbolTableGyh tabelaVariaveis;
	private ArrayList<Comando> comandos;
	private String name;
	
	
	public void generateTarget(String filename) {
		StringBuilder str = new StringBuilder();
		
		
		
		//Includes padrão
		str.append("#include <stdio.h>\n#include <stdlib.h>\n\n\n");
		
		//Main
		str.append("void main(){\n\n");
		
        for (GyhSymbol simbolo : tabelaVariaveis.getAll()) {
            str.append(simbolo.generateCode());
        }
		
        str.append("\n");
		
        for (Comando comando : comandos) {
            str.append(comando.generateCode());
        }

        str.append("\n}");
        
        FileWriter file;
		try {
			file = new FileWriter(new File(filename + ".c"));
	        file.write(str.toString());
	        file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		str.append("}");
		System.out.println("Programa Compilado com Sucesso!");
		System.out.println("Código C Gerado com Sucesso!");
	}
	
	
	
	public SymbolTableGyh getTabelaVariaveis() {
		return tabelaVariaveis;
	}



	public void setTabelaVariaveis(SymbolTableGyh tabelaVariaveis) {
		this.tabelaVariaveis = tabelaVariaveis;
	}



	public ArrayList<Comando> getComandos() {
		return comandos;
	}



	public void setComandos(ArrayList<Comando> comandos) {
		this.comandos = comandos;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}

}
