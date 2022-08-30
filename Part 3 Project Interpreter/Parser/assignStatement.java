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

//Assign, the logic will be handled in Interpreter
//takes in the list of tokens and generates an id value, and an arithmeticStatement
public class assignStatement extends InTree<Object>
{
	private ArrayList<Object> children = new ArrayList<Object>();
	
	//only localized object I use outside of children and params, simply to avoid confusion in interpreter
	private String id;
	
	private ArrayList<String> assignArgs = new ArrayList<String>();
	// id = <arith>
	public assignStatement(ArrayList<String> allTokens)
	{
		System.out.print("Assign -> ");
//		System.out.println("Assignment reached");
//		System.out.println("allTokens at assignment: " + allTokens);
		//pop off the top 2, the id and equals
		id = allTokens.get(0).substring(0, allTokens.get(0).indexOf("->")).trim();
		
		//don't remove in line above due to stack order, removes id and =
		allTokens.remove(0);
		allTokens.remove(0);
		arithmeticStatement art = new arithmeticStatement(allTokens);
		children.add(art);
		assignArgs.addAll(art.returnArgs());
	}
	
	public Collection<? extends String> returnArgs()
	{
		//System.out.println("My ID is! " + id);
		return assignArgs;
	}
	
	//nothing beats a good old getter and setter to avoid confusion
	public String getId()
	{
		return id;
	}
	
	public ArrayList<Object> getChildren()
	{
		return children;
	}
}