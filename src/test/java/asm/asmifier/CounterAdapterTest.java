package asm.asmifier;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import asm.AsmClassloader;

public class CounterAdapterTest {

	@Test
	public void test_counter_visitor() throws Exception {

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		CounterAdapter counterVisitor = new CounterAdapter(cw);

		ClassReader reader = new ClassReader("asm.asmifier.Counter");
		reader.accept(counterVisitor, 0);

		Class<?> clazz = new AsmClassloader().defineClass(CounterAdapter.FQN,
				cw.toByteArray());
		Counter counter = (Counter) clazz.newInstance();
		counter.count();
		counter.increment();

	}
	
}
