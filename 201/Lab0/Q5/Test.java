import java.util.Scanner;

public class Test {
  public static void main(String[] args) {
    DoublyLinkedList list = new DoublyLinkedList<>();
    int option = 1;
    Node<E> song = null;
    while (option != 0) {
      option = printMenu();
      if (option == 1) {
        loadMusic(list);
        song = list.first();
      }
      if (option == 2) playSong(song);
    }
  }

  private static void loadMusic(DoublyLinkedList list) {
    char c = 'A';
    int num_tracks = 5;
    System.out.printf("\r\n%d Tracks Loaded :\r\n", num_tracks);
    for (int i = 0; i < num_tracks; i++) {
      String song = "Song " + c;
      System.out.println(song);
      list.addLast(song);
      c++;
    }
  }
  private static void playSong(Node<E> song) {
    if (song == null) {
      System.out.println("Songs not Loaded");
      return;
    }
    System.out.println(song.getElement());
  }

  private static int printMenu() {
    System.out.println("\r\n== Music Player ==");
    System.out.println("1. Load Music");
    System.out.println("2. Play Song");
    System.out.println("3. Next Song");
    System.out.println("4. Previous Song");
    System.out.println("0. Quit Application");
    Scanner sc = new Scanner(System.in);
    System.out.printf("Please enter your choice: ");
    return sc.nextInt();
  }
}