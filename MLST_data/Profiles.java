import java.util.*;
import java.io.*;
import java.text.*;

class Profiles{
	String profile_name;
	int profile_id;
	Date date_created;

	Profiles(int id, String name){
		this.profile_id = id;
		this.profile_name = name;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date_created = new Date();
	}

	public void print(){
		System.out.println("ID: " + profile_id + "\tProfile name: " + profile_name + "\tDate created: " + date_created);
	}

	public int getId(){ return profile_id; }
}