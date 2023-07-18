package com.tv.uscreen.yojmatv.beanModel.membershipAndPlan;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


public class TrialPeriodDTO{

	@SerializedName("durationTime")
	private int durationTime;

	@SerializedName("durationType")
	private String durationType;

	public void setDurationTime(int durationTime){
		this.durationTime = durationTime;
	}

	public int getDurationTime(){
		return durationTime;
	}

	public void setDurationType(String durationType){
		this.durationType = durationType;
	}

	public String getDurationType(){
		return durationType;
	}

	@NonNull
    @Override
 	public String toString(){
		return 
			"TrialPeriodDTO{" + 
			"durationTime = '" + durationTime + '\'' + 
			",durationType = '" + durationType + '\'' + 
			"}";
		}
}