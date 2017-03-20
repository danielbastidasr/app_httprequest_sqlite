package com.android.dani.who_wants_to_be_millionaire_.Bussiness;

public class HighScore implements Comparable<HighScore>{

	String name;
	String scoring;
	String longitud;
	String latitude;
	
	public HighScore() {
	}
	
	public HighScore (String name, String scoring, String longitude, String latitude) {
		this.name = name;
		this.scoring = scoring;
		this.longitud = longitude;
		this.latitude = latitude;
	}


	@Override
	public int compareTo(HighScore o) {
		// TODO Auto-generated method stub
		if (Integer.parseInt(this.getScoring()) > Integer.parseInt(o.getScoring())) {
			return 1;
		}
		else if (Integer.parseInt(this.getScoring()) < Integer.parseInt(o.getScoring())) {
			return -1;
		}
		else {
			return 0;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScoring() {
		return scoring;
	}

	public void setScoring(String scoring) {
		this.scoring = scoring;
	}

	public String getLongitude() {
		return longitud;
	}

	public void setlongitude(String longitude) {
		this.longitud = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
}
