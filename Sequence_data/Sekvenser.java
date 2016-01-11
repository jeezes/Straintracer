package Sequence_data;

import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


public class Sekvenser{
	String sekvens; // Name of the file. The file gets stored in a seperate file and the path gets stored on the DB
	ArrayList<SekvensDiff> meta;
	int sekundær_id;
	int sekvens_id;

	private String path = "sequences\\";

	public Sekvenser(int sekvens_id, String sekvens){
		this.sekvens_id = sekvens_id;
		meta = new ArrayList<SekvensDiff>();
		this.sekvens = sekvens;
		storeFile(sekvens);
		sekundær_id = 100;
	}
	
	public Sekvenser(int sekvens_id, String sekvens, int sec){
		this.sekvens_id = sekvens_id;
		meta = new ArrayList<SekvensDiff>();
		this.sekvens = sekvens;
		storeFile(sekvens);
		this.sekundær_id = sec;
	}

	public void addMeta(SekvensDiff s){
		meta.add(s);
	}

	/* Method for moving files 
        Path movefrom = FileSystems.getDefault().getPath("C:/tutorial/Swing_2.jpg");
        Path target = FileSystems.getDefault().getPath("C:/tutorial/photos/Swing_2.jpg");
        Path target_dir = FileSystems.getDefault().getPath("C:/tutorial/photos");

        //method 1
        try {
            Files.move(movefrom, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println(e);
        }

        */


	public void storeFile(String file){
		file = file.substring(14);
		final String dir = System.getProperty("user.dir");
		Path curPath = FileSystems.getDefault().getPath(dir);
		Path movefrom = FileSystems.getDefault().getPath(curPath + "/Sequence_data/" + file);
		Path target = FileSystems.getDefault().getPath(curPath + "/Sequence_data/sequences/" + file);
		
		/*System.out.println(dir.toString());
		System.out.println(movefrom.toString());
		System.out.println(target.toString());*/
		try {
            Files.move(movefrom, target, StandardCopyOption.REPLACE_EXISTING);
            sekvens = target.toString();
            System.out.println(sekvens);
        } catch (IOException e) {
            System.err.println(e);
        }
        System.out.println(" ");
	}

	public int getId(){ return sekvens_id; }

	public String getSequence(){ return sekvens; }

	public int getSecId(){ return sekundær_id; }

	public void print(){
		System.out.println("Sekvens_id: " + sekvens_id + "\nSekvens: " + sekvens);
		for(SekvensDiff s: meta){
			s.print();
		}
	}
}