package labirinto;

import java.io.*;
import pilha.*;
import fila.*;
import coordenada.*;

 /**
 * Classe para tratar o Labirinto e resolvê-lo.
 * @author u18057 Julia Campoli Sacco
 * @author u18206 Rafael Pak Bragagnolo
 */
public class Labirinto
{
    /**
	* Indica se a Saída foi encontrada pelo programa
	*/
    protected boolean achouSaida;
    /**
	* Número de linhas que o labirinto tem
	*/
    protected int qtdX;
    /**
	* Número de colunas que o labirinto tem
	*/
    protected int qtdY;
    /**
	* Matriz que irá representar o Labirinto graficamente
	*/
    protected char[][] labirinto;
    /**
	* Objeto da classe Coordenada que representa a entrada do Labirinto
	*/
    protected Coordenada entrada;
    /**
	* Objeto da classe Coordenada que representa a saída do Labirinto
	*/
    protected Coordenada saida;
    /**
	* Objeto da classe Coordenada que representa o movimento de posição para posição durante a resolução do Labirinto
	*/
    protected Coordenada atual;
    /**
	* Caso haja uma solução para o Labirinto, esta Pilha de Coordenadas irá armazenar o caminho da entrada para a saída
	*/
    protected Pilha<Coordenada> inverso;
    /**
	* Durante a resolução do Labirinto, a fila irá armazenar as possibilidades de caminho válidos próximas à Coordenada atual
	*/
    protected Fila<Coordenada> fila;
    /**
	* Caminho irá armazenar as coordenadas que levam da entrada até a saída
	*/
    protected Pilha<Coordenada> caminho;
    /**
	* Possibilidades guarda todas as 'possibilidades' que existem para um caminho até o fim, armazenando fila
	*/
    protected Pilha<Fila<Coordenada>> possibilidades;

    /**
	* Método construtor - armazena na memória, um labirinto encontrado em um arquivo de texto através do caminho passado como parâmetro
    * @param nomeArq caminho no disco até um arquivo onde está armazenado um labirinto
    * @throws Exception se: <br>
    * <ul>
	* 	<li>O labirinto for pequeno demais (se o labirinto for um quadrado menor de 2x2)</li>
	* 	<li>Houver algum caracter não esperado no labirinto</li>
	* 	<li>Não for encontrado o arquivo com o labirinto passado como parâmetro</li>
	*   <li>Houve falha na leitura do número de linhas ou colunas no início do arquivo</li>
	*   <li>Não foi possível ler o arquivo</li>
	* </ul>
	*/
    public Labirinto (String nomeArq) throws Exception
    {
        try
        {
            BufferedReader leitura = new BufferedReader(new FileReader(nomeArq));

            int nLinhas = Integer.parseInt(leitura.readLine());
            int nColunas = Integer.parseInt(leitura.readLine());

            if (nLinhas < 2 || nColunas < 2)
                throw new Exception("Modelo de labirinto incorreto!");

            qtdX = nLinhas;
            qtdY = nColunas;

            labirinto = new char[qtdX][qtdY];
            String linha;
            for (int i = 0; i < qtdX; i++)
            {
                linha = leitura.readLine();
                for (int j = 0; j < qtdY; j++)
                {
                    if (linha.charAt(j) != 'E' &&
                        linha.charAt(j) != 'S' &&
                        linha.charAt(j) != ' ' &&
                        linha.charAt(j) != '#')
                        throw new Exception("O labirinto apresenta erros em sua formulação!");

                    labirinto[i][j] = linha.charAt(j);
                }
            }
            leitura.close();
        }
        catch (FileNotFoundException e1)
        {
            throw new Exception("O arquivo do labirinto não foi encontrado!");
        }
        catch (ArrayIndexOutOfBoundsException e2)
        {
			throw new Exception("Erro de leitura - número de linhas inválido!");
		}
		catch (StringIndexOutOfBoundsException e3)
		{
			throw new Exception("Erro de leitura - número de colunas inválida!");
		}
        catch (IOException e4)
        {
            throw new Exception("A leitura do arquivo falhou!");
        }
    }

    /**
	* Utilizando o inverso e o caminho, devolve o conjunto solução de coordenadas que levam da entrada do labirinto até o final
    * @return as coordenadas em uma String
	*/
    public String getSolucao() throws Exception
    {
        String solucao = "";
        inverso = new Pilha<Coordenada>(this.caminho.getQtd());
        int qtdInverso = this.caminho.getQtd();

        for (int i = 0; i < qtdInverso; i++)
        {
            inverso.guarde(this.caminho.getUmItem());
            this.caminho.jogueForaUmItem();
        }

        for (int x = 0; x < qtdInverso; x++)
        {
            solucao += inverso.getUmItem() + "\r\n";
            inverso.jogueForaUmItem();
        }

        return solucao;
    }

    /**
	* Descobre, na matriz, onde está a entrada e a saída do Labirinto
    * @throws Exception caso tenha mais do que 1, ou menos do que 1 entrada e saída
	*/
    protected void ondeEntradaSaida() throws Exception
    {
        int qtdEntradas = 0;
        int qtdSaidas = 0;

        for (int i = 0; i < qtdX; i++)
            for (int j = 0; j < qtdY; j++)
            {
                if (labirinto[i][j] == 'E')
                {
                    this.entrada = new Coordenada(i, j);
                    qtdEntradas++;
                }

                if (labirinto[i][j] == 'S')
                {
                    this.saida = new Coordenada(i, j);
                    qtdSaidas++;
                }
            }

        if (qtdEntradas != 1)
            throw new Exception ("Número incorreto de entradas no labirinto!");

        if (qtdSaidas != 1)
            throw new Exception ("Número incorreto de saídas no labirinto!");
    }

    /**
	* Resolve o Labirinto que foi guardado no construtor
    * @throws Exception caso o metodo regresso() lance alguma exceção
	*/
    public void resolverLabirinto() throws Exception
    {
        possibilidades = new Pilha<Fila<Coordenada>>(this.qtdX * this.qtdY);
        caminho = new Pilha<Coordenada>(this.qtdX * this.qtdY);

        ondeEntradaSaida();
        this.atual = this.entrada;

        while(!achouSaida)
        {
            progresso();
            regresso();
            guarde();
        }
    }

    /**
    * Guarda o movimento dentro da FilaAtual, tira ele da Fila e Guarda a Fila dentro de possibilidades. Guarda o Movimento atua dentro de Caminho e verifica se achou a saída
    * @throws Exception se: <br>
    * <ul>
	* 	<li>O método getUmItem() da fila de possiveis movimentos lançar exceção</li>
	* 	<li>O método jogueForaUmItem() da fila de possiveis lançar exceção</li>
	* 	<li>O método guarde() da pilha possibilidades lançar exceção</li>
	* </ul>
    */
    protected void guarde() throws Exception
    {
		this.atual = this.fila.getUmItem();
		this.fila.jogueForaUmItem();
		this.possibilidades.guarde(fila);

        caminho.guarde(atual);

		if (this.labirinto[atual.getX()][atual.getY()] != 'S')
			this.labirinto[atual.getX()][atual.getY()] = '*';
		else
			achouSaida = true;
    }

    /**
    * Verifica os possíveis movimentos e armazena dentro da Fila
    * @throws Lança uma exceção caso o objeto que vocÊ deseja guardar esteja nulo ou a Fila esteja cheia
    */
    protected void progresso() throws Exception
    {
        this.fila = new Fila<Coordenada>(3);

        if (this.atual.getX() + 1 < this.qtdX)
        if (labirinto[atual.getX() + 1][atual.getY()] == ' ' ||
            labirinto[atual.getX() + 1][atual.getY()] == 'S')
            this.fila.guarde(new Coordenada(atual.getX() + 1, atual.getY()));

        if (this.atual.getX() > 0)
        if (labirinto[atual.getX() - 1][atual.getY()] == ' ' ||
            labirinto[atual.getX() - 1][atual.getY()] == 'S')
            this.fila.guarde(new Coordenada(atual.getX() - 1, atual.getY()));

        if (this.atual.getY() + 1 < this.qtdY)
        if (labirinto[atual.getX()][atual.getY() + 1] == ' ' ||
            labirinto[atual.getX()][atual.getY() + 1] == 'S')
            this.fila.guarde(new Coordenada(atual.getX(), atual.getY() + 1));

        if (this.atual.getY() > 0)
        if (labirinto[atual.getX()][atual.getY() - 1] == ' ' ||
            labirinto[atual.getX()][atual.getY() - 1] == 'S')
            this.fila.guarde(new Coordenada(atual.getX(), atual.getY() - 1));
    }

    /**
    * Verifica se a fila está vazia, ou seja, não existe nenhum movimento possível. Caso ela esteja vazia, ela retorna um movimento
    * @throws Caso a pilha de Fila de possibilidades esteja vazia
    */
    public void regresso() throws Exception
    {
        while (this.fila.isVazia())
        {
            if (this.possibilidades.isVazia())
            	throw new Exception ("semSolucao");

            this.fila = this.possibilidades.getUmItem();
            this.possibilidades.jogueForaUmItem();
            this.atual = this.caminho.getUmItem();
            this.caminho.jogueForaUmItem();
            this.labirinto[atual.getX()][atual.getY()] = ' ';
        }
    }

    /**
    * Transforma o labirinto em string
    * @return Retorna uma string com o labirinto
    */
	public String toString()
	{
		String matriz = "";

		for (int i = 0; i < qtdX; i++){

			for (int j = 0; j < qtdY; j++)
			{
				matriz += this.labirinto[i][j] + "";
			}
			matriz += "\r\n";
		}

		return matriz;
	}

    /**
    * Utilizado para não mexer nas variáveis da sua classe
    * @return um clone do objeto da classe
    * @throws Exception Lança exceção caso o objeto esteja nulo
    * @param o objeto que você vai clonar da mesma classe
    */
    public Labirinto (Labirinto m) throws Exception
    {
        if (m == null)
            throw new Exception("Modelo ausente");

        this.qtdX = m.qtdX;
        this.qtdY = m.qtdY;
        this.labirinto = m.labirinto.clone();
        this.entrada = (Coordenada)m.entrada.clone();
        this.saida = (Coordenada)m.saida.clone();
        this.atual = (Coordenada)m.atual.clone();
        this.caminho = (Pilha<Coordenada>)m.caminho.clone();
        this.fila = (Fila<Coordenada>)m.fila.clone();
        this.inverso = (Pilha<Coordenada>)m.inverso.clone();
        this.possibilidades = (Pilha<Fila<Coordenada>>)m.possibilidades.clone();
    }

    /**
    * Checa se um objeto tem o mesmo valor que o outro
    * @return retorna uma variável boolean que verifica se os objetos são iguais
    * @param uma instância genérica que vai ser checar se é igual ao referente objeto
    */
    public boolean equals(Object obj)
    {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (!(obj instanceof Labirinto))
			return false;

		Labirinto lab = (Labirinto)obj;

        if (this.qtdX != lab.qtdX)
			return false;

		if (this.qtdY != lab.qtdY)
			return false;

		if (!this.entrada.equals(lab.entrada))
			return false;

		if (!this.saida.equals(lab.saida))
			return false;

        if (!this.atual.equals(lab.atual))
			return false;

		if (this.achouSaida != lab.achouSaida)
			return false;

		if (!this.inverso.equals(lab.inverso))
			return false;

        if (!this.caminho.equals(lab.caminho))
            return false;

        if (!this.fila.equals(lab.fila))
            return false;

        if (!this.possibilidades.equals(lab.possibilidades))
            return false;

		for (int i = 0; i < qtdX; i++)
			for (int j = 0; j < qtdY; j++)
				if (this.labirinto[i][j] != lab.labirinto[i][j])
					return false;

		return true;
    }

    /**
    * Gerar um código para algum objeto
    * @return Retorna um inteiro sendo o código hash da Classe Labirinto
    */
    public int hashCode()
    {
		int ret = 777;

        ret = ret * 3 + new Integer(this.qtdX).hashCode();
        ret = ret * 3 + new Integer(this.qtdY).hashCode();
        ret = ret * 3 + this.entrada.hashCode();
        ret = ret * 3 + this.saida.hashCode();
        ret = ret * 3 + this.atual.hashCode();
		ret = ret * 3 + new Boolean(this.achouSaida).hashCode();
		ret = ret * 3 + this.inverso.hashCode();
        ret = ret * 3 + this.fila.hashCode();
        ret = ret * 3 + this.caminho.hashCode();
        ret = ret * 3 + this.possibilidades.hashCode();

        for (int i = 0; i <= this.qtdX; i++)
            for (int j = 0; j <= this.qtdY; j++)
                ret = ret * 3 + new Character(this.labirinto[i][j]).hashCode();

		return ret;
    }

    /**
    * Cria e retorna um clone do Labirinto
    * @return Retorna um clone do Labirinto
    */
    public Object clone()
    {
        Labirinto ret = null;

        try
        {
            ret = new Labirinto (this);
        }
        catch (Exception erro) {}

        return ret;
    }
}
