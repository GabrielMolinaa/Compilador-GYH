/* 
 * Analisador Léxico para a linguagem GYH
 * 
 * 	Gabriel Molina de Lima - 2208423
 * 	Pedro Mian Parra - 2207249
 * 
 */
package lexico;

public class MainClass {

	public static void main(String[] args) {
		GyhLexico lex = new GyhLexico(args[0]);
		Token t = null;
		
		while((t = lex.proximoToken()).nome != TipoToken.Fim) {
			System.out.println(t);
		}

	}

}
