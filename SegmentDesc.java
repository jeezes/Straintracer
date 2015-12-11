import java.util.ArrayList;

class SegmentDesc{
	int segdesc_id;
	String description;
	Gen gen;

	SegmentDesc(int id, String description){
		this.segdesc_id = id;
		this.description = description;
		gen = null;
	}

	SegmentDesc(int id, String description, Gen g){
		this.segdesc_id = id;
		this.description = description;
		this.gen = g;
	}

	public int getId(){ return segdesc_id; }

	public void print(){
		System.out.println("SegmentDescID: " + segdesc_id + "\tDescription: " + description);
	}

	public Gen getGen(){ return gen; }
	
}