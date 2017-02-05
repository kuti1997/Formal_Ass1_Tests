package il.ac.bgu.cs.fvm.ex4;

import static il.ac.bgu.cs.fvm.util.CollectionHelper.set;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import il.ac.bgu.cs.fvm.FvmFacade;
import il.ac.bgu.cs.fvm.automata.Automaton;
import il.ac.bgu.cs.fvm.automata.MultiColorAutomaton;
import il.ac.bgu.cs.fvm.util.Pair;

public class GNBACritSectionTest {
	FvmFacade fvmFacadeImpl = FvmFacade.createInstance();
	
	@Test
	public void simpleTest() throws Exception {

		MultiColorAutomaton<String, String> mulAut = getMCAut();
		Automaton<Pair<String, Integer>, String> aut = fvmFacadeImpl.GNBA2NBA(mulAut);
		assertEquals(aut, getExpected());

	}

	MultiColorAutomaton<String,String> getMCAut() {
		MultiColorAutomaton<String,String> aut = new MultiColorAutomaton<String,String>();
		HashSet<String> none = new HashSet<String>();
		HashSet<String> crit1 = new HashSet<String>(asList("crit1"));
		HashSet<String> crit2 = new HashSet<String>(asList("crit2"));
		HashSet<String> both = new HashSet<String>(asList("crit1", "crit2"));

		aut.addTransition("s0", crit2, "s2");
		aut.addTransition("s0", crit1, "s1");

		// True transitions
		for (Set<String> s : asList(none, crit1, crit2, both)) {
			aut.addTransition("s0", new HashSet<String>(s), "s0");
			aut.addTransition("s1", new HashSet<String>(s), "s0");
			aut.addTransition("s2", new HashSet<String>(s), "s0");
		}

		aut.setInitial  ("s0");
		aut.setAccepting("s1", 1);
		aut.setAccepting("s2", 2);

		return aut;
	}

	Automaton<String,String> getExpected() {
		Automaton<String,String> aut = new Automaton<>();

		Set<String> none = set();
		Set<String> crit1 = set("crit1");
		Set<String> crit2 = set("crit2");
		Set<String> both = set("crit2", "crit1");

		aut.addTransition("s01", crit2, "s21");
		aut.addTransition("s01", crit1, "s11");
		aut.addTransition("s02", crit2, "a22");
		aut.addTransition("s02", crit1, "s12");

		// True transitions
		for (Set<String> s : asList(none, crit1, crit2, both)) {
			aut.addTransition("s01", s, "s01");
			aut.addTransition("s11", s, "s02");			
			aut.addTransition("s02", s, "s02");
			aut.addTransition("s21", s, "s01");
			aut.addTransition("s12", s, "s02");
			aut.addTransition("a22", s, "s01");
		}

		aut.setInitial("s01");

		aut.setAccepting("s11");

		return aut;
	}

}
