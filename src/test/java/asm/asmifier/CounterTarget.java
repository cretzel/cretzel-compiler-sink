package asm.asmifier;

public class CounterTarget {

	private int n;

	public void increment() {
		System.out.println("increment()");
		n++;
	}

	public int count() {
		System.out.println("count()");
		return n;
	}

}
