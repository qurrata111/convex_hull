import java.util.Random;
import java.lang.Math; 
import java.io.*; 
import java.util.Scanner; 

public class Titik {
	int x;
	int y;

	public Titik(int x, int y){
    	this.x=x;
    	this.y=y;
	}

	public static int generate_N () {
		/* I.S. : prosedur meminta input n						*/
		/* F.S. : mengembalikan nilai n 						*/
		Scanner input = new Scanner(System.in);
		System.out.print("Masukkan banyak titik: ");
		int n = input.nextInt();
		return n;
	}

	public static Titik[] generate_Random_Point (int n){
		/* I.S. : n adalah panjang array yang akan dibuat		*/
		/* F.S. : mengembalikan array bertipe Ttik dengan 		*/
		/* 		  panjang n, setiap elemen dirandom				*/
		Random rand = new Random(); 
		/* input banyak titik secara acak*/
		/* Membuat list titik dengan generate nilai integer sembarang */
	    Titik[] list_titik = new Titik[n];
	    for (int i=0; i<n; i++){
	    	list_titik[i] = new Titik(rand.nextInt(100), rand.nextInt(100));
	    }
		return list_titik;
	}

	public static void write_List_Point (Titik[] l) {
		/* I.S. : l adalah array of titik						*/
		/* F.S. : menuliskan elemen array ke layar				*/
		for (int i=0; i<l.length; i++) {
			if (i == (l.length-1)) {
				System.out.println("(" + l[i].x + "," + l[i].y + ")");
			} else {
				System.out.print("(" + l[i].x + "," + l[i].y + ") ");
			}
		}
	}

	public static int dot_Product (Titik a, Titik b, Titik p) {
		/* I.S. : a, b, dan p adalah titik yang akan membentuk	*/
		/* 		  vektor										*/
		/* F.S. : mengembalikan nilai 1 atau -1 atau 0	*/
		/* KAMUS */
		int res;
		/* ALGORITMA */
		res = ((b.x-a.x)*(p.y-a.y)-(b.y-a.y)*(p.x-a.x));
		if (res > 0) {
			// titik p.y berada di atas garis
			return 1;
		} else if (res < 0) {
			// titik
			return -1;
		} else {
			return 0;
		}
	}

	public static boolean is_Same (int a, int b) {
		/* I.S. : a, b adalah integer							*/
		/* F.S. : mengembalikan true bila a sama dengan b		*/
		return (a == b);
	} 

	public static double len_Vector (Titik a, Titik b) {
		/* I.S. : a, b adalah titik yang akan membentuk	*/
		/* 		  vektor										*/
		/* F.S. : mengembalikan nilai cross product pa dan pb	*/
		/* KAMUS */
		double vx; 
		/* ALGORITMA */
		vx = a.x-b.x; double vy = a.y-b.y;
		return (Math.sqrt(Math.pow(vx,2.0)+Math.pow(vy,2.0)));
	}

	public static Titik[] find_Convex (Titik[] l) {
		/* I.S. : l adalah array of titik yang berisi titik		*/
		/*		  random
		/* F.S. : mengembalikan nilai array of titik yang		*/
		/*		  membentuk poligon, dengan membandingkan setiap*/
		/*		  titik dengan titik lain, bila titik tersebut 	*/
		/* 		  berada di satu sisi maka titik tersebut convex*/
		/* KAMUS */
		int countConvex, idxConvex, cross_prod, count;
		/* ALGORITMA */
		Titik[] l2 = new Titik[l.length];
		countConvex = 0; idxConvex = 0;
		cross_prod = -9999;
		for (int i=0; i<l.length; i++) {
			boolean isOneSide;
			for (int j=0; j<l.length; j++){
				if (j != i) {
					count = 0;
					isOneSide = true;
					for (int k=0; k<l.length; k++) {
						if (k != i && k != j) {
							count++;
							if (count >= 2) {
								if (!is_Same(cross_prod, dot_Product(l[i], l[j], l[k]))) {
									isOneSide = false;
									break;
								}
							}
							cross_prod = dot_Product(l[i], l[j], l[k]);
						}
					}
					if (isOneSide == true) {
						countConvex++;
						if (countConvex%2==0) {
							// tambahkan
							idxConvex++;
							l2[idxConvex-1] = l[i];
						}
					}
				}
			}
		}
		Titik[] l3 = new Titik[idxConvex];
		for (int i=0; i<idxConvex; i++) {
			l3[i] = l2[i];
		}
		return l3;
	}

	public static Titik[] sort_Convex_Hull (Titik[] l) {
		/* I.S. : l adalah array of point membentuk poligon yg belum terurut */
		/* F.S. : mengembalikan array of point yang telah disortir dengan metode */
		/*		  selection sort */
		/* KAMUS */
		int idxMin;
		Titik pTemp;
		/* ALGORITMA*/
		for (int i=0; i<l.length-1; i++) {
			idxMin = i+1;
			for (int j=i+1; j<l.length; j++){
				if (len_Vector(l[i], l[j]) < (len_Vector(l[i], l[idxMin]))) {
					idxMin = j;
				}
			}
			// swap
			pTemp = l[idxMin];
			l[idxMin] = l[i+1];
			l[i+1] = pTemp;
		}
		return l;
	}

	public static void run_Random_Points () {
		/* I.S. : prosedur merandom titik										 */
		/* F.S. : mengembalikan array of point yang telah disortir dengan metode */
		/*		  selection sort */
		/* KAMUS */
		int n;
		float sec;
		long start, end;
		Titik[] l,lconvex,lconvex_sort;
		/* ALGORITMA */
		//long start = System.nanoTime();
		
		n = generate_N();
		l = generate_Random_Point(n);
		System.out.println(n + " titik uji dibangkitkan RANDOM");
		write_List_Point(l);
		if (n == 0 || n == 1 | n == 2) {
			System.out.println(l.length + " titik yg membentuk poligon");
			write_List_Point(l);
			lconvex_sort = sort_Convex_Hull(l);
			System.out.println(l.length + " titik yg membentuk poligon TERURUT");
			write_List_Point(l);
		} else {
			start = System.nanoTime();
			lconvex = find_Convex(l);
			System.out.println(lconvex.length + " titik yg membentuk poligon");
			write_List_Point(lconvex);
			lconvex_sort = sort_Convex_Hull(lconvex);
			end = System.nanoTime();
			System.out.println(lconvex_sort.length + " titik yg membentuk poligon TERURUT");
			write_List_Point(lconvex_sort);
			System.out.print("waktu yang dibutuhkan: ");
			sec = (end - start); System.out.printf("%f nanodetik",sec);

		}
	}

	public static void test_case () {
		float sec;
		long start, end;
		run_Random_Points();
		System.out.println();
		Titik[] l8 = new Titik[20];
		l8[0] = new Titik(10,21);
		l8[1] = new Titik(3,12);
		l8[2] = new Titik(15,15);
		l8[3] = new Titik(12,12);
		l8[4] = new Titik(5,18);
		l8[5] = new Titik(21,22);
		l8[6] = new Titik(17,7);
		l8[7] = new Titik(10,9);
		l8[8] = new Titik(10, 2);
		l8[9] = new Titik(16,2);
		l8[10] = new Titik(6,9);
		l8[11] = new Titik(14,5);
		l8[12] = new Titik(4,2);
		l8[13] = new Titik(18,4);
		l8[14] = new Titik(15,10);
		l8[15] = new Titik(5,1);
		l8[16] = new Titik(11,15);
		l8[17] = new Titik(7,15);
		l8[18] = new Titik(3,5);
		l8[19] = new Titik(11,1);
		System.out.println(l8.length + " titik input uji");
		write_List_Point(l8);
		start = System.nanoTime();
		Titik[] l8out = find_Convex(l8);
		System.out.println(l8out.length + " titik yg membentuk poligon");
		write_List_Point(l8out);
		Titik[] l8final = sort_Convex_Hull(l8out);
		end = System.nanoTime();
		System.out.println(l8final.length + " titik yg membentuk poligon TERURUT");
		write_List_Point(l8final);
		System.out.print("waktu yang dibutuhkan: ");
		sec = (end - start); System.out.printf("%f nanodetik",sec);
		System.out.println();
		Titik[] l9 = new Titik[15];
		l9[0] = new Titik(14,5);
		l9[1] = new Titik(12,12);
		l9[2] = new Titik(9,6);
		l9[3] = new Titik(17,7);
		l9[4] = new Titik(8,6);
		l9[5] = new Titik(3,5);
		l9[6] = new Titik(15,5);
		l9[7] = new Titik(16,2);
		l9[8] = new Titik(11,2);
		l9[9] = new Titik(4,2);
		l9[10] = new Titik(12,9);
		l9[11] = new Titik(10,5);
		l9[12] = new Titik(6,9);
		l9[13] = new Titik(5,1);
		l9[14] = new Titik(11,1);
		System.out.println(l9.length + " titik input uji");
		write_List_Point(l9);
		start = System.nanoTime();
		Titik[] l9out = find_Convex(l9);
		System.out.println(l9out.length + "titik yg membentuk poligon");
		write_List_Point(l9out);
		Titik[] l9final = sort_Convex_Hull(l9out);
		end = System.nanoTime();
		System.out.println(l9final.length + "titik yg membentuk poligon TERURUT");
		write_List_Point(l9final);
		System.out.print("waktu yang dibutuhkan: ");
		sec = (end - start); System.out.printf("%f nanodetik",sec);
		System.out.println();
		Titik[] l5 = new Titik[5];
		l5[0] = new Titik(10,4);
		l5[1] = new Titik(12,3);
		l5[2] = new Titik(9,2);
		l5[3] = new Titik(12,7);
		l5[4] = new Titik(9,5);
		System.out.println(l5.length + " titik input uji");
		write_List_Point((l5));
		start = System.nanoTime();
		Titik[] l5out = find_Convex(l5);
		System.out.println(l5out.length + " titik yg membentuk poligon");
		write_List_Point(l5out);
		Titik[] l5final = sort_Convex_Hull (l5out);
		end = System.nanoTime();
		System.out.println(l5final.length + " titik yg membentuk poligon TERURUT");
		write_List_Point(l5final);
		System.out.print("waktu yang dibutuhkan: ");
		sec = (end - start); System.out.printf("%f nanodetik",sec);
	}
	public static void main(String[] args){
		test_case();
	}
}