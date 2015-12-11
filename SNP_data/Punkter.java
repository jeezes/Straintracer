package SNP_data;

public class Punkter{
	int snp_id, posisjon;
	char nuc1, nuc2; //nuc1 == sekvense1, nuc2 = sekvens2
	PunkterMeta meta;

	public Punkter(int id, char a, char b, int p, PunkterMeta m){
		this.snp_id = id;
		this.nuc1 = a;
		this.nuc2 = b;
		this.posisjon = p;
		meta = m;
	}

	public int getId(){ return snp_id; }

	public int getPosition(){ return posisjon; }

	public char getNuc1(){ return nuc1; }

	public char getNuc2(){ return nuc2; }

	public PunkterMeta getMeta(){ return meta; }

	public void print(){
		System.out.println("SNP_id: " + snp_id + "\tMeta_id: " + meta.punkterMeta_id + "\tPosisjon: " + posisjon + "\tnuc1: " + nuc1 + "\tnuc2: " + nuc2);
		meta.print();
		System.out.println("");
	}
}