class CcProfile{
	int cc_id, antallGener;

	CcProfile(int id){
		this.cc_id = id;
		antallGener = 0;
	}

	public int getId(){ return cc_id; }

	public int getAntallGener(){ return antallGener; }

	public void print(){
		System.out.println("CcProfileID: " + cc_id);
	}
}