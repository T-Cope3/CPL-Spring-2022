package interpretProj;

import java.util.ArrayList;
import java.util.HashMap;

import parserProj.*;

/*
 * Class:       CS 4308 Section 03
 * Term:        _Spring 2022_
 * Name:        <Troy Cope>
 * Instructor:  Sharon Perry
 * Project:     Deliverable P3 Interpreter  
 */

public class Interpreter 
{
	//branchMerge will keep tract of my tree
	public static ArrayList<Object> branchMerge = null;
	//tempMem is going to act as my memory for variables
	private HashMap<String, Integer> tempMem = null;
	
	@SuppressWarnings("unchecked")
	public Interpreter(ParserObj parseTree)
	{
		Object holder = parseTree;
		tempMem = new HashMap<String, Integer>();
		branchMerge = new ArrayList<Object>();
		
		//spacing bc parser printed out before
		System.out.println();
		
		while(((InTree<Object>) holder).getChildren() != null ||
				((InTree<Object>) holder).getChildren().size() == 0)
		{
			
			
			/*
			There was a major design flaw with this parser, they can have splits where objects show a proper tree
			In the case of an interpreter it is better to handle on a 0,1,2,3,4 job basis over a 0,1,1,1,2 basis
			However, for demonstration I will handle 0,1,1,1,2 cases
			
			accounting for multiple children 
			*/
			branchMerge.addAll(0,((InTree<Object>) holder).getChildren());
			
			//essentially we are pulling the children into a linear order here,
			//but in split cases these are handled directly as to avoid confusion
			if(branchMerge.size() > 0)
			{
				holder = branchMerge.remove(0);
				try {
					interpretDirector(holder);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
				break;
		}
	}
	
	/*
	 * Sends the object to the it's respective handler
	 * I'm suppressing unchecked here because all objects are checked by the switch case
	 * And the natural setup of the parser 
	*/
	@SuppressWarnings({ "unchecked"})
	private void interpretDirector(Object o) throws Exception, IndexOutOfBoundsException
	{
		//preprocessing for sorting
		String director = ""+o.getClass();
		director = director.substring(director.indexOf('.')+1).trim();
		//System.out.println("My director is: " + director);
		
		//this is the holder for every case's data
		ArrayList<String> paramArgs = new ArrayList<String>();
		
		//good old switch sorting, very boring
		switch(director)
		{
			default:
			{
				break;
			}
			//very standard and straightforward, using HashMap as memory reference
			case("assignStatement"):
			{
				//data setup
				paramArgs = (ArrayList<String>) ((assignStatement)o).returnArgs();
				String isolatedName = paramArgs.get(0).substring(0, paramArgs.get(0).indexOf("->")).trim();
				//System.out.println("The params are: " + paramArgs);
				
				if(paramArgs.size() > 1)
				{
					//this is going to be an arith statement
					//I didn't simplify this because I'm lazy tbh, double casting for object->child of assign
					int tempValue = ((arithmeticStatement) ((assignStatement)o)
							.getChildren().get(0)).evaluateArith(tempMem);
					
					tempMem.put(((assignStatement)o).getId(), tempValue);
					
				}
				else if(paramArgs.get(0).contains("literal_integer"))
				{
					//big call to parse an integer and the id into the map
					tempMem.put(((assignStatement)o).getId(), Integer.parseInt(isolatedName));
				}
				else
				{
					//big call to get the another variable from the Map
					if(tempMem.get(isolatedName) != null)
						tempMem.put(((assignStatement)o).getId(), tempMem.get(isolatedName));
					else
						throw new Exception("Variable unavailable!");
				}
				break;
			}
			case("printStatement"):
			{
				//more preprocessing, a joy. All this stems from a lazy parser. Done to make a point
				paramArgs = (ArrayList<String>) ((printStatement)o).returnArgs();
				String isolatedName = paramArgs.get(0).substring(0, paramArgs.get(0).indexOf("->")).trim();
				
				//System.out.println("The params are: " + paramArgs);
				
				//accounts for a variable
				if(tempMem.get(isolatedName) != null)
					System.out.println(tempMem.get(isolatedName));
				else
				{
					System.out.println(isolatedName);
				}
				break;
			}

			/* structural breakdown cases are from here down
			 * cleanest of the breakdown cases, shows exactly how to handle branches using Case 2
			 * these cases are shown above the whileBlock segment
			 */
			case("ifBlock"):
			{
				ArrayList<Object> ifChildren = ((InTree<Object>) o).getChildren();
				
				//handles node connection like a multiple connection tree, as intended
				//evaluateBoolean needs to have tempMem so that it can check current variables
				if(((booleanStatement) ifChildren.get(0)).evaluateBoolean(tempMem))
				{
					//narrowing the only block to block 1, aka true block
					((ifBlock) o).removeChildren(2);
					((ifBlock) o).removeChildren(0);
				}
				else
				{
					//narrowing the block to block 2, aka false block
					((ifBlock) o).removeChildren(0);
					((ifBlock) o).removeChildren(0);
				}
			
				break;
			}
			
			/*There are a couple of different ways to do this breakdown structure.
				1: Using the direct while as I've shown here
				2: Building off of the initial structure and adding children for BranchMerge to deal with
				3: Directly moving holder (obviously the most annoying one)
				
				The while block and repeat are the same basically, and work exactly as you would expect.
			*/
			case("whileBlock"):
			{
				ArrayList<Object> whileChildren = ((InTree<Object>) o).getChildren();
				//System.out.println("whileC = " + whileChildren);
				//boolean child 0, block child 1.
				if(((booleanStatement) whileChildren.get(0)).evaluateBoolean(tempMem))
				{
					//System.out.println("i am indeed activated!");
					Object replicatedChild = whileChildren.get(1);
					
					while(((InTree<Object>) replicatedChild).getChildren() != null)
					{
						interpretDirector(replicatedChild);
						
						if(((InTree<Object>) replicatedChild).getChildren() == null)
							break;
						else
							replicatedChild = ((InTree<Object>) replicatedChild).getChildren().get(0);
					}
				}
					
				break;
			}
			//exact same thing as the while block with swapped children order
			case("repeatBlock"):
			{
				ArrayList<Object> repeatChildren = ((InTree<Object>) o).getChildren();
				//System.out.println("repC = " + repeatChildren);
				//boolean child 0, block child 1.
				if(((booleanStatement) repeatChildren.get(1)).evaluateBoolean(tempMem))
				{
					System.out.println("i am indeed activated!");
					Object replicatedChild = repeatChildren.get(0);
					
					while(((InTree<Object>) replicatedChild).getChildren() != null)
					{
						interpretDirector(replicatedChild);
						
						if(((InTree<Object>) replicatedChild).getChildren() == null)
							break;
						else
							replicatedChild = ((InTree<Object>) replicatedChild).getChildren().get(0);
					}
				}
				//System.out.println("The params are: " + ((repeatBlock)o).returnArgs());
				break;
			}
		}
	}
}
