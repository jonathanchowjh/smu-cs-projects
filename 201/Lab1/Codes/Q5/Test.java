import java.util.Scanner;

// This application uses a circular queue (FIFO)
// loadMusic will fill the queue, up to a maximum of 26 tracks.
// Playing the NEXT song will dequeue a track, allowing space for other tracks.
// Using loadMusic again, will fill the queue again to the maximum of 26.

public class Test {
  public static void main(String[] args) {
    CircularQueue<String> circularQueue = new CircularQueue<>();
    
    int option = 1;
    while (option != 0) {
      option = printMenu();
      if (option == 1) loadMusic(circularQueue);
      if (option == 2) playSong(circularQueue, false);
      if (option == 3) playSong(circularQueue, true);
    }
  }

  private static void playSong(CircularQueue<String> cq, boolean next) {
    if (next) cq.dequeue();
    if (cq.front() == null) {
      System.out.println("\r\n ~~~ Music Queue Empty ~~~ ");
      return;
    }
    System.out.println("\r\n ~~~ Playing " + cq.front() + " ~~~ ");
  }

  private static void loadMusic(CircularQueue<String> cq) {
    if (cq.size() >= cq.MAX_SIZE()) {
      System.out.println("\r\n=== 26 tracks | Music Queue Full ===\r\n");
      return;
    }
    String current = "";
    
    char ch = 'A';
    String last_ch = cq.rear();
    if (last_ch != null) ch = last_ch.charAt(last_ch.length() - 1);
    
    while (current != null) {
      System.out.println(current);
      current = cq.enqueue("Song " + ch++);
    }
    System.out.println("\r\n=== 26 tracks | Music Queue Full ===\r\n");
  }

  private static int printMenu() {
    System.out.println("\r\n== Music Player ==");
    System.out.println("1. Load Music");
    System.out.println("2. Play Song");
    System.out.println("3. Next Song");
    System.out.println("0. Quit Application");
    Scanner sc = new Scanner(System.in);
    System.out.printf("Please enter your choice: ");
    return sc.nextInt();
  }
}
