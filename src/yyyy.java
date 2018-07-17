import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

public class yyyy {
	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		Scanner cin = new Scanner(System.in);	
		//exercise1
//		String x = cin.nextLine();
//		String expr = cin.nextLine();
//		int n = cin.nextInt();
//		String[] t = x.split(" ");
//		float[] X = new float[t.length];
//		for (int i = 0; i < t.length; i++) {
//			X[i] = float.parsefloat(t[i]);
//		}
//		System.out.println(exercise1(expr, n, X));
		
		//exercise2
//		String expression = cin.nextLine();
//		int n = cin.nextInt();
//		int m = cin.nextInt();
//		String data = cin.next();
//		
//		exercise2(expression, n, m, data);
		
		
		//exercise3
		String data = cin.nextLine();
		int lambda = cin.nextInt();
		int n = cin.nextInt();
		int m = cin.nextInt();
		
	
		exercise3(lambda, n, m, data);
	}

	public static void exercise3(int lambda, int n, int m, String data) throws IOException {
		//float sigma = 0;
		//String expresstion = "(mul (div 1 (sqrt (mul 2 (pow sigma 2)))) (exp (mul -1 (mul (div 1 (mul 2 (pow sigma 2))) (pow (sub y mu) 2)))))";
		Map<String, float[]> map = Readfile(n, m, data);
		String[] key = new String[m+1];
		for (int i = 0; i < key.length; i++) {
			key[i] = String.valueOf(i);
		}
		String[] population = initial(lambda, n, m, map);
		float[] e = new float[m];
		float[] y = new float[m];
		float[] x = new float[m];
		for (int i = 0; i < m; i++) {
			x = map.get(key[i]);
			e[i] = exercise1(population[0], n, x);
			y[i] = x[n];
		}
//		
		System.out.println(fitness(e, y, m));
	}
	
	public static String[] initial(int lambda, int n, int m, Map<String, float[]> map) {
		String[] population = new String[lambda];
		int it = 3;
		int o = n+1;
		
		float[][] a = new float[it][n];
		float[][] b = new float[n][it];
		float[][] c = new float[it][it];
		float[][] d = new float[it][n];
		float[] e = new float[it];
		float[] x = new float[m];
		float[][] w = new float[m][it];
		String[] key = new String[m+1];
		
		for (int i = 0; i < key.length; i++) {
			key[i] = String.valueOf(i);
		}
		
		for (int i = 0; i < it; i++) {
			for (int j = 0; j < n; j++) {
				a[i][j] = (float) Math.pow(j+1, i+1);
				b[j][i] = (float) Math.pow(j+1, i+1);
				//System.out.println("a"+i+j+" "+ a[i][j]);
				//System.out.println("b"+j+i+" " + b[j][i]);
			}
		}
		
		for (int i = 0; i < it; i++) {
			for (int j = 0; j < it; j++) {
				c[i][j] = 0;
				for (int j2 = 0; j2 < n; j2++) {
					c[i][j] = c[i][j] + a[i][j2] * b[j2][j]; 
				}	
				//System.out.println("c"+i+j+" "+ c[i][j]);
			}
		}
		
		c = getReverseMartrix(c);
		
		for (int i = 0; i < it; i++) {
			for (int j = 0; j < n; j++) {
				d[i][j] = 0;
				for (int j2 = 0; j2 < it; j2++) {
					d[i][j] = d[i][j] + c[i][j2] * a[j2][j]; 
				}	
				//System.out.println("d"+i+j+" "+ d[i][j]);
			}
		}
		
		for (int i = 0; i < m; i++) {
			x = map.get(key[i]);
			for (int j = 0; j < it; j++) {
				w[i][j] = 0;
				for (int j2 = 0; j2 < n; j2++) {
					w[i][j] = w[i][j] + d[j][j2] * x[j2]; 
				}	
				//System.out.println("w"+i+j+" "+ w[i][j]);
			}
		}
		
		for (int i = 0; i < lambda; i++) {
			String[] X = new String[it+1];
			for (int j = 0; j < it; j++) {
				X[j] = "(mul "+w[0][j]+" "+ "(pow "+ o + " "+ j +"))";
			}
			population[i] = X[it];
			for (int j = it-1; j >=0; j--) {
				population[i] = "(add " + population[i] + " " + X[j] + ")"; 
			}
			//System.out.println(population[i]);
		}
		
		return population;
	}
	
	public static float[][] getReverseMartrix(float[][] c) {
        float[][] newdata = new float[c.length][c[0].length];
        float A = getMartrixResult(c);

        for(int i=0; i<c.length; i++) {
            for(int j=0; j<c[0].length; j++) {
                if((i+j)%2 == 0) {
                    newdata[i][j] = getMartrixResult(getConfactor(c, i+1, j+1)) / A;
                }else {
                    newdata[i][j] = -getMartrixResult(getConfactor(c, i+1, j+1)) / A;
                }
            }
        }
        newdata = trans(newdata);
        for(int i=0;i<newdata.length; i++) {
            for(int j=0; j<newdata[0].length; j++) {
                System.out.print(newdata[i][j]+ "   ");
            }
            System.out.println();
        }
        return newdata;
    }

	private static float[][] trans(float[][] newdata) {
        // TODO Auto-generated method stub
        float[][] newdata2 = new float[newdata[0].length][newdata.length];
        for(int i=0; i<newdata.length; i++) 
            for(int j=0; j<newdata[0].length; j++) {
                newdata2[j][i] = newdata[i][j];
            }
        return newdata2;
    }

	public static float getMartrixResult(float[][] data) {
    
        if(data.length == 2) {
            return data[0][0]*data[1][1] - data[0][1]*data[1][0];
        }
     
        float result = 0;
        int num = data.length;
        float[] nums = new float[num];
        for(int i=0; i<data.length; i++) {
            if(i%2 == 0) {
                nums[i] = data[0][i] * getMartrixResult(getConfactor(data, 1, i+1));
            }else {
                nums[i] = -data[0][i] * getMartrixResult(getConfactor(data, 1, i+1));
            }
        }
        for(int i=0; i<data.length; i++) {
            result += nums[i];
        }
        return result;
    }
	
	public static float[][] getConfactor(float[][] data, int h, int v) {
        int H = data.length;
        int V = data[0].length;
        float[][] newdata = new float[H-1][V-1];
        for(int i=0; i<newdata.length; i++) {
            if(i < h-1) {
                for(int j=0; j<newdata[i].length; j++) {
                    if(j < v-1) {
                        newdata[i][j] = data[i][j];
                    }else {
                        newdata[i][j] = data[i][j+1];
                    }
                }
            }else {
                for(int j=0; j<newdata[i].length; j++) {
                    if(j < v-1) {
                        newdata[i][j] = data[i+1][j];
                    }else {
                        newdata[i][j] = data[i+1][j+1];
                    }
                }
            }
        }
        return newdata;
    }
	
	public static void exercise2(String expression, int n, int m, String data) throws IOException {
		Map<String, float[]> map = Readfile(n, m, data);
		String[] key = new String[m+1];
		for (int i = 0; i < key.length; i++) {
			key[i] = String.valueOf(i);
		}
		float[] e = new float[m];
		float[] y = new float[m];
		float X= 0;
		for (int i = 0; i < m; i++) {
			float[] eString = map.get(key[i]);
			e[i] = exercise1(expression, n, eString);
			//System.out.println("e: " + e[i]);
			y[i] = eString[n];
			//System.out.println("y: " + y[i]);
		}
		float output = fitness(e, y, m);
		System.out.println(output);
	}
	
	public static Map<String, float[]> Readfile(int n, int m, String data) throws IOException {
		File dir= new File(".");
		File fin = new File(dir.getCanonicalPath() + File.separator + data);
		String line = null;  
		String[] key = new String[m+1];
		Map<String, float[]> map = new LinkedHashMap<String, float[]>();
		int count = 0;
		
		// read lines in the file
		BufferedReader br = new BufferedReader(new FileReader(fin));   
		while ((line = br.readLine()) != null) {  
		   key[count] = String.valueOf(count);
		   String[] edata = line.split("	");
		   float[] ddata = new float[n+1];
		   for (int i = 0; i < edata.length; i++) {
			   if (!edata[i].isEmpty()) {
				   ddata[i] = Float.parseFloat(edata[i]);
			   }  
		   }
		   map.put(key[count], ddata);// put on the map
		   count++;
		}  
		br.close();	
		return map;
	}
	
	public static float exercise1(String expression, int n, float[] x) {
		expression=expression.replace("(", "");
		expression = expression.replace(")", "");
		//System.out.println(expression);
		String[] element = expression.split(" ");
		
		Stack<Float> st = new Stack<Float>();
		float output = 0;
		//System.out.println(eStrings2[0]);
		
		for (int i = element.length-1; i >= 0; i--) {
			//System.out.println(element[i]);
			if (isfloat(element[i])) {
				st.push(Float.parseFloat(element[i]));
			}else {
				if (element[i].equals("add")) {
					float e1 = st.pop();
					float e2 = st.pop();
					output = add(e1, e2);
				}else if (element[i].equals("sub")) {
					float e1 = st.pop();
					float e2 = st.pop();
					output = sub(e1, e2);
				}else if (element[i].equals("mul")) {
					float e1 = st.pop();
					float e2 = st.pop();
					output = mul(e1, e2);
				}else if (element[i].equals("div")) {
					float e1 = st.pop();
					float e2 = st.pop();
					output = div(e1, e2);
				}else if (element[i].equals("pow")) {	
					float e1 = st.pop();
					float e2 = st.pop();
					output = pow(e1, e2);
				}else if (element[i].equals("max")) {
					float e1 = st.pop();
					float e2 = st.pop();
					output = max(e1, e2);
				}else if (element[i].equals("diff")) {
					float e1 = st.pop();
					float e2 = st.pop();
					output = diff(x, e1, e2, n);
				}else if (element[i].equals("avg")) {
					float e1 = st.pop();
					float e2 = st.pop();
					output = avg(x, e1, e2, n);
				}else if (element[i].equals("sqrt")) {
					float e1 = st.pop();
					output = sqrt(e1);
				}else if (element[i].equals("log")) {
					float e1 = st.pop();
					output = log(e1);
				}else if (element[i].equals("exp")) {
					float e1 = st.pop();
					output = exp(e1);
				}else if (element[i].equals("data")) {
					float e1 = st.pop();
					output = data(x, e1, n);
				}else if (element[i].equals("ifleq")) {
					float e1 = st.pop();
					float e2 = st.pop();
					float e3 = st.pop();
					float e4 = st.pop();
					output = ifleq(e1, e2, e3, e4);
				}
				st.push(output);
			}
		}
		//System.out.println(output);
		return output;
	}

	private static boolean isfloat(String str) {  
	    if (null == str || "".equals(str)) {  
	        return false;  
	    }  
	    Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");  
	    return pattern.matcher(str).matches();  
	} 

	public static float fitness(float[] e, float[] y, int m) {
		float fitness = 0;
		float coe = (float) 1 / m;
		//System.out.println("coe: " + coe);
		for (int i = 0; i < m; i++) {
			fitness = (float) (fitness + Math.pow(y[i] - e[i], 2));
		}
		//System.out.println("fitness: " + fitness);
		fitness = coe * fitness;
		return fitness;
	}
	
	public static float add(float e1, float e2) {
		float output =0;
		output = e1 + e2;
		return output;
	}
	
	public static float sub(float e1, float e2) {
		float output = 0;
		output = e1 - e2;
		return output;
	}
	
	public static float mul(float e1, float e2) {
		float output = 0;
		output = e1*e2;
		return output;
	}
	
	public static float div(float e1, float e2) {
		float output = 0;
		if (e2 != 0) {
			output = e1/e2;
		}
		return output;
	}
	
	public static float pow(float e1, float e2) {
		float output = 0;
		output = (float) Math.pow(e1, e2);
		if (Float.isNaN(output)) {
			return 0;
		}
		return output;

	}
	
	public static float sqrt(float e1) {
		float output = 0;
		if (e1 >= 0) {
			output = (float) Math.sqrt(e1);
		}else {
			output = 0;
		}
		return output;
	}
	
	public static float log(float e1) {
		float output = 0;
		if (e1 > 0) {
			output = (float) (Math.log((float) e1)/Math.log(2));
		}else {
			output = 0;
		}
		return output;
	}
	
	public static float exp(float e1) {
		float output = 0;
		output = (float) Math.exp(e1);
		if (Float.isNaN(output)) {
			return 0;
		}
		return output;
	}
	
	public static float max(float e1, float e2) {
		float output = 0;
		output = Math.max(e1, e2);
		return output;
	}
	
	public static float ifleq(float e1, float e2, float e3, float e4) {
		float output = 0;
		if (e1 < e2) {
			output = e3;
		} else {
			output = e4;
		}
		return output;
	}
	
	public static float data(float[] x, float e1, int n) {
		int j = 0;
		e1 = (float) Math.floor(Math.abs(e1));
		j = (int) (e1 % n);
		return x[j];
	}
	
	public static float diff(float[] x, float e1, float e2, int n) {
		float output = 0;
		int k = 0, l = 0;
		e1 = (float) Math.floor(Math.abs(e1));
		e2 = (float) Math.floor(Math.abs(e2));
		k = (int) (e1 % n);
		l = (int) (e2 % n);
//		System.out.println(k + " "+ l);
		output = x[k] - x[l];
		return output;
	}
	
	public static float avg(float[] x, float e1, float e2, int n) {
		float output = 0, coe = 0;
		int k = 0, l = 0, p = 0, q = 0;
		e1 = (float) Math.floor(Math.abs(e1));
		e2 = (float) Math.floor(Math.abs(e2));
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
			coe =  1 / (float) Math.abs(k-l);
			//System.out.println(k + " "+ l + " " + coe);
			output = coe * output;
			return output;
		}
		
	}

}