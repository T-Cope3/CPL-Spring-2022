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

import interpretProj.InTree;

//Print, the logic will be handled in Interpreter
//takes in the list of tokens and generates  an arithmeticStatement
public class printStatement extends InTree<Object>
{
	private ArrayList<Object> children = new ArrayList<Object>();
	//print(<arith>)
	private ArrayList<String> printArgs = new ArrayList<String>();
	public printStatement(ArrayList<String> allTokens)
	{
		System.out.print("Print -> ");
		//pops off print and first parenthesis
		allTokens.remove(0);
		allTokens.remove(0);
		arithmeticStatement arth = new arithmeticStatement(allTokens);
		printArgs.addAll(arth.returnArgs());
		//print it or something like that
		allTokens.remove(0);
		//pops off final parenthesis
	}
	
	//used when there is a possibly of passing data back
	//this is the purest form of arguments form lower nodes
	public Collection<? extends String> returnArgs()
	{
		return printArgs;
	}
	
	public ArrayList<Object> getChildren()
	{
		return children;
	}
}
