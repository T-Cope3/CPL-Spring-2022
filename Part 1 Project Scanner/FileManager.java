package interpreterProj;

/*
 * Class:       CS 4308 Section 03
 * Term:        _Spring 2022_
 * Name:        <Troy Cope>
 * Instructor:  Sharon Perry
 * Project:     Deliverable P1 Scanner  
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FileManager 
{
	private String fileName = "";
	private File testFile;
	
	public FileManager()
	{
		this("textInput.txt");
	}
	
	public FileManager(String name)
	{
		fileName = name;
		testFile = new File(fileName);
	}
	
	//the only time this fails is if the user doesn't have space or authority to make a file
	public void setUp() throws IOException
	{
		if(testFile.exists())
		{
			testFile.createNewFile();
		}
	}
	
	//self explanatory, splitting by lines
	public ArrayList<String> splitFileByLines() throws FileNotFoundException
	{
		Scanner scan = new Scanner(testFile);
		ArrayList<String> splitLines = new ArrayList<String>();
		while(scan.hasNextLine())	
		{
			splitLines.add(scan.nextLine());
		}
		return splitLines;
	}
	
	//when setup is run this function will always work, therefore exception can be throw
	public ArrayList<String> splitLineByTokens(String token, String line)
	{
		ArrayList<String> splitList = new ArrayList<String>();
		if(!line.isBlank())
		{
			String s = line.trim();
			splitList.addAll(Arrays.asList(s.split(token))) ;
		}
		return splitList;
	}
	
	//straight forward printing into a file
	public void WriteInto(ArrayList<String> data) throws IOException
	{
		FileWriter writer = new FileWriter(testFile);
		
		for(String s: data)
			writer.write(s + "\n");
		
		writer.close();
		
	}
}
