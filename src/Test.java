
public class Test {

	public static void main(String[] args) {
		int totalTicks = 38480;
		
		
		double randProb = Math.random();
		// pick a random index, bias towards smaller bursers
	    randProb = Math.pow(randProb, 0.05);
	    int result =  (int) (1000 + (totalTicks - (totalTicks * randProb)));
	    System.out.println(result);
	}

}
