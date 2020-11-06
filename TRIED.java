import java.util.*;

public class TRIED {
	private String atom;
	private ArrayList<String> currS;
	private HashMap<String, String> currV;
	
	public TRIED(String atom, ArrayList<String> S, HashMap<String, String> V){
		this.atom = atom;
		this.currS = S;
		this.currV = V;
	}
	
	public String getAtom() { return atom; }
	
	public ArrayList<String> getCurrS() { return currS; }
	
	public HashMap<String, String> getCurrV() { return currV; }
	
	public String toString() {
		String TriedObj = "";
		String s1 = "atom: " + atom + "\n";
		String s2 = "S: " + currS + "\n";
		String s3 = "V: " + currV;
		TriedObj = s1 + s2 + s3;
		return TriedObj;
	}
}
