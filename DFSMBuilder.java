import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class DFSMBuilder {
	
	/**
	 * Course Name:CS-5313 Formal Language Theory
	 * Submitted By:Asim Neupane
	 * Programming Assignment Number: 1
	 *
	 * This program takes the string as input and creates DFSM specification that accepts the string which contains the 
	 * given string as a substring. This program condiers substring contains all alphabets of text.
	 *
	 */
	//global variables
	String pattern;
	String text;
	String fileName;
	char [] alphabets;
	int finalState;
 static	DFSMBuilder objDFSM = new DFSMBuilder();
 
 
	public void DFSMBuilderInputs(String fileName, String pattern){
		//System.out.println("Enter the file name(with extension): ");
		//fileName = ReadInput();
		fileName = fileName.replaceAll("\\s", "");
		
		if(fileName.isEmpty()){
			System.err.println("Error: Invalid filename or filename cannot be empty");
			System.exit(-1);
		}
		//System.out.println("Enter the pattern: ");
		//pattern = ReadInput();
		pattern = pattern.replaceAll("\\s", "");
		if(pattern.isEmpty()){
			System.err.println("Error: Invalid pattern or pattern can not be empty");
			System.exit(-1);
		}
		FetchAlphabets();
	}		
	//Write to a file
		public void WritetoFile(int [][] dfa){
			try {
				BufferedWriter brWriter = new BufferedWriter(new FileWriter(fileName));
				PrintWriter printWriter=new PrintWriter(brWriter);
				for(char ch : alphabets){
					printWriter.print(ch + " ");
				}
				printWriter.println();
				for(int row =0;row<dfa.length;row++){
					for(int column=0;column<dfa[row].length;column++){
						printWriter.print(dfa[row][column] + " ");
					} printWriter.println();
				}
				printWriter.println(objDFSM.finalState);
				printWriter.flush();
				brWriter.close();
				printWriter.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		public void FetchAlphabets(){
			
		//counting the number of unique alphabets in a substring
		// as this will determine the number of states
		
		//removing the duplicates
		char [] chars = DublicateRemover(pattern).toCharArray();
		//sorting
		Arrays.sort(chars);
		//copying chars to alphabet
		alphabets = Arrays.copyOf(chars, chars.length);
					
		}

		public String DublicateRemover(String str){
			Set<Character> set = new HashSet<Character>(); 
			StringBuffer sf = new StringBuffer();
			for(int i = 0; i<str.length();i++){
				Character c = str.charAt(i);
				if(!set.contains(c)){
					set.add(c);
					sf.append(c);
				}
			}
			return sf.toString();		
		}
		
		//Creates the DFA Specification
		public int[][] DFABuilder(String pattern){			
			char [] patrn = pattern.toCharArray();
			int [][] dfa = new int[pattern.length()+1][alphabets.length];

			// building transition table for base case or the matching case
			int rows = 1;
				for(int column = 0; column< pattern.length(); column++){
					dfa[rows-1][GetPosition(patrn[column])] = rows +1;
					rows++;
				}
			//At last state for all input, the next state will be same state	   	
			finalState = pattern.length() +1 ;
			for(int i = 0; i<alphabets.length; i++){
			dfa[finalState-1][i] = finalState;
			}
			
		//building transition table for non-matching case
				
			//base case (for first state for all other input state remains same)
			for(int i = 0; i<alphabets.length; i++){
				if(dfa[0][i] == 0) dfa[0][i]= 1;
					}
			//for other cases that is normal cases 
			int  X= 0,pos=0;
			for (int i =1; i<patrn.length; i++){
				for(int j = 0; j<alphabets.length;j++){
					
				if(dfa[i][j] == 0){
					dfa[i][j] = dfa[X][j];
					
				}	
				else{
					pos =j;   //Checks position so that X position can be determined
				}
				}
				X = dfa[X][pos]-1;
			}		
			
			return dfa;
		}
		public int GetPosition(char presentInput){
			
			int index = -1;
			for (int i=0;i<alphabets.length;i++) {
			    if (alphabets[i] == presentInput) {
			        index = i;  //it starts with 0
			        break;
			    }
			}
			if(index == -1){
			System.err.println("Error: Input read does not belong to alphabets");
			System.exit(-1);
			}
			return index;  
		}
		
		public void PrintRow(int[] row){
			System.out.println();
			for(int i : row){
				System.out.print(i);
				System.out.print("\t");
			}
			
		}
	
		public static void main(String[] args){
			
			if(args.length <2){
				System.err.println("Error: Insufficient Inputs");
				System.exit(-1);
			}
			if(args.length>2){
				System.err.println("Error: Too many input passed");
				System.exit(-1);
			}
			
			objDFSM.fileName = args[0];
			objDFSM.pattern = args[1];
			objDFSM.DFSMBuilderInputs(objDFSM.fileName,objDFSM.pattern);
			int[][] dfa =objDFSM.DFABuilder(objDFSM.pattern);
			objDFSM.WritetoFile(dfa);
			
		}
		
}