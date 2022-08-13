/** COM212 Final Project - MySocialProfile
	By: Syed Haider Naqvi. Japmeet Bedi, Kara Walp, Sarah Padilla
	This program reflects a social media application where the
	user can create their own profile, post events and messages, 
	add friends, and more
**/	


import java.io.*;
import java.util.Scanner;	//imported for user input
import java.util.Calendar;	//imported for time/date for events
import java.io.FileWriter;	//imported to write to file
import java.util.Arrays;


/**
	Constructor for MySocialProfile class
	You are able to create a new social profile
	using fullName, email, password, and classYear
	which will be used in the main method

	@param String,String,String,String

*/
public class MySocialProfile{

	private static String name;
	private static String pass;
	private static String login;
	private static String year;
	private static ArrayHeap eventHeap = new ArrayHeap(20); //ArrayHeap object created for events
	private static String eventString;
	private static String friendString;
	private static String postString;

	public MySocialProfile(String fullName, String email, String password, String classYear){
		//Parameters for creating an account
		name = fullName;
		pass = password;
		login = email;
		year = classYear;

	}



	/** 
		The changeUser method is used to create, obtain, and store a newly created user inside 
		the mysocialprofile.txt file using the user inputted information

		@param String,String,String,String
		@return void
	*/
	public void changeUser(String fullName, String email, String password, String classYear){

		name = fullName;
		pass = password;
		login = email;
		year = classYear;	//initialization and declaration for storing parameters
		eventString = "";
		friendString = "";
		postString = "";

		try{

			FileWriter fw = new FileWriter("mysocialprofile.txt");	//fileWriter object that opens file for input

			//writes to the file with the given information
			fw.write(name + "\n" + login + "\n" + password + "\n" + year + "\n" + eventString + "\n" + postString + "\n" + friendString);
			fw.close();	//closes the file

		} catch(IOException e) {	//if mysocialprofile.txt is not present in the same folder
			System.out.println("File not Found");
			System.exit(0);
		}
	}

	/** 
		The fillHeap method initially checks for events and if there are, it splits them up and
		places them in the user's profile following the template of day,month,year,hour, and minute
		Afterwards, it inserts the event into the eventHeap to keep track of it.

		@param none
		@return boolean
	*/

	public boolean fillHeap(){


		String[] userData = getProfileData();	//obtains all user data

		String stringOfEvents = userData[4]; //Index 4 contains the events in the file

		if (stringOfEvents.equals("")){	//if empty
			return false;
		}

		else{

			String[] arrayOfEvents = stringOfEvents.split(",");	//Splits events with comma

			//for loop for splitting all events in the array to match template given
			for(int i = 0; i < arrayOfEvents.length; i++){	

				String[] arrayOfElements = arrayOfEvents[i].split(" ");

				int day = Integer.parseInt(arrayOfElements[0]);
				int month = Integer.parseInt(arrayOfElements[1]);
				int year = Integer.parseInt(arrayOfElements[2]);
				int hour = Integer.parseInt(arrayOfElements[3]);
				int min = Integer.parseInt(arrayOfElements[4]);

				//range for description
				String[] descriptionArray = Arrays.copyOfRange(arrayOfElements, 5, arrayOfElements.length);

				String description = String.join(" ", descriptionArray); //joins numerical data with description

				//creates new Event object and inserts into heap to keep track of it
				Event newEvent = new Event(day, month, year, hour, min, description);

				eventHeap.insert(newEvent);

			}
		}
		return true;	//return true if everything goes through

	}


	/**
		The getProfileData method obtains all of the data that the user had inputted into
		the file to store it into an array of Strings for manipulating as things are added or deleted.
		mysocialprofile.txt must be present in the folder for method to run correctly

		@param none
		@return String[]
	*/


	public static String[] getProfileData(){

		try{

			Scanner profileData = new Scanner(new FileInputStream("mysocialprofile.txt"));
			//obtains data from textFile and stores them in variables
			name = profileData.nextLine();
			login = profileData.nextLine();
			pass = profileData.nextLine();
			year = profileData.nextLine();
			String stringOfEvents = "";
			String stringOfPosts = "";
			String stringOfFriends = "";

			if (profileData.hasNextLine()){
				stringOfEvents = profileData.nextLine();
				//Checks for input on the line
			}

			if (profileData.hasNextLine()){
				stringOfPosts = profileData.nextLine();
				//Checks for input on the line
			}

			if (profileData.hasNextLine()){
				stringOfFriends = profileData.nextLine();
				//Checks for input on the line

			}

			//Create array of Strings to hold all data
			String[] allProfileData = new String[7];

			allProfileData[0] = name;
			allProfileData[1] = login;
			allProfileData[2] = pass;
			allProfileData[3] = year;
			allProfileData[4] = stringOfEvents;
			allProfileData[5] = stringOfPosts;
			allProfileData[6] = stringOfFriends;

			return allProfileData;	//returns in an array of Stirng format

		} catch(FileNotFoundException ex) {		//if file is not found
			System.out.println("File not Found");
			System.exit(0);
			return null;
		}

	}


	/**
		The addPost method is used when the user wants to add a post onto their 
		profile from within the program.

		@param String
		@return void
	*/


	public void addPost(String textOfPost){

		try{
			//Obtains profile
			String[] userDataArray = getProfileData();

			//Puts the text into a String variable
			String stringOfPosts = userDataArray[5] + textOfPost + ",";

			FileWriter fw = new FileWriter("mysocialprofile.txt");	//opens file for input

			//writing the post to the file
			fw.write(userDataArray[0] + "\n" + userDataArray[1] + "\n" + userDataArray[2] + "\n" + userDataArray[3] + "\n" + userDataArray[4] + "\n" + stringOfPosts + "\n" + userDataArray[6]);
			fw.close();	//closing file

			postString = stringOfPosts;

		} catch(IOException e) {	//if file is not in folder
			System.out.println("File not Found");
			System.exit(0);
		}
	}


	/**
		The addRemoveFriend method is used to add friends or remove friends at the user's
		command. This method removes and adds friends by checking whether the friend email
		is already in the file and if it is, it removes it and if it is not, it adds the 
		friend

		@param String
		@return boolean
	*/

	public boolean addRemoveFriend(String email){

		try{

			boolean addedOrRemoved = false;
			String[] userDataArray = getProfileData();	//obtain user data
			String stringOfFriends = userDataArray[6];	//Index where friends are stored
			email = email + ",";
			 
			if(stringOfFriends.contains(email)){	//If contains email then remove friend

				stringOfFriends = stringOfFriends.replace(email, "");
				friendString = stringOfFriends;

			}

			else{	//if does not contain email, add the friend

				stringOfFriends = stringOfFriends = stringOfFriends + email;
				addedOrRemoved = true;	//changes to true if added
				friendString = stringOfFriends;
			}


			FileWriter fw = new FileWriter("mysocialprofile.txt");	//Opens file for input

			//writes to file
			fw.write(userDataArray[0] + "\n" + userDataArray[1] + "\n" + userDataArray[2] + "\n" + userDataArray[3] + "\n" + userDataArray[4] + "\n" + userDataArray[5] + "\n" + stringOfFriends);
			fw.close();

			if(addedOrRemoved == true){	//boolean return to show which process was done

				return true;
			}

			else{

				return false;
			}

		} catch (IOException e) {	//if an error occurred
			e.printStackTrace();
			return false;
		}
	}



	/**
		The getFriends method allows the user to view their friends list within the
		program

		@param none
		@return String
	*/
	public String getFriends(){

		String[] userFriends = getProfileData();	//obtains user data
		String friends_ = userFriends[6];	//index where user friends are stored
		return friends_;	//returns string
	}



	/**
		The addEvent method adds an event to the user's profile using the user inputted
		data they provide

		@param int,int,int,int,int,String
		@return none
	*/

	public void addEvent(int day, int month, int year, int hour, int min, String description){

		try{

			String[] userDataArray = getProfileData();	//obtains user data

			String stringOfEvents = userDataArray[4];	//index where event is stored

			Event event = new Event(day, month, year, hour, min, description);	//creates new Event object using user input

			eventHeap.insert(event);	//inserts event into array heap

			stringOfEvents = stringOfEvents + event.toString() + ",";	//separates event with a comma

			eventString = stringOfEvents;

			FileWriter fw = new FileWriter("mysocialprofile.txt"); //opens file for input

			//writes to file
			fw.write(userDataArray[0] + "\n" + userDataArray[1] + "\n" + userDataArray[2] + "\n" + userDataArray[3] + "\n" + stringOfEvents + "\n" + userDataArray[5] + "\n" + userDataArray[6]);
			fw.close();

		} catch (IOException e){

		}

	}

	/** 
		The removeEvent method removes the event from the file and user data after it 
		has passed. This operation occurs many times to ensure old events are deleted.

		@param none
		@return void
	*/
	public void removeEvent(){

		try{
			//heap is sorted by time so getMin gets the date closest to the present and checks
			//if the event has passed
			while(MySocialProfile.eventPassed(eventHeap.getMin())){	

				//System.out.println(eventHeap.size());

				Event event = eventHeap.extractMin();	//extracts the event if it has passed

				String[] userDataArray = getProfileData();	//obtains user data

				String stringOfEvents = userDataArray[4];	//index where events is stored

				System.out.println(event.toString());	//prints deleted event

				stringOfEvents = stringOfEvents.replace(event.toString() + ",", "");	//replaces event with empty string

				eventString = stringOfEvents;

				FileWriter fw = new FileWriter("mysocialprofile.txt");	//Opens and writes to the file

				fw.write(userDataArray[0] + "\n" + userDataArray[1] + "\n" + userDataArray[2] + "\n" + userDataArray[3] + "\n" + stringOfEvents + "\n" + userDataArray[5] + "\n" + userDataArray[6]);
				fw.close();
			

			}
		} catch (IOException e){

		}
	}


	

	
	public static boolean eventPassed(Event event){

		Calendar now = Calendar.getInstance();

		if(now.getTimeInMillis() > event.getDateTime().getTimeInMillis()){

			return true;
		}

		else{

			return false;
		}

	}

	public ArrayHeap getHeap(){

		return eventHeap;
	}

	public String[] recentPosts(){
		String[] userDataArray = getProfileData();
		String timeline = userDataArray[5];
		String[] postArr = timeline.split(",");
		if(postArr.length < 4){
			return postArr;
		}
		else{
			String[] threePostArr = Arrays.copyOfRange(postArr, postArr.length-3, postArr.length);
			return threePostArr;

		}
	}

}

class Event{

	private Calendar eventDateTime = Calendar.getInstance();
	private String eventDescription;
	private int eventday;
	private int eventmonth;
	private int eventyear;
	private int eventhour;
	private int eventmin;

	public Event(int day, int month, int year, int hour, int min, String description){

		eventDateTime.set(year, month, day, hour, min);
		eventDescription = description;
		eventday = day;
		eventmonth = month;
		eventyear = year;
		eventhour = hour;
		eventmin = min;
	}	

	public Calendar getDateTime(){

		return eventDateTime;
	}

	public String getDescription(){

		return eventDescription;
	}

	public String toString(){

		return (Integer.toString(eventday) + " " + Integer.toString(eventmonth) + " " + Integer.toString(eventyear) + " " + Integer.toString(eventhour) + " " + Integer.toString(eventmin) + " " + eventDescription);
	}
}

class main{

	public static int options(MySocialProfile user, Scanner s){

		ArrayHeap eventHeap = user.getHeap();

		System.out.println("Welcome to your homepage.");
		System.out.println("");
		if(eventHeap.size() == 0){
			System.out.println("There is no most recent event since you have no events.");
		}
		else{
			System.out.println("Upcoming Event: " + eventHeap.getMin().toString());
		}
		System.out.println("");
		System.out.println("Posts:");
		String[] tlPost = user.recentPosts();
		for(int i = 0; i < tlPost.length; i++){
			System.out.println("-" + tlPost[i]);
		}
		System.out.println("");
		if(eventHeap.size() == 0){
			System.out.println("There is no list of events as you have not added any!");
		}
		else{
			for(int i = 1; i < eventHeap.size() + 1; i++){
				System.out.println("Event: " + eventHeap.getObject(i));
			}
		}
		System.out.println("");
		System.out.println("Please choose an option");
		System.out.println("1) Post on my timeline");
		System.out.println("2) Add an event I would like to attend");
		System.out.println("3) View my friends");
		System.out.println("4) Add/remove a friend");
		System.out.println("5) Log out");
		int optInput = s.nextInt();
		System.out.println("You chose option " + optInput);
		Scanner info = new Scanner(System.in);


		if(optInput == 1){
			System.out.println("");
			System.out.println("What would you like to post?");
			String strInput = info.nextLine();
			user.addPost(strInput);
			System.out.println("");
		}

		if(optInput == 2){
			System.out.println("");
			System.out.println("What is the numerical day of the event? (Ex. 01, 11, 22)");
			int day = s.nextInt();
			// do {
			// 	System.out.println("What is the numerical day of the event? (Ex. 01, 11, 22)");
			// 	while (!s.hasNextInt()){
			// 		System.out.println("Please enter a valid input");
			// 		s.next();
			// 	}
			// 	day = s.nextInt();
			// } while (day < 31 && day > 0);
			// System.out.println("Thank you");
			System.out.println("");
			System.out.println("What is the numerical month of the event? (Ex. 01, 05, 11)");
			int month = s.nextInt();
			System.out.println("");
			System.out.println("What is the year the event will be held? (Ex. 2021, 2024, 2030)");
			int year = s.nextInt();
			System.out.println("");
			System.out.println("What hour will the event occur? Please use military time. (Ex. 08, 12, 15)");
			int hr = s.nextInt();
			System.out.println("");
			System.out.println("What minute will the event occur? (Ex. 15, 30, 45)");
			int minute_ = s.nextInt();
			System.out.println("");
			System.out.println("Please provide a brief label for the event.");
			String desc = info.nextLine();
			System.out.println("");
			user.addEvent(day, month, year, hr, minute_, desc);
			System.out.println("Your event has been created!");
			System.out.println("");
		}

		if(optInput == 3){
			System.out.println("");
			System.out.println("Friend's List: ");
			System.out.println(user.getFriends());
			System.out.println("");
		}

		if(optInput == 4){
			System.out.println("");
			System.out.println("What is your friend's email?");
			String friendEmail = info.nextLine();
			user.addRemoveFriend(friendEmail);
			System.out.println("Done!");
			System.out.println("");

		}
		if(optInput == 5){
			System.out.println("");
			System.out.println("Goodbye");
			System.exit(0);
		}
	
		return optInput;
	}


	public static void main(String[] args){
		System.out.println("");

		System.out.println("Hello, Welcome to the Social Profile.");

		System.out.println("Please choose an option by indicating a number.");

		System.out.println("");

		System.out.println("1) Create a new account/profile.");

		System.out.println("2) Load an existing profile (if one exists in the file).");

		System.out.println("3) Exit the entire program.");

		Scanner input = new Scanner(System.in);
		int userInput = input.nextInt();

		System.out.println("The option you chose is option " + userInput);



 		if (userInput == 1){


 			Scanner input2 = new Scanner(System.in);

 			System.out.println("Please input your full name");

 			String fullName_ = input2.nextLine();

 			System.out.println("Please input your email");

 			String email_ = input2.nextLine();

 			System.out.println("Please input your password");

 			String password_ = input2.nextLine();

 			System.out.println("Please input your class year");

 			String classYear_ = input2.nextLine();

 			MySocialProfile profile = new MySocialProfile("a", "b", "c", "d");

 			profile.changeUser(fullName_, email_, password_, classYear_);

 			//try{
 				//FileWriter fw2 = new FileWriter("mysocialprofile.txt");
 				//w2.write(fullName_);
 				//fw2.write(System.getProperty("line.separator"));
 			// 	fw2.write(email_);
 			// 	fw2.write(System.getProperty("line.separator"));
 			// 	fw2.write(password_);
 			// 	fw2.write(System.getProperty("line.separator"));
 			// 	fw2.write(classYear_);
 			// 	fw2.write(System.getProperty("line.separator"));
 			// 	fw2.close();
 			// }
 			// catch (IOException e) {
 			// 	System.out.println("An error occurred.");
 			// }

			
			System.out.println("Your account has been created!");
			System.out.println("");
			System.out.println("You have no information to show on your homepage!");
			int homepage = options(profile, input2);
			while(homepage != 5){
				options(profile, input2);
			}
			System.exit(0);

 		}

 		else if (userInput == 2){

 			String[] array = MySocialProfile.getProfileData();

 			String fullName_ = array[0];

 			String email_ = array[1];

 			String password_ = array[2];

 			String classYear_ = array[3];

 			MySocialProfile profile2 = new MySocialProfile(array[0], array[1], array[2], array[3]);

 			boolean tf = profile2.fillHeap();

 			if(tf == true){
 				profile2.removeEvent();
 			}

 			System.out.println("");
 			System.out.println("Welcome, " + fullName_);

 			int homepage2 = options(profile2, input);
 			while(homepage2 != 5){
 				options(profile2, input);
 			}
 			System.exit(0);
 		}

 		else if (userInput == 3){

 			System.out.println("Goodbye");
 			System.exit(0);
 		}

 		//System.out.println("Welcome to your profile, " + fullName_ + "!");

 		//if (userInput == 1){

			
 		//}

 		//else {
		
 		//}

 		//System.out.println("");

 	}
 }



