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
            if (atual == this.raiz) {
                this.raiz = null;
            } else if (atual == pai.getEsq()) {
                pai.setEsq(null);
            } else {
                pai.setDir(null);
            }
        }
        // Caso 2: Nó tem apenas filho à direita
        else if (atual.getEsq() == null) {
            if (atual == this.raiz) {
                this.raiz = atual.getDir();
            } else if (atual == pai.getEsq()) {
                pai.setEsq(atual.getDir());
            } else {
                pai.setDir(atual.getDir());
            }
        }
        // Caso 3: Nó tem apenas filho à esquerda
        else if (atual.getDir() == null) {
            if (atual == this.raiz) {
                this.raiz = atual.getEsq();
            } else if (atual == pai.getEsq()) {
                pai.setEsq(atual.getEsq());
            } else {
                pai.setDir(atual.getEsq());
            }
        }
        // Caso 4: Nó tem dois filhos
        else {
            No sucessor = null;
            if(getQtdDeNodos(atual.getDir()) < getQtdDeNodos(atual.getEsq()))
            {
                pai = atual;
                sucessor = atual.getEsq();
                while (sucessor.getDir() != null)
                {
                    pai = sucessor;
                    sucessor = sucessor.getDir();
                }
                pai.setDir(sucessor.getEsq());
            }
            else {
                pai =atual;
                sucessor = atual.getDir();
                while (sucessor.getEsq() != null){
                    pai = sucessor;
                    sucessor = sucessor.getEsq();
                }
                pai.setEsq(sucessor.getDir());
            }
            atual.setInfo(sucessor.getInfo());
        }
    }

    public void balanceieSe () throws Exception {
        balanceieSe(this.raiz);
    }
    private void balanceieSe(No r) throws Exception {
        if(r ==null)return;
        // enquanto a quantidade de nós a esquerda menos
        // a quantidade de nós a direita for maior 1,
        // remova da esquerda a extrema direita, guardando
        // numa variável o valor ali presente; substitua por
        // esse valor o valor presente na raiz, salvando-o
        // antes numa outra variavel; insira na arvore o valor
        // que estava presente na raiz
        // OBS: CHAME 1 SÓ VEZ getQtdDeNodos PARA A ESQUERDA E
        //      PARA A DIREITA, ARMAZENANDO OS RESULTADOS EM
        //      VARIÁVEIS QUE VOCÊ ATUALIZA NO WHILE
        int qtdDireita = getQtdDeNodos(r.getDir());
        int qtdEsquerda = getQtdDeNodos(r.getEsq());
        No antigaRaiz = r;

        X valorExtremo = null;

        while (Math.abs(qtdEsquerda-qtdDireita) > 1)
        {
            if(qtdEsquerda > qtdDireita +1)
            {
                X antigaRaizInfo = r.getInfo();
                No atual = r;
                while (atual.getDir()!= null)
                {
                    atual = atual.getDir();
                }
                r.setInfo(atual.getInfo());
                remova(atual.getInfo());
                inclua(antigaRaizInfo);
                qtdEsquerda--;
                qtdDireita++;
            }
            else {
                X antigaRaizInfo = r.getInfo();
                No atual = r;
                while (atual.getEsq() != null)
                {
                    atual = atual.getEsq();
                }
                r.setInfo(atual.getInfo());
                remova(atual.getInfo());
                inclua(antigaRaizInfo);
                qtdDireita--;
                qtdEsquerda++;


            }
        }



        // enquanto a quantidade de nós a direita menos
        // a quantidade de nós a esquerda for maior 1,
        // remova da direita a extrema esquerda, guardando
        // numa variável o valor ali presente; substitua por
        // esse valor o valor presente na raiz, salvando-o
        // antes numa outra variavel; insira na arvore o valor
        // que estava presente na raiz
        // OBS: CHAME 1 SÓ VEZ getQtdDeNodos PARA A ESQUERDA E
        //      PARA A DIREITA, ARMAZENANDO OS RESULTADOS EM
        //      VARIÁVEIS QUE VOCÊ ATUALIZA NO WHILE



        // faça recursão para a esquerda e para a direita






    }










}














