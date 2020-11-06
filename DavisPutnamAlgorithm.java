import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class DavisPutnamAlgorithm {

	public static void main(String[] args) throws IOException {
		
		// declare variable
		ArrayList<String> clauses = new ArrayList<String>(); 	// to store all clauses
		ArrayList<String> strings = new ArrayList<String>(); 	// to store rest of the lines
		Set<String> ATOMS = new HashSet<String>(); 				// to store atoms from clauses
		HashMap<String, String> atomVal = new HashMap<String, String>();
		
		// check if file is provided
		if(args.length == 0) {
			System.out.println("File name not specified.");
			System.exit(1);
		}
		
		File file = null;
		boolean flag = false;
		// if flag is provided
		if(args.length == 1) {
			// No flag provided
			file = new File(args[0]);
		}
		else if(args.length == 2) {
			// flag provided
			if(args[0].equals("-s")) {
				flag = true;
			}
			else {
				System.out.println("INVALID FLAG");
				System.exit(1);
			}
			file = new File(args[1]);
		}
		
		// read file
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		
		// parse file
		String s;
		boolean afterZero = false;
		while((s = br.readLine()) != null) {
			if(s.equals("0")){
				afterZero = true;
				continue;
			}
			
			if(afterZero) {
				strings.add(s);
			}
			else {
				clauses.add(s);
			}
		} // end while loop
		
		// parse atoms in clauses
		for(String clause : clauses) {
			StringTokenizer st = new StringTokenizer(clause, " ");
			while(st.hasMoreTokens()) {
				String temp = st.nextToken();
				
				// test if it contains negation
				if(temp.indexOf('-') >= 0) {
					temp = temp.replace("-", "");
				}
				
				ATOMS.add(temp);
			} // end while loop
		} // end foreach loop
		
		DP dp = new DP(ATOMS, clauses, flag);
		atomVal = dp.getV();
		if(flag) {
			System.out.println("V: " + atomVal);
			System.out.println("S: " + clauses);
			System.out.println();
		}
		
		Stack<TRIED> stack = new Stack<TRIED>();
		HashMap<String, String> finalAtomVal = new HashMap<String, String>();
		while((finalAtomVal = dp.DPHelper(ATOMS, clauses, atomVal)) == null) {
			
			stack = dp.getTried();
			if(stack.isEmpty()) {
				break;
			}
			TRIED t = stack.pop();
			
			String atom = t.getAtom();
			ArrayList<String> currS = new ArrayList<String>();
			currS = t.getCurrS();
			HashMap<String, String> currV = new HashMap<String, String>();
			currV = t.getCurrV();
			for(Map.Entry<String, String> entry : currV.entrySet()) {
				if(entry.getKey().equals(atom)) {
					if(flag) {
						System.out.println("TRY " + atom + " FALSE");
					}
					currV.put(atom, "FALSE");
					dp.elimDoneAtom(currS, atom, "FALSE");
					if(flag) {
						System.out.println("CLAUSES WITH " + atom + " ARE DELETED");
						System.out.println("V: " + currV);
						System.out.println("S: " + currS);
						System.out.println("\n> READY FOR NEXT ITERATION\n");
					}
				}
			}
			
			clauses = currS;
			atomVal = currV;
		}
		
		if(finalAtomVal != null) {
			for(Map.Entry<String, String> entry : finalAtomVal.entrySet()) {
				String atom = entry.getKey();
				String truthVal = entry.getValue();
				if(truthVal.equals("TRUE")) {
					System.out.println(atom + " T");
				}
				else if(truthVal.equals("FALSE")) {
					System.out.println(atom + " F");
				}
				else if(truthVal.equals("TRUE/FALSE")) {
					System.out.println(atom + " T/F");
				}
			}
		}
		System.out.println("0");
		for(String string : strings) {
			System.out.println(string);
		}
		
	} // end main method

} // end class DavidPutnamAlgorithm
