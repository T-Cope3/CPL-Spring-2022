package parserProj;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/*
 * Class:       CS 4308 Section 03
 * Term:        _Spring 2022_
 * Name:        <Troy Cope>
 * Instructor:  Sharon Perry
 * Project:     Deliverable P2 Parser  
 */


//ParserObj, the root node
//takes in the list of tokens and generates blocks
public class ParserObj 
{
	public static ArrayList<String> listOfTokens = null;
	//functID() <Block> end
	private ArrayList<Block> children = new ArrayList<Block>();
	public ParserObj(ArrayList<String> allTokens) throws Exception
	{
		listOfTokens = allTokens;
		//System.out.println("allTokens atm is: " + allTokens);
		String tokenIdentity = allTokens.get(0).substring(allTokens.get(0).indexOf("->")+2).trim();
		//System.out.println("tokenIden: " + tokenIdentity);
		
		if(tokenIdentity.equals("function_keyword"))
		{
			//manually eating the keyword, declaration and ending end
			listOfTokens.remove(0);
			listOfTokens.remove(0);
			if(!listOfTokens.remove(listOfTokens.size()-1).contains("end_Of_Function"))
			{
				throw new Exception("Program does not finish with an 'end' statement.");
			}
		}
		
		//showing the structure
		System.out.print("Program Struct -> ");
		
		int counter = 0;
		while(listOfTokens.size() > 0)
		{
			counter++;
			
			//building more and more blocks as long as arguments exist
			//System.out.println("Block" + counter + " has been created!");
			
			children.add(new Block(listOfTokens));
		}
	}
	
	//shown in all types, used for architecture and tree structure
	public ArrayList<Block> getChildren()
	{
		return children;
	}

}

//Block, the node that is referenced in most statements
//takes in the list of tokens and generates statements
class Block
{
	public static int counter = 0;
	// <statement> | <statement><block>
	private ArrayList<Statement> children = new ArrayList<Statement>();
	public Block(ArrayList<String> allTokens)
	{
		System.out.print("Block -> ");
		counter++;
		children.add(new Statement(allTokens));
	}
	
	public ArrayList<Statement> getChildren()
	{
		return children;
	}
}

//Statement, the node that is referenced after blocks only
//takes in the list of tokens and generates ifs, assigns, while, print and repeat
class Statement
{
	public static int counter = 0;
	private ArrayList<Object> children = new ArrayList<Object>();
	public Statement(ArrayList<String> allTokens)
	{
		System.out.print("Statement -> ");
		//System.out.println("with Tokens: " + allTokens);
		counter++;
//		System.out.println("System had begun a build! num: " + counter);
//		try {
//			TimeUnit.SECONDS.sleep(2);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		children.add(buildChild(allTokens));
	}
	
	//The sorting function to decide which Statement is created
	// if, assign, while, print or repeat, null if none
	private Object buildChild(ArrayList<String> allTokens)
	{
		//this long statement is used to peel off the assigned token, since I'm lazy and don't want to do more classes
		String tokenIdentity = allTokens.get(0).substring(allTokens.get(0).indexOf("->")+2).trim();
		switch(tokenIdentity)
		{
		//Default case can be handled as assign due to it not having assign first
			default:
			{
				System.out.println("This token was not recognized: " + tokenIdentity);
				break;
			}
			case "if_Statement":
			{
				return new ifBlock(allTokens);
			}
			//this is the only one that does not begin with it's own name
			case "id":
			{
				if(allTokens.get(1).substring(0, allTokens.get(0).indexOf("->")).trim().equals("=") || allTokens.get(1).substring(0, allTokens.get(0).indexOf("->")).trim().equals("+="))
					{
					return new assignStatement(allTokens);
					}
				System.out.println("triggered assignment_stm but did not find = in index 1");
				break;
			}
				
			case "while_Statement":
			{
				return new whileBlock(allTokens);
			}
			case "print_Statement":
			{
				return new printStatement(allTokens);
			}
			case "repeat_Statement":
			{
				return new repeatBlock(allTokens);
			}
		}
		return null;
	}
	
	public ArrayList<Object> getChildren()
	{
		return children;
	}
	//if | assign | while | print | repeat
}


//If, the logic will be handled in Interpreter
//takes in the list of tokens and generates a boolean block and two blocks
class ifBlock
{
	private ArrayList<Object> children = new ArrayList<Object>();
	//if <boolStm> then <block> else <block> end
	public ifBlock(ArrayList<String> allTokens)
	{
		System.out.print("If -> ");
		allTokens.remove(0);
		//boolean business here
		booleanStatement bool1 = new booleanStatement(allTokens);
		allTokens.remove(0);
		//another block made
		Block b1 = new Block(allTokens);
		//removes the then
		allTokens.remove(0);
		//another block made
		Block b2 = new Block(allTokens);
		//removes the end
		allTokens.remove(0);
	}
	
	public ArrayList<Object> getChildren()
	{
		return children;
	}
}

//While, the logic will be handled in Interpreter
//takes in the list of tokens and generates a boolean block and one blocks
class whileBlock
{
	private ArrayList<Object> children = new ArrayList<Object>();
	//while <boolStm> do <block> end
	public whileBlock(ArrayList<String> allTokens)
	{
		System.out.print("While -> ");
		//removes the while
		allTokens.remove(0);
		//boolean business here
		booleanStatement bool1 = new booleanStatement(allTokens);
		//removes do
		allTokens.remove(0);
		//another block made
		Block b1 = new Block(allTokens);
		
//		System.out.println("When the removal takes place the stack is: " + allTokens);
		
		//removal of end
		allTokens.remove(0);
	}
	
	public ArrayList<Object> getChildren()
	{
		return children;
	}
}

//Assign, the logic will be handled in Interpreter
//takes in the list of tokens and generates an id value, and an arithmeticStatement
class assignStatement
{
	private ArrayList<Object> children = new ArrayList<Object>();
	private String id;
	ArrayList<String> assignArgs = new ArrayList<String>();
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
		assignArgs.addAll(art.returnArgs());
	}
	
	public ArrayList<Object> getChildren()
	{
		return children;
	}
}

//Repeat, the logic will be handled in Interpreter
//takes in the list of tokens and generates a block, and a boolean Statement
class repeatBlock
{
	private ArrayList<Object> children = new ArrayList<Object>();
	//repeat <block> until <boolState>
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
}

//Print, the logic will be handled in Interpreter
//takes in the list of tokens and generates  an arithmeticStatement
class printStatement
{
	private ArrayList<Object> children = new ArrayList<Object>();
	//print(<arith>)
	ArrayList<String> printArgs = new ArrayList<String>();
	public printStatement(ArrayList<String> allTokens)
	{
		System.out.print("Print -> ");
		//pops off print and first parenthesis
		allTokens.remove(0);
		allTokens.remove(0);
		arithmeticStatement arth = new arithmeticStatement(allTokens);
		children.add(arth);
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


//Boolean, might be passed up to parent for flexibility
//takes in the list of tokens and generates arguments:
	//a boolean operator and two arithmetic statements
class booleanStatement
{
	private ArrayList<Object> children = new ArrayList<Object>();
	//<relative_OP> <arith> <arith>
	ArrayList<String> boolArgs = new ArrayList<String>();
	
	public booleanStatement(ArrayList<String> allTokens)
	{
		System.out.print("Boolean -> ");
		
		//relative_OP
		boolArgs.add(allTokens.remove(0));
		
		arithmeticStatement arth = new arithmeticStatement(allTokens);
		boolArgs.addAll(arth.returnArgs());
		children.add(arth);
		
		arithmeticStatement arth1 = new arithmeticStatement(allTokens);
		boolArgs.addAll(arth1.returnArgs());
		children.add(arth1);
	}
	
	public Collection<? extends String> returnArgs()
	{
		return boolArgs;
	}
	
	public ArrayList<Object> getChildren()
	{
		return children;
	}
}

//Arithmetic,  might be passed up to parent for flexibility
//takes in the list of tokens and generates arguments:
	//an arithmetic operator and two arithmetic statements
	//or an id 
	//or literal int
	//can be chained multiple times with minor adjustment
class arithmeticStatement
{
	private ArrayList<Object> children = new ArrayList<Object>();
	String statement;
	ArrayList<String> arithArgs = new ArrayList<String>();
	
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
//			System.out.println("id tripped");
			arithArgs.add(s);
		}
		//starts with 0-9
		else if(s.contains("literal_integer"))
		{
			System.out.print("Literal Integer -> ");
//			System.out.println("lit int tripped");
			arithArgs.add(s);
		}
		else
		{
//			System.out.println("else tripped");
			//peels off the Arith_OP and two ariths pieces
			arithArgs.add(s);
			
			arithmeticStatement arth = new arithmeticStatement(allTokens);
			arithArgs.addAll(arth.returnArgs());
			children.add(arth);
			
			arithmeticStatement arth1 = new arithmeticStatement(allTokens);
			arithArgs.addAll(arth1.returnArgs());
			children.add(arth1);
		}
		//conditional to detect all these.
		//check if leading with +, -, *, /
		//triple if block for each setup
	}
	
	public Collection<? extends String> returnArgs()
	{
		return arithArgs;
	}
	
	public ArrayList<Object> getChildren()
	{
		return children;
	}
}