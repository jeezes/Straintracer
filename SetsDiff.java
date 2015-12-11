class SetsDiff{
	Allele allele;
	Profiles profile;

	SetsDiff(Allele a, Profiles p){
		this.allele = a;
		this.profile = p;
	}

	public void print(){
		System.out.println("AlleleID: " + allele.getId() + "\tProfileId: " + profile.getId());
	}

}