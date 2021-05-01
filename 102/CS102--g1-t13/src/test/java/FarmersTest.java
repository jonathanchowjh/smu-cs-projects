import org.junit.jupiter.api.*;
import java.io.File;
import java.util.*;
import entities.farmers.*;
import controller.*;

public class FarmersTest {
  private CityFarmersCtrl ctrl;
  public FarmersTest() {
    this.ctrl = new CityFarmersCtrl();
  }
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
  public void getSeeds() {
    System.out.println("--- Test 1: Controller Get Seeds ---");
    ArrayList<Seed> seeds = ctrl.getSeeds("jon");
    String equals = "[{username=jon, crop={name=Papaya, cost=20, time=30, xp=8, minYield=75, maxYield=100, salePrice=15}, quantity=3}, {username=jon, crop={name=Sunflower, cost=40, time=120, xp=20, minYield=15, maxYield=20, salePrice=40}, quantity=2}]";
    Assertions.assertEquals(equals, seeds.toString());
  }
  @Test
  public void getLands() {
    System.out.println("--- Test 2: Controller Get Lands ---");
    ArrayList<Land> lands = ctrl.getLands("jon");
    String equals = "[{lid=1, username=jon, crop={name=Papaya, cost=20, time=30, xp=8, minYield=75, maxYield=100, salePrice=15}, startTime=2020-01-01 13:01:52.0}, {lid=2, username=jon, crop={name=Pumpkin, cost=30, time=60, xp=5, minYield=5, maxYield=200, salePrice=20}, startTime=2020-01-01 13:01:52.0}, {lid=3, username=jon, crop={name=Papaya, cost=20, time=30, xp=8, minYield=75, maxYield=100, salePrice=15}, startTime=2020-01-01 13:01:52.0}, {lid=4, username=jon, crop={name=Sunflower, cost=40, time=120, xp=20, minYield=15, maxYield=20, salePrice=40}, startTime=2020-01-01 13:01:52.0}, {lid=5, username=jon, crop={name=Watermelon, cost=50, time=240, xp=1, minYield=5, maxYield=800, salePrice=10}, startTime=2020-01-01 13:01:52.0}]";
    Assertions.assertEquals(equals, lands.toString());
  }
}