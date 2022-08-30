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

//Repeat, the logic will be handled in Interpreter
//takes in the list of tokens and generates a block, and a boolean Statement
public class repeatBlock extends InTree<Object>
{
	private ArrayList<Object> children = new ArrayList<Object>();
	//repeat <block> until <boolState>
	private ArrayList<String> repeatArgs = new ArrayList<String>();
	
	//I could make these back trace, or handle the cases as lower level breakdowns
	public repeatBlock(ArrayList<String> allTokens) 
	{
		System.out.print("Repeat -> ");
		allTokens.remove(0);
		//call new block
		Block b1 = new Block(allTokens);
		children.add(b1);
		allTokens.remove(0);
		//call new booleanStatement
		booleanStatement bool1 = new booleanStatement(allTokens);
		children.add(bool1);
	}
	
	public ArrayList<Object> getChildren()
	{
		return children;
	}
	
	public Collection<? extends String> returnArgs()
	{
		return repeatArgs;
	}
}
