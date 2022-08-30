package helperObjects;

import java.util.ArrayList;

import interpretProj.Interpreter;
import parserProj.ParserObj;
import scannerProj.ScannerObj;

/*
 * Class:       CS 4308 Section 03
 * Term:        _Spring 2022_
 * Name:        <Troy Cope>
 * Instructor:  Sharon Perry
 * Project:     Deliverable P3 Parser  
 */

public class headerProgram 
{
	public static void main(String[] args)
	{
		ScannerObj scannerBuild = new ScannerObj();
		
		try {
			//build of ouptut lines: "work -> token"
			ArrayList<String> output = scannerBuild.buildTheFile("Test3.jl");
			
			//the tree of all items, output as Parent Class -> Child Class in stack order
			ParserObj parseTree = new ParserObj(output);
			
			//interpret the tree built before and spit out what the program calls
			Interpreter interpret = new Interpreter(parseTree);
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
