package il.ac.bgu.cs.fvm;

import org.junit.Test;

import il.ac.bgu.cs.fvm.channelsystem.ChannelSystem;
import il.ac.bgu.cs.fvm.examples.AlternatingBitProtocolBuilder;
import il.ac.bgu.cs.fvm.transitionsystem.AlternatingSequence;
import il.ac.bgu.cs.fvm.transitionsystem.TransitionSystem;
import il.ac.bgu.cs.fvm.util.codeprinter.TsPrinter;
import static java.lang.System.out;
import static il.ac.bgu.cs.fvm.util.CollectionHelper.p;
import static il.ac.bgu.cs.fvm.util.CollectionHelper.map;
import static il.ac.bgu.cs.fvm.util.CollectionHelper.seq;
import il.ac.bgu.cs.fvm.util.Pair;
import static org.junit.Assert.assertTrue;

public class ChannelSystemTest {

    @Test
    @SuppressWarnings("serial")
    public void debug() throws Exception {

        FvmFacade fvmFacadeImpl = FvmFacade.createInstance();

        TransitionSystem ts = fvmFacadeImpl.transitionSystemFromChannelSystem(AlternatingBitProtocolBuilder.build());

        //out.println(new TsPrinter().getAssertions(ts));

        /*
		assertTrue(fvmFacadeImpl.isInitialExecutionFragment(ts, //
				new Vector<NamedElement>() {
					{
						"[location=snd_msg(0),off,wait(0), eval={}]",
						"C!0",
						"[location=set_tmr(0),off,wait(0), eval={C=[0]}]",
						"_tmr_on!|_tmr_on?",
						"[location=wait(0),on,wait(0), eval={C=[0]}]",
						"_timeout?|_timeout!",
						"[location=snd_msg(0),off,wait(0), eval={C=[0]}]",
						"C!0",
						"[location=set_tmr(0),off,wait(0), eval={C=[0, 0]}]",
						"C?y",
						"[location=set_tmr(0),off,pr_msg(0), eval={y=0, C=[0]}]",
						"",
						"[location=set_tmr(0),off,snd_ack(0), eval={y=0, C=[0]}]",
						"D!0",
						"[location=set_tmr(0),off,wait(1), eval={y=0, C=[0], D=[0]}]",
						"C?y",
						"[location=set_tmr(0),off,pr_msg(1), eval={y=0, C=[], D=[0]}]",
						"",
						"[location=set_tmr(0),off,wait(1), eval={y=0, C=[], D=[0]}]",

					}
				}));
         */
    }

    @Test
    @SuppressWarnings("serial")
    public void differentPgOrder() throws Exception {

        FvmFacade fvmFacadeImpl = FvmFacade.createInstance();

        ChannelSystem<String, String> cs = AlternatingBitProtocolBuilder.build();

        TransitionSystem tss = fvmFacadeImpl.transitionSystemFromChannelSystem(cs);

        Object is = tss.getInitialStates().iterator().next();
        out.println(new TsPrinter().getObj(tss.getActions()));

        AlternatingSequence<Pair, Object> seq = AlternatingSequence.of(
                p(seq("snd_msg(0)", "off", "wait(0)"), map()),
                "C!0",
                p(seq("set_tmr(0)", "off", "wait(0)"), map(p("C", seq(0)))),
                "_tmr_on!|_tmr_on?",
                p(seq("wait(0)", "on", "wait(0)"), map(p("C", seq(0))))
        //                "[location=wait(0),off,snd_msg(0), eval={}]",              
        //                "C!0"
        //                "[location=wait(0),off,set_tmr(0), eval={C=[0]}]",
        //                "_tmr_on?|_tmr_on!",
        //                "[location=wait(0),on,wait(0), eval={C=[0]}]",
        //                "_timeout!|_timeout?",
        //                "[location=wait(0),off,snd_msg(0), eval={C=[0]}]",
        //                "C!0",
        //                "[location=wait(0),off,set_tmr(0), eval={C=[0, 0]}]",
        //                "C?y",
        //                "[location=pr_msg(0),off,set_tmr(0), eval={y=0, C=[0]}]",
        //                "",
        //                "[location=snd_ack(0),off,set_tmr(0), eval={y=0, C=[0]}]",
        //                "D!0",
        //                "[location=wait(1),off,set_tmr(0), eval={y=0, C=[0], D=[0]}]",
        //                "C?y",
        //                "[location=pr_msg(1),off,set_tmr(0), eval={y=0, C=[], D=[0]}]",
        //                "",
        //                "[location=wait(1),off,set_tmr(0), eval={y=0, C=[], D=[0]}]"
        );

        assertTrue(fvmFacadeImpl.isInitialExecutionFragment(tss, seq));

    }

}
