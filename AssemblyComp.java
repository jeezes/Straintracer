import java.util.*;
import java.io.*;

class AssemblyComp{
	private String sequenceFile, report, reportRight, reportLeft, sequence_output, reportToDB;

	public AssemblyComp(String seq, String re, String rer, String rel, String se, String r){
		this.sequenceFile = seq;
		this.report = re;
		this.reportRight = rer;
		this.reportLeft = rel;
		this.sequence_output = se;
		this.reportToDB = r;
		writeFile();
	}

	public void writeFile(){
		Writer writer = null;
		try {
		    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reportToDB), "utf-8"));
		    writer.write(sequenceFile + ";" + report + ";" + reportRight + ";" + reportLeft);

		    File dest = new File(sequence_output);
			File source = new File(sequenceFile);
			InputStream input = new FileInputStream(source);
			OutputStream output = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
		} catch (IOException ex) {
		  ex.printStackTrace();
		} finally {
		   try {writer.close();} catch (Exception ex) {/*ignore*/}
		}
	}

	public static void main(String[] args){
		if(args.length != 6){
			System.out.println("To few arguments. Should have 6, had " + args.length);
			System.exit(-1);
		}else{
			new AssemblyComp(args[0], args[1], args[2], args[3], args[4], args[5]);
		}
	}
}