package Input_data;

public class Bruker{
	private int bruker_id;
	private String firstname;
	private String lastname;

	public Bruker(int bruker_id, String first, String last){
		this.bruker_id = bruker_id;
		this.firstname = first;
		this.lastname = last;
	}

	public String getName(){ return firstname + " " + lastname; }
	
	public String getLastName(){ return lastname; }
	
	public String getFirstName(){ return firstname; }

	public int getId(){ return bruker_id; }

	public void print(){
		System.out.println("Bruker_id " + bruker_id + "\tNavn " + this.getName());
	}
}
