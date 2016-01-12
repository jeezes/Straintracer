package Input_data;

import Sequence_data.Sekvenser;

class Isolate{
	private String file_path;
	private int id;
	private Sekvenser sequence_id;

	Isolate(int id, String path){
		this.id = id;
		this.file_path = path;
		sequence_id = null;
	}

	public setSequence(Sekvenser seq){
		this.sequence_id = seq;
	}

	public int getId(){ return id; }

	public String getPath(){ return file_path; }

	public Sekvenser getSequence(){ return sequence_id; }

	public void print(){
		System.out.println("ID: " + id + "\tPath: " + file_path);
	}
}