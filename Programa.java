import java.io.*;
import pilha.*;
import fila.*;
import coordenada.*;
import labirinto.*;

 /**
 * Serve para ler e abrir o arquivo do Labirinto, utilizar as classes pra resolve-lo, mostra o resultado e salva ele em um arquivo de texto
 * @author u18057 Julia Campoli Sacco
 * @author u18206 Rafael Pak Bragagnolo
 */
public class Programa
{
    public static void main(String[] args) throws Exception
    {
        boolean continuar = true;
        while (continuar)
        {
            BufferedReader leitor = new BufferedReader(new InputStreamReader(System.in));
            Labirinto lab;

            System.out.println();
            System.out.println("Resolver Labirinto");
            System.out.println("======== =========");
            System.out.println();
            System.out.println("Digite o caminho para o arquivo do labirinto:");
            String caminhoParaArq = leitor.readLine();
            System.out.println();
            try
            {
				lab = new Labirinto (caminhoParaArq);

				System.out.println(lab.toString());
				lab.resolverLabirinto();
				try
				{
					int tamanho = caminhoParaArq.length() - 4;
					PrintStream resultado = new PrintStream(caminhoParaArq.substring(0, tamanho) + ".res.txt");
					resultado.println(lab.getSolucao() + lab);
					resultado.close();
				}
				catch (FileNotFoundException erro1)
				{
					throw new Exception ("Não foi possivel alcancar o arquivo para se salvar o labirinto resolvido");
        		}

            }
            catch (Exception e)
            {
				if (e.getMessage().equals("semSolucao"))
				{
					System.out.println("Não existe solução para este labirinto!");
					break;
				}
                throw new Exception (e);
            }
            System.out.println("Deseja continuar? (S/N)");

            char resposta = Character.toUpperCase(leitor.readLine().charAt(0));
            if (resposta == 'N')
                continuar = false;
        }
    }

}
