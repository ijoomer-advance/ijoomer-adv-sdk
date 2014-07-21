package com.ijoomer.common.classes;

/**
 * This Class Contains All Method Related To VideoTrimmerUtilities.
 * 
 * @author tasol
 * 
 */
public class VideoTrimmerUtilities {
	
	/**
	 * Function to convert milliseconds time to
	 * Timer Format
	 * Hours:Minutes:Seconds
	 * */
	public static String milliSecondsToTimer(long milliseconds){
		String finalTimerString = "";
		String secondsString = "";
		
		// Convert total duration into time
		   int hours = (int)( milliseconds / (1000*60*60));
		   int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
		   int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
		   // Add hours if there
		   if(hours > 0){
			   finalTimerString = hours + ".";
		   }
		   
		   // Prepending 0 to seconds if it is one digit
		   if(seconds < 10){ 
			   secondsString = "0" + seconds;
		   }else{
			   secondsString = "" + seconds;}
		   
		   finalTimerString = finalTimerString + minutes + "." + secondsString;
		
		// return timer string
		return finalTimerString;
	}
	
	/**
	 * Function to get Progress percentage
	 * @param currentDuration
	 * @param totalDuration
	 * */
	public static int getProgressPercentage(long currentDuration, long totalDuration){
		Double percentage = (double) 0;
		if(currentDuration==0){
			currentDuration = 1;
		}
		long currentSeconds = (int) (currentDuration  );
		long totalSeconds = (int) (totalDuration );
		
		// calculating percentage
		percentage =(((double)currentSeconds)/totalSeconds)*100;
		
		// return percentage
		return percentage.intValue();
	}

	/**
	 * Function to change progress to timer
	 * @param progress - 
	 * @param totalDuration
	 * returns current duration in milliseconds
	 * */
	public  static double progressToTimer(int progress, double totalDuration) {
		double currentDuration = 0;
		totalDuration =  (totalDuration / 1000);
		currentDuration =  ((((double)progress) / 100) * totalDuration);
		
		// return current duration in seconds
		return currentDuration ;
	}
	
	/**
	 * This method used to milliseconds to seconds.
	 * @param millisec represented {@link Long} milliseconds
	 * @return represented {@link Double}
	 */
	public static double millisecondsToSeconds(long millisec) {
		double seconds = 0;
		seconds = (int) (millisec / 1000) % 60 ;
		return seconds ;
	}
	
}
