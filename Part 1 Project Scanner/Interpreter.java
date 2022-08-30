package interpreterProj;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Class:       CS 4308 Section 03
 * Term:        _Spring 2022_
 * Name:        <Troy Cope>
 * Instructor:  Sharon Perry
 * Project:     Deliverable P1 Scanner  
 */


public class Interpreter 
{

	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException
	{
		//preparing to read in the file
		FileManager fileMan = new FileManager("Test3.jl");
		String splitToken = " ";
		fileMan.setUp();
		//myFile.createNewFile(); always creates a file, so after being run the setup is done.
		
		//putting all the values into the string array
		ArrayList<String> myValues = fileMan.splitFileByLines();
		
		//splitting them all via spaces
		ArrayList<String> newVals = new ArrayList<String>();
		for(int i = 0; i < myValues.size(); i++)
		{
			newVals.addAll(fileMan.splitLineByTokens(" ", myValues.get(i)));
		}
		
		//setting up the 2D array of Strings
		ArrayList<ArrayList<String>> twoDLineByLine = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < myValues.size(); i++)
		{
			twoDLineByLine.add(new ArrayList<String>());
			twoDLineByLine.get(i).add(myValues.get(i));
		}
		
		
		//splitting each line up into individual phrases, splitting lone parenthesis into spaced out ones
		for(int i = 0; i < twoDLineByLine.size(); i++)
			twoDLineByLine.set(i, fileMan.splitLineByTokens(splitToken, accountForFunctions(twoDLineByLine.get(i).get(0))));
		
		System.out.println("The lines, pre-tokenizer are: " + twoDLineByLine);
		
		ArrayList<String> outputLines = new ArrayList<String>();
		
		//giving tokens to every individual phrase, separated by spaces in last bit
		for(ArrayList<String> L1: twoDLineByLine)
		{
			for(String s: L1)
			{
				outputLines.add(s + " -> " + assignToken(s));
			}
		}
		
		System.out.println("The lines, post-tokenizer are: " + outputLines);
				
		//outputting to the file, standard
		FileManager outFile = new FileManager("nextInParser");
		outFile.setUp();
		outFile.WriteInto(outputLines);
		
	}
	
	//this is the major parsing method for the entire program, uses a direct parse then conditional parse
	public static String assignToken(String word)
	{
		//Final lists that work as my parallel tables
		final List<String> tokenNames = Arrays.asList
				(
				"end_Of_Function","function_keyword", "function_Opening", "function_Closing", "if_Statement", "then_Statement", "else_Statement", 
				"while_Statement", "do_Statement", "repeat_Statement", "until_Statement", "print_Statement", 
				"assignment_operator","le_operator", "lt_operator","ge_operator", "gt_operator", "eq_operator",
				"ne_operator", "add_operator", "sub_operator", "mul_operator", "div_operator", "Unknown"
				);
		
		final List<String> tokens = Arrays.asList
				(
				"end","function", "(", ")", "if", "then", "else", "while", "do", "repeat", "until", "print","=", 
				"<=", "<", ">=", ">", "==", "~=", "+", "-", "*", "/", "letter", "digit literal_integer | digit"
				);
		
		//literal assignment, very linear using the tables
		int counter = 0;
		for(String token: tokens)
		{
			if(word.toLowerCase().equals(token))
			{
				return tokenNames.get(counter);
			}
			counter++;
		}
		
		//section of conditional assignment which uses ascii values
		boolean isValidID = true;
		
		for(int i = 0; i < word.length(); i++)
		{
			if((int)word.toLowerCase().charAt(i) >= 97 && (int)word.toLowerCase().charAt(i) <= 122 && word.length() == 1)
			{}
			else
			{
				isValidID = false;
				break;
			}
		}
		
		if(isValidID)
			return "id";
			
		
		//more conditional assignment using ascii values
		boolean isValidNumeric = true;
		for(int i = 0; i < word.length(); i++)
		{
			if(((int) word.charAt(i)) >= 48 && ((int) word.charAt(i)) <=57)
			{			}
			else
			{
				isValidNumeric = false;
				break;
			}
		}
		
		if(isValidNumeric)
			return "literal_integer";
		
		//parse for function setup, and conditions that are irregular
		if(word.contains("()") && (int)word.toLowerCase().charAt(0) >= 97 && (int)word.toLowerCase().charAt(0) <= 122)
			return "function_Declaration";
		
		return tokenNames.get(tokenNames.size()-1);
	}
	
	//puts spaces around a singular phrase
	public static String putSpaceAroundSelection(String line, String choice)
	{
		String newString = line;
		
		newString.replaceAll(choice, " "+choice+" ");
		
		return newString;
	}
	
	//specifically giving space around singular brackets, makes space parsing work properly
	public static String accountForFunctions(String line) throws InterruptedException
	{
		String newLine = line;
		String repFront = " ( ";
		String repEnd = " ) ";
		String beforeInsert;
		String afterInsert;
		
		int start = 0;
		for(int i = newLine.indexOf("(", start); i > 0; i = newLine.indexOf("(", start))
		{
			if(!(newLine.charAt(i+1) == ')'))
			{
				beforeInsert = newLine.substring(0, i);
				afterInsert = newLine.substring(i+1);
				newLine = beforeInsert+repFront+afterInsert;
				
				start = i+2;
			}
			else
				start = i+2;
		}
		
		start = 0;
		for(int i = newLine.indexOf(")", start); i > 0; i = newLine.indexOf(")", start))
		{
			if(!(newLine.charAt(i-1) == '('))
			{
				beforeInsert = newLine.substring(0, i);
				afterInsert = newLine.substring(i+1);
				newLine = beforeInsert+repEnd+afterInsert;
				start = i+2;
			}
			else
				start = i+2;
		}
		return newLine;
	}
	
}
