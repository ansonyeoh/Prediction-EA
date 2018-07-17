import java.util.Random;
import java.util.Scanner;

public class Generate {
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner cin = new Scanner(System.in);	
		int n = cin.nextInt();
		int m = cin.nextInt();
		
		for (int j = 0; j < m; j++) {
			String data = "";
			for (int i = 0; i <= n; i++) {
				Random random = new Random();
				double num = random.nextDouble()*40;
				data = data + String.valueOf(num) + "	";
			}
			System.out.println(data);
		}
//		for (int i = 0; i < 10; i++) {
//			Random random = new Random();
//			int x = random.nextInt(2) + 3;
//			System.out.println(x);
//		}
//		int j = (int) (-3.4 % 2);
//		System.out.println(j);
	}
}
