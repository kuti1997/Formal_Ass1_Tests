package il.ac.bgu.cs.fvm;

import static org.junit.Assert.assertEquals;
import static il.ac.bgu.cs.fvm.util.CollectionHelper.set;
import org.junit.Test;

import il.ac.bgu.cs.fvm.automata.Automaton;
import il.ac.bgu.cs.fvm.transitionsystem.Transition;
import il.ac.bgu.cs.fvm.transitionsystem.TransitionSystem;
import il.ac.bgu.cs.fvm.util.Pair;
import java.util.Collections;
import java.util.Set;

public class AutTsProductTests2 {
	FvmFacade fvmFacadeImpl = FvmFacade.createInstance();

	@Test
	public void autTimesTs() {
		TransitionSystem ts = buildTransitionSystem();
		Automaton aut = buildAutomaton();

		TransitionSystem comb = fvmFacadeImpl.product(ts, aut);

		TransitionSystem expected = expected();
		
		assertEquals(expected.getInitialStates(),      comb.getInitialStates());
		assertEquals(expected.getStates(),             comb.getStates());
		assertEquals(expected.getTransitions(),        comb.getTransitions());
		assertEquals(expected.getActions(),            comb.getActions());
		assertEquals(expected.getAtomicPropositions(), comb.getAtomicPropositions());
		assertEquals(expected.getLabelingFunction(),   comb.getLabelingFunction());
	}

	private Automaton<String> buildAutomaton() {
		Automaton<String> aut = new Automaton<>();

		String q0 = "q0";
		String q1 = "q1";
		String qf = "qF";

		Set<String> notRedAndNotYellow = Collections.emptySet();
		Set<String> redAndNotYellow = set("red");
		Set<String> yellowAndNotRed = set("yellow");
		Set<String> redAndYellow    = set("red", "yellow");

		aut.addTransition(q0, notRedAndNotYellow, q0);
		aut.addTransition(q0, yellowAndNotRed, q1);
		aut.addTransition(q0, redAndNotYellow, qf);
		aut.addTransition(q0, redAndYellow, qf);

		aut.addTransition(q1, yellowAndNotRed, q1);
		aut.addTransition(q1, redAndYellow, q1);
		aut.addTransition(q1, redAndNotYellow, q0);
		aut.addTransition(q1, notRedAndNotYellow, q0);

		aut.addTransition(qf, notRedAndNotYellow, qf);
		aut.addTransition(qf, redAndNotYellow, qf);
		aut.addTransition(qf, yellowAndNotRed, qf);
		aut.addTransition(qf, redAndYellow, qf);

		aut.setInitial(q0);
		aut.setAccepting(qf);
		return aut;
	}

	private TransitionSystem<String, String, String> buildTransitionSystem() {
		TransitionSystem ts = fvmFacadeImpl.createTransitionSystem();

		String gr = "green";
		String yl = "yellow";
		String rd = "red";
		String ry = "red/yellow";

		ts.addState(gr);
		ts.addState(yl);
		ts.addState(rd);
		ts.addState(ry);
		ts.addInitialState(gr);

		String sw = "switch";
		ts.addAction(sw);

		ts.addTransition(new Transition(gr, sw, yl));
		ts.addTransition(new Transition(yl, sw, rd));
		ts.addTransition(new Transition(rd, sw, ry));
		ts.addTransition(new Transition(ry, sw, gr));

		ts.addAtomicProposition("yellow");
		ts.addAtomicProposition("red");

		ts.addToLabel(yl, "yellow");
		ts.addToLabel(rd, "red");
		return ts;
	}

	TransitionSystem<Pair<String,String>,String,String> expected() {
		TransitionSystem ts = fvmFacadeImpl.createTransitionSystem();

		Pair<String,String> gr0 = new Pair("green","q0");
		Pair<String,String> yl1 = new Pair("yellow","q1");
		Pair<String,String> ry0 = new Pair("red/yellow","q0");
		Pair<String,String> rd0 = new Pair("red","q0");

		ts.addStates(gr0, yl1, ry0, rd0);

		ts.addInitialState(gr0);

		String sw = "switch";
		ts.addAction(sw);

		ts.addTransition(new Transition(rd0, sw, ry0));
		ts.addTransition(new Transition(yl1, sw, rd0));
		ts.addTransition(new Transition(gr0, sw, yl1));
		ts.addTransition(new Transition(ry0, sw, gr0));

		ts.addAtomicProposition("q1");
		ts.addAtomicProposition("q0");

		ts.addToLabel(gr0, "q0");
		ts.addToLabel(yl1, "q1");
		ts.addToLabel(ry0, "q0");
		ts.addToLabel(rd0, "q0");

		return ts;

	}

}