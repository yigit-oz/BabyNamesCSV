import edu.duke.*;
import java.io.File;
import org.apache.commons.csv.*;

public class BabyNamesCSV {

	// Print total number of births and names, males and females separately.
	public void totalBirths(FileResource f) {
		int maleBirths = 0, femaleBirths = 0, totalBirths, maleNames = 0, femaleNames = 0, totalNames;
		CSVParser parser = f.getCSVParser(false);
		for(CSVRecord record : parser) {
			if(record.get(1).equals("M")) {
				maleBirths += Integer.parseInt(record.get(2));
				maleNames++;
			}
			else {
				femaleBirths += Integer.parseInt(record.get(2));
				femaleNames++;
			}
		}
		totalBirths = femaleBirths + maleBirths;
		totalNames = maleNames + femaleNames;
		System.out.println("Total Births: " + totalBirths + "\nMale Births: " + maleBirths + "\nFemale Births: " + femaleBirths);
		System.out.println("Total Names Given: " + totalNames + "\nMale Names: " + maleNames + "\nFemale Names: " + femaleNames);
	}
	
	// Test totalBirths method.
	public void testTotalBirths() {
		FileResource f1 = new FileResource("us_babynames/us_babynames_test/example-small.csv");
		totalBirths(f1);
		System.out.println();
		
		FileResource f2 = new FileResource("us_babynames/us_babynames_test/yob2012short.csv");
		totalBirths(f2);
		System.out.println();
		
		FileResource f3 = new FileResource("us_babynames/us_babynames_test/yob2013short.csv");
		totalBirths(f3);
		System.out.println();
		
		FileResource f4 = new FileResource("us_babynames/us_babynames_by_year/yob1905.csv");
		totalBirths(f4);
	}

	// Return total number of female names in a parser.
	public int numberOfFemales(CSVParser parser) {
		int result = 0;
		for(CSVRecord record : parser) {
			if(record.get(1).equals("F")) {
				result++;
			}
			else {
				break;
			}
		}
		return result;
	}
	
	// Test numberOfFemales method.
	public void testNumberOfFemales() {
		FileResource fr = new FileResource("us_babynames/us_babynames_test/yob2012short.csv");
		CSVParser parser = fr.getCSVParser(false);
		int test1 = numberOfFemales(parser);
		System.out.println("Test 1: " + test1);
	}
	
	// Select multiple files and return the rank of the name in the right file for the given gender.
	public int getRank(int year, String name, String gender) {
		DirectoryResource dir = new DirectoryResource();		
		for(File f : dir.selectedFiles()) {
			if(f.getName().contains(String.valueOf(year))) {
				FileResource fr = new FileResource(f);
				CSVParser parser = fr.getCSVParser(false);
				int totalFemales = numberOfFemales(parser);
				parser = fr.getCSVParser(false);
				for(CSVRecord record : parser) {
					String g = record.get(1), n = record.get(0);
					if(gender.equals("F")) {
						if(g.equals("F") && n.equalsIgnoreCase(name)) {
							return (int) parser.getCurrentLineNumber();
						}
					}
					else {
						if(g.equals("M") && n.equalsIgnoreCase(name)) {
							int result = (int) (parser.getCurrentLineNumber() - totalFemales);
							return result;
						}
					}
				}
			}
		}
		return -1;
	}
	
	// Return the rank of the name in the file for the given gender.
	public int getRank(FileResource fr, String name, String gender) {
		CSVParser parser = fr.getCSVParser(false);
		int totalFemales = numberOfFemales(parser);
		parser = fr.getCSVParser(false);
		for(CSVRecord record : parser) {
			String g = record.get(1), n = record.get(0);
			if(gender.equals("F")) {
				if(g.equals("F") && n.equalsIgnoreCase(name)) {
					return (int) parser.getCurrentLineNumber();
				}
			}
			else {
				if(g.equals("M") && n.equalsIgnoreCase(name)) {
					int result = (int) (parser.getCurrentLineNumber() - totalFemales);
					return result;
				}
			}
		}
		return -1;
	}
	
	// Test GetRank method.
	public void testGetRank() {
		int test1 = getRank(2013, "Emma", "F"); 
		System.out.println("Rank of test1: " + test1);
		
		int test2 = getRank(2012, "Ethan", "M"); 
		System.out.println("Rank of test2: " + test2);
		
		int test3 = getRank(1924, "Angelina", "F");
		System.out.println("Rank of test3: " + test3);
		
		int test4 = getRank(1971, "Frank", "M");
		System.out.println("Rank of test4: " + test4);
	}	
	
	// Return the name of the person in the file at given rank, for the given gender.
	public String getName(int year, int rank, String gender) {
		DirectoryResource dir = new DirectoryResource();		
		int position = 1;
		for(File f : dir.selectedFiles()) {
			if(f.getName().contains(String.valueOf(year))) {
				FileResource fr = new FileResource(f);
				CSVParser parser = fr.getCSVParser(false);
				int totalFemales = numberOfFemales(parser);
				parser = fr.getCSVParser(false);
				for(CSVRecord record : parser) {
					String g = record.get(1), n = record.get(0);
					if(gender.equals("F")) {
						if(g.equals("F") && position == rank) {
							return n;
						}
					}
					else {
						if(g.equals("M") && (position - totalFemales) == rank) {
							return n;
						}
					}
					position++;
				}
			}
		}
		return "NO NAME";
	}

	// Test getName method.
	public void testGetName() {
		String test1 = getName(2013, 3, "F"); 
		System.out.println("Rank of test1: " + test1);
		
		String test2 = getName(2012, 3, "M"); 
		System.out.println("Rank of test2: " + test2);
		
		String test3 = getName(1924, 5392, "F");
		System.out.println("Rank of test3: " + test3);
		
		String test4 = getName(1989, 3456, "M");
		System.out.println("Rank of test4: " + test4);
		
		String test5 = getName(1982, 450, "M");
		System.out.println("Rank of test5: " + test5);
	}
	
	// Print what name would have been named if they were born in different year, based on the same popularity.
	public void whatIsNameInYear(String name, int year, int newYear, String gender) {
		int rank1 = getRank(year, name, gender);
		String newName = getName(newYear, rank1, gender),
			result = name + " born in " + year + " would be " + newName + " if she was born in " + newYear;
		System.out.println(result);
	}
	
	// Test whatIsNameInYear method.
	public void testWhatIsNameInYear() {
		whatIsNameInYear("Isabella", 2012, 2014, "F");
		whatIsNameInYear("Ethan", 1995, 1967, "M");
		whatIsNameInYear("Susan", 1972, 2014, "F");
		whatIsNameInYear("Owen", 1974, 2014, "M");
	}
	
	// Select a range of files and return the year with the highest rank for the name and gender.
	public int yearOfHighestRank(String name, String gender) {
		int minRank = 0, current, year = 0;
		DirectoryResource dir = new DirectoryResource();
		for(File f : dir.selectedFiles()) {
			String FileName = f.getName();
			FileResource fr = new FileResource(f);
			current = getRank(fr, name, gender);
			if(minRank == 0 && current != -1) {
				minRank = current;
				year = Integer.parseInt(FileName.substring(3, 7));
			}
			if(current < minRank && current != -1) {
				minRank = current; 
				year = Integer.parseInt(FileName.substring(3, 7));
			}
		}
		return year;
	}
	
	// Test yearOfHighestRank method.
	public void testYearOfHighestRank() {
		int test1 = yearOfHighestRank("Mason", "M"), 
			test2 = yearOfHighestRank("Noah", "M"),
			test3 = yearOfHighestRank("Frank", "M");	
		
		System.out.println("Test 1: " + test1);
		System.out.println("Test 2: " + test2);
		System.out.println(test3);
	}
	
	// Select a range of files and return a double representing the average rank of the name and gender over the selected files.
	public double getAverageRank(String name, String gender) {
		double count = 0, total = 0;
		DirectoryResource dir = new DirectoryResource();
		for(File f : dir.selectedFiles()) {
			FileResource fr = new FileResource(f);
			total += getRank(fr, name, gender);
			count++;
		}
		if(count == 0) {
			return -1;
		}
		return total/count;
	}

	// Test getAverageRank method.
	public void testGetAverageRank() {
		double test1 = getAverageRank("Susan", "F");
		double test2 = getAverageRank("Robert", "M");
		
		System.out.println("Test 1: " + test1);
		System.out.println("Test 2: " + test2);
	}
	
	// Return the total number of births of those names with the same gender and same year who are ranked higher than name parameter.
	public int getTotalBirthsRankedHigher(int year, String name, String gender) {
		DirectoryResource dir = new DirectoryResource();
		int total = 0, rank = 0;
		for(File f : dir.selectedFiles()) {
			FileResource fr = new FileResource(f);
			if(f.getName().contains(String.valueOf(year))) {
				rank = getRank(fr, name, gender);
				CSVParser parser = fr.getCSVParser(false);
				for(CSVRecord record : parser) {
					int currentRank = getRank(fr, record.get(0), record.get(1));
					if(currentRank < rank && gender.equals(record.get(1))) {
						total += Integer.parseInt(record.get(2));
					}
				}
			}
		}
		return total;
	}
	
	// Test testgetTotalBirthsRankedHigher method.
	
	public void testgetTotalBirthsRankedHigher() {
		int test1 = getTotalBirthsRankedHigher(1990, "Emily", "F");
			//test2 = getTotalBirthsRankedHigher(2012, "Isabella", "F");
		
		System.out.println("Test 1: " + test1);
		//System.out.println("Test 2: " + test2);
	}
	
	
	public static void main(String[] args) {
		BabyNamesCSV test = new BabyNamesCSV();
		//test.testTotalBirths();
		//test.testNumberOfFemales();
		//test.testGetRank();
		//test.testGetName();
		//test.testWhatIsNameInYear();
		//test.testYearOfHighestRank();
		//test.testGetAverageRank();
		//test.testgetTotalBirthsRankedHigher();
	}
	
}











