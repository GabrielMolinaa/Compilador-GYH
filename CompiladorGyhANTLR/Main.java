/* 
 * 	AP3 - Compilador Completo
 * 
 * 	Gabriel Molina de Lima - 2208423
 * 	Pedro Mian Parra - 2207249
 * 	
 * 	link para o vídeo: https://drive.google.com/file/d/1YfWQj636b_gaTLf73777UEeHclJRehNV/view?usp=sharing
 */




import java.io.IOException;
import java.util.HashMap;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

public class Main {

	public static void main(String[] args) throws IOException {
		

		String filename = "prog.gyh";
		
		CharStream cs = CharStreams.fromFileName(filename);
		LinguagemGyhLexer lexer = new LinguagemGyhLexer(cs);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LinguagemGyhParser parser = new LinguagemGyhParser(tokens);
		
		// Registrar o error lister personalizado aqui
		ErroDetector mError = new ErroDetector();
		//parser.removeErrorListeners();
		parser.addErrorListener(mError);
		
		parser.programa();
		parser.generateCode(filename);

	}

}
