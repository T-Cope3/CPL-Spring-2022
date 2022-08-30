package parserProj;

/*
 * Class:       CS 4308 Section 03
 * Term:        _Spring 2022_
 * Name:        <Troy Cope>
 * Instructor:  Sharon Perry
 * Project:     Deliverable P3 Interpreter  
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import interpretProj.InTree;

//Arithmetic,  might be passed up to parent for flexibility
//takes in the list of tokens and generates arguments:
	//an arithmetic operator and two arithmetic statements
	//or an id 
	//or literal int
	//can be chained multiple times with minor adjustment
public class arithmeticStatement extends InTree<Object>
{
	private ArrayList<Object> children = new ArrayList<Object>();
	private ArrayList<String> arithArgs = new ArrayList<String>();
	
	static int counter = 0;
	//<id> | <literal_int> | <arith_OP> <arith> <arith>
	public arithmeticStatement(ArrayList<String> allTokens)
	{
		System.out.print("Arithmetic -> ");
		counter++;
		
//		System.out.println("allTokens at arithStatement_" + counter + ": " + allTokens);
		//once again, important to play it safe with the stack order
		String s = allTokens.remove(0);
		
		
		//a -> z setup
		if(s.contains("id"))
		{
			System.out.print("Id -> ");
			arithArgs.add(s);
//			System.out.println("id tripped");
		}
		//starts with 0-9
		else if(s.contains("literal_integer"))
		{
			System.out.print("Literal Integer -> ");
			arithArgs.add(s);
//			System.out.println("lit int tripped");
		}
		else
		{
//			System.out.println("else tripped");
			//peels off the Arith_OP and two ariths pieces
			arithArgs.add(s);
			
			arithmeticStatement arth = new arithmeticStatement(allTokens);
			arithArgs.addAll(arth.returnArgs());
			
			arithmeticStatement arth1 = new arithmeticStatement(allTokens);
			arithArgs.addAll(arth1.returnArgs());
		}
		//conditional to detect all these.
		//check if leading with +, -, *, /
		//triple if block for each setup
	}
	
	public Collection<? extends String> returnArgs()
	{
		return arithArgs;
	}
	
	//identical to booleanStatement's evaluation but with math instead.
	public int evaluateArith(HashMap<String, Integer> localMem)
	{
		//call we've seen thousands of time, just parsing the name directly
		String isolatedArg = arithArgs.get(1).substring(0, arithArgs.get(1).indexOf("->")).trim();
		String isolatedArg2 = arithArgs.get(2).substring(0, arithArgs.get(2).indexOf("->")).trim();
		
		switch(arithArgs.get(0).substring(0, arithArgs.get(0).indexOf("->")).trim())
		{
		//order does not matter with this case
		/* I'm not going to expand this beyond the test cases.
		 * It's very obvious that the handling just requires a singular variable change for
		 * both this and for booleanStatement. For that reason I think it's very redundant to define.
		 * 
		 * I gave some consideration to making another function to handle this, but ran into the issue
		 * that I would have to use a switch anyways, and beyond that there is no neat way to handle this in Java
		 * without building my own HashMap subclass, therefore it's more intuitive to do this.
		 */
			case("+"):
			{
				if(localMem.get(isolatedArg) != null)
				{
					if(localMem.get(isolatedArg2) != null)
						return (localMem.get(isolatedArg) + localMem.get(isolatedArg2));
					else
						return (localMem.get(isolatedArg) + Integer.parseInt(isolatedArg2));
				}
				else
				{
					if(localMem.get(isolatedArg2) != null)
						return (Integer.parseInt(isolatedArg) + localMem.get(isolatedArg2));
					else
						return (Integer.parseInt(isolatedArg) + Integer.parseInt(isolatedArg2));
				}
			}
		}
		//zero is default, but this will never trigger.
		return 0;
	}
	
	public ArrayList<Object> getChildren()
	{
		return children;
	}
}