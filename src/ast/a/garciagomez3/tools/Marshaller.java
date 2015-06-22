package ast.a.garciagomez3.tools;

import java.io.UnsupportedEncodingException;

public class Marshaller {
	private final static int INTSIZE = 4;

	public static byte[] mars_int(int n){
		byte r[] = new byte[INTSIZE];
		r[3] = (byte) (n >> 24 & 0xff);
		r[2] = (byte) (n >> 16 & 0xff);
		r[1] = (byte) (n >> 8 & 0xff);
		r[0] = (byte) (n & 0xff);
		return r;
	}

	public static int unmars_int(byte[] b){
		int n0;
		int n1;
		int n2;
		int n3;
		n0 = (b[0] & 0xff);
		n1 = (b[1] & 0xff) << 8;
		n2 = (b[2] & 0xff) << 16;
		n3 = (b[3] & 0xff) << 24;
		return (n0 | n1 | n2 | n3);
	}

	public static byte [] mars_str(String s){
		byte r[] = null;
		try {
			byte [] b = s.getBytes("UTF-8");
			r = new byte [b.length];
			System.arraycopy(b, 0, r, 0, b.length);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return r;
	}

	public static String unmars_str (byte [] b, int size){
		byte[] st = new byte[size];
		System.arraycopy(b, 0, st, 0, size);
		String str = null;
		try {
			str = new String (st,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
}
