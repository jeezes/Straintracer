package Input_data;

import Sequence_data.Sekvenser;

public class Isolate{
	private String file_path;
	private int id;
	private Sekvenser sequence_id;
	private String date;

<<<<<<< HEAD
	public Isolate(int id, String path, String date){
=======
	public Isolate(int id, String path){
>>>>>>> 2fc14e94cec8dabb301e259d80477ce89e6da055
		this.id = id;
		this.file_path = path;
		sequence_id = null;
		this.date = date;
	}

<<<<<<< HEAD
	public Isolate(int id, String path){
		this.id = id;
		this.file_path = path;
		sequence_id = null;
		this.date = "2016/01/17";
	}

=======
>>>>>>> 2fc14e94cec8dabb301e259d80477ce89e6da055
	public void setSequence(Sekvenser seq){
		this.sequence_id = seq;
	}

	public int getId(){ return id; }

	public String getPath(){ return file_path; }

	public Sekvenser getSequence(){ return sequence_id; }

	public String getDate(){ return date; }

	public void print(){
		System.out.println("ID: " + id + "\tPath: " + file_path);
	}
}