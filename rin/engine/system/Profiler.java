package rin.engine.system;

public interface Profiler {
	public void startProfiler();
	public void stopProfiler();
	public double getProfileTimeS();
	public long getProfileTimeNS();
	public double getCurrentProfileTimeS();
	public long getCurrentProfileTimeNS();
}
