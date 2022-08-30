package interpretProj;

/*
 * Class:       CS 4308 Section 03
 * Term:        _Spring 2022_
 * Name:        <Troy Cope>
 * Instructor:  Sharon Perry
 * Project:     Deliverable P3 Parser  
 */

import java.util.ArrayList;


public abstract class InTree<T>
{
	private ArrayList<Object> children;
	
	@SuppressWarnings("unchecked")
	public ArrayList<T> getChildren()
	{
		return (ArrayList<T>) children;
	}
}
