import java.util.ArrayList;

class Sets{
	int set_id;
	Allele allele;

	Sets(int id, Allele a){
		this.set_id = id;
		this.allele = a;
	}

	public int getId(){ return set_id; }

	public void print(){
		System.out.println("SetsID: " + set_id + "\tAlleleId: " + allele.getId());
	}

}