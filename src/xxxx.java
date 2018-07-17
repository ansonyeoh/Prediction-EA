import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

public class xxxx {
	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		Scanner cin = new Scanner(System.in);	
		//exercise1
		String x = cin.nextLine();
		String expr = cin.nextLine();
		int n = cin.nextInt();
		String[] t = x.split(" ");
		double[] X = new double[t.length];
		for (int i = 0; i < t.length; i++) {
			X[i] = Double.parseDouble(t[i]);
		}
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.000000");
		String b = df.format(exercise1(expr,n, X));
		System.out.println(b);
		
		//exercise2
//		String expression = cin.nextLine();
//		int n = cin.nextInt();
//		int m = cin.nextInt();
//		String data = cin.next();
//		
//		exercise2(expression, n, m, data);
		
		
		//exercise3
//		String data = cin.nextLine();
//		int lambda = cin.nextInt();
//		int n = cin.nextInt();
//		int m = cin.nextInt();
//		double time_budget = cin.nextFloat();
//		double chi = cin.nextFloat();
//		for (double j = 0; j <= chi; j += 0.1) {
//			for (int i = 0; i < 100; i++) {
//				exercise3(lambda, n, m, data,time_budget, j);
//			}
//		}
		
		
	}

	public static void exercise3(int lambda, int n, int m, String data, double time_budget, double chi) throws IOException {
		Map<String, double[]> map = Readfile(n, m, data);
		String[] key = new String[m+1];
		for (int i = 0; i < key.length; i++) {
			key[i] = String.valueOf(i);
		}
		Node[] population = initial(lambda, n, m);
		Node[] parents = new Node[2];
		double[] e = new double[m];
		double[] y = new double[m];
		double[] x = new double[m];
		String[] populations = new String[lambda];
		double bestr = 1000000;
		double second = 1000000;
		int b = 0;
		int s = 0;
		String best = "";
		
		long start = System.currentTimeMillis() ;
		while(System.currentTimeMillis() < start + time_budget * 1000) {
			for (int j = 0; j < lambda; j++) {
				populations[j] = rebuild(population[j]);
				for (int i = 0; i < m; i++) {
					x = map.get(key[i]);
					e[i] = exercise1(populations[j], n, x);
					y[i] = x[n];
					if(System.currentTimeMillis() >= start + time_budget * 1000) {
						break;
					}
				}
				//Selection
				double r = fitness(e, y, m);
				if(bestr > r) {
					bestr = r;
					b = j;
				}else {
					if(second > r) {
						second = r;
						s = j;
					}
				}
			}
			parents[0] = population[b];
			parents[1] = population[s];
			
			population = evolution(parents, chi, n, lambda);
		}
		best = populations[b];
		System.out.println(best);
		//System.out.println(bestr);
	}
	
	public static Node[] evolution(Node[] parents, double chi, int n, int lambda) {
		Node[] select = new Node[lambda];
		Random random = new Random();
		for (int i = 0; i < lambda; i++) {
			// Mutation
			if (random.nextDouble() < chi) {
				Node left1 = parents[0].getLeft();
				Node left2 = parents[1].getLeft();
				Node right1 = parents[0].getRight();
				Node right2 = parents[1].getRight();
				//int depth = random.nextInt();
				parents[0].setLeft(right1);
				parents[0].setRight(left1);
				parents[1].setLeft(right2);
				parents[1].setRight(left2);
			}
			//Crossover
			Node node = parents[0].getRight();
			parents[1].setRight(node);
			select[i] = parents[1];
		}
		return select;	
	}
	
	public static Node[] initial(int lambda, int n, int m) {
		Node[] population = new Node[lambda];
		Random random = new Random();
		for (int i = 0; i < lambda; i++) {
			int depth =random.nextInt(10);
			Node tree= tree(depth);// build tree
			population[i]=tree;// rebuild population
		}		   
		return population;
	}
	
	public static String rebuild(Node root) {
	    List<List<Node>> levels = leveldis(root);
	    Stack<String> st = new Stack<String>();
	    String string = "";
	    
	    for (int i = levels.size()-1; i>=0; i--) {
	        for (int j = 0; j < levels.get(i).size(); j++) {
	           String element = levels.get(i).get(j).getElement();
	           
	           if (element.equals("add") || element.equals("sub") || element.equals("mul") || element.equals("div") || element.equals("pow") || element.equals("max") || element.equals("diff") || element.equals("avg")) {
					String eString = levels.get(i).get(j).getLeft().getElement();
					String eString2 = levels.get(i).get(j).getRight().getElement();
					levels.get(i).get(j).setElement("(" + element +" "+ eString + " " + eString2 +")");
	           }else if (element.equals("sqrt") || element.equals("log") || element.equals("exp") || element.equals("data")) {
	        	    String eString = levels.get(i).get(j).getLeft().getElement();
	        	    levels.get(i).get(j).setElement("(" + element +" "+ eString +")");
	           }else if (element.equals("ifleq")){
					String eString = levels.get(i).get(j).getLeft().getElement();
					String eString2 = levels.get(i).get(j).getRight().getElement();
					String eString3 = levels.get(i).get(j).getThird().getElement();
					String eString4 = levels.get(i).get(j).getForth().getElement();
					levels.get(i).get(j).setElement("(" + element +" " + eString+ " " + eString2+ " " + eString3 +" "+ eString4 +")");
	           }
	        }
	        if (i == 0) {
				string = levels.get(0).get(0).getElement();
			}
	    }
//	    System.out.println(string);
	    return string;
	}
	
	private static List<List<Node>> leveldis(Node root) {
	    if (root == null) {
	        return Collections.emptyList();
	    }
	    List<List<Node>> levels = new LinkedList<>();

	    Queue<Node> nodes = new LinkedList<>();
	    nodes.add(root);

	    while (!nodes.isEmpty()) {
	        List<Node> level = new ArrayList<>(nodes.size());
	        levels.add(level);

	        for (Node node : new ArrayList<>(nodes)) {
	            level.add(node);
	            if (node.left != null) {
	                nodes.add(node.left);
	            }
	            if (node.right != null) {
	                nodes.add(node.right);
	            }
	            if (node.third != null) {
	                nodes.add(node.third);
	            }
	            if (node.forth != null) {
	                nodes.add(node.forth);
	            }
	            nodes.poll();
	        }
	    }
	    return levels;
	}
	
	public static Node tree(int depth){
		Random random = new Random();
		String[] operator = {"add", "sub", "mul", "div", "pow", "sqrt", "log", "exp", "max", "ifleq", "data", "diff", "avg"};
		
	    if (depth > 1 && random.nextDouble()<0.5) {
            String s = operator[random.nextInt(operator.length)];
            if (s.equals("add") || s.equals("sub") || s.equals("mul") || s.equals("div") || s.equals("pow") || s.equals("max") || s.equals("diff") || s.equals("avg")){
            	return new Node(s, tree(depth-1), tree(depth-1));
			}else if (s.equals("sqrt") || s.equals("log") || s.equals("exp") || s.equals("data")) {
				return new Node(s, tree(depth-1));
			}else if (s.equals("ifleq")){
				return new Node(s, tree(depth-1), tree(depth-1), tree(depth-1), tree(depth-1));
			} else {
				return null;
			}  
        } else {
        	int i = random.nextInt(2);
        	if (i==0) {
				return new Node(String.valueOf(random.nextInt(10)*random.nextDouble()));
			}else {
				return new Node(String.valueOf(-1*random.nextInt(10)*random.nextDouble()));
			}
            
        }
	}
	
	public static class Node {
	    String element;
	    Node left;
	    Node right;
	    Node third;
	    Node forth;
	    
		public Node(String element) {
	        this.element = element;
	    }
	    public Node(String element, Node left) {
	        this.element = element;
	        this.left = left;
	    }
	    public Node(String element, Node left, Node right) {
	        this.element = element;
	        this.left = left;
	        this.right = right;
	    }	    
	    public Node(String element, Node left, Node right, Node third, Node forth) {
	        this.element = element;
	        this.left = left;
	        this.right = right;
	        this.third = third;
	        this.forth = forth;
	    }
		public String getElement() {
			return element;
		}
		public void setElement(String element) {
			this.element = element;
		}
		public Node getLeft() {
			return left;
		}
		public void setLeft(Node left) {
			this.left = left;
		}
		public Node getRight() {
			return right;
		}
		public void setRight(Node right) {
			this.right = right;
		}
		public Node getThird() {
			return third;
		}
		public void setThird(Node third) {
			this.third = third;
		}
		public Node getForth() {
			return forth;
		}
		public void setForth(Node forth) {
			this.forth = forth;
		}
	}
	
	public static void exercise2(String expression, int n, int m, String data) throws IOException {
		Map<String, double[]> map = Readfile(n, m, data);
		String[] key = new String[m+1];
		for (int i = 0; i < key.length; i++) {
			key[i] = String.valueOf(i);
		}
		double[] e = new double[m];
		double[] y = new double[m];
		for (int i = 0; i < m; i++) {
			double[] eString = map.get(key[i]);
			e[i] = exercise1(expression, n, eString);
			//System.out.println("e: " + e[i]);
			y[i] = eString[n];
			//System.out.println("y: " + y[i]);
		}
		double output = fitness(e, y, m);
		if (Double.isNaN(output) || Double.isInfinite(output)) {
			output = 0;
		}
		
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.000000");
		String b = df.format(output);
		System.out.println(b);
	}
	
	public static Map<String, double[]> Readfile(int n, int m, String data) throws IOException {
		File dir= new File(".");
		File fin = new File(dir.getCanonicalPath() + File.separator + data);
		String line = null;  
		String[] key = new String[m+1];
		Map<String, double[]> map = new LinkedHashMap<String, double[]>();
		int count = 0;
		
		// read lines in the file
		BufferedReader br = new BufferedReader(new FileReader(fin));   
		while ((line = br.readLine()) != null) {  
		   key[count] = String.valueOf(count);
		   String[] edata = line.split("	");
		   double[] ddata = new double[n+1];
		   for (int i = 0; i < edata.length; i++) {
			   if (!edata[i].isEmpty()) {
				   ddata[i] = Double.parseDouble(edata[i]);
			   }  
		   }
		   map.put(key[count], ddata);// put on the map
		   count++;
		}  
		br.close();	
		return map;
	}
	
	public static double exercise1(String expression, int n, double[] x) {
		expression = expression.replace("(", "");
		expression = expression.replace(")", "");
		//System.out.println(expression);
		String[] element = expression.split(" ");
		
		Stack<Double> st = new Stack<Double>();
		double output = 0;
		//System.out.println(eStrings2[0]);
		
		for (int i = element.length-1; i >= 0; i--) {
			//System.out.println(element[i]);
			if (isDouble(element[i])) {
				output = Double.parseDouble(element[i]);
			}else {
				if (element[i].equals("add")) {
					double e1 = st.pop();
					double e2 = st.pop();
					output = add(e1, e2);
				}else if (element[i].equals("sub")) {
					double e1 = st.pop();
					double e2 = st.pop();
					output = sub(e1, e2);
				}else if (element[i].equals("mul")) {
					double e1 = st.pop();
					double e2 = st.pop();
					output = mul(e1, e2);
				}else if (element[i].equals("div")) {
					double e1 = st.pop();
					double e2 = st.pop();
					output = div(e1, e2);
				}else if (element[i].equals("pow")) {	
					double e1 = st.pop();
					double e2 = st.pop();
					output = pow(e1, e2);
				}else if (element[i].equals("max")) {
					double e1 = st.pop();
					double e2 = st.pop();
					output = max(e1, e2);
				}else if (element[i].equals("diff")) {
					double e1 = st.pop();
					double e2 = st.pop();
					output = diff(x, e1, e2, n);
				}else if (element[i].equals("avg")) {
					double e1 = st.pop();
					double e2 = st.pop();
					output = avg(x, e1, e2, n);
				}else if (element[i].equals("sqrt")) {
					double e1 = st.pop();
					output = sqrt(e1);
				}else if (element[i].equals("log")) {
					double e1 = st.pop();
					output = log(e1);
				}else if (element[i].equals("exp")) {
					double e1 = st.pop();
					output = exp(e1);
				}else if (element[i].equals("data")) {
					double e1 = st.pop();
					output = data(x, e1, n);
				}else if (element[i].equals("ifleq")) {
					double e1 = st.pop();
					double e2 = st.pop();
					double e3 = st.pop();
					double e4 = st.pop();
					output = ifleq(e1, e2, e3, e4);
				}
			}
			st.push(output);
		}
		//System.out.println(output);
		if (Double.isNaN(output) || Double.isInfinite(output)) {
			output = 0;
		}
		
		return output;
	}

	private static boolean isDouble(String str) {  
	    if (null == str || "".equals(str)) {  
	        return false;  
	    }  
	    Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");  
	    return pattern.matcher(str).matches();  
	} 

	public static double fitness(double[] e, double[] y, int m) {
		double fitness = 0;
		double coe = (double) 1 / m;
		//System.out.println("coe: " + coe);
		for (int i = 0; i < m; i++) {
			fitness = fitness + Math.pow(y[i] - e[i], 2);
		}
		//System.out.println("fitness: " + fitness);
		fitness = coe * fitness;
		return fitness;
	}
	
	public static double add(double e1, double e2) {
		double output =0;
		output = e1 + e2;
		return output;
	}
	
	public static double sub(double e1, double e2) {
		double output = 0;
		output = e1 - e2;
		return output;
	}
	
	public static double mul(double e1, double e2) {
		double output = 0;
		output = e1*e2;
		return output;
	}
	
	public static double div(double e1, double e2) {
		double output = 0;
		if (e2 != 0) {
			output = e1/e2;
		}
		return output;
	}
	
	public static double pow(double e1, double e2) {
		double output = 0;
		if (e2 == 0) {
			return 1;
		}
		if (e1 == 0) {
			return 0;
		}
		output = Math.pow(e1, e2);
		if (Double.isNaN(output)) {
			return 0;
		}
		return output;

	}
	
	public static double sqrt(double e1) {
		double output = 0;
		if (e1 >= 0) {
			output = Math.sqrt(e1);
		}else {
			output = 0;
		}
		return output;
	}
	
	public static double log(double e1) {
		double output = 0;
		if (e1 > 0) {
			output = Math.log((double) e1)/Math.log(2);
		}else {
			output = 0;
		}
		return output;
	}
	
	public static double exp(double e1) {
		double output = 0;
		output = Math.exp(e1);
		if (Double.isNaN(output)) {
			return 0;
		}
		return output;
	}
	
	public static double max(double e1, double e2) {
		double output = 0;
		output = Math.max(e1, e2);
		return output;
	}
	
	public static double ifleq(double e1, double e2, double e3, double e4) {
		double output = 0;
		if (e1 <= e2) {
			output = e3;
		} else {
			output = e4;
		}
		return output;
	}
	
	public static double data(double[] x, double e1, int n) {
		int j = 0;
		e1 = Math.abs(Math.floor(e1));
		j = (int) (e1 % n);
		return x[j];
	}
	
	public static double diff(double[] x, double e1, double e2, int n) {
		double output = 0;
		int k = 0, l = 0;
		e1 = Math.abs(Math.floor(e1));
		e2 = Math.abs(Math.floor(e2));
		k = (int) (e1 % n);
		l = (int) (e2 % n);
//		System.out.println(k + " "+ l);
		output = x[k] - x[l];
		return output;
	}
	
	public static double avg(double[] x, double e1, double e2, int n) {
		double output = 0, coe = 0;
		int k = 0, l = 0, p = 0, q = 0;
		e1 = Math.abs(Math.floor(e1));
		e2 = Math.abs(Math.floor(e2));
		k = (int) (e1 % n);
		l = (int) (e2 % n);
		if (k == l) {
			return 0;
		}else {
			p = Math.min(k, l);
			q = Math.max(k, l);
			for (int t = p; t < q; t++) {
				output = output + x[t];
			}
			coe =  1 / (double) Math.abs(k-l);
			//System.out.println(k + " "+ l + " " + coe);
			output = coe * output;
			return output;
		}
		
	}

}