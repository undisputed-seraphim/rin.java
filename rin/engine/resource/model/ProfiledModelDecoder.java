package rin.engine.resource.model;

import rin.engine.system.Profiler;
import rin.engine.util.binary.BinaryReader;

public abstract class ProfiledModelDecoder extends BinaryReader implements ModelDecoder, Profiler {
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
