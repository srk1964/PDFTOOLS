package com.srk.pdfAnnotations;

public class ProcessTimer {
	private long startTime;
	private long endTime;
	public long getStartTime() {
		return startTime;
	}
	public long getEndTime() {
		return endTime;
	}

	private long lapsedTime;
	
	public long getLapsedTime() {
		return lapsedTime;
	}
	public void setLapsedTime(long lapsedTime) {
		this.lapsedTime = lapsedTime;
	}
	public void setStartTime() {
		this.startTime = System.currentTimeMillis() / 1000;
	}
	public void setEndTime() {
		this.endTime = System.currentTimeMillis() / 1000;
	}
	public void setLapsedTime() {
		this.lapsedTime = getEndTime() - getStartTime() ;
	}
	
	@Override
	public String toString(){ 
		if(getLapsedTime()>60){
			String result = "Elapsed time in minutes = "+ (getLapsedTime()/60);
			return result;
		}
		
		String result = "Elapsed time in seconds = "+ getLapsedTime();
		return result;
		
	}

}
