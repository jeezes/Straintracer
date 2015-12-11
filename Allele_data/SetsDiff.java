package Allele_data;

import MLST_data.Profiles;

public class SetsDiff{
	Allele allele;
	Profiles profile;

	public SetsDiff(Allele a, Profiles p){
		this.allele = a;
		this.profile = p;
	}

	public Allele getAllele(){ return allele; }

	public Profiles getProfile(){ return profile; }

	public void print(){
		System.out.println("AlleleID: " + allele.getId() + "\tProfileId: " + profile.getId());
	}

}