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

//While, the logic will be handled in Interpreter
//takes in the list of tokens and generates a boolean block and one blocks
public class whileBlock extends InTree<Object>
{
	private ArrayList<Object> children = new ArrayList<Object>();
	//while <boolStm> do <block> end
	//can treat the while block and the repeat block identically, simply reversed
	public whileBlock(ArrayList<String> allTokens)
	{
		System.out.print("While -> ");
		//removes the while
		allTokens.remove(0);
		//boolean business here
		booleanStatement bool1 = new booleanStatement(allTokens);
		children.add(bool1);
		//removes do
		allTokens.remove(0);
		//another block made
		Block b1 = new Block(allTokens);
		children.add(b1);
		
//		System.out.println("When the removal takes place the stack is: " + allTokens);
		
		//removal of end
		allTokens.remove(0);
	}
	
	public ArrayList<Object> getChildren()
	{
		return children;
	}
}
