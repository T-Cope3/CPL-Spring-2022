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

//Boolean, might be passed up to parent for flexibility
//takes in the list of tokens and generates arguments:
	//a boolean operator and two arithmetic statements
public class booleanStatement extends InTree<Object>
{
	private ArrayList<Object> children = new ArrayList<Object>();
	//<relative_OP> <arith> <arith>
	private ArrayList<String> boolArgs = new ArrayList<String>();
	
	private String operator = "";
	
	public booleanStatement(ArrayList<String> allTokens)
	{
		System.out.print("Boolean -> ");
		
		//relative_OP
		operator = allTokens.remove(0);
		operator = operator.substring(0, operator.indexOf("->")).trim();
		
		arithmeticStatement arth = new arithmeticStatement(allTokens);
		boolArgs.addAll(arth.returnArgs());
		
		arithmeticStatement arth1 = new arithmeticStatement(allTokens);
		boolArgs.addAll(arth1.returnArgs());
	}
	
	//returns true or false depending on the condition
	public ArrayList<String> returnArgs()
	{
		return boolArgs;
	}
	
	public boolean evaluateBoolean(HashMap<String, Integer> localMem)
	{
		//call we've seen thousands of time, just parsing the name directly
		String isolatedArg = boolArgs.get(0).substring(0, boolArgs.get(0).indexOf("->")).trim();
		String isolatedArg2 = boolArgs.get(1).substring(0, boolArgs.get(1).indexOf("->")).trim();
		
		//another fun sorter, handles everything using simulated memory or integers
		//lots of notes in arithmeticStatement about this, same function there
		switch(operator)
		{
		//order does not matter with this case
			case("~="):
			{
				if(localMem.get(isolatedArg) != null)
				{
					if(localMem.get(isolatedArg2) != null)
						return (localMem.get(isolatedArg) != localMem.get(isolatedArg2));
					else
						return (localMem.get(isolatedArg) != Integer.parseInt(isolatedArg2));
				}
				else
				{
					if(localMem.get(isolatedArg2) != null)
						return (Integer.parseInt(isolatedArg) != localMem.get(isolatedArg2));
					else
						return (Integer.parseInt(isolatedArg) != Integer.parseInt(isolatedArg2));
				}
			}
			case("<"):
			{
				if(localMem.get(isolatedArg) != null)
				{
					if(localMem.get(isolatedArg2) != null)
						return (localMem.get(isolatedArg) < localMem.get(isolatedArg2));
					else
						return (localMem.get(isolatedArg) < Integer.parseInt(isolatedArg2));
				}
				else
				{
					if(localMem.get(isolatedArg2) != null)
						return (Integer.parseInt(isolatedArg) < localMem.get(isolatedArg2));
					else
						return (Integer.parseInt(isolatedArg) < Integer.parseInt(isolatedArg2));
				}
			}
		}
		return false;
	}
	
	//line ends here, necessary due to casting
	public ArrayList<Object> getChildren()
	{
		return children;
	}
}
