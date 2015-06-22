package ast.a.garciagomez3.tools;

import static org.junit.Assert.*;

import org.junit.Test;

public class MarshallerTest {

	@Test
	public void testInt() {
		int i = 1;
		byte[] b = Marshaller.mars_int(i);
		int j = Marshaller.unmars_int(b);
		assertEquals(i,j);
	}
	@Test
	public void testString(){
		String s = "hola";
		byte[] b = Marshaller.mars_str(s);
		String t = Marshaller.unmars_str(b,s.length());
		assertEquals(s,t);
	}
}
