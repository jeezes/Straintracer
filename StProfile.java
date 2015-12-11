class StProfile{
	int st_id, antallGener;

	StProfile(int id){
		this.st_id = id;
		antallGener = 0;
	}

	public int getId(){ return st_id; }

	public int getAntallGener(){ return antallGener; }

	public void print(){
		System.out.println("StProfileID: " + st_id);
	}
}