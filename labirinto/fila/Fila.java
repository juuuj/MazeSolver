package fila;

import java.lang.reflect.*;
/**
 * Armazena valores dentro de um vetor no formato Fila
 * @author u18057 Julia Campoli Sacco
 * @author u18206 Rafael Pak Bragagnolo
 */
public class Fila<X> implements Cloneable
{
    /**
    * Cria um vetor genérico
    */
    protected Object[] vetor;  //protected String[] vetor --> ainda não tem tamanho

    /**
    * Conta quantos valores tem dentro do vetor
    */
    protected int qtd = 0;

    /**
    * Guarda o índice do início da Fila
    */
    protected int inicio = 0;

    /**
    * Guarda o índice do fim da Fila
    */
    protected int fim = 0;

    /**
    * Cria um vetor genérico
    * @param inteiro que representa a capacidade do vetor da Fila
    * @throws Exception caso o valor para a capacidade seja negativo
    */
    public Fila(int capacidade) throws Exception
    {
        if(capacidade < 0)
           throw new Exception("Capacidade inválida");

        this.vetor = new Object[capacidade];
    }

    /**
    * Método de chamar clone quando a classe é genérica
    * @return Retorna o clone
    * @param o objeto que você vai clonar da mesma classe
    */
    protected X meuCloneDeX(X x)
    {
        X ret = null;
        try
        {
            Class<?> classe = x.getClass();
            Class<?>[] tiposDeParametrosFormais = null;
            Method metodo = classe.getMethod("clone", tiposDeParametrosFormais);
            Object[] tiposDeParametrosReais = null;
            ret = (X)metodo.invoke(x,tiposDeParametrosReais);
        }
        catch(NoSuchMethodException erro)
        {}
        catch(IllegalAccessException erro)
        {}
        catch(InvocationTargetException erro)
        {}

        return ret;
    }

    /**
    * Guarda dentro da classe o objeto enfileirado
    * @throws Exception
    * <ul>
    *   <li>Caso o clone esteja nulo</li>
    *   <li>Caso a Fila esteja cheia</li>
    * </ul>
    * @param objeto genérico
    */
    public void guarde(X s) throws Exception
    {
        if(s==null) // s.equals antes não daria certo, pois se ele for null vai dar errado já que não se pode chamar método para objeto null
           throw new Exception("Informação ausente");

        if(this.isCheia())
           throw new Exception("Fila cheia");

        if(s instanceof Cloneable)
        {
            if(fim == this.vetor.length-1)
            {
               fim = 0;
               this.vetor[this.fim] = meuCloneDeX(s);
            }
            //this.vetor[this.qtd] = (Horario)s.clone();
            else
                    this.vetor[this.fim++] = meuCloneDeX(s);
        }
        else
        {
            if(fim == this.vetor.length-1)
            {
               fim = 0;
               this.vetor[this.fim] = s;
            }
            //this.vetor[this.qtd] = (Horario)s.clone();
            else
                    this.vetor[this.fim++] = s;
        }
        this.qtd++;
    }

    /**
    * Acessa o valor do primeiro item sem perigo de altera-lo
    * @return Retorna o valor do getter
    * @throws Exception Caso esteja vazia
    */
    public X getUmItem() throws Exception
    {
        if(this.isVazia())
           throw new Exception("Nada a recuperar");

        if(this.vetor[this.inicio] instanceof Cloneable)
            return meuCloneDeX((X)this.vetor[this.inicio]);

        return (X)this.vetor[this.inicio];
    }

    /**
    * Joga fora o primeiro item da Pilha
    * @throws Exception Caso a Fila esteja vazia
    */
    public void jogueForaUmItem() throws Exception
    {
        if(this.isVazia())
           throw new Exception("Pilha vazia");

        this.vetor[this.inicio] = null;

        if(this.inicio == this.vetor.length-1)
           inicio = 0;
        else
            inicio++;

        qtd--;
    }

    /**
    * Devolve se a Fila está cheia ou não
    * @return Retorna se a Fila está cheia ou não
    */
    public boolean isCheia()
    {
        return this.qtd == this.vetor.length;
    }

    /**
    * Devolve se a Fila está vazia ou não
    * @return Retorna se a Fila está vazia ou não
    */
    public boolean isVazia()
    {
        return this.qtd == 0;
    }

    /**
    * Transforma o elemento em string
    * @return a string do objeto
    */
    public String toString()
    {
        if(this.qtd==0)
           return "Vazia";

        return this.qtd+" elementos, sendo o primeiro "+this.vetor[inicio];
    }

    //compara this e obj
    /**
    * Checa se um objeto é igual ao que está sendo comparado
    * @return um boolean dizendo se é ou não igual
    * @param uma instância genérica que vai ser checar se é igual ao referente objeto
    */
    public boolean equals (Object obj) //compara this e obj
    {
        if(this == obj) //dispensável, mas deixa método mais rápido
           return true;

        if(obj == null)
           return false;

        if(this.getClass()!=obj.getClass())
           return false;

        Fila<X> fila = (Fila<X>)obj; // java enxerga que existe uma Fila chamada fila (que é o mesmo obj)

        if(this.qtd!=fila.qtd)
           return false;

        for(int i = 0,
                posThis=this.inicio,
                posFila=fila.inicio;

                i < this.qtd;

                i++,
                posThis=(posThis<this.vetor.length-1?posThis+1:0),
                posFila=(posFila<fila.vetor.length-1?posFila+1:0))

        if(!this.vetor[posThis].equals(fila.vetor[posFila]))
            return false;

        return true;
    }

    /**
    * Gerar um código para algum objeto
    * @return Retorna um inteiro sendo o código hash da Classe Labirinto
    */
    public int hashCode()
    {
        int ret = 1; //só não pode ser 0

        ret = ret * 2 + new Integer(this.qtd).hashCode();

        for(int i=0, pos=inicio; i<this.qtd; i++, pos=(pos<vetor.length-1?pos+1:0))
                ret = ret*2 + this.vetor[pos].hashCode();

        return ret;
    }

    //construtor de copia
    /**
    * Utilizado para não mexer nas variáveis da sua classe
    * @return um clone do objeto da classe
    * @throws Exception Lança exceção caso o objeto esteja nulo
    * @param o objeto que você vai clonar da mesma classe
    */
    public Fila (Fila<X> modelo) throws Exception
    {
        if(modelo == null)
        throw new Exception("Modelo ausente");

        this.qtd = modelo.qtd;

        this.inicio = modelo.inicio;

        this.fim = modelo.fim;

        this.vetor = new Object[modelo.vetor.length];

        for(int i=0; i<modelo.vetor.length-1; i++)
        this.vetor[i] = modelo.vetor[i];
    }

    /**
    * Cria e retorna um clone da Fila
    * @return Retorna um clone da Fila
    */
    public Object clone()
    {
        Fila<X> ret = null;
        try
        {
                ret = new Fila<X>(this);
        }
        catch(Exception erro)
        {}

        return ret;
    }
}
