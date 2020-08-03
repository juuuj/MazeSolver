package coordenada;

/**
 * Classe para tratamento de coordenadas
 * @author u18057 Julia Campoli Sacco
 * @author u18206 Rafael Pak Bragagnolo
 */
public class Coordenada implements Cloneable
{
    /**
    * Quantidade de linhas e colunas
    */
    protected int x, y;

    /**
    * Constrói a coordenada
    * @throws Exception Caso tenha um número inválido de linhas ou colunas
    * @param um inteiro que representa as linhas, e um inteiro que representa colunas
    */
    public Coordenada (int linha, int coluna) throws Exception
    {
        this.setX(linha);
        this.setY(coluna);
    }

    /**
    * Altera o valor da quantidade de linhas
    * @param um inteiro que representa linhas
    * @throws Exception Caso tenha um número inválido de linhas
    */
    public void setX (int linha) throws Exception
    {
        if (linha < 0)
            throw new Exception("Número de linha inválido");

        this.x = linha;
    }

    /**
    * Altera o valor da quantidade de colunas
    * @param um inteiro que representa colunas
    * @throws Exception Caso tenha um número inválido de colunas
    */
    public void setY (int coluna) throws Exception
    {
        if (coluna < 0)
            throw new Exception("Número de coluna inválida");

        this.y = coluna;
    }

    /**
    * Acessa o valor de linhas
    * @return Retorna o valor de linhas
    */
    public int getX()
    {
        return this.x;
    }

    /**
    * Acessa o valor de colunas
    * @return Retorna o valor de colunas
    */
    public int getY()
    {
        return this.y;
    }

    /**
    * Checa se um objeto tem o mesmo valor que o outro
    * @param uma instância genérica que vai ser checar se é igual ao referente objeto
    * @return retorna uma variável boolean que verifica se os objetos são iguais
    */
    public boolean equals (Object obj)
    {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (this.getClass() != obj.getClass())
            return false;

        Coordenada coord = (Coordenada)obj;

        if (this.x != coord.x)
            return false;

        if (this.y != coord.y)
            return false;

        return true;
    }

    /**
    * Gerar um código para algum objeto
    * @return Retorna um inteiro sendo o código hash da Classe Coordenada
    */
    public int hashCode ()
    {
        int ret = 777;

        ret = ret * 5 + new Integer (this.x).hashCode();
        ret = ret * 5 + new Integer (this.y).hashCode();

        return ret;
    }

    /**
    * Transforma a coordenada em uma String
    * @return Retorna uma string com a coordenada
    */
    public String toString ()
    {
        return ("(" + this.x + ", " + this.y + ")");
    }

    /**
    * Construtor de cópia da classe Coordenada
    * @return Retorna uma string com a coordenada
    * @param o objeto que você vai clonar da mesma classe
    * @throws Exception caso o modelo de Coordenada esteja nulo
    */
    public Coordenada (Coordenada modelo) throws Exception
    {
        if (modelo == null)
          throw new Exception("Modelo ausente");

        this.x = modelo.x;
        this.y = modelo.y;
    }

    /**
    * Cria e retorna um clone do Labirinto
    * @return Retorna um clone do Labirinto
    */
    public Object clone ()
    {
        Coordenada ret = null;

        try
        {
            ret = new Coordenada (this);
        }
        catch (Exception erro) {}

        return ret;
    }
}

