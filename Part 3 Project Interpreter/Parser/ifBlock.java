package parserProj;

/*
 * Class:       CS 4308 Section 03
 * Term:        _Spring 2022_
 * Name:        <Troy Cope>
 * Instructor:  Sharon Perry
 * Project:     Deliverable P3 Interpreter  
 */

import java.util.ArrayList;

import interpretProj.InTree;

//If, the logic will be handled in Interpreter
//takes in the list of tokens and generates a boolean block and two blocks
public class ifBlock extends InTree<Object>
{
	private ArrayList<Object> children = new ArrayList<Object>();
	//if <boolStm> then <block> else <block> end
	public ifBlock(ArrayList<String> allTokens)
	{
		System.out.print("If -> ");
		allTokens.remove(0);
		//boolean business here
		booleanStatement bool1 = new booleanStatement(allTokens);
		children.add(bool1);
		allTokens.remove(0);
		//another block made
		Block b1 = new Block(allTokens);
		children.add(b1);
		//removes the then
		allTokens.remove(0);
		//another block made
		Block b2 = new Block(allTokens);
		children.add(b2);
		//removes the end
		allTokens.remove(0);
	}

	public void removeChildren(int a)
	{
		children.remove(a);
	}
	
	public ArrayList<Object> getChildren()
	{
		return children;
	}
}
