package pilha;

import java.lang.reflect.*;
/**
 * Armazena valores dentro de um vetor no formato Pilha
 * @author u18057 Julia Campoli Sacco
 * @author u18206 Rafael Pak Bragagnolo
 */
public class Pilha<X> implements Cloneable
{
    /**
	* Cria um vetor genérico
	*/
    protected Object[] vetor;
    /**
	* Conta a quantidade de objetos armazenados na Pilha
	*/
    protected int qtd = 0;

    /**
    * Acessar a quantidade do vetor
	* @return Retorna a quantidade do vetor
	*/
    public int getQtd ()
    {
        return this.qtd;
    }

    //versao preventiva
    /**
	* Construtor da classe Pilha
	* @param inteiro que representa a capacidade do vetor da Pilha
    * @throws Exception Lança exceção quando a capacidade da Pilha está negativa
	*/
    public Pilha (int capacidade) throws Exception
    {
        if (capacidade<0)
            throw new Exception ("Capacidade invalida");

        this.vetor = new Object [capacidade];
    }

    /**
	* Método de chamar clone quando a classe é genérica
    * @return Retorna o clone
    * @param o objeto que você vai clonar da mesma classe
	*/
    protected X meuCloneDeX (X x)
    {
        // agora, o que quero fazer dum jeito DEMONIACO é
        // return x.clone();
        X ret=null;

        try
        {
                Class<?> classe = x.getClass();
                Class<?>[] tiposDosParametrosFormais = null; // null pq clone tem 0 parametros
                Method metodo=classe.getMethod("clone",tiposDosParametrosFormais);
                Object[] parametrosReais = null; // null pq clone tem 0 parametros
                ret=(X)metodo.invoke(x,parametrosReais);
        }
        catch (NoSuchMethodException erro)
        {}
        catch (IllegalAccessException erro)
        {}
        catch (InvocationTargetException erro)
        {}
        return ret;
    }

    /**
	* Guarda dentro da classe o objeto de maneira empilhada
    * @throws Exception
    * <ul>
	* 	<li>Caso o clone esteja nulo</li>
	* 	<li>Caso a Pilha esteja cheia</li>
	* </ul>
	* @param objeto genérico
	*/
    public void guarde (X s) throws Exception
    {
        if (s==null)
            throw new Exception ("Informacao ausente");

        if (this.isCheia())
            throw new Exception ("Pilha cheia");

        if (s instanceof Cloneable)
            this.vetor[this.qtd] = meuCloneDeX(s);  // vai dar pau; tem que contornar
        else
            this.vetor[this.qtd] = s;

        this.qtd++;
    }

    /**
	* Acessa o valor do último item sem perigo de altera-lo
    * @return Retorna o valor do getter
    * @throws Exception Caso esteja vazia
	*/
    public X getUmItem () throws Exception
    {
        if (this.isVazia())
            throw new Exception ("Nada a recuperar");

        if (this.vetor[this.qtd-1] instanceof Cloneable)
            return meuCloneDeX ((X)this.vetor[this.qtd-1]); // vai dar pau; tem que contornar

        return (X)this.vetor[this.qtd-1];
    }

    /**
	* Joga fora o último item da Pilha
    * @throws Exception Caso a Pilha esteja vazia
	*/
    public void jogueForaUmItem () throws Exception
    {
        if (this.isVazia())
        {
                Exception problema;
            problema = new Exception ("Pilha vazia");
            throw problema;
        }

        this.qtd--;
        this.vetor[this.qtd]=null;
    }

    /**
	* Devolve se a Pilha está cheia ou não
    * @return Retorna se a Pilha está cheia ou não
	*/
    public boolean isCheia ()
    {
        return this.qtd==this.vetor.length;
    }

    /**
	* Devolve se a Pilha está vazia ou não
    * @return Retorna se a Pilha está vazia ou não
	*/
    public boolean isVazia ()
    {
        return this.qtd==0;
    }

    /**
	* Transforma o elemento em string
    * @return a string do objeto
	*/
    public String toString ()
    {
        if (this.qtd==0)
            return "Vazia";

        return this.qtd+" elementos, sendo o ultimo "+this.vetor[this.qtd-1];
    }

    //compara this e obj
    /**
	* Checa se um objeto é igual ao que está sendo comparado
    * @return um boolean dizendo se é ou não igual
    * @param uma instância genérica que vai ser checar se é igual ao referente objeto
	*/
    public boolean equals (Object obj)
    {
        if (this==obj)
            return true;

        if (obj==null)
            return false;

        if (this.getClass()!=obj.getClass())
            return false;

        Pilha<X> pil = (Pilha<X>)obj;

        if (this.qtd!=pil.qtd)
            return false;

        for (int i=0; i<this.qtd; i++)
            if (!this.vetor[i].equals(pil.vetor[i]))
                return false;

        return true;
    }

    /**
    * Gerar um código para algum objeto
    * @return Retorna um inteiro sendo o código hash da Classe Labirinto
    */
    public int hashCode ()
    {
        int ret=666; // so nao pode ser zero

        ret = ret*2 + new Integer(this.qtd).hashCode();

        for (int i=0; i<this.qtd; i++)
          //if (this.vetor[i]!=null)
            ret = ret*2 + this.vetor[i].hashCode();

        return ret;
    }

    //construtor de copia
    /**
    * Utilizado para não mexer nas variáveis da sua classe
    * @return um clone do objeto da classe
    * @throws Exception Lança exceção caso o objeto esteja nulo
    * @param o objeto que você vai clonar da mesma classe
    */
    public Pilha (Pilha modelo) throws Exception
    {
        if (modelo==null)
            throw new Exception ("Modelo ausente");

        this.qtd = modelo.qtd;

        this.vetor = new Object [modelo.vetor.length];

        for (int i=0; i<=modelo.qtd; i++)
            this.vetor[i] = modelo.vetor[i];
    }

    /**
    * Cria e retorna um clone da Pilha
    * @return Retorna um clone da Pilha
    */
    public Object clone ()
    {
        Pilha<X> ret=null;

        try
        {
            ret = new Pilha<X> (this);
        }
        catch (Exception erro)
        {}

        return ret;
    }
}
