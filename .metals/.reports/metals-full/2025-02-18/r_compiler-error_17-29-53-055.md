file:///C:/Users/loris/OneDrive/Desktop/PACproject/spendlyapi/backend/spendly/src/main/java/bmt/spendly/models/Alert.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file:///C:/Users/loris/OneDrive/Desktop/PACproject/spendlyapi/backend/spendly/src/main/java/bmt/spendly/models/Alert.java
text:
```scala
package bmt.spendly.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Alert")
public class Alert {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double limite;
    private String nome;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public Alert(){}

    public Alert(String nome,double limite, Group group){
        this.nome=nome;
        this.limite=limite;
        this.group=group;
    }

    public Long getId(){
        return this.id;
    }
    
    public double getLimite(){
        return this.limite;
    }

    public void setLimite(double limite){
        this.limite=limite;
    }

    public String getNome(){
        return this.nome;
    }

    public void setNome(String nome){
        this.nome=nome;
    }

    public Group getGroup(){
        return this.group;
    }


    public void setGroup(Group group){
        this.group=group;
    }

    @Override
    public String toString() {
        return "Alert{id=" + id + ", nome='" + nome + "', limite=" + limite + ", groupId=" + (group != null ? group.getId() : null) + "}";
    }



    
}

```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:935)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:164)
	dotty.tools.pc.CachingDriver.run(CachingDriver.scala:45)
	dotty.tools.pc.WithCompilationUnit.<init>(WithCompilationUnit.scala:31)
	dotty.tools.pc.SimpleCollector.<init>(PcCollector.scala:351)
	dotty.tools.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:88)
	dotty.tools.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:111)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator