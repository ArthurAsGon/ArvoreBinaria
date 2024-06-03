import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ArvoreBinaria<X extends Comparable<X>> implements Cloneable
{
    private class No
    {
        private No esq;
        private X  info;
        private No dir;

        public No (No esq, X info, No dir)
        {
            this.esq =esq;
            this.info=info;
            this.dir =dir;
        }

        public No (X info)
        {
            this(null,info,null);
        }

        public No (No esq, X info)
        {
            this(esq,info,null);
        }

        public No (X info, No dir)
        {
            this(null,info,dir);
        }

        public No getEsq ()
        {
            return this.esq;
        }

        public X getInfo ()
        {
            return this.info;
        }

        public No getDir ()
        {
            return this.dir;
        }

        public void setEsq (No esq)
        {
            this.esq=esq;
        }

        public void setInfo (X info)
        {
            this.info=info;
        }

        public void setDir (No dir)
        {
            this.dir=dir;
        }

        @Override
        public String toString() {
            return "No{" +
                    "esq=" + (esq != null ? esq.getInfo() : "null") +
                    ", info=" + info +
                    ", dir=" + (dir != null ? dir.getInfo() : "null") +
                    '}';
        }

        @Override
        public int hashCode() {
            int result = info != null ? info.hashCode() : 0;
            result = 31 * result + (esq != null ? esq.hashCode() : 0);
            result = 31 * result + (dir != null ? dir.hashCode() : 0);
            return result;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected No clone() {
            try {
                No cloned = (No) super.clone();
                if (this.esq != null) {
                    cloned.esq = this.esq.clone();
                }
                if (this.dir != null) {
                    cloned.dir = this.dir.clone();
                }
                return cloned;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(); // Should never happen
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            No no = (No) obj;

            if (info != null ? !info.equals(no.info) : no.info != null) return false;
            if (esq != null ? !esq.equals(no.esq) : no.esq != null) return false;
            return dir != null ? dir.equals(no.dir) : no.dir == null;
        }


        public int compareTo(X o) {
            return this.info.compareTo(o);
        }

    }

    private No raiz;

    private X meuCloneDeX (X x)
    {
        X ret=null;

        try
        {
            Class<?> classe         = x.getClass();
            Class<?>[] tipoDosParms = null;
            Method metodo           = classe.getMethod("clone",tipoDosParms);
            Object[] parms          = null;
            ret                     = (X)metodo.invoke(x,parms);
        }
        catch(NoSuchMethodException erro)
        {}
        catch(IllegalAccessException erro)
        {}
        catch(InvocationTargetException erro)
        {}

        return ret;
    }

    public void inclua (X inf) throws Exception
    {
        if (inf==null) throw new Exception ("informacao ausente");

        No info;
        if (inf instanceof Cloneable)
            info = (No) meuCloneDeX(inf);
        else
            info = (No) inf;

        if (this.raiz==null)
        {
            this.raiz = new No ((X) info);
            return;
        }

        No atual = this.raiz;

        for(;;) // forever
        {
            int comparacao = info.compareTo(atual.getInfo());
            if (comparacao==0) throw new Exception ("informacao repetida");

            if (comparacao<0) // deve-se inserir info para o lado esquerdo
                if (atual.getEsq()!=null)
                    atual=atual.getEsq();
                else // achei onde inserir; eh para a esquerda do atual
                {
                    atual.setEsq (new No ((X) info));
                    return;
                }
            else // deve-se inserir info para o lado direito
                if (atual.getDir()!=null)
                    atual=atual.getDir();
                else // achei onde inserir; eh para a direito do atual
                {
                    atual.setDir (new No ((X) info));
                    return;
                }
        }
    }

    public boolean tem (X info) throws Exception
    {
        if (info==null) throw new Exception ("informacao ausente");
        if (this.raiz == null) return false;

        No atual = this.raiz;

        for(;;) // forever
        {
            if (atual == null) return false;
            int comparacao = info.compareTo(atual.getInfo());
            if (comparacao==0) return true;


            if (comparacao<0) // deve-se verificar para o lado esquerdo
                    atual = atual.getEsq();

            else // deve-se inserir info para o lado direito
                    atual=atual.getDir();


        }


    }
    public X getMenor () throws Exception
    {
        if(this.raiz == null) throw new Exception("Arvore vazia");
        No atual = this.raiz;
        No ret = null;

        for(;;) // forever
        {
            if(atual.getEsq() == null)
            {
                if(atual.getInfo() instanceof  Cloneable)
                    ret = (No) meuCloneDeX(atual.getInfo());
                else
                    ret = (No) atual.getInfo();
                break;
            }

            atual = atual.getEsq();

        }
        return (X) ret;
    }

    public int getQtdDeNodos ()
    {
        return getQtdDeNodos(this.raiz);
    }
    private int getQtdDeNodos (No r)
    {
        if(r == null) return 0;
        return 1 + getQtdDeNodos(r.getEsq()) + getQtdDeNodos(r.getDir());
    }



    public X getMaior() throws Exception
    {
        if(this.raiz == null) throw new Exception("Arvore vazia");
        No atual = this.raiz;
        No ret = null;

        for(;;) // forever
        {
            if(atual.getDir() == null)
            {
                if(atual.getInfo() instanceof  Cloneable)
                    ret = (No) meuCloneDeX(atual.getInfo());
                else
                    ret = (No) atual.getInfo();
                break;
            }

            atual = atual.getDir();

        }
        return (X) ret;
    }

    public void remova(X info) throws Exception
    {
        if (info == null) throw new Exception("Informação ausente");

        No pai = null;
        No atual = raiz;

        // Encontrar o nó a ser removido e seu pai
        while (atual != null && !atual.getInfo().equals(info)) {
            pai = atual;
            if (info.compareTo(atual.getInfo()) < 0) {
                atual = atual.getEsq();
            } else {
                atual = atual.getDir();
            }
        }

        if (atual == null) {
            throw new Exception("Informação não encontrada");
        }

        // Caso 1: Nó é uma folha
        if (atual.getEsq() == null && atual.getDir() == null) {
            if (atual == raiz) {
                raiz = null;
            } else if (atual == pai.getEsq()) {
                pai.setEsq(null);
            } else {
                pai.setDir(null);
            }
        }
        // Caso 2: Nó tem apenas filho à direita
        else if (atual.getEsq() == null) {
            if (atual == raiz) {
                raiz = atual.getDir();
            } else if (atual == pai.getEsq()) {
                pai.setEsq(atual.getDir());
            } else {
                pai.setDir(atual.getDir());
            }
        }
        // Caso 3: Nó tem apenas filho à esquerda
        else if (atual.getDir() == null) {
            if (atual == raiz) {
                raiz = atual.getEsq();
            } else if (atual == pai.getEsq()) {
                pai.setEsq(atual.getEsq());
            } else {
                pai.setDir(atual.getEsq());
            }
        }
        // Caso 4: Nó tem dois filhos
        else {
            No successorPai = atual;
            No successor = atual.getDir();

            // Encontrar o menor nó na subárvore direita (sucessor)
            while (successor.getEsq() != null) {
                successorPai = successor;
                successor = successor.getEsq();
            }

            // Substituir o info do nó atual com o info do sucessor
            atual.setInfo(successor.getInfo());

            // Remover o sucessor
            if (successorPai != atual) {
                successorPai.setEsq(successor.getDir());
            } else {
                successorPai.setDir(successor.getDir());
            }
        }
    }

    public void balanceieSe ()
    {
        balanceieSe(this.raiz);
    }
    private void balanceieSe(No r)
    {
        // se quantidade da direita - esquerda for >1 ou <-1 faça
        /*
        * enquanto a quantidade de nós para a esquerda de r for 2 ou maior do que
        * a quantidade de nós a direita de r, remova da esquerda a extrema direita,
        * guardando numa variável o valor ali presente; substitua por esse valor
        * o valor presente na raiz, salvando-o antes de outra variável; insira na
        * arvore o valor que estava presente a raiz
        * OBS chame apenas uam vez o getQtdDeNodos()
        *
        *  enquanto a quantidade de nós para a direita de r for 2 ou maior do que
         * a quantidade de nós a esquerda de r, remova da direita a extrema esquerda,
         * guardando numa variável o valor ali presente; substitua por esse valor
         * o valor presente na raiz, salvando-o antes de outra variável; insira na
         * arvore o valor que estava presente a raiz
         *
         * faça recursão para a esquerda e para a direita
        * */
    }









}














