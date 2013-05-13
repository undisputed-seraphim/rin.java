package rin.engine.util.binary;

import rin.engine.system.Profiler;
import rin.engine.util.binary.BinaryReader;

public class ProfiledBinaryReader extends BinaryReader implements Profiler {
	private long startTime = 0L;
	private long endTime = 0L;
	private long profileTime = 0L;
	
	@Override
	public void startProfiler() {
		startTime = System.nanoTime();
	}
	
	@Override
	public void stopProfiler() {
		endTime = System.nanoTime();
		profileTime = endTime - startTime;
	}
	
	@Override
	public double getProfileTimeS() {
		return profileTime * 1e-9;
	}
	
	@Override
	public long getProfileTimeNS() {
		return profileTime;
	}
	
	@Override
	public double getCurrentProfileTimeS() {
		return ( System.nanoTime() - startTime ) * 1e-9;
	}
	
	@Override
	public long getCurrentProfileTimeNS() {
		return System.nanoTime() - startTime;
	}
}
