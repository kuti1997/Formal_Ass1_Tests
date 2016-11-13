package il.ac.bgu.cs.fvm;

import java.util.Set;

import org.junit.Test;

import il.ac.bgu.cs.fvm.automata.Automaton;
import il.ac.bgu.cs.fvm.transitionsystem.Transition;
import il.ac.bgu.cs.fvm.transitionsystem.TransitionSystem;

import static org.junit.Assert.assertEquals;
import static il.ac.bgu.cs.fvm.AutTsProductTests.Actions.*;
import static il.ac.bgu.cs.fvm.AutTsProductTests.Lights.*;
import static il.ac.bgu.cs.fvm.util.CollectionHelper.*;
import static il.ac.bgu.cs.fvm.AutTsProductTests.AutomatonStates.*;
import il.ac.bgu.cs.fvm.util.Pair;

public class AutTsProductTests {
    
    public enum Lights{ Off, Green, Red }
    public enum Actions{ Switch }
    public enum AutomatonStates {Q0, Q1, Q2}
    
	FvmFacade fvmFacadeImpl = FvmFacade.createInstance();

	@Test
	public void autTimesTs() {
		TransitionSystem ts1 = buildTransitionSystem1();
		TransitionSystem ts2 = buildTransitionSystem2();
		Automaton aut = buildAutomaton();

		TransitionSystem comb1 = fvmFacadeImpl.product(ts1, aut);
		TransitionSystem expected1 = expected1();

		assertEquals(expected1.getInitialStates(), comb1.getInitialStates());
		assertEquals(expected1.getStates(), comb1.getStates());
		assertEquals(expected1.getTransitions(), comb1.getTransitions());
		assertEquals(expected1.getActions(), comb1.getActions());
		assertEquals(expected1.getAtomicPropositions(), comb1.getAtomicPropositions());
		assertEquals(expected1.getLabelingFunction(), comb1.getLabelingFunction());

		TransitionSystem comb2 = fvmFacadeImpl.product(ts2, aut);
		TransitionSystem expected2 = expected2();

		assertEquals(expected2.getInitialStates(), comb2.getInitialStates());
		assertEquals(expected2.getStates(), comb2.getStates());
		assertEquals(expected2.getTransitions(), comb2.getTransitions());
		assertEquals(expected2.getActions(), comb2.getActions());
		assertEquals(expected2.getAtomicPropositions(), comb2.getAtomicPropositions());
		assertEquals(expected2.getLabelingFunction(), comb2.getLabelingFunction());
	}

	private Automaton<AutomatonStates> buildAutomaton() {
		Automaton<AutomatonStates> aut = new Automaton();

		Set<String> notRedAndNotGreen = set();
		Set<String> redAndNotGreen    = set("red");
		Set<String> greenAndNotRed    = set("green");
		Set<String> redAndGreen       = set("red", "green");

		aut.addTransition(Q0, notRedAndNotGreen, Q0);
		aut.addTransition(Q0, redAndNotGreen, Q0);
		aut.addTransition(Q0, greenAndNotRed, Q0);
		aut.addTransition(Q0, redAndGreen, Q0);

		aut.addTransition(Q0, notRedAndNotGreen, Q1);
		aut.addTransition(Q0, redAndNotGreen, Q1);

		aut.addTransition(Q1, notRedAndNotGreen, Q1);
		aut.addTransition(Q1, redAndNotGreen, Q1);

		aut.addTransition(Q1, greenAndNotRed, Q2);
		aut.addTransition(Q1, redAndGreen, Q2);

		aut.addTransition(Q2, notRedAndNotGreen, Q2);
		aut.addTransition(Q2, redAndNotGreen, Q2);
		aut.addTransition(Q2, greenAndNotRed, Q2);
		aut.addTransition(Q2, redAndGreen, Q2);

		aut.setInitial(Q0);
		aut.setAccepting(Q1);
		return aut;
	}

	private TransitionSystem<Lights,Actions,String> buildTransitionSystem1() {
		TransitionSystem<Lights,Actions,String> ts = fvmFacadeImpl.createTransitionSystem();

		ts.addState(Green);
		ts.addState(Red);

		ts.addInitialState(Red);

		ts.addAction(Actions.Switch);

		ts.addTransitionFrom(Red).action(Switch).to(Green);
		ts.addTransitionFrom(Green).action(Switch).to(Red);

		ts.addAtomicProposition("green");
		ts.addAtomicProposition("red");

		ts.addToLabel(Green, "green");
		ts.addToLabel(Red, "red");
		return ts;
	}

	private TransitionSystem<Lights,Actions,String> buildTransitionSystem2() {
		TransitionSystem<Lights,Actions,String> ts = fvmFacadeImpl.createTransitionSystem();

		ts.addStates(Off, Red, Green);
		ts.addInitialState(Red);
		ts.addAction(Switch);

		ts.addTransitionFrom(Red).action(Switch).to(Green);
		ts.addTransitionFrom(Green).action(Switch).to(Red);
		ts.addTransitionFrom(Red).action(Switch).to(Off);
		ts.addTransitionFrom(Off).action(Switch).to(Red);

		ts.addAtomicProposition("Green");
		ts.addAtomicProposition("Red");

		ts.addToLabel(Green, "Green");
		ts.addToLabel(Red, "Red");
		return ts;
	}

	TransitionSystem<Pair<Lights, AutomatonStates>, Actions, String> expected1() {
		TransitionSystem<Pair<Lights, AutomatonStates>, Actions, String> ts = fvmFacadeImpl.createTransitionSystem();

		Pair<Lights, AutomatonStates> gr2 = new Pair<>(Green,Q2);
		Pair<Lights, AutomatonStates> gr0 = new Pair<>(Green,Q0);
		Pair<Lights, AutomatonStates> rd2 = new Pair<>(Red,Q2);
		Pair<Lights, AutomatonStates> rd1 = new Pair<>(Red,Q1);
		Pair<Lights, AutomatonStates> rd0 = new Pair<>(Red,Q0);

		ts.addStates(gr2, gr0, rd2, rd1, rd0);
		ts.addInitialState(rd1);
		ts.addInitialState(rd0);

		ts.addAction(Switch);

		ts.addTransition(new Transition(gr0, Switch, rd1));
		ts.addTransition(new Transition(gr2, Switch, rd2));
		ts.addTransition(new Transition(gr0, Switch, rd0));
		ts.addTransition(new Transition(rd0, Switch, gr0));
		ts.addTransition(new Transition(rd2, Switch, gr2));
		ts.addTransition(new Transition(rd1, Switch, gr2));

		ts.addAtomicProposition("Q1");
		ts.addAtomicProposition("Q2");
		ts.addAtomicProposition("Q0");

		ts.addToLabel(gr2, "Q2");
		ts.addToLabel(gr0, "Q0");
		ts.addToLabel(rd2, "Q2");
		ts.addToLabel(rd1, "Q1");
		ts.addToLabel(rd0, "Q0");

		return ts;

	}

	TransitionSystem<Pair<Lights, AutomatonStates>, Actions, String> expected2() {
		TransitionSystem<Pair<Lights, AutomatonStates>, Actions, String> ts = fvmFacadeImpl.createTransitionSystem();

		Pair<Lights, AutomatonStates> gr2 = new Pair<>(Green,Q2);
		Pair<Lights, AutomatonStates> gr0 = new Pair<>(Green,Q0);
		Pair<Lights, AutomatonStates> of2 = new Pair<>(Off,Q2);
		Pair<Lights, AutomatonStates> rd2 = new Pair<>(Red,Q2);
		Pair<Lights, AutomatonStates> of0 = new Pair<>(Off,Q0);
		Pair<Lights, AutomatonStates> of1 = new Pair<>(Off,Q1);
		Pair<Lights, AutomatonStates> rd1 = new Pair<>(Red,Q1);
		Pair<Lights, AutomatonStates> rd0 = new Pair<>(Red,Q0);

		ts.addState(gr2);
		ts.addState(gr0);
		ts.addState(of2);
		ts.addState(rd2);
		ts.addState(of0);
		ts.addState(of1);
		ts.addState(rd1);
		ts.addState(rd0);

		ts.addInitialState(rd1);
		ts.addInitialState(rd0);

		ts.addAction(Switch);

		ts.addTransition(new Transition(rd1, Switch, of1));
		ts.addTransition(new Transition(rd1, Switch, gr2));
		ts.addTransition(new Transition(rd0, Switch, of0));
		ts.addTransition(new Transition(rd0, Switch, gr0));
		ts.addTransition(new Transition(rd0, Switch, of1));
		ts.addTransition(new Transition(rd2, Switch, of2));
		ts.addTransition(new Transition(rd2, Switch, gr2));
		ts.addTransition(new Transition(gr0, Switch, rd0));
		ts.addTransition(new Transition(gr0, Switch, rd1));
		ts.addTransition(new Transition(of0, Switch, rd1));
		ts.addTransition(new Transition(of0, Switch, rd0));
		ts.addTransition(new Transition(gr2, Switch, rd2));
		ts.addTransition(new Transition(of1, Switch, rd1));
		ts.addTransition(new Transition(of2, Switch, rd2));

		ts.addAtomicProposition("Q1");
		ts.addAtomicProposition("Q2");
		ts.addAtomicProposition("Q0");

		ts.addToLabel(gr2, "Q2");
		ts.addToLabel(gr0, "Q0");
		ts.addToLabel(of2, "Q2");
		ts.addToLabel(rd2, "Q2");
		ts.addToLabel(of0, "Q0");
		ts.addToLabel(of1, "Q1");
		ts.addToLabel(rd1, "Q1");
		ts.addToLabel(rd0, "Q0");
		return ts;

	}

}