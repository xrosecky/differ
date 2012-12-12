package cz.nkp.differ.listener;


public interface ProgressListener {
	public void ready(Object c);
	public void setCompleted(int percentage);
}
