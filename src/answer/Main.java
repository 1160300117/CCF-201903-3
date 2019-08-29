package answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main (String args[]) {
		Scanner scan = new Scanner(System.in);
		String[] firstLine = scan.nextLine().split(" ");
		int n = Integer.parseInt(firstLine[0]);
		int s = Integer.parseInt(firstLine[1]);
		int l = Integer.parseInt(firstLine[2]);
		String[] Dsk = new String[n];
		int blocks = 0;	// 每个硬盘含有的块数
		
		// 存储每块硬盘的序号和内容
		String line = "";
		for (int i = 0; i < l; i++) {
			line = scan.nextLine();
			Dsk[Integer.parseInt(line.split(" ")[0])] = line.split(" ")[1];
			blocks = line.split(" ")[1].length() / 8;
		}
		for (int i = 0; i < n; i++) {
			if (Dsk[i] == null) {
				Dsk[i] = "FF";
			}
		}
		
		// 接收查询块ID，确定每个查询块所在硬盘
		int bands = blocks / s;	// 每个硬盘含有的条带数
		int m = Integer.parseInt(scan.nextLine());
		int[] read = new int[m];	// 查询块序号
		int[] DSK = new int[m];	// 查询块所在硬盘序号
		for (int i = 0; i < m; i++) {
			read[i] = Integer.parseInt(scan.nextLine());
			for (int j = 0; j < n; j++) {
				if (s*j <= read[i] % (s*n) && read[i] % (s*n) < s*(j+1)) {
					DSK[i] = j;
				}
			}
		}
		scan.close();
		
		// 构造RAID块ID矩阵
		List<List<int[]>> RAIDs = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			List<int[]> RAID = new ArrayList<>();
			for (int j = i*(n-1); j < blocks*(n-1); j += s*n) {
				int[] band = new int[s];
				for (int k = 0; k < s; k++) {
					band[k] = j + k;
				}
				RAID.add(band);
			}
			for (int j = 0; j < bands; j++) {
				if (j % n == n-i-1) {
					int[] band = new int[s];
					for (int k = 0; k < s; k++) {
						band[k] = -1;
					}
					RAID.add(j, band);
				}
			}
			RAIDs.add(RAID);
		}
		
		// 查询
		for (int i = 0; i < m; i++) {
			List<int[]> RAID = RAIDs.get(DSK[i]);	// 查询块所在硬盘
			for (int j = 0; j < RAID.size(); j++) {
				for (int k = 0; k < s; k++) {
					if (RAID.get(j)[k] == read[i] && Dsk[DSK[i]] != null) {	// j为硬盘上条带序号，k为块序号
						if (Dsk[DSK[i]].length() != 2) {
							System.out.println(Dsk[DSK[i]].substring((j*s+k)*8, (j*s+k+1)*8));
						} else {
							String sum = "00000000";
							for (int p = 0; p < n; p++) {
								if (p != DSK[i]) {
									sum = xor(sum, Dsk[p].substring((j*s+k)*8, (j*s+k+1)*8));
								}
							}
							System.out.println(sum);
						}
					}
				}
			}
		}
	}
	
	private static String HexToBinary (String Hex) {
		String result = "";
		String word = "";
		for (int i = 0; i < Hex.length(); i += 4) {
			word = Integer.toBinaryString(Integer.valueOf(Hex.substring(i, i+4), 16));
			if (word.length() != 16) {
				for (int j = word.length(); j < 16; j++) { 
					word = "0" + word; 
				}
			}
			result += word;
		}
		return result;
	}
	
	private static String BinaryToHex (String Binary) {
		String result = "";
		String word = "";
		for (int i = 0; i < Binary.length(); i += 4) {
			word = Integer.toHexString(Integer.valueOf(Binary.substring(i, i+4), 2));
			result += word;
		}
		if (result.length() != 8) {
			for (int j = word.length(); j < 16; j++) { 
				word = "0" + word; 
			}
		}
		return result.toUpperCase();
	}
	
	private static String xor (String strHex_X, String strHex_Y) { 
		
		String BinaryX = HexToBinary(strHex_X); 
		String BinaryY = HexToBinary(strHex_Y); 
		String result = ""; 
		
		//异或运算 
		for(int i = 0; i < BinaryX.length(); i++){ 
		//如果相同位置数相同，则补0，否则补1 
			if(BinaryY.charAt(i) == BinaryX.charAt(i)) 
				result += "0"; 
			else{ 
				result += "1"; 
			} 
		}
		return BinaryToHex(result); 
	}
}