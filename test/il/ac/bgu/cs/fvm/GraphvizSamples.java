package il.ac.bgu.cs.fvm;

import il.ac.bgu.cs.fvm.examples.VendingMachineBuilder;
import il.ac.bgu.cs.fvm.transitionsystem.TransitionSystem;
import il.ac.bgu.cs.fvm.util.GraphvizPainter;

/**
 *
 */
public class GraphvizSamples {

	@SuppressWarnings({ "rawtypes", "unchecked" })
    public static void main(String[] args) {
//        TransitionSystem ts = TSTestUtils.makeCircularTsWithReset(5);
        TransitionSystem ts = new VendingMachineBuilder().build();
        
        
//        TransitionSystem<States,Actions,APs> ts = TSTestUtils.threeStateTS();
//        GraphvizPainter<States,Actions,APs> gvp = new GraphvizPainter<>(
//                s->s.name(), a->a.name(), p->p.name()
//        );
//        
//        System.out.println(gvp.makeDotCode(ts));
        
        System.out.println( GraphvizPainter.toStringPainter().makeDotCode(ts) );
    }
    
}
