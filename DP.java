import java.util.*;

public class DP {
	
	private HashMap<String, String> V;
	private ArrayList<String> S;
	private Set<String> ATOMS;
	private ArrayList<String> SC;
	private HashMap<String, String> VC;
	private Stack<TRIED> TriedAtomValNSet = new Stack<TRIED>();
	private boolean flag;
	
	public DP(Set<String> ATOMS, ArrayList<String> Clauses, boolean flag) {
		this.ATOMS = ATOMS; 
		this.S = Clauses;
		this.V = new HashMap<String, String>();
		this.flag = flag;
		
		for(String atom : this.ATOMS) {
			this.V.put(atom, "UNBOUND");
		}
	}
	
	public ArrayList<String> getSC(){ return SC; }
	
	public HashMap<String, String> getVC(){ return VC; }
	
	public HashMap<String, String> getV(){ return V; }
	
	public Stack<TRIED> getTried(){ return TriedAtomValNSet; }
	
	public boolean SetHasPureLiteral(ArrayList<String> S) {
		boolean tempBool = true;
		Set<String> tempSet = new HashSet<String>();
		
		for(String clause : S) {
			StringTokenizer st = new StringTokenizer(clause, " ");
			while(st.hasMoreTokens()) {
				tempSet.add(st.nextToken());
			}
		} // end for loop
		
		for(String atom : ATOMS) {
			String negateAtom = "-" + atom;
			
			if(tempSet.contains(atom) && tempSet.contains(negateAtom)) {
				tempBool = false;
				continue;
			}
			else if(!tempSet.contains(atom) && !tempSet.contains(negateAtom)){
				continue;
			}
			
			tempBool = true;
			break;
		} // end for loop
		return tempBool;
	} // end method hasPureLiteral
	
	public boolean SetHasSingleAtomClause(ArrayList<String> S) {
		boolean tempBool = true;
		
		for(String clause : S) {
			StringTokenizer st = new StringTokenizer(clause, " ");
			st.nextToken();
			if(st.hasMoreTokens()) {
				tempBool = false;
			}
			else {
				tempBool = true;
				break;
			}
		} // end foreach loop
		return tempBool;
	} // end hasSingleAtomClause method
	
	public void elimDoneAtom(ArrayList<String> currSet, String atom, String truthVal) {
		String negateAtom = "-" + atom;
		
		for(int i = 0; i < currSet.size(); i++) {
			String clause = currSet.get(i);
			
			if(clause.contains(atom)) {
				String newClause = "";
				StringTokenizer st = new StringTokenizer(clause, " ");
				
				while(st.hasMoreTokens()) {
					String temp = st.nextToken();
					if(temp.equals(atom)) {
						if(truthVal.equals("TRUE")) {
							newClause = "";
							break;
						}
						else if(truthVal.equals("FALSE")) {
							// don't add to newClause
							if(currSet.size() == 1) {
								newClause = "NULL ";
							}
						}
					}
					else if (temp.equals(negateAtom)) {
						if(truthVal == "TRUE") {
							// don't add to newClause
							if(currSet.size() == 1) {
								newClause = "NULL ";
							}
						}
						else if(truthVal == "FALSE") {
							newClause = "";
							break;
						}
					}
					else {
						newClause += temp + " ";
					}
				} // end while loop
				if(newClause.length() != 0) {
					newClause = newClause.substring(0, newClause.length()-1);
				}
				
				currSet.set(i, newClause); // replace old clause with new clause
			} // end if
		} // end for loop
		while(currSet.contains("")) {
			currSet.remove("");
		}
	} // end method elimDoneAtom
	
	public HashMap<String, String> DPHelper(Set<String> ATOMS, ArrayList<String> S, HashMap<String, String> V){
		
		while(true) {
			if(flag) {
				System.out.println("> BEGINNING THE LOOP\n");
			}
			
			if(S.isEmpty()) {
				if(flag) {
					System.out.println("<<< SET IS EMPTY >>>\n");
				}
				
				for(Map.Entry<String, String> entry : V.entrySet()) {
					if(entry.getValue().equals("UNBOUND")) {
						V.put(entry.getKey(), "TRUE/FALSE");
					}
				}
				return V;
			}
			
			if(S.contains("NULL")) {
				if(flag) {
					System.out.println("<<< SET CONTAINS NULL CLAUSE >>>\n");
				}
				
				return null;
			}
			
			if(this.SetHasPureLiteral(S)) {
				Set<String> tempSet = new HashSet<String>();
				
				for(String clause : S) {
					StringTokenizer st = new StringTokenizer(clause, " ");
					while(st.hasMoreTokens()) {
						tempSet.add(st.nextToken());
					}
				} // end for loop
				
				for(String atom : ATOMS) {
					String negateAtom = "-" + atom;
					
					if(tempSet.contains(atom) && tempSet.contains(negateAtom)) {
						// not pure literal
					}
					else {
						// is pure literal
						if(tempSet.contains(atom) && !tempSet.contains(negateAtom)) {
							if(flag) {
								System.out.println("SET HAS PURE LITERAL: " + atom);
							}
							
							V.put(atom, "TRUE");
							this.elimDoneAtom(S, atom, "TRUE");
							if(flag) {
								System.out.println("CLAUSES WITH " + atom + " ARE DELETED");
							}
							
							break;
						}
						
						if(!tempSet.contains(atom) && tempSet.contains(negateAtom)) {
							if(flag) {
								System.out.println("SET HAS PURE LITERAL " + negateAtom);
							}
							
							V.put(atom, "FALSE");
							this.elimDoneAtom(S, atom, "FALSE");
							if(flag) {
								System.out.println("CLAUSES WITH " + atom + " ARE DELETED");
							}
							
							break;
						}
					}
				} // end for loop
				if(flag) {
					System.out.println("V: " + V);
					System.out.println("S: " + S);
					System.out.println("END <PURE LITERAL>\n");
				}
				
				continue; // back to the beginning of loop -- while loop
			}
			
			if(this.SetHasSingleAtomClause(S)) {
				for(String clause : S) {
					StringTokenizer st = new StringTokenizer(clause, " ");
					
					if(st.countTokens() == 1) {
						String temp = st.nextToken();
						String truthVal = "UNBOUND";
						
						if(flag) {
							System.out.println("SET HAS SINGLE ATOM CLAUSE: " + temp);
						}
						
						// test if it contains negation
						if(temp.indexOf('-') >= 0) {
							temp = temp.replace("-", "");
							truthVal = "FALSE";
						}
						else {
							truthVal = "TRUE";
						}
						
						V.put(temp, truthVal);
						S.remove(clause);
						this.elimDoneAtom(S, temp, truthVal);
						if(flag) {
							System.out.println("CLAUSES WITH " + temp + " ARE DELETED");
						}
						
						break; // break inner loop -- for loop
					}	
				} // end foreach loop
				if(flag) {
					System.out.println("V: " + V);
					System.out.println("S: " + S);
					System.out.println("END <SINGLE ATOM>\n");
				}
				
				continue; // back to the beginning of loop -- while loop
			}
			
			break; // when 4 if are not executed, break while loop
		} // end while
		
		// true/false atom stuff
		if(flag) {
			System.out.println("> OUTSIDE OF LOOP\n");
		}
		
		// ====================== DEEP COPY ======================
		SC = new ArrayList<String>();
		VC = new HashMap<String, String>();
		
		for(String clause : S) {
			SC.add(new String(clause));
		}
		
		for(Map.Entry<String, String> entry : V.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			VC.put(new String(key), new String(value));
		}
		// ====================== DEEP COPY ======================
		
		for(Map.Entry<String, String> entry : VC.entrySet()) {
			if(entry.getValue().equals("UNBOUND")) {
				String atom = entry.getKey();
				if(flag) {
					System.out.println("TRY " + atom + " TRUE");
				}
				VC.put(atom, "TRUE");
				
				ArrayList<String> tempStoreClause = new ArrayList<String>();
				for(String clause : SC) {
					tempStoreClause.add(new String(clause));
				}
				HashMap<String, String> m1 = new HashMap<String, String>();
				for(Map.Entry<String, String> entry1 : VC.entrySet()) {
					String key = entry1.getKey();
					String value = entry1.getValue();
					m1.put(new String(key), new String(value));
				}
				TRIED triedObj = new TRIED(atom, tempStoreClause, m1);
				TriedAtomValNSet.push(triedObj);
				
				this.elimDoneAtom(SC, atom, "TRUE");
				if(flag) {
					System.out.println("CLAUSES WITH " + atom + " ARE DELETED");
				}
				
				break;
			}
		}
		if(flag) {
			System.out.println("V: " + VC);
			System.out.println("S: " + SC);
			System.out.println("\n> READY FOR NEXT ITERATION\n");
		}
		
		return DPHelper(ATOMS, SC, VC);
	}
}
