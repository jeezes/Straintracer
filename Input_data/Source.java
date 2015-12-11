package Input_data;

public class Source{
	int source_id;
	String source_name;

	public Source(int id, String name){
		this.source_id = id;
		this.source_name = name;
	}

	public String getName(){ return source_name; }

	public void print(){
		System.out.println("SourceID: " + source_id + "\tSource_name: " + source_name);
	}

	public boolean compare(String name){
		if(source_name.equalsIgnoreCase(name)) return true;
		return false;
	}

	public int getId(){ return source_id; }
}