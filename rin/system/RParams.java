package rin.system;

public class RParams {
	public static class One<T1> {
		private T1 t1;
		public One( T1 t1 ) { this.t1 = t1; }
		public T1 getOne() { return this.t1; }
	}
	
	public static class Two<T1, T2> {
		private T1 t1;
		private T2 t2;
		public Two( T1 t1, T2 t2 ) { this.t1 = t1; this.t2 = t2; }
		public T1 getOne() { return this.t1; }
		public T2 getTwo() { return this.t2; }
	}
}
