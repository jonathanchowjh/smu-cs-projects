
import org.junit.jupiter.api.*;
import java.io.File;
import java.util.*;
import entities.farmers.*;
import dao.*;

// RERUN SQL DUMMY DATA BEFORE RUNNING THIS TEST
public class DaoTest {
  private UserDAO udao;
  private FarmerDAO fdao;
  private PostDAO pdao;
  public DaoTest() {
    this.udao = new UserDAO();
    this.fdao = new FarmerDAO();
    this.pdao = new PostDAO();
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
  public void getUser(){
    try {
      System.out.println("--- Test 1: Get User ---");
      Map<String, String> user = this.udao.getUser("jon");
      String equals = "{gold=50, password=1111, name=jonathan chow, xp=0, username=jon}";
      Assertions.assertEquals(equals, user.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  @Test
  public void getFriends(){
    try {
      System.out.println("--- Test 2: Get Friends ---");
      ArrayList<Map<String, String>> friends = this.udao.getFriends("jon");
      String equals = "[{gold=50, password=2222, name=adam ang, xp=0, username=adam}, {gold=50, password=1111, name=jie mi chong, xp=0, username=jiemi}, {gold=50, password=1111, name=lindy lim, xp=0, username=lindy}]";
      Assertions.assertEquals(equals, friends.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  @Test
  public void getFriendReqS(){
    try {
      System.out.println("--- Test 3: Get Friends Request 1 ---");
      ArrayList<Map<String, String>> frs = this.udao.getFriendsRequestS("jon");
      String equals = "[{gold=50, password=3333, name=pius lee, xp=0, username=pius}]";
      Assertions.assertEquals(equals, frs.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  @Test
  public void getFriendReqR(){
    try {
      System.out.println("--- Test 4: Get Friend Request 2 ---");
      ArrayList<Map<String, String>> frr = this.udao.getFriendsRequestR("jon");
      String equals = "[{gold=50, password=2222, name=john low, xp=0, username=john}]";
      Assertions.assertEquals(equals, frr.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  @Test
  public void getLands(){
    try {
      System.out.println("--- Test 5: Get Lands ---");
      ArrayList<Map<String, String>> lands = fdao.getLands("jon");
      String equals = "[{lid=1, startTime=2020-01-01 13-01-52, crop=Papaya, username=jon}, {lid=2, startTime=2020-01-01 13-01-52, crop=Pumpkin, username=jon}, {lid=3, startTime=2020-01-01 13-01-52, crop=Papaya, username=jon}, {lid=4, startTime=2020-01-01 13-01-52, crop=Sunflower, username=jon}, {lid=5, startTime=2020-01-01 13-01-52, crop=Watermelon, username=jon}]";
      Assertions.assertEquals(equals, lands.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  @Test
  public void getSeeds(){
    try {
      System.out.println("--- Test 6 Get Seeds ---");
      ArrayList<Map<String, String>> seeds = fdao.getSeeds("jon");
      String equals = "[{quantity=3, crop=Papaya, username=jon}, {quantity=2, crop=Sunflower, username=jon}]";
      Assertions.assertEquals(equals, seeds.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  @Test
  public void getPostUser(){
    try {
      System.out.println("--- Test 7 Get Post User ---");
      ArrayList<Map<String, String>> ps = pdao.getPostUser("jon");
      String equals = "[{dt=2020-01-01 13-01-52, post=hello I'm jon!, dst_username=jiemi, pid=1, username=jon}, {dt=2020-01-01 13-01-52, post=i love coding, dst_username=jiemi, pid=2, username=jon}]";
      Assertions.assertEquals(equals, ps.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  @Test
  public void getPostDest(){
    try {
      System.out.println("--- Test 8 Get Post Dest ---");
      ArrayList<Map<String, String>> pd = pdao.getPostDest("jiemi");
      String equals = "[{dt=2020-01-01 13-01-52, post=hello I'm jon!, dst_username=jiemi, pid=1, username=jon}, {dt=2020-01-01 13-01-52, post=i love coding, dst_username=jiemi, pid=2, username=jon}, {dt=2020-01-01 13-01-52, post=c is cool, dst_username=jiemi, pid=3, username=pius}, {dt=2020-01-01 13-01-52, post=Let's meet up for lunch!, dst_username=jiemi, pid=4, username=adam}, {dt=2020-01-01 13-01-52, post=hahahha, dst_username=jiemi, pid=5, username=john}, {dt=2020-01-01 13-01-52, post=hello it's me, dst_username=jiemi, pid=6, username=adam}]";
      Assertions.assertEquals(equals, pd.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  @Test
  public void getLikes(){
    try {
      System.out.println("--- Test 9 Get Post Likes ---");
      ArrayList<Map<String, String>> likes = pdao.getLikes(2);
      String equals = "[{isLike=true, pid=2, username=adam}]";
      Assertions.assertEquals(equals, likes.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  @Test
  public void getReplies(){
    try {
      System.out.println("--- Test 10 Get Post Replies ---");
      ArrayList<Map<String, String>> replies = pdao.getReplies(2);
      String equals = "[{dt=2020-01-01 13-01-52, pid=2, rid=3, reply=me too, username=pius}]";
      Assertions.assertEquals(equals, replies.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  @Test
  public void getGifts(){
    try {
      System.out.println("--- Test 11 Get Gifts ---");
      ArrayList<Map<String, String>> gifts = pdao.getGifts("jon");
      String equals = "[{dt=2020-01-01 13-01-52, gid=2, gifter=lindy, accepted=false, giftee=jon, crop=Pumpkin}, {dt=2020-01-01 13-01-52, gid=3, gifter=adam, accepted=false, giftee=jon, crop=Pumpkin}, {dt=2020-01-01 13-01-52, gid=4, gifter=pius, accepted=false, giftee=jon, crop=Pumpkin}, {dt=2020-01-01 13-01-52, gid=5, gifter=luke, accepted=false, giftee=jon, crop=Pumpkin}, {dt=2020-01-01 13-01-52, gid=6, gifter=lindy, accepted=true, giftee=jon, crop=Pumpkin}]";
      Assertions.assertEquals(equals, gifts.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  @Test
  public void getGiftsGifter(){
    try {
      System.out.println("--- Test 12 Get Gifts Gifter ---");
      ArrayList<Map<String, String>> gg = pdao.getGiftsGifter("jon");
      String equals = "[{dt=2020-01-01 13-01-52, gid=1, gifter=jon, accepted=false, giftee=jiemi, crop=Papaya}]";
      Assertions.assertEquals(equals, gg.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}