
public class Scratch extends Thread{
  public static void main(String[] args) {
    System.out.println("Hello");
    Thread tidA = new Scratch();
    tidA.start();
    Thread tidB= new Scratch();
    tidB.start();
    
  }
  
  
 


  @Override
  public void run() {
    int val;
    System.out.println("hi");
    for (int i = 0; i<1000; i++) {
      System.out.println("Thread id " +Thread.currentThread().getName() + " and i is :" +i);
    }
    
  }
}
