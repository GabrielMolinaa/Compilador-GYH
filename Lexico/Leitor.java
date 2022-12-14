package lexico;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

// Método de leitura do Arquivo com a utilização do Buffer Duplo

public class Leitor {
	private final static int TAMANHO_BUFFER = 20;
	int [] bufferLeitura;
	int ponteiro;
	int bufferAtual;
	int inicioLexema;
	int numeroLinha = 0;
	private String lexema;
	InputStream is;
	
	// Inicializa os buffers e abre o arquivo
	
	public Leitor(String arquivo) {
		try {
			is = new FileInputStream(new File(arquivo));
			inicializarBuffer();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void inicializarBuffer() {
		bufferAtual = 2;
		inicioLexema = 0;
		lexema = "";
		bufferLeitura = new int[TAMANHO_BUFFER * 2];
		ponteiro = 0;
		recarregarBuffer1();
	}
	
	// Método para avançar o ponteiro no buffer
	private void incrementarPonteiro() {
		ponteiro++;
		// Caso o buffer 1 encha, chama o método para recarregar o buffer 2
		if(ponteiro == TAMANHO_BUFFER) {
			recarregarBuffer2();
		// Caso o buffer 2 encha, chama o método para recarregar o buffer 1
		}else if(ponteiro == TAMANHO_BUFFER*2) {
			recarregarBuffer1();
			ponteiro = 0;
		}
	}
	
	// Método para recarregar o Buffer 1
	private void recarregarBuffer1() {
		if(bufferAtual == 2) {
			bufferAtual = 1;
			
			// Carregando o buffer com o conteúdo do arquivo 
			for(int i=0;i<TAMANHO_BUFFER;i++) {
				try {
					bufferLeitura[i] = is.read();
					if(bufferLeitura[i] == -1) {
						break;
					}
				}catch(IOException e) {
					e.printStackTrace();
				}//catch
			}//for
		}//if
	}//recarregarBuffer1
	
	private void recarregarBuffer2() {
		if(bufferAtual == 1) {
			bufferAtual = 2;

			// Carregando o buffer com o conteúdo do arquivo
			for(int i=TAMANHO_BUFFER;i<TAMANHO_BUFFER*2;i++) {
				try {
					bufferLeitura[i] = is.read();
					if(bufferLeitura[i] == -1) {
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}//catch
			}//for
		}//if
	}//recarregarBuffer2
	
	// Método que retorna caractere do buffer de acordo com o ponteiro
	private int lerCaractereBuffer() {
		int ret = bufferLeitura[ponteiro];
		incrementarPonteiro();
		
		// Contagem de linhas
		if((char) ret == '\n') {
			contLinhas();
		
		}
		return ret;
	}
	
	// Método para criar um lexema
	public int lerProximoCaractere() {
		int c = lerCaractereBuffer();
		lexema += (char)c;
		return c;
	}
	
	// Método para retroceder o lexema
	public void retroceder() {
		ponteiro--;
		lexema = lexema.substring(0,lexema.length()-1);
		if(ponteiro<0) {
			ponteiro = TAMANHO_BUFFER*2 -1;
		}
	}
	
	// Método para permitir novamente a leitura do lexema
	public void zerar() {
		ponteiro = inicioLexema;
		lexema = "";
	}
	
	// Método utilizado após validar o lexema
	public void confirmar() {
		inicioLexema = ponteiro;
		lexema = "";
	}
	
	// Método para retornar o lexema
	public String getLexema() {
		return lexema;
	}
	
	// Método para imprimir o buffer/Linha caso haja um erro léxico
	@Override
	public String toString() {
		String buffer = "Buffer:[";
		for(int i: bufferLeitura) {
			char c = (char) i;
		
			if( Character.isWhitespace(c)) {
				buffer += ' ';	
			}else {
				buffer += (char) i;
			}
		}
		buffer += "]\n";
		buffer += "        " ;
		for(int i = 0; i<TAMANHO_BUFFER * 2;i++) {
			if(i==inicioLexema && i == ponteiro) {
				buffer += "%";
			}else if (i==inicioLexema) {
				buffer += "^";
			}else if(i==ponteiro) {
				buffer += "*";
			}else {
				buffer += " ";
			}
		}
		return buffer;
	}
	
	// Método para contar o número de linhas do arquivo (programa GYH) de entrada
	public void contLinhas() {
		this.numeroLinha++;
	//	System.out.println(numeroLinha);
	}
	
	
}
