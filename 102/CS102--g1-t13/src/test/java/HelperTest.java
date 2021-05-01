
import org.junit.jupiter.api.*;
import java.io.File;
import java.util.*;
import java.sql.Timestamp;
import entities.farmers.*;
import helpers.*;

public class HelperTest {

	@BeforeAll
	public static void overallSetup() {
		System.out.println();
		System.out.println("[--------------------]");
		System.out.println("Overall Setup");
	}
	// @BeforeEach
	// public void setup() {
	// 	System.out.println("###########");
	// 	System.out.println("Starting...");
	// }

	// @AfterEach
	// public void teardown() {
	// 	System.out.println("Success");
	// 	System.out.println("###########");
	// }

	@AfterAll
	public static void overallTeardown() {
		System.out.println();
		System.out.println("Overall Teardown");
		System.out.println("[--------------------]");
	}


	@Test
	public void csvCrop() {
		System.out.println("--- Test 1: Crop Types ---");
		// Test CropTypes
		ArrayList<Crop> crops = Data.cropTypes();
		String equals = "[{name=Papaya, cost=20, time=30, xp=8, minYield=75, maxYield=100, salePrice=15}, {name=Pumpkin, cost=30, time=60, xp=5, minYield=5, maxYield=200, salePrice=20}, {name=Sunflower, cost=40, time=120, xp=20, minYield=15, maxYield=20, salePrice=40}, {name=Watermelon, cost=50, time=240, xp=1, minYield=5, maxYield=800, salePrice=10}]";
		Assertions.assertEquals(equals, crops.toString());
	}

	@Test
	public void csvRank() {
		System.out.println("--- Test 2: Rank Types ---");
		// Test RankTypes
		ArrayList<Rank> rank = Data.rankTypes();
		String equals = "[{name=Novice, xp=0, plot=5}, {name=Apprentice, xp=1000, plot=6}, {name=Journeyman, xp=2500, plot=7}, {name=Grandmaster, xp=5000, plot=8}, {name=Legendary, xp=12000, plot=9}]";
		Assertions.assertEquals(equals, rank.toString());
	}

	@Test
	public void jsonParser() {
		System.out.println("--- Test 3: Json Parser ---");
		String json = "username:jon,name:jon chow,password:1111,gold:50,xp:0//username:jon,name:jon chow,password:1111,gold:50,xp:0//";
		ArrayList<Map<String, String>> arr = M.jsonAofObj(json);
		String equals = "[{gold=50, password=1111, name=jon chow, xp=0, username=jon}, {gold=50, password=1111, name=jon chow, xp=0, username=jon}]";
		Assertions.assertEquals(equals, arr.toString());
	}
	@Test
	public void timeStamp() {
		System.out.println("--- Test 4: TimeStamp Parser ---");
		String s = "2020-02-02 16-10-10.0";
		Timestamp t = M.toTimestamp(s);
		String equals = "";
		Assertions.assertEquals("2020-02-02 16:10:10.0", t.toString());
	}
}