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

    public void remova(X info) throws Exception {
        if (info == null) throw new Exception("Informação ausente");

        No parent = null;
        No current = raiz;

        // Encontrar o nó a ser removido e seu pai
        while (current != null && !current.getInfo().equals(info)) {
            parent = current;
            if (info.compareTo(current.getInfo()) < 0) {
                current = current.getEsq();
            } else {
                current = current.getDir();
            }
        }

        if (current == null) {
            throw new Exception("Informação não encontrada");
        }

        // Caso 1: Nó é uma folha
        if (current.getEsq() == null && current.getDir() == null) {
            if (current == raiz) {
                raiz = null;
            } else if (current == parent.getEsq()) {
                parent.setEsq(null);
            } else {
                parent.setDir(null);
            }
        }
        // Caso 2: Nó tem apenas filho à direita
        else if (current.getEsq() == null) {
            if (current == raiz) {
                raiz = current.getDir();
            } else if (current == parent.getEsq()) {
                parent.setEsq(current.getDir());
            } else {
                parent.setDir(current.getDir());
            }
        }
        // Caso 3: Nó tem apenas filho à esquerda
        else if (current.getDir() == null) {
            if (current == raiz) {
                raiz = current.getEsq();
            } else if (current == parent.getEsq()) {
                parent.setEsq(current.getEsq());
            } else {
                parent.setDir(current.getEsq());
            }
        }
        // Caso 4: Nó tem dois filhos
        else {
            No successorParent = current;
            No successor = current.getDir();

            // Encontrar o menor nó na subárvore direita (sucessor)
            while (successor.getEsq() != null) {
                successorParent = successor;
                successor = successor.getEsq();
            }

            // Substituir o info do nó atual com o info do sucessor
            current.setInfo(successor.getInfo());

            // Remover o sucessor
            if (successorParent != current) {
                successorParent.setEsq(successor.getDir());
            } else {
                successorParent.setDir(successor.getDir());
            }
        }
    }









}














